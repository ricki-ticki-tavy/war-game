package core.system.error;

public enum GameErrors {

  UNKNOWN_EVENT_FOR_LOGGER("EV-1", "Неизвестный для логирования тип события %s")

  , PLAYER_UNITS_LIMIT_EXCEEDED("PL-1", "Пользователь с UID %s уже имеет максимальное кол-во воинов %s.")
  , PLAYER_NOT_CONNECTED("PL-2", "Пользователь  %s не является участником ни одной из игр.")
  , PLAYER_IS_NOT_OWENER_OF_THIS_ROUND("PL-3", "Пользователь  %s пытается выполнить действие не в свой ход %s")
  , PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED("PL-4", "Пользователь  %s выполнил ход всеми допустимыми фигурками (%s).")
  , PLAYER_CAN_T_PASS_THE_TURN_PLAYER_IS_NOT_TURN_OWNER("PL-5", "Пользователь  %s игра %s (id %s) не может передать ход дальше так как не его ход. Сейчас ход игрока %s")

  , WARRIOR_HANDS_NO_FREE_SLOTS("WR-1", "Руки воина имеют свободных %s мест, а оружие '%s' требует %s места")
  , WARRIOR_WEAPON_NOT_FOUND("WR-2", "Воин не имеет оружия с id '%s'")
  , WARRIOR_BASE_ATTRS_IS_FINAL("WR-3", "Воину уже назначены базовыеатрибуты. Переопределение не возможно")
  , WARRIOR_BASE_CLASS_NOT_FOUND_BY_NAME("WR-4", "Класс воина с именем %s не существует")
  , WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME("WR-5", "Класс воина с id %s не существует у игрока %s")
  , WARRIOR_CAN_T_MORE_MOVE_ON_THIS_TURN("WR-6", "Воин %s (id %s) игрока %s в игре %s (id %s) не может более выполнить перемещение в данном ходе")
  , WARRIOR_CAN_T_MOVE_TO_THIS_POINT("WR-7", "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может переместиться на %s так как эти координаты заняты")

  , WEAPON_BASE_CLASS_NOT_FOUND_BY_NAME("WP-1", "Класс оружия с именем %s не существует")

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
  , CONTEXT_GAME_NOT_STARTED("CT-3", "игра '%s' (context %s) не была начата.")
  , CONTEXT_DELETE_ALREADY_IN_PROGRESS("CT-4", "игра '%s' (context %s) уже находится в процессе удаления.")
  , CONTEXT_IS_IN_DELETING_STATE("CT-5", "игра '%s' (context %s) в процессе удаления.")
  , CONTEXT_IS_NOT_IN_DELETING_STATE("CT-6", "игра '%s' (context %s) не находится в процессе удаления.")
  , CONTEXT_IN_GAME_RAN_STATE("CT-7", "игра '%s' (context %s) уже в процессе игры.")
  , CONTEXT_NOT_IN_GAME_RAN_STATE("CT-8", "игра '%s' (context %s) еще не началась. Игроки готовятся.")

  , SYSTEM_NOT_REALIZED("NR-1", "Метод %s не реализован.")

  , SYSTEM_USER_ERROR("UM-1", "%s.")




//  ,  UNKNOWN_EVENT_FOR_LOGGER("EV-1", "Неизвестный для логирования тип события %s")
//
//  , PLAYER_UNITS_LIMIT_EXCEEDED("PL-1", "Пользователь с UID %s уже имеет максимальное кол-во воинов %s.")
//  , PLAYER_NOT_CONNECTED("PL-2", "Пользователь  %s не является участником ни одной из игр.")
//  , PLAYER_IS_NOT_OWENER_OF_THIS_ROUND("PL-3", "Пользователь  %s пытается выполнить действие не в свой ход %s")
//
//  , WARRIOR_HANDS_NO_FREE_SLOTS("WR-1", "Руки воина имеют свободных %s мест, а оружие '%s' требует %s места")
//  , WARRIOR_WEAPON_NOT_FOUND("WR-2", "Воин не имеет оружия с id '%s'")
//  , WARRIOR_BASE_ATTRS_IS_FINAL("WR-3", "Воину уже назначены базовыеатрибуты. Переопределение не возможно")
//  , WARRIOR_BASE_CLASS_NOT_FOUND_BY_NAME("WR-4", "Класс воина с именем %s не существует")
//  , WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME("WR-5", "Класс воина с id %s не существует у игрока %s")
//
//  , WEAPON_BASE_CLASS_NOT_FOUND_BY_NAME("WP-1", "Класс оружия с именем %s не существует")
//
//  , MAP_LOAD_ERROR("M-1", "Ошибка загрузки карты: %s")
//  , MAP_IS_NOT_LOADED("M-1", "Карта не загружена")
//
//  , USER_CONNECT_TO_CONTEXT_TOO_MANY_USERS("U-1", "Не удалось подключить пользователя к игре. Все слоты заняты")
//  , USER_CONNECT_TO_CONTEXT_GAME_RUNNING("U-2", "Не удалось подключить пользователя к игре. Игра уже идет")
//  , USER_DISCONNECT_NOT_CONNECTED("U-3", "Не удалось отключить пользователя от игры так как он не был подключен.")
//  , USER_NOT_LOGGED_IN("U-4", "Пользователь %s не авторизован")
//  , USER_NOT_CONNECTED_TO_ANY_GAME("U-5", "Пользователь %s не подключен ни к одной игре")
//
//  , CONTEXT_REMOVE_NOT_FOUND("CT-1", "Удаляемый контекст не существует или не активен")
//  , CONTEXT_NOT_FOUND_BY_ID("CT-2", "Контекст %s не существует или уничтожен.")
//  , CONTEXT_GAME_NOT_STARTED("CT-3", "игра '%s' (context %s) не была начата.")
//  , CONTEXT_DELETE_ALREADY_IN_PROGRESS("CT-4", "игра '%s' (context %s) уже находится в процессе удаления.")
//  , CONTEXT_IS_IN_DELETING_STATE("CT-5", "игра '%s' (context %s) в процессе удаления.")
//  , CONTEXT_IS_NOT_IN_DELETING_STATE("CT-5", "игра '%s' (context %s) не находится в процессе удаления.")
//  , CONTEXT_IN_GAME_RAN_STATE("CT-6", "игра '%s' (context %s) уже в процессе игры.")
//  , CONTEXT_NOT_IN_GAME_RAN_STATE("CT-7", "игра '%s' (context %s) еще не началась. Игроки готовятся.")
//

  ;

  private GameError error;
  private String message;
  private String errorCode;

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
