package api.entity.warrior;

import api.entity.base.BaseEntityHeader;
import api.game.Coords;

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
   * Задать начальные координаты
   * @param coords
   */
  void initCoords(Coords coords);

}
