package core.system;

import api.core.Result;
import core.system.error.GameError;
import core.system.error.GameErrors;
import core.system.log.LogUtils;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Класс результата действия
 */
public class ResultImpl implements Result {
  private boolean fail = false;
  private boolean success = false;
  private Object result = null;
  private GameError error = null;

  protected ResultImpl setFail(GameError error) {
    this.fail = true;
    this.success = false;
    this.error = error;
    return this;
  }

  protected ResultImpl setSuccess(Object result) {
    this.success = true;
    this.fail = false;
    this.result = result;
    return this;
  }

  /**
   * Ошибка выполнения
   *
   * @param error
   * @return
   */
  public static ResultImpl fail(GameError error) {
    return new ResultImpl().setFail(error);
  }

  /**
   * Успешное выполнение
   *
   * @param result
   * @return
   */
  public static ResultImpl success(Object result) {
    return new ResultImpl().setSuccess(result);
  }


  public boolean isFail() {
    return fail;
  }

  public boolean isFail(GameErrors error) {
    return fail && error.isMatchedTo(this.error);
  }

  public boolean isSuccess() {
    return success;
  }

  public GameError getError() {
    return error;
  }

  public Object getResult() {
    return result;
  }

  @Override
  public Result peak(Consumer consumer) {
    if (success) {
      consumer.accept(result);
    }
    return this;
  }

  @Override
  public Result doIfFail(Consumer consumer) {
    if (fail) {
      consumer.accept(error);
    }
    return this;
  }

  @Override
  public Result map(Function consumer) {
    if (success) {
      return (Result) consumer.apply(result);
    } else {
      return this;
    }
  }

  public Result mapSafe(Function consumer) {
    if (success) {
      try {
        return (Result) consumer.apply(result);
      } catch (Throwable th) {
        Result r;
        if (th instanceof GameError) {
          r = ResultImpl.fail((GameError) th);
        } else {
          r = ResultImpl.fail(GameErrors.SYSTEM_RUNTIME_ERROR.getError(LogUtils.exceptionToString(th)));
        }
        return r;
      }
    } else {
      return this;
    }
  }

  @Override
  public Result mapFail(Function consumer) {
    if (fail) {
      return (Result) consumer.apply(this);
    } else {
      return this;
    }
  }

  @Override
  public String toString() {
    if (success) {
      return "Успешно";
    } else if (fail) {
      return "ОТКАЗ: " + error.getMessage();
    } else {
      return "НЕТ СТАТУСА";
    }
  }

  @Override
  public Result logIfError(Logger logger) {
    if (fail) {
      logger.error(error.getMessage());
    }
    return this;
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
