package api.enums;

/**
 * Тип срабатывания триггера
 */
public enum EventType {
  ON_PLAYER_ROUND("При ходе игрока")
  , ALWAYS("Всегда")
  , ROUND_FULL("игровой круг")
  , ROUND_START("в начале хода игрока")
  , ROUND_END("в конце хода игрока")

  , ATTACK_MELEE_AFTER("После каждого рукопашного удара при атаке")
  , ATTACK_MELEE_BEFORE("Перед каждым рукопашным ударом при атаке")
  , ATTACK_DISTANCE_AFTER("После каждого дистанционного удара при атаке")
  , ATTACK_DISTANCE_BEFORE("Перед каждым дистанционным ударом при атаке")
  , ATTACK_AFTER_EVERY_BIT("После каждого удара при атаке")
  , ATTACK_BEFORE_EVERY_BIT("Перед каждым ударом при атаке")

  , DEFENCE_AFTER("Перед защитой")
  , DEFENCE_BEFORE("После защиты")

  , PLAYER_LOGGED_IN("Вход игрока %s. %s")
  , PLAYER_CONNECTED("Игрок '%s' присоединился к игре (контекст '%s'). Теперь в игре %s игроков из %s. %s")
  , PLAYER_RECONNECTED("Игрок '%s' повторно подключился к игре (контекст '%s'). В игре %s игроков из %s")
  , PLAYER_DISCONNECTED("Игрок '%s' покинул игру (контекст '%s'). Теперь в игре %s игроков из %s. %s")
  , PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS("Игрок '%s' в игре %s (контекст '%s') сообщил о %a. %s")

  , WARRIOR_ADDED("В игре '%s' (контекст '%s') игроком '%s' добавлен воин '%s'")
  , WARRIOR_SUMMONED("В игре '%s' (контекст '%s') игроком '%s' призван воин '%s' с '%s' очками жизни")
  , WARRIOR_MOVED("В игре '%s' игрок '%s' переместил юнит '%s' на координаты '%s'")

  , WEAPON_TAKEN("В игре '%s' игрок '%s' снарядил юнит '%s' (id '%s') оружием '%s'. %s")
  , WEAPON_TRY_TO_DROP("В игре '%s' игрок '%s' попытался убрать у юнита '%s' (id '%s') оружие id '%s'. %s")
  , WEAPON_DROPED("В игре '%s' игрок '%s' убрал у юнита '%s' (id '%s') оружие '%s' (id '%s'). %s")

  , GAME_CONTEXT_CREATE("Создание контекста игры '%s' игроком '%s'. %s")
  , GAME_CONTEXT_CREATED("Создание контекста игры '%s' игроком '%s'. %s")
  , GAME_CONTEXT_LOAD_MAP("игра '%s' Контекст %s : загрузка карты '%s' игроком '%s'. тип игры %s.  %s")
  , GAME_CONTEXT_REMOVED("Удаление контекста игры '%s' (владелец '%s'). %s")
  , GAME_CONTEXT_GAME_HAS_BEGAN("Игра '%s' (id '%s') началась по готовности всех игроков. В игре %s игрока(ов)")

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
