package api.rule.game;

import javax.xml.bind.annotation.*;

/**
 * Системные настройки разные
 */
@XmlType(propOrder = {"lengthOfSimpleLengthUnit", "luckProbability", "attribute", "minValue", "maxValue"
        , "probability", "duration", "target"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "modifier")
public class SystemRules {
  /**
   * длина единичного отрезка длины в точках карты.
   */
  @XmlElement(name = "lengthOfSimpleLengthUnit")
  public int lengthOfSimpleLengthUnit;

  /**
   * Вероятность удачив процентах при выполнении действия
   */
  @XmlElement(name = "luckProbability")
  public int luckProbability;


}
