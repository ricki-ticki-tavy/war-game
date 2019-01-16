package api.old.rule.ability;

import api.game.war.enums.AttributeEnum;
import api.old.rule.attribute.CreatureTargeter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Изменение какого-либо параметра
 */
@XmlType(propOrder = {"id", "title", "attribute", "minValue", "maxValue"
        , "probability", "duration", "target"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "modifier")
public class Modifier implements Serializable{

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
  public AttributeEnum attribute;

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
   * длительность оказанного влияния в полных игровых кругах. -1, то влияние выполнено разово и его последствия
   * не должны в альнейшем компенсироваться, 0 - параметр восстановить в конце хода. далее - кол-во кругов, начиная с текущего
   * хода игрока
   */
  @XmlElement(name = "duration", defaultValue = "0")
  public int duration;

  /**
   * На кого оказывается влияние. По умолчанию TARGETED
   */
  @XmlElement(name = "target", defaultValue = "TARGETED")
  public CreatureTargeter target;
}
