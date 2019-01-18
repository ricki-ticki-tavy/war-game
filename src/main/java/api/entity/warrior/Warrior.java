package api.entity.warrior;

/**
 * Класс воина на карте
 */
public interface Warrior extends WarriorBaseClass{
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
}
