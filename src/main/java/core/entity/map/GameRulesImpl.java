package core.entity.map;

import api.game.map.metadata.GameRules;

/**
 * Глобальные правила
 */
public class GameRulesImpl implements GameRules {
  private int maxStartCreaturePerPlayer;
  private int movesCountPerTurnForEachPlayer;
  private int maxSummonedCreaturePerPlayer;
  private int maxPlayerRoundTime;
  private int startMannaPoints;
  private int maxMannaPoints;
  private int restorationMannaPointsPerTotalRound;
  private int warriorSize;



  public int getMaxStartCreaturePerPlayer() {
    return maxStartCreaturePerPlayer;
  }

  public int getMaxSummonedCreaturePerPlayer() {
    return maxSummonedCreaturePerPlayer;
  }

  public int getMaxPlayerRoundTime() {
    return maxPlayerRoundTime;
  }

  public int getStartMannaPoints() {
    return startMannaPoints;
  }

  public int getMaxMannaPoints() {
    return maxMannaPoints;
  }

  public int getRestorationMannaPointsPerTotalRound() {
    return restorationMannaPointsPerTotalRound;
  }

  @Override
  public int getWarriorSize() {
    return warriorSize;
  }

  public GameRulesImpl(GameRules gameRules){
    this.maxStartCreaturePerPlayer = gameRules.getMaxStartCreaturePerPlayer();
    this.maxSummonedCreaturePerPlayer = gameRules.getMaxSummonedCreaturePerPlayer();
    this.startMannaPoints = gameRules.getStartMannaPoints();
    this.maxMannaPoints = gameRules.getMaxMannaPoints();
    this.restorationMannaPointsPerTotalRound = gameRules.getRestorationMannaPointsPerTotalRound();
    this.maxPlayerRoundTime = gameRules.getMaxPlayerRoundTime();
    this.warriorSize = gameRules.getWarriorSize();
    this.movesCountPerTurnForEachPlayer = gameRules.getMovesCountPerTurnForEachPlayer();
  }

  public GameRulesImpl(int maxStartCreaturePerPlayer, int maxSummonedCreaturePerPlayer, int movesCountPerTurnForEachPlayer
          , int startMannaPoints
          , int maxMannaPoints, int restorationMannaPointsPerTotalRound, int maxPlayerRoundTime, int warriorSize) {
    this.maxStartCreaturePerPlayer = maxStartCreaturePerPlayer;
    this.maxSummonedCreaturePerPlayer = maxSummonedCreaturePerPlayer;
    this.startMannaPoints = startMannaPoints;
    this.maxMannaPoints = maxMannaPoints;
    this.restorationMannaPointsPerTotalRound = restorationMannaPointsPerTotalRound;
    this.maxPlayerRoundTime = maxPlayerRoundTime;
    this.warriorSize = warriorSize;
    this.movesCountPerTurnForEachPlayer = movesCountPerTurnForEachPlayer;
  }

  @Override
  public int getMovesCountPerTurnForEachPlayer() {
    return movesCountPerTurnForEachPlayer;
  }
}
