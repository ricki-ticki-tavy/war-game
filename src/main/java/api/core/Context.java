package api.core;

import api.game.GameEvent;
import api.game.map.LevelMap;

import java.io.InputStream;

/**
 * Контекст игровой
 */
public interface Context {

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

}
