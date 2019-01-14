package api.rule.creature;

import api.rule.ability.CreatureAbility;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Базовый класс воина
 */
@XmlType(propOrder = {"id", "title", "actionPoints", "armorClass", "costMove", "maxHitPoints"
        , "maxMannaPoints", "hands", "abilityActionPoints", "abilities"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "warriorClass")
public class BaseWarriorClass {

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
   * руки воина
   */
  @XmlElementWrapper(name = "hands")
  @XmlElement(name= "hand")
  public List<Hand> hands;

  /**
   * Кол-во очков применения способностей за ход
   */
  @XmlElement(name= "abilityActionPoints", defaultValue = "1")
  public int abilityActionPoints;

  /**
   * Способности создания
   */
  @XmlElementWrapper(name = "abilities")
  @XmlElement(name= "creatureAbility")
  public List<CreatureAbility> abilities;

}
