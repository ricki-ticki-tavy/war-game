package api.rule.creature;

import api.rule.weapon.Weapon;

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
  public List<Weapon> weapons;

}
