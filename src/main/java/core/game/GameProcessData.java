package core.game;


import api.core.Result;
import api.game.map.Player;
import core.system.ResultImpl;
import core.system.game.WarriorHeapElement;

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
    new AtomicInteger(0);
  }

  public final Map<Integer, Player> frozenListOfPlayers = new ConcurrentHashMap<>(5);

  public final Map<String, WarriorHeapElement> playerTransactionalData = new ConcurrentHashMap<>(10);

}
