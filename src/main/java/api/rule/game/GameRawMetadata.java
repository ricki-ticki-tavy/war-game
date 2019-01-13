package api.rule.game;

import api.rule.ability.Ability;
import api.rule.ability.Modifier;
import api.rule.weapon.BaseWeapon;
import api.rule.weapon.DistanceWeapon;
import api.rule.weapon.MeleeWeapon;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(propOrder = {"lengthOfSimpleLengthUnit", "modifiers", "abilities", "weapons"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "gameRules")
public class GameRawMetadata {
  /**
   * длина единичного отрезка длины в точках карты.
   */
  public int lengthOfSimpleLengthUnit;

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
          , @XmlElementRef(name = "distanceWeapon", type = DistanceWeapon.class)})
  public List<BaseWeapon> weapons;

  /**
   * базовые типы созданий, их характеристики и способности
   */
//  public List<BaseCreatureClass> baseCreatureClasses;


  /**
   * параметры и способности игрока
   */
//  public BasePlayer basePlayer;


}
