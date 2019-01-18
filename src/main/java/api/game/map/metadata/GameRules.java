package api.game.map.metadata;

/**
 * Глобальные правила
 */
public class GameRules {
  /**
   * Максимум созданий на игрока при старте игры
   */
  int maxStartCreaturePerPlayer;

  /**
   * Максимум призванных созданий на игрока
   */
  int maxSummonedCreaturePerPlayer;

  /**
   * Максимальное время на ход каждому игроку
   */
  int maxPlayerRoundTime;

  /**
   * Кол-во очков магии у каждого игрока при старте
   */
  int startMannaPoints;

  /**
   * Максимально допустимое кол-во очков магии игрока
   */
  int maxMannaPoints;

  /**
   * Кол-во магии, восстанавливаемый каждый ход
   */
  int restorationMannaPointsPerTotalRound;

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

  public GameRules(int maxStartCreaturePerPlayer, int maxSummonedCreaturePerPlayer, int startMannaPoints
          , int maxMannaPoints, int restorationMannaPointsPerTotalRound, int maxPlayerRoundTime) {
    this.maxStartCreaturePerPlayer = maxStartCreaturePerPlayer;
    this.maxSummonedCreaturePerPlayer = maxSummonedCreaturePerPlayer;
    this.startMannaPoints = startMannaPoints;
    this.maxMannaPoints = maxMannaPoints;
    this.restorationMannaPointsPerTotalRound = restorationMannaPointsPerTotalRound;
    this.maxPlayerRoundTime = maxPlayerRoundTime;
  }
}
