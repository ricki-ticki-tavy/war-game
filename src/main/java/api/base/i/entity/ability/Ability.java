package api.base.i.entity.ability;

import api.base.i.core.Context;
import api.base.i.entity.BaseEntityHeader;
import api.base.i.enums.EventEnum;

import java.util.List;

/**
 * Способность
 */
public interface Ability extends BaseEntityHeader {
  /**
   * применить способность
   * @return
   */
  boolean applay(Context context);

  /**
   * Отменить действие способности
   * @param context
   * @return
   */
  boolean rollback(Context context);

  /**
   * Доступна ли способность в данном собитии
   * @param triggerType
   * @return
   */
  boolean isValidForEvent(EventEnum triggerType);

  /**
   * Вернуть все события на которые способность оказывает влияние
   * @return
   */
  List<EventEnum> getAllTriggerTypes();
}
