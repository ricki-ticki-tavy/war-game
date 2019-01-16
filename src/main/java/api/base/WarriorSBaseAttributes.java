package api.base;

/**
 * Базовые атрибуты воина
 */
public class WarriorSBaseAttributes {

  int health;
  int maxHealth;

  int manna;
  int maxManna;

  int armorClass;
  int costMove;

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

  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
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

  public void setMaxManna(int maxManna) {
    this.maxManna = maxManna;
  }

  /**
   * класс брони
   */
  public int getArmorClass() {
    return armorClass;
  }

  public void setArmorClass(int armorClass) {
    this.armorClass = armorClass;
  }

  /**
   * цена передвижения
   */
  public int getCostMove() {
    return costMove;
  }

  public void setCostMove(int costMove) {
    this.costMove = costMove;
  }

  /**
   * Кол-во очков применения способностей за ход
   */
  public int getAbilityActionPoints() {
    return abilityActionPoints;
  }

  public void setAbilityActionPoints(int abilityActionPoints) {
    this.abilityActionPoints = abilityActionPoints;
  }

  /**
   * Кол-во очков применения способностей за ход
   */
  public int getMaxAbilityActionPoints() {
    return maxAbilityActionPoints;
  }

  public void setMaxAbilityActionPoints(int maxAbilityActionPoints) {
    this.maxAbilityActionPoints = maxAbilityActionPoints;
  }

  public int getActionPoints() {
    return actionPoints;
  }

  public void setActionPoints(int actionPoints) {
    this.actionPoints = actionPoints;
  }

  public int addActionPoints(int deltaActionPoints) {
    return actionPoints = Math.min(Math.max(0, actionPoints + deltaActionPoints), maxActionPoints);
  }

  public int getMaxActionPoints() {
    return maxActionPoints;
  }

  public void setMaxActionPoints(int maxActionPoints) {
    this.maxActionPoints = maxActionPoints;
  }

  public WarriorSBaseAttributes() {

  }

  public WarriorSBaseAttributes(
          int maxHealth, int health
          , int maxManna, int manna
          , int armorClass, int costMove
          , int maxAbilityActionPoints, int abilityActionPoints
          , int maxActionPoints, int actionPoints) {
    this.health = health;
    this.maxHealth = maxHealth;
    this.maxManna = maxManna;
    this.manna = manna;
    this.armorClass = armorClass;
    this.costMove = costMove;
    this.maxAbilityActionPoints = maxAbilityActionPoints;
    this.abilityActionPoints = abilityActionPoints;
    this.maxActionPoints = maxActionPoints;
    this.actionPoints = actionPoints;
  }

}
