package api.structure;

/**
 * Активный объект. Способный взаимодействовать на карте
 */
public interface ActiveObject {
  /**
   * Оставшееся кол-во очков действия
   * @return
   */
  int getActionPoints();

  /**
   * изменить кол-во очков действия на заданное значение
   * @param deltaActionPoints
   * @return
   */
  int addAndGetActionPoints(int deltaActionPoints);
}
