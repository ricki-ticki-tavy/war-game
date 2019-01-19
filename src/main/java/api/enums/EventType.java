package api.enums;

/**
 * Тип срабатывания триггера
 */
public enum EventType {
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
  DEFENCE_BEFORE("После защиты")

  ,WARRIOR_ADDED("В игре '%s' (контекст '%s') игроком '%s' добавлен воин '%s'")
  ,WARRIOR_SUMMONED("В игре '%s' (контекст '%s') игроком '%s' призван воин '%s' с '%s' очками жизни")
  ,PLAYER_REMOVED("Игрок '%s' покинул игру '%s'  (контекст '%s')")
  , WARRIOR_MOVED("В игре '%s' игрок '%s' переместил юнит '%s' на координаты '%s'")
  ;

  private String caption;

  public String getTitle() {
    return caption;
  }

  public String getFormattedMessage(String... args) {
    return String.format(getTitle(), args);
  }

  EventType(String caption){
    this.caption = caption;
  }
}
