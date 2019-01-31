package api.entity.warrior;

import api.core.Result;
import api.entity.base.BaseEntityHeader;
import api.entity.weapon.Weapon;
import api.game.Coords;
import api.game.map.Player;

import java.util.List;

/**
 * Класс воина на карте
 */
public interface Warrior extends BaseEntityHeader, HasCoordinates{
  /**
   * Получить базовый класс
   * @return
   */
  WarriorBaseClass getWarriorBaseClass();

  /**
   * Призванный воин
   * @return
   */
  boolean isSummoned();

  /**
   * Возвращает руки воина со всем снаряжением в них
   * @return
   */
  List<WarriorSHand> getHands();

  /**
   * Получить оружие юнита
   * @return
   */
  List<Weapon> getWeapons();

  /**
   * Перемещает юнит в заданные координаты
   * @param coords
   */
  Result<Warrior> moveTo(Coords coords);

  /**
   * Получить игрока - владельца юнита
   * @return
   */
  Player getOwner();

  /**
   * Взять в руку оружие
   * @param weaponClass
   * @return
   */
  Result<Weapon> takeWeapon(Class<? extends Weapon> weaponClass);

  /**
   * Бросить оружие. Передается id экземпляра оружия, которое надо бросить
   * @param weaponInstanceId
   * @return
   */
  Result<Weapon> dropWeapon(String weaponInstanceId);

  /**
   * Получить значения атрибутов этого юнита
   * @return
   */
  Result<WarriorSBaseAttributes> getAttributes();

  /**
   * Подготовка воина перед ходом игрока. Восстановление различных параметров до нормальных значений
   * @return
   */
  Result<Warrior> prepareToAttackPhase();

  /**
   * Подготовка параметров юнита к фазе защиты. То есть когда ход игрока-владельца юнита закончен и ход переходит
   * к следующему игроку
   * @return
   */
  Result<Warrior> prepareToDefensePhase();


}
