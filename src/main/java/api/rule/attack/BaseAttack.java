package api.rule.attack;

import api.rule.attribute.AttackType;

public class BaseAttack {
  /**
   * минимальный урон
   */
  int minDamage;

  /**
   * Максимальный урон
   */
  int maxDamage;

  /**
   * Угол луча поражения
   */
  int damageAngle;

  /**
   *  Атака ВСЕХ юнитов
   */
  boolean mustAttackAllCreatures;

  /**
   * Стоимость атаки в единицах действия
   */
  int costActionPoints;

  /**
   * Тип атаки
   */
  AttackType attackType;


}
