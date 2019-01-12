package api.game;

import api.rule.ability.CreatureAbility;
import api.rule.attack.BaseAttack;
import api.rule.creature.BaseCreatureClass;

import java.util.List;

public class Creature {

  /**
   * Базовый класс воина
   */
  BaseCreatureClass parentCreatureClass;

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
  List<BaseAttack> attackMethods;

  /**
   * Способности ЭТОГО создания
   */
  List<CreatureAbility> abilities;



}
