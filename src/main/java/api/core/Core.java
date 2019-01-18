package api.core;

import api.game.map.metadata.GameRules;

import java.io.InputStream;

/**
 * взаимодействие с игрой.
 */
public interface Core {
  int getRandom(int min, int max);

  /**
   * Создать  игру
   * @param userGameCreator
   * @param gameRules
   * @param map
   * @return
   */
  Context createGame(String userGameCreator, GameRules gameRules, InputStream map);
}
