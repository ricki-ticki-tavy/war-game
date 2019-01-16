package api.structure;

import api.base.i.enums.EventEnum;

/**
 * базовая спосоность
 */
public interface BaseAbility extends NamedObject{
  /**
   * Возвращает когда активна способность
   * @return
   */
  EventEnum getTriggerType();

}
