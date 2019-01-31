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


  public Result<Player> getPlayerOwnsTheThisTurn(){
    return ResultImpl.success(frozenListOfPlayers.get(indexOfPlayerOwnsTheTurn));
  }

  public int getIndexOfPlayerOwnsTheNextTurn(){
    int index = indexOfPlayerOwnsTheTurn.get();
    if (++index >= frozenListOfPlayers.size()) {
      index = 0;
    }
    return index;
  }

  public GameProcessData setIndexOfPlayerOwnsTheTurn(int val){
    indexOfPlayerOwnsTheTurn.set(val);
    return this;
  }

  /**
   * Сделать следующего игрока по кругу текущим владельцем хода
   * @return
   */
  public Result<Player> switchToNextPlayerTurn(){
    if (indexOfPlayerOwnsTheTurn.incrementAndGet() >= frozenListOfPlayers.size()) {
      indexOfPlayerOwnsTheTurn.set(0);
    }
    return getPlayerOwnsTheThisTurn();
  }

}
