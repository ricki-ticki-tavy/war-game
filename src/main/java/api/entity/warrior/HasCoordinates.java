package api.entity.warrior;

import api.geo.Coords;

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
