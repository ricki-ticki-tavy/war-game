package api.entity.warrior;

import api.entity.base.BaseEntityHeader;

/**
 * класс описывающий влияние, примененное к юниту
 */
public interface Influencer extends BaseEntityHeader {
  /**
   * на кого оказывается данное влияние
   * @return
   */
  Warrior getTargetWarrior();

  /**
   * заставить влияние отписаться ото всех событий. необходимо вызывать перед удалением
   */
  void unsubscribe();
}
