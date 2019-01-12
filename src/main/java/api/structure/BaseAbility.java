package api.structure;

import api.rule.attribute.TriggerTarget;

/**
 * базовая спосоность
 */
public interface BaseAbility extends NamedObject{
  /**
   * Возвращает когда активна способность
   * @return
   */
  TriggerTarget getTriggerType();

}
