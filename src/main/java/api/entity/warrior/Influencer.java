package api.entity.warrior;

import api.core.Result;
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
   * Удаление влияния у юнита
   * @return
   */
  Result<Influencer> removeFromWarrior(boolean silent);
}
