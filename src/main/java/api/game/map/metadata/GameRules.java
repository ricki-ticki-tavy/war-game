package api.game.map.metadata;

/**
 * Глобальные правила
 */
public interface GameRules {
  /**
   * Максимум созданий на игрока при старте игры
   */
  int getMaxStartCreaturePerPlayer();

  /**
   * Максимум призванных созданий на игрока
   */
  int getMaxSummonedCreaturePerPlayer();

  /**
   * Максимальное время на ход каждому игроку
   */
  int getMaxPlayerRoundTime();

  /**
   * Кол-во очков магии у каждого игрока при старте
   */
  int getStartMannaPoints();

  /**
   * Максимально допустимое кол-во очков магии игрока
   */
  int getMaxMannaPoints();

  /**
   * Кол-во магии, восстанавливаемый каждый ход
   */
  int getRestorationMannaPointsPerTotalRound();

}
