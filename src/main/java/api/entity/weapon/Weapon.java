package api.entity.weapon;

import api.entity.ability.Modifier;
import api.entity.base.BaseEntityHeader;

import java.util.List;

/**
 * базовое боевое снаряжение
 */
public interface Weapon extends BaseEntityHeader{
  /**
   * минимальный урон рукопашной атаки
   */
  int getMeleeMinDamage();

  /**
   * минимальный урон дистанционной атаки
   */
  int getRangedMinDamage();

  /**
   * Максимальный урон рукопашной атаки
   */
  int getMeleeMaxDamage();

  /**
   * Максимальный урон дистанционной атаки
   */
  int getRangedMaxDamage();

  /**
   * Стоимость атаки в единицах действия
   */
  int getBitCost();

  /**
   * Дополнительные влияния
   */
  List<Modifier> getAdditionalModifiers();

  /**
   * Невозможность отразить удар
   */
  boolean isUnrejectable();

  /**
   * Допустимое кол-во применений за ход. 0 - бесконечность
   * @return
   */
  int getUseCountPerRound();

  /**
   * Максимальное кол-во использований дистанционного за игру. 0 - бесконечность
   * @return
   */
  int getRangedTotalUseCount();

  /**
   * Минимальный расстояние для атаки
   */
  int getMinRange();

  /**
   * Максимальное расстояние для атаки
   */
  int getMaxRange();

  /**
   * Дальность начала спадания поражения
   */
  int getFadeRangeStart();

  /**
   * процент спадания урона на единицу длины
   */
  int getFadeDamagePercentPerLength();

  /**
   * Может ли применять дистанционную атаку
   * @return
   */
  boolean isCanDealRangedDamage();

  /**
   * Может ли наносить атаку в ближнем бою
   * @return
   */
  boolean canDealMelleDamage();

  /**
   * Возвращает сколько рук нужно для данного оружия
   * @return
   */
  int getNeededHandsCountToTakeWeapon();

}
