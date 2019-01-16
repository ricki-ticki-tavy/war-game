package api.old.rule.weapon;

import javax.xml.bind.annotation.*;

@XmlType(propOrder = {"minRange", "maxRange", "fadeRangeStart", "fadeDamagePercentPerLength"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "distanceWeapon")
public class DistanceWeapon extends XmlWeapon {
  /**
   * Минимальный расстояние для атаки
   */
  @XmlElement(name = "minRange")
  public int minRange;

  /**
   * Максимальное расстояние для атаки
   */
  @XmlElement(name = "maxRange")
  public int maxRange;

  /**
   * Дальность начала спадания поражения
   */
  @XmlElement(name = "fadeRangeStart")
  public int fadeRangeStart;

  /**
   * процент спадания урона на единицу длины
   */
  @XmlElement(name = "fadeDamagePercentPerLength")
  public int fadeDamagePercentPerLength;
}
