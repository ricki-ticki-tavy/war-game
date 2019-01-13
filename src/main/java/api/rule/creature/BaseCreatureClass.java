package api.rule.creature;

import api.rule.ability.CreatureAbility;
import api.rule.weapon.BaseWeapon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Базовый класс воина
 */
@XmlType(propOrder = {"name", "actionPoints", "armorClass", "costMove", "maxHitPoints"
        , "maxMannaPoints", "attackMethods", "abilities"})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BaseCreatureClass")
public class BaseCreatureClass {
  /**
   * Название класса
   */
  String name;

  /**
   * Базовое кол-во очков действия
   */
  int actionPoints;

  /**
   * класс брони
   */
  int armorClass;

  /**
   * цена передвижения
   */
  int costMove;

  /**
   * Максимальное кол-во очков жизни
   */
  int maxHitPoints;

  /**
   * Максимальное кол-во очков магии
   */
  int maxMannaPoints;

  /**
   * Виды атак
   */
  List<BaseWeapon> attackMethods;

  /**
   * Способности создания
   */
  List<CreatureAbility> abilities;

}
