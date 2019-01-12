package api.rule.ability;

/**
 * Способность создания
 */
public class CreatureAbility {
  /**
   * Способность
   */
  Ability ability;

  /**
   * Вероятность успеха.
   */
  int probability;

  /**
   * цена попытки применения способности
   */
  int costInActionPoints;

  /**
   * Время восстановления. В игровых кругах. 0 - повторно доступна сразу
   */
  int restoreInterval;
}
