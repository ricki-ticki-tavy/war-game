package api.rule.weapon;

import api.rule.ability.Modifier;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(propOrder = {"id", "title", "minDamage", "maxDamage", "damageAngle", "mustAttackAllCreatures"
        , "costActionPoints", "additionalModifiers", "unrejectable"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Weapon {

  @XmlAttribute(name = "ref")
  public String ref = null;

  @XmlID
  @XmlElement(name = "id")
  public String id;

  @XmlElement(name = "title")
  public String title;

  /**
   * минимальный урон
   */
  @XmlElement(name = "minDamage")
  public int minDamage;

  /**
   * Максимальный урон
   */
  @XmlElement(name = "maxDamage")
  public int maxDamage;

  /**
   * Угол луча поражения
   */
  @XmlElement(name = "damageAngle")
  public int damageAngle;

  /**
   * Атака ВСЕХ юнитов
   */
  @XmlElement(name = "mustAttackAllCreatures", defaultValue = "false")
  public boolean mustAttackAllCreatures;

  /**
   * Стоимость атаки в единицах действия
   */
  @XmlElement(name = "costActionPoints")
  public int costActionPoints;

  /**
   * Дополнительные влияния
   */
  @XmlElementWrapper(name = "additionalModifiers")
  @XmlElement(name = "modifier")
  public List<Modifier> additionalModifiers;


  /**
   * Невозможность отразить удар
   */
  @XmlElement(name = "unrejectable", defaultValue = "false")
  public boolean unrejectable;

}
