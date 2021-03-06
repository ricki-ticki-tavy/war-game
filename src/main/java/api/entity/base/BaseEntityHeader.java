package api.entity.base;

/**
 * Базовые поля почти всех сущностей
 */
public interface BaseEntityHeader {
  /**
   * Получить код
   * @return
   */
  String getId();

  /**
   * Название
   * @return
   */
  String getTitle();

  /**
   * Описание
   * @return
   */
  String getDescription();
}
