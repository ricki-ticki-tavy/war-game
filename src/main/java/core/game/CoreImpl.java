package core.game;

import api.core.Core;
import api.core.GameContext;
import api.enums.EventType;
import api.game.GameEvent;
import api.game.map.metadata.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static core.game.GameContextImpl.NULL_GAME_CONTEXT;

/**
 * Игровой движок. Все операции выполняютсяв рамках динамического игрового контекста
 */
@Component
public class CoreImpl implements Core {

  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

  private Map<String, GameContext> contextMap = new ConcurrentHashMap<>(10);

  /**
   * групировка по контекстам, далее внутри групировка по типам событий. Один и тот же потребитель может
   * быть в разных группах одновременно. Последний мэп - UID потребителя для возможности отписки даже лямды
   */
  private Map<GameContext, Map<EventType, Map<String, Consumer<GameEvent>>>> eventConsumers =
          new ConcurrentHashMap<>(10);

  @Autowired
  BeanFactory beanFactory;

  @Override
  public GameContext createGameContext(String userGameCreator, GameRules gameRules
          , InputStream map, String gameName, boolean hidden) {
    GameContext context = beanFactory.getBean(GameContext.class);
    context.loadMap(userGameCreator, gameRules, map, gameName, hidden);
    contextMap.put(context.getContextId(), context);
    return context;
  }

  @PostConstruct
  public void init() {
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
  private void fireEventInContext(GameContext fireInContext, GameEvent event) {
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
  public GameEvent fireEvent(GameEvent event) {
    fireEventInContext(event.getSourceContext(), event);
    fireEventInContext(NULL_GAME_CONTEXT, event);
    return event;
  }

  @Override
  public String subscribeEvent(GameContext context, List<EventType> eventTypes, Consumer<GameEvent> consumer) {
    if (context == null) {
      context = NULL_GAME_CONTEXT;
    }

    String consumerUID = UUID.randomUUID().toString();

    Map<EventType, Map<String, Consumer<GameEvent>>> contextConsumers = eventConsumers
            .computeIfAbsent(context, keyContext -> new ConcurrentHashMap<>(EventType.values().length));

    eventTypes.stream().forEach(eventType -> {
      contextConsumers
              .computeIfAbsent(eventType, keyEventType -> new ConcurrentHashMap<>(20))
              .put(consumerUID, consumer);
    });
    return consumerUID;
  }

}
