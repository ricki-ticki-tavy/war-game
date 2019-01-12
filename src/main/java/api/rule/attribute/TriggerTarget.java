package api.rule.attribute;

import api.structure.NamedObject;

/**
 * Тип срабатывания триггера
 */
public enum TriggerTarget implements NamedObject {
  ROUND_FULL("игровой круг"),
  ROUND_START("в начале хода игрока"),
  ROUND_END("в конце хода игрока"),
  ATACK_AFTER_EVERY_BIT("После каждого удара при атаке"),
  ATACK_BEFORE_EVERY_BIT("Перед каждым ударом при атаке"),
  DEFENCE_AFTER_EVERY_BIT("После каждого удара при атаке")

  ;

  private String caption;

  @Override
  public String getCption() {
    return caption;
  }

  TriggerTarget(String caption){
    this.caption = caption;
  }
}
