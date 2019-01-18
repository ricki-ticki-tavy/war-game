package api;

import api.enums.ArmorClassEnum;

/**
 * Базовые атрибуты воина
 */
public class WarriorSBaseAttributes {

  int health;
  int maxHealth;

  int manna;
  int maxManna;

  ArmorClassEnum armorClass;
  Integer deltaCostMove;

  int abilityActionPoints;
  int maxAbilityActionPoints;

  int actionPoints;
  int maxActionPoints;

  int luckMeleeAtack;
  int luckRangeAtack;
  int luckDefence;

  /**
   * ВОзвращает очки здоровья
   *
   * @return
   */
  public int getHealth() {
    return health;
  }

  /**
   * Изменяет значение здоровья
   *
   * @param deltaHealth
   * @return
   */
  public int addHealth(int deltaHealth) {
    return health = Math.min(Math.max(0, health + deltaHealth), maxHealth);
  }

  public WarriorSBaseAttributes setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
    return this;
  }

  public int getManna() {
    return manna;
  }

  /**
   * Изменить на N очков магии
   *
   * @param deltaManna
   * @return
   */
  public int addManna(int deltaManna) {
    return manna = Math.min(Math.max(0, manna + deltaManna), maxManna);
  }

  public WarriorSBaseAttributes setMaxManna(int maxManna) {
    this.maxManna = maxManna;
    return this;
  }

  /**
   * класс брони
   */
  public ArmorClassEnum getArmorClass() {
    return armorClass;
  }

  public WarriorSBaseAttributes setArmorClass(ArmorClassEnum armorClass) {
    this.armorClass = armorClass;
    return this;
  }

  /**
   * Дельта к цене передвижения, определяемому по классу брони
   */
  public Integer getDeltaCostMove() {
    return deltaCostMove;
  }

  public WarriorSBaseAttributes setDeltaCostMove(int deltaCostMove) {
    this.deltaCostMove = deltaCostMove;
    return this;
  }

  /**
   * Кол-во очков применения способностей за ход
   */
  public int getAbilityActionPoints() {
    return abilityActionPoints;
  }

  public WarriorSBaseAttributes setAbilityActionPoints(int abilityActionPoints) {
    this.abilityActionPoints = abilityActionPoints;
    return this;
  }

  /**
   * Кол-во очков применения способностей за ход
   */
  public int getMaxAbilityActionPoints() {
    return maxAbilityActionPoints;
  }

  public WarriorSBaseAttributes setMaxAbilityActionPoints(int maxAbilityActionPoints) {
    this.maxAbilityActionPoints = maxAbilityActionPoints;
    return this;
  }

  public int getActionPoints() {
    return actionPoints;
  }

  public WarriorSBaseAttributes setActionPoints(int actionPoints) {
    this.actionPoints = actionPoints;
    return this;
  }

  public int addActionPoints(int deltaActionPoints) {
    return actionPoints = Math.min(Math.max(0, actionPoints + deltaActionPoints), maxActionPoints);
  }

  public int getMaxActionPoints() {
    return maxActionPoints;
  }

  public WarriorSBaseAttributes setMaxActionPoints(int maxActionPoints) {
    this.maxActionPoints = maxActionPoints;
    return this;
  }

  public WarriorSBaseAttributes() {
    health = 0;
    manna = 0;
    deltaCostMove = 0;
    abilityActionPoints = 0;
    actionPoints = 0;
  }

  public WarriorSBaseAttributes(
          int maxHealth, int health
          , int maxManna, int manna
          , ArmorClassEnum armorClass, int deltaCostMove
          , int maxAbilityActionPoints, int abilityActionPoints
          , int maxActionPoints, int actionPoints) {
    this.health = health;
    this.maxHealth = maxHealth;
    this.maxManna = maxManna;
    this.manna = manna;
    this.armorClass = armorClass;
    this.deltaCostMove = deltaCostMove;
    this.maxAbilityActionPoints = maxAbilityActionPoints;
    this.abilityActionPoints = abilityActionPoints;
    this.maxActionPoints = maxActionPoints;
    this.actionPoints = actionPoints;
  }

  public WarriorSBaseAttributes(
          int health
          , int manna
          , int abilityActionPoints
          , int actionPoints) {
    this.health = health;
    this.manna = manna;
    this.abilityActionPoints = abilityActionPoints;
    this.actionPoints = actionPoints;
  }

  @Override
  public WarriorSBaseAttributes clone() {
    return new WarriorSBaseAttributes(maxHealth, health
            , maxManna, manna
            , armorClass, deltaCostMove,
            maxAbilityActionPoints, abilityActionPoints,
            maxActionPoints, actionPoints);
  }

}
