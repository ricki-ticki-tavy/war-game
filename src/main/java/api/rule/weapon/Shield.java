package api.rule.weapon;

import javax.xml.bind.annotation.*;

/**
 * Щиты и доп.броня
 */
@XmlType(propOrder = {"armorClass", "probabilityToRejectMeleeAttack", "probabilityToRejectDistanceAttack"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Shield extends Weapon{

  @XmlElement(name = "armorClass", defaultValue = "0")
  public int armorClass;

  @XmlElement(name = "probabilityToRejectMeleeAttack")
  public int probabilityToRejectMeleeAttack;

  @XmlElement(name = "probabilityToRejectDistanceAttack")
  public int probabilityToRejectDistanceAttack;
}
