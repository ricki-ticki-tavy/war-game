package api.rule.creature;

import api.rule.ability.Ability;
import api.rule.ability.CreatureAbility;
import api.rule.weapon.Weapon;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Базовый класс воина
 */
@XmlType(propOrder = {"id", "title", "actionPoints", "armorClass", "costMove", "maxHitPoints"
        , "maxMannaPoints", "weapons", "abilities"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "creatureClass")
public class BaseCreatureClass {

  @XmlAttribute(name = "ref")
  public String ref = null;

  @XmlID
  @XmlElement(name= "id")
  public String id;

  /**
   * Название класса
   */
  @XmlElement(name= "title")
  public String title;

  /**
   * Базовое кол-во очков действия
   */
  @XmlElement(name= "actionPoints")
  public int actionPoints;

  /**
   * класс брони
   */
  @XmlElement(name= "armorClass")
  public int armorClass;

  /**
   * цена передвижения
   */
  @XmlElement(name= "costMove")
  public int costMove;

  /**
   * Максимальное кол-во очков жизни
   */
  @XmlElement(name= "maxHitPoints")
  public int maxHitPoints;

  /**
   * Максимальное кол-во очков магии
   */
  @XmlElement(name= "maxMannaPoints")
  public int maxMannaPoints;

  /**
   * Виды атак
   */
  @XmlElementWrapper(name = "weapons")
  @XmlElement(name= "weapon")
  public List<Weapon> weapons;

  /**
   * Способности создания
   */
  @XmlElementWrapper(name = "abilities")
  @XmlElement(name= "ability")
  public List<Ability> abilities;

}
