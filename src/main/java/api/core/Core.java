package api.core;

import api.enums.MapTypeEnum;
import api.game.map.LevelMap;
import api.game.GameEvent;

/**
 * взаимодействие с игрой.
 */
public interface Core {
  int getRandom(int min, int max);

  /**
   * Создать
   * @param mapType
   * @return
   */
  Context createGame(MapTypeEnum mapType);
}
