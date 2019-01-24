package api.core;

import api.enums.EventType;
import api.game.Event;
import api.game.map.Player;
import api.game.map.metadata.GameRules;

import java.io.InputStream;
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
  Context createGameContext(Player userGameCreator, GameRules gameRules, InputStream map, String gameName, boolean hidden);

  /**
   * Найти контекст по его UID
   * @param contextId
   * @return
   */
  Context findGameContextByUID(String contextId);

  /**
   * Удалить игру и ее контекст с сервера
   * @param context
   */
  void removeGameContext(Context context);

  /**
   * Отправить сообщение о событии
   *
   * @param event
   * @return
   */
  Event fireEvent(Event event);

  /**
   * Подписаться на событие
   *
   * @param context    контекст, в рамках которого интересует данное событие. Если null, то по всем контекстам
   * @param consumer
   * @param eventTypes
   * @return
   */
  String subscribeEvent(Context context, Consumer<Event> consumer, EventType... eventTypes);

  /**
   * подключить к игре игрока.
   *
   * @param playerName
   * @return
   */
  Result loginPlayer(String playerName);



//  void unsubscribeEvent(Context context, Consumer<EventImpl> consumer);
}
