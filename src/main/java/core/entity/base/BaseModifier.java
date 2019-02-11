package core.entity.base;

import api.core.Context;
import api.core.IntParam;
import api.core.Result;
import api.entity.ability.Modifier;
import api.enums.AttributeEnum;
import api.enums.TargetTypeEnum;
import core.system.ResultImpl;

public class BaseModifier implements Modifier {

  protected TargetTypeEnum target;
  protected String title;
  protected String description;
  protected AttributeEnum attribute;
  protected int probability;
  protected int minValue, maxValue;
  protected Context context;
  protected int calculatedValue;


  public BaseModifier(){}
  //===================================================================================================

  @Override
  public TargetTypeEnum getTarget() {
    return target;
  }
  //===================================================================================================

  public BaseModifier(TargetTypeEnum target) {
    this.target = target;
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
    if (probability == 100 || context.getCore().getRandom(0, 100) <= probability){
      calculatedValue = minValue == maxValue
              ? minValue
              : context.getCore().getRandom(minValue, maxValue);

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
          , AttributeEnum attribute, int minValue, int maxValue
          , int probability) {
    this.target = target;
    this.title = title;
    this.description = description;
    this.attribute = attribute;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.probability = probability;
    this.context = context;
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

}
