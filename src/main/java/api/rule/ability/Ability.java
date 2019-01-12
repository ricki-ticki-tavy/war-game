package api.rule.ability;

import api.rule.attribute.CreatureTargeter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Спосоюность. Любая
 */
@XmlType(propOrder = {"id", "title", "abilityModificators", "target"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ability")
public class Ability {

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
  public List<Modifier> abilityModificators;

  /**
   * На кого применяется способность в целом
   */
  @XmlElement(name = "target")
  public CreatureTargeter target;

}
