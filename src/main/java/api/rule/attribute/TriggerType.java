package api.rule.attribute;

import api.structure.NamedObject;

/**
 * Тип срабатывания триггера
 */
public enum TriggerType implements NamedObject {
  ON_PLAYER_ROUND("При ходе игрока"),
  ALLWAYS("Всегда"),
  ROUND_FULL("игровой круг"),
  ROUND_START("в начале хода игрока"),
  ROUND_END("в конце хода игрока"),
  ATACK_MELEE_AFTER("После каждого рукопашного удара при атаке"),
  ATACK_MELEE_BEFORE("Перед каждым рукопашным ударом при атаке"),
  ATACK_DISTANCE_AFTER("После каждого дистанционного удара при атаке"),
  ATACK_DISTANCE_BEFORE("Перед каждым дистанционным ударом при атаке"),
  ATACK_AFTER_EVERY_BIT("После каждого удара при атаке"),
  ATACK_BEFORE_EVERY_BIT("Перед каждым ударом при атаке"),
  DEFENCE_AFTER("Перед защитой"),
  DEFENCE_BEFORE("После защиты");

  private String caption;

  @Override
  public String getTitle() {
    return caption;
  }

  TriggerType(String caption){
    this.caption = caption;
  }
}
