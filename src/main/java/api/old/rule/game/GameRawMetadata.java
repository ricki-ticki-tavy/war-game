package api.old.rule.game;

import api.old.rule.ability.Ability;
import api.old.rule.ability.Modifier;
import api.old.rule.creature.BaseWarriorClass;
import api.old.rule.weapon.Shield;
import api.old.rule.weapon.XmlWeapon;
import api.old.rule.weapon.DistanceWeapon;
import api.old.rule.weapon.MeleeWeapon;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(propOrder = {"systemRules", "modifiers", "abilities", "weapons", "baseWarriorClasses"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "gameRules")
public class GameRawMetadata {
  /**
   * длина единичного отрезка длины в точках карты.
   */
  @XmlElement(name = "systemRules")
  public SystemRules systemRules;

  /**
   * Все возможные модификаторы
   */
  @XmlElementWrapper(name = "modifiers")
  @XmlElement(name = "modifier")
  public List<Modifier> modifiers;

  /**
   * Способности. разные
   */
  @XmlElementWrapper(name = "abilities")
  @XmlElement(name = "ability")
  public List<Ability> abilities;

  @XmlElementWrapper(name = "weapons")
  @XmlElementRefs({@XmlElementRef(name = "meleeWeapon", type = MeleeWeapon.class)
          , @XmlElementRef(name = "distanceWeapon", type = DistanceWeapon.class)
          , @XmlElementRef(name = "shield", type = Shield.class)
  })
  public List<XmlWeapon> weapons;

  /**
   * базовые типы созданий, их характеристики и способности
   */
  @XmlElementWrapper(name = "baseWarriorClasses")
  @XmlElement(name = "baseWarriorClass")
  public List<BaseWarriorClass> baseWarriorClasses;


  /**
   * параметры и способности игрока
   */
//  public BasePlayer basePlayer;


}
