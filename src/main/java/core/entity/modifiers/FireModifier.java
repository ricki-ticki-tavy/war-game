package core.entity.modifiers;

import api.core.Context;
import api.enums.AttributeEnum;
import api.enums.LifeTimeUnit;
import api.enums.ModifierClass;
import api.enums.TargetTypeEnum;
import core.entity.base.BaseModifier;

/**
 * Огненное воздействие
 */
public class FireModifier extends BaseModifier {

  /**
   * Основной конструктор огненного воздействия
   * @param probability
   * @param luck
   * @param minDamage
   * @param maxDamage
   */
  public FireModifier(
          Context context
          , int probability
          , int luck
          , int minDamage
          , int maxDamage) {
    super(context, "Огонь", "Урон огнем", TargetTypeEnum.ENEMY_WARRIOR, ModifierClass.FIRE, AttributeEnum.HEALTH, minDamage, maxDamage, probability, luck);
  }
}
