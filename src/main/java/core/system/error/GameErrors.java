package core.system.error;

public enum GameErrors {

  UNKNOWN_EVENT_FOR_LOGGER("EV-1", "Неизвестный для логирования тип события %s")

  , PLAYER_UNITS_LIMIT_EXCEEDED("PL-1", "Пользователь с UID %s уже имеет максимальное кол-во воинов %s.")
  , PLAYER_NOT_CONNECTED("PL-2", "Пользователь  %s не является участником ни одной из игр.")
  , PLAYER_IS_NOT_OWENER_OF_THIS_ROUND("PL-3", "В игре '%s' (id %s) пользователь  '%s' пытается выполнить действие не в свой ход %s")
  , PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED("PL-4", "Пользователь  %s выполнил ход всеми допустимыми юнитами (%s) и в этом ходу не может использовать других юнитов.")
//  , PLAYER_CAN_T_PASS_THE_TURN_PLAYER_IS_NOT_TURN_OWNER("PL-5", "Пользователь  '%s' игра '%s' (id %s) не может передать ход дальше так как не его ход. Сейчас ход игрока '%s'")

  , WARRIOR_HANDS_NO_FREE_SLOTS("WR-1", "Руки воина имеют свободных %s мест, а оружие '%s' требует %s места")
  , WARRIOR_WEAPON_NOT_FOUND("WR-2", "В игре %s (id %s) у игрока %s воин '%s %s' (id %s) не имеет оружия с id '%s'")
  , WARRIOR_BASE_ATTRS_IS_FINAL("WR-3", "Воину уже назначены базовыеатрибуты. Переопределение не возможно")
  , WARRIOR_BASE_CLASS_NOT_FOUND_BY_NAME("WR-4", "Класс воина с именем %s не существует")
  , WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME("WR-5", "Игра %s (id %s) игрок %s не имеет воина с id %s")
  , WARRIOR_NOT_FOUND_ON_THE_MAP("WR-6", "Игра %s (id %s) не имеет воина с id %s")
  , WARRIOR_CAN_T_MORE_MOVE_ON_THIS_TURN("WR-7", "Воин %s (id %s) игрока %s в игре %s (id %s) не может более выполнить перемещение в данном ходе")
  , WARRIOR_CAN_T_MOVE_TO_THIS_POINT("WR-8", "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может переместиться на %s так как эти координаты заняты")
  , WARRIOR_CAN_T_ROLLBACK_MOVE("WR-9", "В игре %s (id %s) игрок %s не может откатить перемещение воина '%s %s' (id %s) так как откат заблокирован последующими действиями")

  , WARRIOR_ATTACK_TARGET_IS_OUT_OF_RANGE("WR-30", "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может атаковать воина '%s %s' (id %s) игрока %s так как дистанция для атаки велика")
  , WARRIOR_ATTACK_WEAPON_IS_OUT_OF_CHARGES("WR-31", "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может атаковать воина '%s %s' (id %s) игрока %s так как не осталось зарядов")
  , WARRIOR_ATTACK_RANGED_NOT_POSIBLE_ENEMYS_IS_NEAR_ATTACKER("WR-32", "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может дистанционно атаковать воина '%s %s' (id %s) игрока %s так как рядом есть враги")
  , WARRIOR_ATTACK_TARGET_WARRIOR_IS_ALIED("WR-33", "В игре %s (id %s)  воин '%s %s' (id %s) не является врагом для воина '%s %s' (id %s) игрока %s %s")
  , WARRIOR_ATTACK_TARGET_WARRIOR_IS_NOT_ALIED("WR-34", "В игре %s (id %s)  воин '%s %s' (id %s) является врагом для воина '%s %s' (id %s) игрока %s %s")
  , WARRIOR_ATTACK_THERE_IS_NOT_ENOUGH_ACTION_POINTS("WR-35", "В игре %s (id %s)  воин '%s %s' (id %s) не может атаковать воина '%s %s' (id %s) игрока %s так как не хватает очков действия")
  , WARRIOR_ATTACK_UNKNOW_REASON_SHAISE("WR-35", "В игре %s (id %s)  воин '%s %s' (id %s) не может атаковать воина '%s %s' (id %s) игрока %s так как не ясно почему. Шайсе...")

