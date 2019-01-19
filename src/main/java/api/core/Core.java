package api.core;

import api.enums.EventType;
import api.game.GameEvent;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * взаимодействие с игрой.
 */
public interface Core {
  int getRandom(int min, int max);

  /**
   * Создать  игру
   *
   * @param userGameCreator
   * @param gameRules
   * @param map
   * @return
   */
  GameContext createGameContext(String userGameCreator, GameRules gameRules, InputStream map, String gameName, boolean hidden);

  /**
   * Найти контекст по его UID
   * @param contextId
   * @return
   */
  GameContext findGameContextByUID(String contextId);

  /**
   * Удалить игру и ее контекст с сервера
   * @param context
   */
  void removeGameContext(GameContext context);

  /**
   * Отправить сообщение о событии
   *
   * @param event
   * @return
   */
  GameEvent fireEvent(GameEvent event);

  /**
   * Подписаться на событие
   *
   * @param context    контекст, в рамках которого интересует данное событие. Если null, то по всем контекстам
   * @param consumer
   * @param eventTypes
   * @return
   */
  String subscribeEvent(GameContext context, Consumer<GameEvent> consumer,  EventType... eventTypes);

//  void unsubscribeEvent(GameContext context, Consumer<GameEvent> consumer);
}
