package api.rule.ability;

import api.rule.attribute.CreatureAttribute;
import api.rule.attribute.CreatureTargeter;

import javax.xml.bind.annotation.*;

/**
 * Изменение какого-либо параметра
 */
@XmlType(propOrder = {"id", "title", "attribute", "minValue", "maxValue"
        , "probability", "duration", "target"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "modifier")
public class Modifier {

  /**
   * для ссылки
   */
  @XmlAttribute(name = "ref")
  public String ref = null;

  /**
   * идентификатор
   */
  @XmlElement(name = "id", required = true)
  @XmlID
  public String id;

  /**
   * Название модификатора
   */
  @XmlElement(name = "title", required = true)
  public String title;

  /**
   * Атрибут, на который влияет модификатор
   */
  @XmlElement(name = "attribute", required = true)
  public CreatureAttribute attribute;

  /**
   * минимальное значение
   */
  @XmlElement(name = "minValue", required = true)
  public int minValue;

  /**
   * Максимальное значение
   */
  @XmlElement(name = "maxValue", defaultValue = "0")
  public int maxValue;

  /**
   * Вероятность успеха.
   */
  @XmlElement(name = "probability", defaultValue = "100")
  public int probability;

  /**
   * длительность оказанного влияния в полных игровых кругах. Если ноль, то влияние выполнено разово и не его последствия
   * не должны в альнейшем компенсироваться
   */
  @XmlElement(name = "duration", defaultValue = "0")
  public int duration;

  /**
   * На кого оказывается влияние. По умолчанию TARGETED
   */
  @XmlElement(name = "target", defaultValue = "TARGETED")
  public CreatureTargeter target;
}
