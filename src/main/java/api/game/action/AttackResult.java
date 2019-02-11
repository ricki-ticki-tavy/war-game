package api.game.action;

import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Event;
import api.game.EventDataContainer;
import api.game.map.Player;

import java.util.List;

/**
 * Результат атаки
 */
public interface AttackResult {

  /**
   * Получить атаковавшего юнита. Может быть NULL если атаковал сам игрок с помощью магии
   * @return
   */
  Warrior getAttacker();

  /**
   * Получить оружие с помощью которого была выполнена атака. Может быть NULL в случае атаки магией
   * @return
   */
  Weapon getAttackerWeapon();

  /**
   * Получить атаковавшего игрока
   * @return
   */
  Player getAttackerPlayer();

  /**
   * Получить атакованного игрока
   * @return
   */
  Player getTargetPlayer();

  /**
   * Получить того, кого атаковали
   * @return
   */
  Warrior getTarget();

  /**
   * Получить сообщение, топравленное при атаке
   * @return
   */
  Event getEvent();

  /**
   * Возвращает влияния на цель. В том числе и прямое поражение от оружия
   * @return
   */
  List<Influencer> getInfluencers();

  /**
   * Добавить влияние атаки на цель
   * @param influencer
   * @return
   */
  AttackResult addInfluencer(Influencer influencer);

}
