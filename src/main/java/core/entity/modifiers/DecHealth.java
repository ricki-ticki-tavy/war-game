package core.entity.modifiers;

import api.base.i.core.Context;
import api.base.i.enums.AttributeEnum;
import api.base.i.enums.TargetTypeEnum;
import core.entity.base.AbstractModifier;


public class DecHealth extends AbstractModifier{
  public DecHealth(Context context, int minValue, int maxValue){
    super(context, "Понижение здоровья", "", TargetTypeEnum.ENEMY_WARRIOR, AttributeEnum.HEALTH, minValue, maxValue, 100);
  }
}
