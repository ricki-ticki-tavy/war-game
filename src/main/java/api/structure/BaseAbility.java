package api.structure;

import api.rule.attribute.TriggerType;

/**
 * базовая спосоность
 */
public interface BaseAbility extends NamedObject{
  /**
   * Возвращает когда активна способность
   * @return
   */
  TriggerType getTriggerType();

}
