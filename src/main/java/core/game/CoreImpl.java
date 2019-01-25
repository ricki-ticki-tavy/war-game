package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.game.Event;
import api.game.EventDataContainer;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.warrior.Skeleton;
import core.entity.warrior.Viking;
import core.entity.weapon.Bow;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import core.system.ResultImpl;
import core.system.eco.EventLogger;
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
import java.util.concurrent.atomic.AtomicBoolean;
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

  /**
   * Зарегистрировать базовый класс воина
   *
   * @param className
   * @param warriorBaseClass
   */
  private void registerWarriorBaseClass(String className, Class<? extends WarriorBaseClass> warriorBaseClass) {
    registeredWarriorBaseClasses.put(className, warriorBaseClass);
  }

  /**
   * Зарегистрировать базовый класс оружия
   *
   * @param className
   * @param weaponClass
   */
  private void registerWeaponClass(String className, Class<? extends Weapon> weaponClass) {
    registeredWeaponClasses.put(className, weaponClass);
  }


  @Override
  public Result<Context> createGameContext(Player gameCreator, GameRules gameRules
          , InputStream map, String gameName, boolean hidden) {
    // Если у пользователя была привязка к другим контекстам - надо разорвать ее
    return gameCreator.replaceContext(null).onSuccess(nullCtx ->
            // Создадим контекст новой игры
            beanFactory.getBean(Context.class, gameCreator)
                    // Загрузим карту
                    .loadMap(gameRules, map, gameName, hidden).onSuccess(createdCtx ->
                    // сделаем вход пользователем в эту карту
                    createdCtx.connectPlayer(gameCreator).onSuccess(connectedPlayer -> {
                      contextMap.put(createdCtx.getContextId(), createdCtx);
                      // подменим в ответе добавленного пользователя на контекст
                      return ResultImpl.success(createdCtx);
                    })));
  }

  @Override
  public Result<Context> findGameContextByUID(String contextId) {
    return Optional.ofNullable(contextMap.get(contextId))
            .map(foundContext -> ResultImpl.success(foundContext))
            .orElse(ResultImpl.fail(CONTEXT_NOT_FOUND_BY_ID.getError(contextId)));
  }

  @Override
  public void removeGameContext(Context context) {
    AtomicBoolean found = new AtomicBoolean(false);
    Optional.ofNullable(contextMap.get(context.getContextId()))
            .ifPresent(foundContext -> {
              contextMap.remove(foundContext.getContextId());
              eventConsumers.remove(foundContext);
              found.set(true);
            });
    context.fireGameEvent(null, GAME_CONTEXT_REMOVED
            , new EventDataContainer(context, found.get()
                    ? ResultImpl.success(context)
                    : ResultImpl.fail(CONTEXT_REMOVE_NOT_FOUND.getError()))
            , null);
  }

  @PostConstruct
  public void init() {
    subscribeEvent(null, this::eventLogger, WARRIOR_MOVED, PLAYER_DISCONNECTED
            , WARRIOR_ADDED, WEAPON_TAKEN, WEAPON_TRY_TO_DROP, WEAPON_DROPED
            , GAME_CONTEXT_CREATED, GAME_CONTEXT_CREATE, GAME_CONTEXT_LOAD_MAP, GAME_CONTEXT_REMOVED
            , PLAYER_LOGGED_IN, PLAYER_CONNECTED, PLAYER_DISCONNECTED, PLAYER_RECONNECTED);

    registerWarriorBaseClass(Skeleton.CLASS_NAME, Skeleton.class);
    registerWarriorBaseClass(Viking.CLASS_NAME, Viking.class);

    registerWeaponClass(Bow.CLASS_NAME, Bow.class);
    registerWeaponClass(ShortSword.CLASS_NAME, ShortSword.class);
    registerWeaponClass(Sword.CLASS_NAME, Sword.class);
  }

  @Override
  //TODO
  public int getRandom(int min, int max) {
    return 0;
  }

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

  /**
   * Событие сначала обрабатывается обработчиками привязанными к контексту, п потом без привязки к контексту,
   *
   * @param event
   * @return
   */
  @Override
  public Event fireEvent(Event event) {
    fireEventInContext(event.getSourceContext(), event);
    fireEventInContext(NULL_GAME_CONTEXT, event);
    return event;
  }

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

  private void eventLogger(Event event) {
    gameEventLogger.logGameEvent(event);
  }

  @Override
  public Result loginPlayer(String playerName) {
    Player player = players.computeIfAbsent(playerName, playerNameKey -> beanFactory.getBean(Player.class, playerNameKey));
    Result result = ResultImpl.success(player);
    fireEvent(new EventImpl(null, null, PLAYER_LOGGED_IN, new EventDataContainer(player, result), null));
    return result;
  }

  @Override
  public Result<Player> findUserByName(String playerName) {
    return Optional.ofNullable(players.get(playerName))
            .map(player -> ResultImpl.success(player))
            .orElse(ResultImpl.fail(USER_NOT_LOGGED_IN.getError(playerName)));
  }

  @Override
  public List<Context> getContextList() {
    return new ArrayList(contextMap.values());
  }

  @Override
  public Result<List<String>> getBaseWarriorClasses() {
    return ResultImpl.success(new ArrayList(registeredWarriorBaseClasses.keySet()));
  }

  @Override
  public Result<List<String>> getWeaponClasses() {
    return ResultImpl.success(new ArrayList(registeredWeaponClasses.keySet()));
  }

  @Override
  public Result<Class<? extends WarriorBaseClass>> findWarriorBaseClassByName(String className) {
    return Optional.ofNullable(registeredWarriorBaseClasses.get(className))
            .map(clazz -> ResultImpl.success(clazz))
            .orElse(ResultImpl.fail(WARRIOR_BASE_CLASS_NOT_FOUND_BY_NAME.getError(className)));
  }

  @Override
  public Result<Class<? extends Weapon>> findWeaponByName(String weaponClassName) {
    return Optional.ofNullable(registeredWeaponClasses.get(weaponClassName))
            .map(clazz -> ResultImpl.success(clazz))
            .orElse(ResultImpl.fail(WEAPON_BASE_CLASS_NOT_FOUND_BY_NAME.getError(weaponClassName)));
  }
}