  , ABILITY_USE_COUNT_PER_TURN_HAS_EXCEEDED("AB-1", "В игре %s способность %s не может быть применена более %s раз")

  , WEAPON_BASE_CLASS_NOT_FOUND_BY_NAME("WP-1", "Класс оружия с именем %s не существует")

  , ARTIFACT_WRONG_ABILITY("AR-1", "Игра %s. артефакт '%s' не может использовать способность '%s'. Эту способность может использовать только %s")
  , ARTIFACT_ALREADY_EXISTS("AR-2", "В игре %s. воин '%s %s' игрока '%s' уже владеет артефактом '%s'.")
  , ARTIFACT_BASE_CLASS_ARTIFACT_OF_WARRIOR_NOT_FOUND_BY_NAME("AR-3", "Класс воинского артефакта  с именем %s не существует")
  , ARTIFACT_NOT_FOUND_BY_WARRIOR("AR-4", "В игре %s (id %s) у игрока %s воин '%s %s' (id %s) не имеет артефакта с id '%s'")

  , MAP_LOAD_ERROR("M-1", "Ошибка загрузки карты: %s")
  , MAP_IS_NOT_LOADED("M-2", "Карта не загружена")

  , USER_CONNECT_TO_CONTEXT_TOO_MANY_USERS("U-1", "Не удалось подключить пользователя к игре. Все слоты заняты")
  , USER_CONNECT_TO_CONTEXT_GAME_RUNNING("U-2", "Не удалось подключить пользователя к игре. Игра уже идет")
  , USER_DISCONNECT_NOT_CONNECTED("U-3", "Не удалось отключить пользователя от игры так как он не был подключен.")
  , USER_NOT_LOGGED_IN("U-4", "Пользователь %s не авторизован")
  , USER_NOT_CONNECTED_TO_ANY_GAME("U-5", "Пользователь %s не подключен ни к одной игре")
  , USER_NOT_CONNECTED_TO_THIS_GAME("U-6", "Пользователь %s не подключен к игре %s (id %s)")
  , USER_IS_READY_TO_PLAY("U-7", "Пользователь %s в к игре %s (id %s) завершил приготовления и не может ни чего менять")
  , USER_UNKNOWN_ID("U-80", "Пользователь с UID %s не найден.")

  , CONTEXT_REMOVE_NOT_FOUND("CT-1", "Удаляемый контекст не существует или не активен")
  , CONTEXT_NOT_FOUND_BY_ID("CT-2", "Контекст %s не существует или уничтожен.")
  , CONTEXT_GAME_NOT_STARTED("CT-3", "игра '%s' (context %s) еще не началась. Игроки готовятся.")
  , CONTEXT_DELETE_ALREADY_IN_PROGRESS("CT-4", "игра '%s' (context %s) уже находится в процессе удаления.")
  , CONTEXT_IS_IN_DELETING_STATE("CT-5", "игра '%s' (context %s) в процессе удаления.")
  , CONTEXT_IS_NOT_IN_DELETING_STATE("CT-6", "игра '%s' (context %s) не находится в процессе удаления.")
  , CONTEXT_IN_GAME_RAN_STATE("CT-7", "игра '%s' (context %s) уже в процессе игры.")

  , SYSTEM_NOT_REALIZED("SE-1", "Метод %s не реализован.")
  , SYSTEM_USER_ERROR("SE-2", "%s.")
  , SYSTEM_RUNTIME_ERROR("SE-3", "%s.")
  , SYSTEM_OBJECT_ALREADY_INITIALIZED("SE-4", "Объект %s уже инициализирован (%s). Повторное переопределение значения невозможно.")
  , SYSTEM_BAD_PARAMETER("SE-5", "Недопустимое значение параметра %s (%s). %s")

  ;

  private GameError error;
  private String message;
  private String errorCode;

  public boolean isMatchedTo(GameError error){
    return error.getCode().equals(errorCode);

  }

  public GameError getError(String... args){
    return new GameError(errorCode, String.format(String.format("%s - %s", errorCode, message), args));
  }

  public void error(String... args){
    throw getError(args);
  }

  private GameErrors(String errorCode, String message){
    this.errorCode = errorCode;
    this.message = message;
  }



}
