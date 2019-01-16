package api.old.rule.game;

import api.old.rule.ability.Ability;

import java.util.List;

/**
 * Настройки игрока
 */
public class BasePlayer {
  /**
   * Начальное кол-во магии
   */
  int startMannaPoints;

  /**
   * максимальное кол-во магии
   */
  int maxMannaPoints;

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
   * Способности игрока
   */
  List<Ability> abilities;



}
