package api.core;

import api.entity.base.BaseEntityHeader;

/**
 * Именуемый (имеющий код,название, описание ) объект, умеющий давать ссылку на контекст
 */
public interface Owner extends BaseEntityHeader{
  /**
   * Вернуть контекст
   * @return
   */
  Context getContext();
}
