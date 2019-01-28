package core.game;

import api.game.map.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Данные игры
 */
public class GameProcessData {
  /**
   * Номер игрока, которыйсейчас ходит
   */
  public int indexOfPlayerOwnsTheRound = 0;

  public GameProcessData(){
    indexOfPlayerOwnsTheRound = 0;
  }

  public final Map<Integer, Player> frozenListOfPlayers = new ConcurrentHashMap<>(5);


}
