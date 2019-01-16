package api.old.structure;

import api.game.war.enums.EventType;

/**
 * триггер, срабатывающий при определенном условии
 */
public interface Trigger {
  EventType getTrigerType();

  /**
   * срабатывание тригера
   */
  void fireAction();



}

