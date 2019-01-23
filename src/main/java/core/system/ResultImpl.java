package core.system;

import api.core.Result;
import core.system.error.GameError;

import java.util.Optional;

/**
 * Класс результата действия
 */
public class ResultImpl implements Result {
  private boolean fail = false;
  private boolean success = false;
  private Object result = null;
  private GameError error = null;

  protected ResultImpl setFail(GameError error){
    this.fail = true;
    this.error = error;
    return this;
  }

  protected ResultImpl setSuccess(Object result){
    this.success = true;
    this.result = result;
    return this;
  }

  /**
   * Ошибка выполнения
   * @param error
   * @return
   */
  public static ResultImpl fail(GameError error){
    return new ResultImpl().setFail(error);
  }

  /**
   * Успешное выполнение
   * @param result
   * @return
   */
  public static ResultImpl success(Object result){
    return new ResultImpl().setSuccess(result);
  }


  public boolean isFail(){
    return  fail;
  }

  public boolean isSuccess(){
    return  success;
  }

  public Optional<GameError> getError(){
    return Optional.ofNullable(error);
  }

  public Optional<Object> getResult(){
    return Optional.ofNullable(result);
  }

  @Override
  public String toString(){
    if (success) {
      return "Успешно";
    } else if (fail){
      return "ОТКАЗ: " + error.getMessage();
    } else {
      return "НЕТ СТАТУСА";
    }
  }

  @Override
  public String getId() {
    return "Результат операци";
  }

  @Override
  public String getTitle() {
    return getId();
  }

  @Override
  public String getDescription() {
    return getId();
  }
}
