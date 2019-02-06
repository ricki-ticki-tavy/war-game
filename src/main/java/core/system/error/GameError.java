package core.system.error;

public class GameError extends RuntimeException{
  private String message;
  private String errorCode;

  public String getCode(){
    return errorCode;
  }

  public GameError(String errorCode, String message){
    super(message);
    this.message = message;
    this.errorCode = errorCode;
  }
}
