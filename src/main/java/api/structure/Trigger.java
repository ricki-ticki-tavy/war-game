package api.structure;

import api.rule.attribute.TriggerType;

/**
 * триггер, срабатывающий при определенном условии
 */
public interface Trigger {
  TriggerType getTrigerType();

  /**
   * срабатывание тригера
   */
  void fireAction();



}

