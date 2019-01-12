package api.structure;

import api.rule.attribute.TriggerTarget;

/**
 * триггер, срабатывающий при определенном условии
 */
public interface Trigger {
  TriggerTarget getTrigerType();

  /**
   * срабатывание тригера
   */
  void fireAction();



}

