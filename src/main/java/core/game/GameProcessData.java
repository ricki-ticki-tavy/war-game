package core.game;


import api.game.map.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Данные игры
 */
public class GameProcessData {
  /**
   * Номер игрока, которыйсейчас ходит
   */

  public AtomicInteger indexOfPlayerOwnsTheTurn;

  public GameProcessData() {
    indexOfPlayerOwnsTheTurn = new AtomicInteger(0);
  }

  public final Map<Integer, Player> frozenListOfPlayers = new ConcurrentHashMap<>(5);
}
