package core.game;

import api.core.Core;
import api.core.Context;
import api.core.Result;
import api.enums.EventType;
import api.game.Event;
import api.game.EventDataContainer;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.player.PlayerImpl;
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
import java.util.function.Consumer;
import java.util.stream.Stream;

import static api.enums.EventType.*;
import static core.game.ContextImpl.NULL_GAME_CONTEXT;

/**
 * Игровой движок. Все операции выполняютсяв рамках динамического игрового контекста
 */
@Component
public class CoreImpl implements Core {

  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

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

  @Override
  public Context createGameContext(Player gameCreator, GameRules gameRules
          , InputStream map, String gameName, boolean hidden) {
    Context context = beanFactory.getBean(Context.class);
    context.loadMap(gameCreator, gameRules, map, gameName, hidden);
    contextMap.put(context.getContextId(), context);
    return context;
  }

  @Override
  public Context findGameContextByUID(String contextId) {
    return contextMap.get(contextId);
  }

  @Override
  public void removeGameContext(Context context) {
    Optional.ofNullable(contextMap.get(context.getContextId()))
            .ifPresent(foundContext -> {
              contextMap.remove(foundContext.getContextId());
              eventConsumers.remove(foundContext);
            });
  }

  @PostConstruct
  public void init() {
    subscribeEvent(null, this::eventLogger, WARRIOR_MOVED, PLAYER_DISCONNECTED
            , WARRIOR_ADDED, WEAPON_TAKEN, WEAPON_TRY_TO_DROP, WEAPON_DROPED
            , GAME_CONTEXT_CREATED, GAME_CONTEXT_CREATE, GAME_CONTEXT_LOAD_MAP
            , PLAYER_LOGGED_IN, PLAYER_CONNECTED, PLAYER_DISCONNECTED, PLAYER_RECONNECTED);
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
    Optional.ofNullable(eventConsumers.get(fireInContext))
            .ifPresent(consumerMap -> Optional.ofNullable(consumerMap.get(event.getEventType()))
                    .ifPresent(uidToConsumerMap -> uidToConsumerMap.values().stream()
                            .forEach(gameEventConsumer -> gameEventConsumer.accept(event))));
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
    fireEvent(new EventImpl(null, null, PLAYER_LOGGED_IN, new EventDataContainer(player), null));
    return result;
  }
}
