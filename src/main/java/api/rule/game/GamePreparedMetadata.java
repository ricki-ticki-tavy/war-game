package api.rule.game;

import api.rule.ability.Ability;
import api.rule.ability.Modifier;

import java.util.HashMap;
import java.util.Map;

public class GamePreparedMetadata {
  /**
   * длина единичного отрезка длины в точках карты.
   */
  public int lengthOfSimpleLengthUnit;

  /**
   * Все возможные модификаторы
   */
  public Map<String, Modifier> modifiers = new HashMap<>();

  /**
   * Способности. разные
   */
  public Map<String, Ability> abilities = new HashMap<>();

  /**
   * базовые типы созданий, их характеристики и способности
   */
//  public List<BaseCreatureClass> baseCreatureClasses;


  /**
   * параметры и способности игрока
   */
//  public BasePlayer basePlayer;


}
