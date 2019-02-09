package api.entity.weapon;

import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.game.action.AttackResult;
import api.game.map.Player;

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
   * Стоимость дистанционной атаки в единицах действия
   */
  int getRangedAttackCost();

  /**
   * Стоимость рукопашной атаки в единицах действия
   */
  int getMeleeAttackCost();

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
  int getRangedAttackMinRange();

  /**
   * Максимальное расстояние для атаки
   */
  int getRangedAttackMaxRange();

  /**
   * Дистанция с которой уже может наноситься рукопашная атака
   * @return
   */
  int getMeleeAttackRange();

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

  /**
   * Получить воина, владеющего снаряжением
   * @return
   */
  Warrior getOwner();

  /**
   * Атаковать противника
   * @param targetWarrior
   */
  Result<AttackResult> attack(Warrior targetWarrior);

  /**
   * задать  владельца снаряжения
   * @param owner
   * @return
   */
  Weapon setOwner(Warrior owner);

}
