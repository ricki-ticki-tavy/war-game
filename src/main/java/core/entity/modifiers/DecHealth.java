package core.entity.modifiers;

import api.core.Context;
import api.enums.AttributeEnum;
import api.enums.TargetTypeEnum;
import core.entity.base.BaseModifier;


public class DecHealth extends BaseModifier {
  public DecHealth(Context context, int minValue, int maxValue){
    super(context, "Понижение здоровья", "", TargetTypeEnum.ENEMY_WARRIOR, AttributeEnum.HEALTH, minValue, maxValue, 100);
  }
}
