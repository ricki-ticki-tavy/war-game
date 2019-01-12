package api.structure;

/**
 * Объект, способный перемещаться на карте
 */
public interface MovableObject extends MaterialObject, ActiveObject {
  /**
   * Возвращает стоимость, в очках действия, за перемещение на единицу длины
   * @return
   */
  int getMoveCost();

  /**
   * Изменить на время стоимость перемещения
   * @param deltaCost
   * @return
   */
  int addAndGetMoveCost(int deltaCost);

  /**
   * Сброс параметров движения на изначальные для данного
   */
  void resetMoveAbility();

  /**
   * Переместить объект в новую позицию
   * @param x
   * @param y
   * @return
   */
  boolean doMove(int x, int y);
}
