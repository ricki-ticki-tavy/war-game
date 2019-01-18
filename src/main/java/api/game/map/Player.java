package api.game.map;

import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.game.Rectangle;

import java.util.List;

/**
 * Игрок
 */
public interface Player<T> extends BaseEntityHeader{

  /**
   * Добавить в коллекцию воина
   * @param warrior
   * @return
   */
  T addWarrior(T warrior);

  /**
   *  Вернуть всех воинов
   * @return
   */
  List<T> getWarriors();

  /**
   * Задать зону выставления воинов
   * @param startZone
   */
  void setStartZone(Rectangle startZone);


  /**
   * Получить зону выставления фигурок
   * @return
   */
  Rectangle getStartZone();

}
