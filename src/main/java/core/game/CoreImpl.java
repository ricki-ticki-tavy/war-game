package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.core.Event;
import api.core.EventDataContainer;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.warrior.Skeleton;
import core.entity.warrior.Viking;
import core.entity.warrior.Vityaz;
import core.entity.weapon.Bow;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import core.system.ResultImpl;
import core.system.log.EventLogger;
import core.system.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static api.enums.EventType.*;
import static core.game.ContextImpl.NULL_GAME_CONTEXT;
import static core.system.error.GameErrors.*;

/**
 * Игровой движок. Все операции выполняютсяв рамках динамического игрового контекста
 */
@Component
public class CoreImpl implements Core {

  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

  private final Map<String, Class<? extends WarriorBaseClass>> registeredWarriorBaseClasses = new ConcurrentHashMap<>(100);
  private final Map<String, Class<? extends Weapon>> registeredWeaponClasses = new ConcurrentHashMap<>(100);

  private Map<String, Context> contextMap = new ConcurrentHashMap<>(10);
  private Map<String, Player> players = new ConcurrentHashMap<>(1000);
  private Random random = new Random(new Date().getTime());

  /**
   * групировка по контекстам, далее внутри групировка по типам событий. Один и тот же потребитель может
   * быть в разных группах одновременно. Последний мэп - UID потребителя для возможности отписки даже лямды
   */
  private Map<Context, Map<EventType, Map<String, Consumer<Event>>>> eventConsumers =
          new ConcurrentHashMap<>(10);

  @Autowired
  BeanFactory beanFactory;

  @Autowired
  EventLogger gameEventLogger;
  //===================================================================================================
  //===================================================================================================

  /**
   * Зарегистрировать базовый класс воина
   *
   * @param className
   * @param warriorBaseClass
   */
  public void registerWarriorBaseClass(String className, Class<? extends WarriorBaseClass> warriorBaseClass) {
    registeredWarriorBaseClasses.put(className, warriorBaseClass);
  }
  //===================================================================================================

  /**
   * Зарегистрировать базовый класс оружия
   *
   * @param className
   * @param weaponClass
   */
  public void registerWeaponClass(String className, Class<? extends Weapon> weaponClass) {
    registeredWeaponClasses.put(className, weaponClass);
  }
  //===================================================================================================

  @Override
  public Result<Context> createGameContext(Player gameCreator, GameRules gameRules
          , InputStream map, String gameName, boolean hidden) {
    // Если у пользователя была привязка к другим контекстам - надо разорвать ее
    return gameCreator.replaceContext(null).map(nullCtx ->
            // Создадим контекст новой игры
            beanFactory.getBean(Context.class, gameCreator)
                    // Загрузим карту
                    .loadMap(gameRules, map, gameName, hidden).map(createdCtx ->
                    // сделаем вход пользователем в эту карту
                    createdCtx.connectPlayer(gameCreator).map(connectedPlayer -> {
                      contextMap.put(createdCtx.getContextId(), createdCtx);
                      // подменим в ответе добавленного пользователя на контекст
                      return ResultImpl.success(createdCtx);
                    })));
  }
  //===================================================================================================

  @Override
  public Result<Context> findGameContextByUID(String contextId) {
    return Optional.ofNullable(contextMap.get(contextId))
            .map(foundContext -> ResultImpl.success(foundContext))
            .orElse(ResultImpl.fail(CONTEXT_NOT_FOUND_BY_ID.getError(contextId)));
  }
  //===================================================================================================

  @Override
  public Result<Context> removeGameContext(String contextId) {

    return findGameContextByUID(contextId)
            // переводим контекст в режим удаления
            .map(contextToDelete -> contextToDelete.initDelete())
            .map(foundContext -> {
              // отключим всех пользователей
              foundContext.getLevelMap().getPlayers()
                      .stream()
                      .forEach(player ->
                              foundContext.disconnectPlayer(player));
              // вычеркнуть контекст из списка
              contextMap.remove(foundContext.getContextId());
              // отправимсообщение об удалении контекста
              Result result = ResultImpl.success(foundContext);
              foundContext.fireGameEvent(null, GAME_CONTEXT_REMOVED, new EventDataContainer(foundContext, result), null);
              // удалить все подписи данного контекста  на события
              eventConsumers.remove(foundContext);
              return result;
            });
  }
  //===================================================================================================

  @PostConstruct
  public void init() {
    // подпишем логер на все события. Пусть пишет по ним инфу
    subscribeEvent(null, this::eventLogger, EventType.values());

    registerWarriorBaseClass(Skeleton.CLASS_NAME, Skeleton.class);
    registerWarriorBaseClass(Viking.CLASS_NAME, Viking.class);
    registerWarriorBaseClass(Vityaz.CLASS_NAME, Vityaz.class);

    registerWeaponClass(Bow.CLASS_NAME, Bow.class);
    registerWeaponClass(ShortSword.CLASS_NAME, ShortSword.class);
    registerWeaponClass(Sword.CLASS_NAME, Sword.class);
  }
  //===================================================================================================

