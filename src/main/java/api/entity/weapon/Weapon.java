package api.entity.weapon;

import api.entity.ability.Modifier;
import api.entity.base.BaseEntityHeader;

import java.util.List;

/**
 * базовое боевое снаряжение
 */
public interface Weapon extends BaseEntityHeader{
  /**
   * минимальный урон
   */
  int getMinDamage();

  /**
   * Максимальный урон
   */
  int getMaxDamage();

  /**
   * Стоимость атаки в единицах действия
   */
  int getCost();

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
  int getUseCoountPerRound();

  /**
   * Максимальное кол-во использований за игру. 0 - бесконечность
   * @return
   */
  int getTotalUseCount();

}
