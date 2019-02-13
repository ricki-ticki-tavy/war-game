package api.game;

import api.core.Owner;
import api.core.Result;
import api.game.ability.Modifier;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.game.action.AttackResult;

import java.util.Collection;
import java.util.List;

/**
 * класс описывающий влияние, примененное к юниту
 */
public interface Influencer extends Owner {
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

  /**
   * Добавить зависимое влияние
   * @param influencer
   * @return
   */
  Influencer addChild(Influencer influencer);
  
  /**
   * Добавить зависимое влияния
   * @param influencers
   * @return
   */
  Influencer addChildren(Collection<Influencer> influencers);

  /**
   * вернуть зависимые влияния от данного. Это нужно для того, что если не удалось данное влияние, то и
   * все осатльные тоже не будут оказывать влияния в этои раз
   * @return
   */
  List<Influencer> getChildren();

  /**
   * Возвращает модификаторы, связанные с этим влиянием
   * @return
   */
  Modifier getModifier();

  /**
   * Применить влияние на воина
   * @param attackResult
   * @return
   */
  Result<Warrior> applyToWarrior(AttackResult attackResult);
}
