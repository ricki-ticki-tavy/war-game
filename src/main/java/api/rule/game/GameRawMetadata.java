package api.rule.game;

import api.rule.ability.Ability;
import api.rule.ability.Modifier;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(propOrder = {"lengthOfSimpleLengthUnit", "modifiers", "abilities"})
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

  /**
   * базовые типы созданий, их характеристики и способности
   */
//  public List<BaseCreatureClass> baseCreatureClasses;


  /**
   * параметры и способности игрока
   */
//  public BasePlayer basePlayer;


}
