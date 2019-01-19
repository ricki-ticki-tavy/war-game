package core.entity.modifiers;

import api.core.GameContext;
import api.enums.AttributeEnum;
import api.enums.TargetTypeEnum;
import core.entity.base.AbstractModifier;


public class DecHealth extends AbstractModifier{
  public DecHealth(GameContext context, int minValue, int maxValue){
    super(context, "Понижение здоровья", "", TargetTypeEnum.ENEMY_WARRIOR, AttributeEnum.HEALTH, minValue, maxValue, 100);
  }
}
