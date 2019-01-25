package core.system.error;

public enum GameErrors {
    UNKNOWN_EVENT_FOR_LOGGER("EV-1", "Неизвестный для логирования тип события %s")

  , PLAYER_UNITS_LIMIT_EXCEEDED("PL-1", "Пользователь с UID %s уже имеет максимальное кол-во воинов %s.")

  , WARRIOR_HANDS_NO_FREE_SLOTS("WR-1", "Руки воина имеют свободных %s мест, а оружие '%s' требует %s места")
  , WARRIOR_WEAPON_NOT_FOUND("WR-2", "Воин не имеет оружия с id '%s'")
  , WARRIOR_BASE_ATTRS_IS_FINAL("WR-3", "Воину уже назначены базовыеатрибуты. Переопределение не возможно")
  , WARRIOR_BASE_CLASS_NOT_FOUND_BY_NAME("WR-4", "Класс воина с именем %s не существует")
  , WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME("WR-5", "Класс воина с id %s не существует у игрока %s")

  , WEAPON_BASE_CLASS_NOT_FOUND_BY_NAME("WP-1", "Класс оружия с именем %s не существует")

  , MAP_LOAD_ERROR("M-1", "Ошибка загрузки карты: %s")
  , MAP_IS_NOT_LOADED("M-1", "Карта не загружена")

  , USER_CONNECT_TO_CONTEXT_TOO_MANY_USERS("U-1", "Не удалось подключить пользователя к игре. Все слоты заняты")
  , USER_CONNECT_TO_CONTEXT_GAME_RUNNING("U-2", "Не удалось подключить пользователя к игре. Игра уже идет")
  , USER_DISCONNECT_NOT_CONNECTED("U-3", "Не удалось отключить пользователя от игры так как он не был подключен.")
  , USER_NOT_LOGGED_IN("U-4", "Пользователь %s не авторизован")
  , USER_NOT_CONNECTED_TO_ANY_GAME("U-5", "Пользователь %s не подключен ни к одной игре")
  , USER_UNKNOWN_ID("U-6", "Пользователь с UID %s не найден.")

  , CONTEXT_REMOVE_NOT_FOUND("C-1", "Удаляемый контекст не существует или не активен")
  , CONTEXT_NOT_FOUND_BY_ID("C-2", "Контекст %s не существует или уничтожен.")


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
