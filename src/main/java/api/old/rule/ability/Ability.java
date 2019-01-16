package api.old.rule.ability;

import api.old.rule.attribute.CreatureTargeter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Спосоюность. Любая
 */
@XmlType(propOrder = {"id", "title", "abilityModifiers", "target"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ability")
public class Ability  implements Serializable {

  @XmlAttribute(name = "ref")
  public String ref = null;

  @XmlElement(name = "id")
  @XmlID
  public String id;

  /**
   * Название способности
   */
  @XmlElement(name = "title")
  public String title;

  /**
   * что меняет способность
   */
  @XmlElementWrapper(name = "modifiers")
  @XmlElement(name = "modifier")
  public List<Modifier> abilityModifiers;

  /**
   * На кого применяется способность в целом
   */
  @XmlElement(name = "target")
  public CreatureTargeter target;

}
