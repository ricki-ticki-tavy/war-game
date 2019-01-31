package api.entity.warrior;

import api.game.Coords;

/**
 * класс, имеющий координаты
 */
public interface HasCoordinates {

  /**
   * Получить координаты юнита
   * @return
   */
  Coords getCoords();
}
