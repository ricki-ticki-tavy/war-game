package api.old.structure;

import api.game.war.enums.EventType;

/**
 * базовая спосоность
 */
public interface BaseAbility extends NamedObject{
  /**
   * Возвращает когда активна способность
   * @return
   */
  EventType getTriggerType();

}
