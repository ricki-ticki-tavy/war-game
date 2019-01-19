package core.system.error;

public enum GameErrors {
  GAME_ERROR_BASE_WARRIOR_S_ATTRS_IS_FINAL("F-1", "Воину уже назначены базовыеатрибуты. Переопределение не возможно")
  , GAME_ERROR_UNKNOWN_USER_UID("U-1", "Пользователь с UID %s не найден.")
  , GAME_ERROR_TOO_MANY_UNITS_FOR_PLAYER("O-1", "Пользователь с UID %s уже имеет максимальное кол-во воинов %s.")
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
