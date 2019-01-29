package core.game;


import api.game.map.Player;
import core.system.game.WarriorHeapElement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Данные игры
 */
public class GameProcessData {
  /**
   * Номер игрока, которыйсейчас ходит
   */

  public int indexOfPlayerOwnsTheTurn = 0;

  public GameProcessData() {
    indexOfPlayerOwnsTheTurn = 0;
  }

  public final Map<Integer, Player> frozenListOfPlayers = new ConcurrentHashMap<>(5);

  public final Map<String, WarriorHeapElement> playerTransactionalData = new ConcurrentHashMap<>(10);


}
