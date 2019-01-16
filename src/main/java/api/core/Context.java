package api.core;

import api.game.GameEvent;
import api.game.map.LevelMap;
import api.game.map.Player;

import java.io.InputStream;

/**
 * Контекст игровой
 */
public interface Context {

  /**
   * UUID контекста (сессии)
   * @return
   */
  String getContextId();

  /**
   * Возвращает игровой движок
   * @return
   */
  Core getCore();

  /**
   * Инициировать срабатывание события
   * @param gameEvent
   * @return
   */
  boolean fireGameEvent(GameEvent gameEvent);

  /**
   * Получить карту игры со всеми игроками и воинами
   * @return
   */
  LevelMap getLevelMap();

  /**
   * Загрузить карту
   * @param xmlMapMetadata
   * @return
   */
  void loadMap(InputStream xmlMapMetadata);

  /**
   * Создать и подключить к игре игрока.
   * @param playerSessionId
   * @return
   */
  Player connectPlayer(String playerSessionId);

}
