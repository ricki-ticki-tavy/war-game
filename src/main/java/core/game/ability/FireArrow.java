package core.game.ability;

import api.enums.ActorTypeEnum;
import api.enums.TargetTypeEnum;

/**
 * Способность огненной стрелы
 */
public class FireArrow extends AbstractAbilityImpl{

  private int level;

  /**
   *
   * @param level уровень силы огня
   * @param useCount оставшееся кол-во использований. -1 без ограничений
   */
  public FireArrow(int level, int useCount){
    super(-1, "AblFireArrow_", "Огненная стрела", "Урон огнем"); // бесконечно

    this.level = level;

    actorType = ActorTypeEnum.WEAPON;
    targetType = TargetTypeEnum.ENEMY_WARRIOR;
    this.useCount.set(useCount);
  }

}
