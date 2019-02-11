package api.core;

import api.entity.base.BaseEntityHeader;

/**
 * Класс-обертка для числа
 */
public interface IntParam extends BaseEntityHeader{
  /**
   * Возвращает значение
   * @return
   */
  int getIntValue();
}
