package core.entity.base;

import api.core.Context;
import api.core.EventDataContainer;
import api.core.Result;
import api.entity.warrior.WarriorSBaseAttributes;
import api.enums.EventType;
import api.game.ability.Modifier;
import api.enums.AttributeEnum;
import api.enums.ModifierClass;
import api.enums.TargetTypeEnum;
import api.game.action.AttackResult;
import core.system.ResultImpl;

import static api.enums.AttributeEnum.HEALTH;

public class BaseModifier implements Modifier {

  protected TargetTypeEnum target;
  protected String title;
  protected String description;
  protected AttributeEnum attribute;
  protected int probability;
  protected int minValue, maxValue;
  protected Context context;
  protected int calculatedValue;
  protected ModifierClass modifierClass;
  protected int luck;
  protected boolean luckyRollOfDice = false;
  protected boolean hitSuccess = false;


  public BaseModifier() {
  }
  //===================================================================================================

  @Override
  public TargetTypeEnum getTarget() {
    return target;
  }
  //===================================================================================================

  @Override
  public String getTitle() {
    return title;
  }
  //===================================================================================================

  @Override
  public String getDescription() {
    return description;
  }
  //===================================================================================================

  @Override
  public AttributeEnum getAttribute() {
    return attribute;
  }
  //===================================================================================================

  @Override
  public int getProbability() {
    return 0;
  }
  //===================================================================================================

  @Override
  public Result<Integer> getValue() {
    luckyRollOfDice = luck > 0 && luck >= context.getCore().getRandom(1, 100);
    hitSuccess = probability == 100 || context.getCore().getRandom(0, 100) <= probability;
    // вероятность попадания или удача
    if (hitSuccess || luckyRollOfDice) {
      calculatedValue = minValue == maxValue
              ? minValue
              : context.getCore().getRandom(minValue, maxValue);
      // если была удача, то удвоим его, либо сделаем результат максимальным, если удвенное значение менее максимума
      if (luckyRollOfDice) {
        calculatedValue *= 2;
        if (calculatedValue < maxValue) {
          calculatedValue = maxValue;
        }
      }
    } else {
      calculatedValue = 0;
    }
    return ResultImpl.success(calculatedValue);
  }
  //===================================================================================================

  @Override
  public int getMinValue() {
    return minValue;
  }
  //===================================================================================================

  @Override
  public int getMaxValue() {
    return maxValue;
  }
  //===================================================================================================

  public BaseModifier(Context context
          , String title, String description, TargetTypeEnum target
          , ModifierClass modifierClass
          , AttributeEnum attribute, int minValue, int maxValue
          , int probability, int luck) {
    this.target = target;
    this.title = title;
    this.description = description;
    this.attribute = attribute;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.probability = probability;
    this.context = context;
    this.modifierClass = modifierClass;
    this.luck = luck;
    getValue();
  }
  //===================================================================================================


  @Override
  public Context getContext() {
    return context;
  }
  //===================================================================================================

  @Override
  public int getLastCalculatedValue() {
    return calculatedValue;
  }
  //===================================================================================================

  @Override
  public Modifier setLastCalculatedValue(int value) {
    this.calculatedValue = value;
    return this;
  }
  //===================================================================================================

  @Override
  public ModifierClass getModifierClass() {
    return modifierClass;
  }
  //===================================================================================================

  @Override
  public int getLuck() {
    return luck;
  }
  //===================================================================================================

  @Override
  public boolean isLuckyRollOfDice() {
    return luckyRollOfDice;
  }
  //===================================================================================================

  @Override
  public boolean isHitSuccess() {
    return hitSuccess;
  }
  //===================================================================================================

  @Override
  public Modifier addLuck(int delta) {
    luck += delta;
    return this;
  }
  //===================================================================================================

  @Override
  public Result<Modifier> apply(AttackResult attackResult) {
    WarriorSBaseAttributes attributes = attackResult.getTarget().getAttributes();
    // Уже всерассчитано. Применяем значение, рассчитанное заранее (getLastCalculatedValue())
    if (getTarget() == TargetTypeEnum.ENEMY_WARRIOR) {
      switch (getAttribute()) {
        case HEALTH:
          attributes.addHealth(- calculatedValue);
          // Отправим сообщение
          attackResult.getTarget().getContext()
                  .fireGameEvent(null
                          , EventType.WARRIOR_WAS_ATTACKED_BY_ENEMY
                          , new EventDataContainer(attackResult, this)
                          , null);
          break;
      }
    }
    return ResultImpl.success(this);
  }
  //===================================================================================================

}
