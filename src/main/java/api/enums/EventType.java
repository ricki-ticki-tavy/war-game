package api.enums;

/**
 * Тип срабатывания триггера
 */
public enum EventType {
  ON_PLAYER_ROUND("При ходе игрока"),
  ALWAYS("Всегда"),
  ROUND_FULL("игровой круг"),
  ROUND_START("в начале хода игрока"),
  ROUND_END("в конце хода игрока"),
  ATTACK_MELEE_AFTER("После каждого рукопашного удара при атаке"),
  ATTACK_MELEE_BEFORE("Перед каждым рукопашным ударом при атаке"),
  ATTACK_DISTANCE_AFTER("После каждого дистанционного удара при атаке"),
  ATTACK_DISTANCE_BEFORE("Перед каждым дистанционным ударом при атаке"),
  ATTACK_AFTER_EVERY_BIT("После каждого удара при атаке"),
  ATTACK_BEFORE_EVERY_BIT("Перед каждым ударом при атаке"),
  DEFENCE_AFTER("Перед защитой"),
  DEFENCE_BEFORE("После защиты")

  ,PLAYER_ADDED("Игрок '%s' присоединился к игре '%s'  (контекст '%s'). Теперь в игре %s игроков из %s")
  ,PLAYER_CONNECTED("Игрок '%s' повторно подключился к игре '%s'  (контекст '%s'). В игре %s игроков из %s")
  ,PLAYER_REMOVED("Игрок '%s' покинул игру '%s'  (контекст '%s'). Теперь в игре %s игроков из %s")
  ,WARRIOR_ADDED("В игре '%s' (контекст '%s') игроком '%s' добавлен воин '%s'")
  ,WARRIOR_SUMMONED("В игре '%s' (контекст '%s') игроком '%s' призван воин '%s' с '%s' очками жизни")
  , WARRIOR_MOVED("В игре '%s' игрок '%s' переместил юнит '%s' на координаты '%s'")
  , WEAPON_TAKEN("В игре '%s' игрок '%s' снарядил юнит '%s' (id '%s') оружием '%s'. %s")
  , WEAPON_TRY_TO_DROP("В игре '%s' игрок '%s' попытался убрать у юнита '%s' (id '%s') оружие id '%s'. %s")
  , WEAPON_DROPED("В игре '%s' игрок '%s' убрал у юнита '%s' (id '%s') оружие '%s' (id '%s'). %s")

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
