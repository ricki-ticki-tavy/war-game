package api.core;

import api.entity.base.BaseEntityHeader;
import api.enums.OwnerTypeEnum;

/**
 * Именуемый (имеющий код,название, описание ) объект, умеющий давать ссылку на контекст
 */
public interface Owner extends BaseEntityHeader{
  /**
   * Вернуть контекст
   * @return
   */
  Context getContext();

  /**
   * Вернуть свой тип
   * @return
   */
  OwnerTypeEnum getThisOwnerType();

  /**
   * Вернуть своего владельца
   * @return
   */
  Owner getOwner();
}
