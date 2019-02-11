package api.enums;

/**
 * Тип срабатывания триггера
 */
public enum EventType {
  ALWAYS("Всегда")
  , ROUND_FULL("В игре %s (id %s) завершился игровой круг")

  , ATTACK_MELEE_AFTER("После каждого рукопашного удара при атаке")
  , ATTACK_MELEE_BEFORE("Перед каждым рукопашным ударом при атаке")
  , ATTACK_DISTANCE_AFTER("После каждого дистанционного удара при атаке")
  , ATTACK_DISTANCE_BEFORE("Перед каждым дистанционным ударом при атаке")
  , ATTACK_AFTER_EVERY_BIT("После каждого удара при атаке")
  , ATTACK_BEFORE_EVERY_BIT("Перед каждым ударом при атаке")

  , DEFENSE_AFTER("Перед защитой")
  , DEFENSE_BEFORE("После защиты")

  , PLAYER_LOGGED_IN("Вход игрока %s. %s")
  , PLAYER_CONNECTED("Игрок '%s' присоединился к игре (контекст '%s'). Теперь в игре %s игроков из %s. %s")
  , PLAYER_RECONNECTED("Игрок '%s' повторно подключился к игре (контекст '%s'). В игре %s игроков из %s")
  , PLAYER_DISCONNECTED("Игрок '%s' покинул игру (контекст '%s'). Теперь в игре %s игроков из %s. %s")
  , PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS("Игрок '%s' в игре %s (контекст '%s') сообщил о %s.")
  , PLAYER_LOOSE_TURN("Игрок '%s' в игре %s (контекст '%s') завершил ход.")
  , PLAYER_TAKES_TURN("Игрок '%s' в игре %s (контекст '%s') получил ход")

  , WARRIOR_ADDED("В игре '%s' (контекст '%s') игроком '%s' добавлен воин '%s'")
  , WARRIOR_SUMMONED("В игре '%s' (контекст '%s') игроком '%s' призван воин '%s' с '%s' очками жизни")
  , WARRIOR_MOVED("В игре '%s' (id %s) игрок '%s' переместил юнит '%s %s' (id %s) на координаты '%s'")
  , WARRIOR_MOVE_ROLLEDBACK("В игре '%s' (id %s) игрок '%s' отменил движение юнитом '%s %s' (id %s) и вернул его на координаты '%s'")
  , WARRIOR_REMOVED("В игре '%s' (контекст '%s') игроком '%s' удален воин '%s' (id %s)")
  , WARRIOR_INFLUENCER_ADDED("В игре '%s' (контекст '%s') у игрока '%s' воину '%s' (id %s) добавлено влияние '%s' (id %s)")
  , WARRIOR_INFLUENCER_REMOVED("В игре '%s' (контекст '%s') у игрока '%s' с воина '%s' (id %s) снято влияние '%s' (id %s)")
  , WARRIOR_PREPARED_TO_DEFENCE("В игре '%s' (контекст '%s') у игрока '%s' воин '%s (%s)' (id %s) подготовился к защите")
  , WARRIOR_PREPARED_TO_ATTACK("В игре '%s' (контекст '%s') у игрока '%s' воин '%s (%s)' (id %s) подготовился к действиям и атаке")

  , WARRIOR_HAS_LUCK("В игре '%s' воину '%s %s' игрока '%s' улыбнулась удача %s")
  , WARRIOR_WAS_ATTACKED_BY_ENEMY("В игре '%s' воин '%s %s' игрока '%s' нанес оружием %s воину '%s %s' игрока %s %s единиц урона")

  , WEAPON_TAKEN("В игре '%s' игрок '%s' снарядил юнит '%s' (id '%s') оружием '%s'. %s")
  , WEAPON_TRY_TO_DROP("В игре '%s' игрок '%s' попытался убрать у юнита '%s' (id '%s') оружие id '%s'. %s")
  , WEAPON_DROPED("В игре '%s' игрок '%s' убрал у юнита '%s' (id '%s') оружие '%s' (id '%s'). %s")

  , GAME_CONTEXT_CREATE("Создание контекста игры '%s' игроком '%s'. %s")
  , GAME_CONTEXT_CREATED("Создание контекста игры '%s' игроком '%s'. %s")
  , GAME_CONTEXT_LOAD_MAP("игра '%s' Контекст %s : загрузка карты '%s' игроком '%s'. тип игры %s.  %s")
  , GAME_CONTEXT_REMOVED("Удаление контекста игры '%s' (владелец '%s'). %s")
  , GAME_CONTEXT_GAME_HAS_BEGAN("Игра '%s' (id '%s') началась по готовности всех игроков. В игре %s игрока(ов)")

  ,
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