  @Override
  public int getRandom(int min, int max) {
    synchronized (random) {
      return max == min
              ? random.nextInt(max + 1) + 1
              : random.nextInt(max - min + 1) + min;
    }
  }
  //===================================================================================================

  /**
   * Отправить сообщения в отпределенный контекст
   *
   * @param event
   */

  private void fireEventInContext(Context fireInContext, Event event) {
    Optional.ofNullable(fireInContext)
            .ifPresent(firedContext -> Optional.ofNullable(eventConsumers.get(firedContext))
                    .ifPresent(consumerMap -> Optional.ofNullable(consumerMap.get(event.getEventType()))
                            .ifPresent(uidToConsumerMap -> uidToConsumerMap.values().stream()
                                    .forEach(gameEventConsumer -> gameEventConsumer.accept(event)))));
  }
  //===================================================================================================

  @Override
  public Event fireEvent(Event event) {
    fireEventInContext(event.getSourceContext(), event);
    fireEventInContext(NULL_GAME_CONTEXT, event);
    return event;
  }
  //===================================================================================================

  @Override
  public String subscribeEvent(Context context, Consumer<Event> consumer, EventType... eventTypes) {
    if (context == null) {
      context = NULL_GAME_CONTEXT;
    }

    String consumerUID = UUID.randomUUID().toString();

    Map<EventType, Map<String, Consumer<Event>>> contextConsumers = eventConsumers
            .computeIfAbsent(context, keyContext -> new ConcurrentHashMap<>(EventType.values().length));

    Stream.of(eventTypes).forEach(eventType -> {
      contextConsumers
              .computeIfAbsent(eventType, keyEventType -> new ConcurrentHashMap<>(20))
              .put(consumerUID, consumer);
    });
    return consumerUID;
  }
  //===================================================================================================

  @Override
  public Result<Context> unsubscribeEvent(Context context, String consumerId, EventType... eventTypes) {
    Map<EventType, Map<String, Consumer<Event>>> contextConsumers = eventConsumers.get(context);
    if (contextConsumers != null) {
      if (eventTypes.length != 0) {
        Arrays.stream(eventTypes).forEach(eventType -> {
          Map<String, Consumer<Event>> consumersOfEvent = contextConsumers.get(eventType);
          if (consumersOfEvent != null) {
            consumersOfEvent.remove(consumerId);
          }
        });
      } else {
        // не указан тип события. Отписываемся везде
        contextConsumers.values().stream()
                .forEach(stringConsumerMap -> stringConsumerMap.remove(consumerId));
      }
    }
    return ResultImpl.success(context);
  }
  //===================================================================================================

  private void eventLogger(Event event) {
    gameEventLogger.logGameEvent(event);
  }
  //===================================================================================================

  @Override
  public Result loginPlayer(String playerName) {
    Player player = players.computeIfAbsent(playerName, playerNameKey -> beanFactory.getBean(Player.class, playerNameKey));
    Result result = ResultImpl.success(player);
    fireEvent(new EventImpl(null, null, PLAYER_LOGGED_IN, new EventDataContainer(player, result), null));
    return result;
  }
  //===================================================================================================

  @Override
  public Result<Player> findUserByName(String playerName) {
    return Optional.ofNullable(players.get(playerName))
            .map(player -> ResultImpl.success(player))
            .orElse(ResultImpl.fail(USER_NOT_LOGGED_IN.getError(playerName)));
  }
  //===================================================================================================

  @Override
  public List<Context> getContextList() {
    return new ArrayList(contextMap.values());
  }
  //===================================================================================================

  @Override
  public Result<List<String>> getBaseWarriorClasses() {
    return ResultImpl.success(new ArrayList(registeredWarriorBaseClasses.keySet()));
  }
  //===================================================================================================

  @Override
  public Result<List<String>> getWeaponClasses() {
    return ResultImpl.success(new ArrayList(registeredWeaponClasses.keySet()));
  }
  //===================================================================================================

  @Override
  public Result<Class<? extends WarriorBaseClass>> findWarriorBaseClassByName(String className) {
    return Optional.ofNullable(registeredWarriorBaseClasses.get(className))
            .map(clazz -> ResultImpl.success(clazz))
            .orElse(ResultImpl.fail(WARRIOR_BASE_CLASS_NOT_FOUND_BY_NAME.getError(className)));
  }
  //===================================================================================================

  @Override
  public Result<Class<? extends Weapon>> findWeaponByName(String weaponClassName) {
    return Optional.ofNullable(registeredWeaponClasses.get(weaponClassName))
            .map(clazz -> ResultImpl.success(clazz))
            .orElse(ResultImpl.fail(WEAPON_BASE_CLASS_NOT_FOUND_BY_NAME.getError(weaponClassName)));
  }
  //===================================================================================================
}
