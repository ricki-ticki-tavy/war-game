package api.old.game;

import api.old.rule.ability.CreatureAbility;
import api.old.rule.weapon.XmlWeapon;
import api.old.rule.creature.BaseWarriorClass;

import java.util.List;

public class Creature {

  /**
   * Базовый класс воина
   */
  BaseWarriorClass parentCreatureClass;

  /**
   * Имя персонажа
   */
  String name;

  /**
   * координаты на поле
   */
  int x, y;

  /**
   * Остаток очков действия в этом ходе
   */
  int actionPoints;

  /**
   * цена передвижения на текущий ход
   */
  int costMoveCurrent;

  /**
   * очки жизни
   */
  int hitPoints;

  /**
   * Очки магии
   */
  int mannaPoints;

  /**
   * Допустимые в динном ходе виды атак ЭТОГО создания
   */
  List<XmlWeapon> attackMethods;

  /**
   * Способности ЭТОГО создания
   */
  List<CreatureAbility> abilities;



}
