package core.entity.base;

import api.core.Context;
import api.entity.ability.Modifier;
import api.enums.AttributeEnum;
import api.enums.TargetTypeEnum;

public abstract class AbstractModifier implements Modifier {

  protected TargetTypeEnum target;
  protected String title;
  protected String description;
  protected AttributeEnum attribute;
  protected int probability;
  protected int minValue, maxValue;
  protected Context context;

  @Override
  public TargetTypeEnum getTarget() {
    return target;
  }

  public AbstractModifier(TargetTypeEnum target) {
    this.target = target;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public AttributeEnum getAttribute() {
    return attribute;
  }

  @Override
  public int getProbability() {
    return 0;
  }

  @Override
  public int getValue() {
    return minValue == maxValue
            ? minValue
            : context.getCore().getRandom(minValue, maxValue);
  }

  @Override
  public int getMinValue() {
    return 0;
  }

  @Override
  public int getMaxValue() {
    return maxValue;
  }

  public AbstractModifier(Context context
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
  }

  @Override
  public Context getContext() {
    return context;
  }

  public AbstractModifier(){}
}
