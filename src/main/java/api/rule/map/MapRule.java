package api.rule.map;

/**
 * Глобальные правила
 */
public class MapRule {
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

  /**
   * кол-во игроков
   */
  int playerCount;

  /**
   * габариты карты
   */
  int mapWidth, mapHeight;




}
