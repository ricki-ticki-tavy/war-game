package api.game.action;

import api.game.ability.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.core.Event;
import api.game.map.Player;

import java.util.List;

/**
 * Результат воздействия, атаки
 */
public interface InfluenceResult {

  /**
   * Получить воздействовавшего, атаковавшего юнита. Может быть NULL если атаковал сам игрок с помощью магии
   * @return
   */
  Warrior getActor();

  /**
   * Получить оружие с помощью которого была выполнена атака. Может быть NULL в случае атаки магией
   * @return
   */
  Weapon getAttackerWeapon();

  /**
   * Получить атаковавшего игрока
   * @return
   */
  Player getActorPlayer();

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
  InfluenceResult addInfluencer(Influencer influencer);

  /**
   * Получить кол-во очков, затраченных на атаку
   * @return
   */
  int getConsumedActionPoints();
}

