package api.rule.attack;

public class DistancedAttack extends BaseAttack{
  /**
   * Минимальный расстояние для атаки
   */
  int minRange;

  /**
   * Максимальное расстояние для атаки
   */
  int maxRange;

  /**
   * Дальность начала спадания поражения
   */
  int fadeRangeStart;

  /**
   * процент спадания урона на единицу длины
   */
  int fadeDamagePercentPerLength;
}
