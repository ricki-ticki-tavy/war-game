package api.rule.game;

import api.rule.ability.Ability;
import api.rule.ability.Modifier;
import api.rule.weapon.BaseWeapon;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePreparedMetadata {
  /**
   * длина единичного отрезка длины в точках карты.
   */
  public int lengthOfSimpleLengthUnit;

  /**
   * Все возможные модификаторы
   */
  public Map<String, Modifier> modifiers = new ConcurrentHashMap<>();

  /**
   * Способности. разные
   */
  public Map<String, Ability> abilities = new ConcurrentHashMap<>();

  /**
   * Виды оружия
   */
  public Map<String, BaseWeapon> weapons = new ConcurrentHashMap<>();
  /**
   * базовые типы созданий, их характеристики и способности
   */
//  public List<BaseCreatureClass> baseCreatureClasses;


  /**
   * параметры и способности игрока
   */
//  public BasePlayer basePlayer;


}
