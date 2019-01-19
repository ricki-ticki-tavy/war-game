package api.entity.warrior;

import api.entity.base.BaseEntityHeader;
import api.game.Coords;
import api.game.map.Player;

import java.util.List;

/**
 * Класс воина на карте
 */
public interface Warrior extends BaseEntityHeader{
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
   * Перемещает юнит в заданные координаты
   * @param coords
   */
  Warrior moveTo(Coords coords);

  /**
   * Получить игрока - владельца юнита
   * @return
   */
  Player getOwner();

  /**
   * Получить координаты юнита
   * @return
   */
  Coords getCoords();
}
