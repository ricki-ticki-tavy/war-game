package api.old.rule.creature;

import api.old.rule.weapon.XmlWeapon;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * что в руке воина
 */
@XmlType(propOrder = {"weapons"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Hand {
  @XmlElement(name= "weapon")
  public List<XmlWeapon> weapons;

}
