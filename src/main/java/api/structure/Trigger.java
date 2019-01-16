package api.structure;

import api.base.i.enums.EventEnum;

/**
 * триггер, срабатывающий при определенном условии
 */
public interface Trigger {
  EventEnum getTrigerType();

  /**
   * срабатывание тригера
   */
  void fireAction();



}

