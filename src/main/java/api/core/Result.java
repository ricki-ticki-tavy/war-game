package api.core;

import api.entity.base.BaseEntityHeader;
import core.system.error.GameError;
import core.system.error.GameErrors;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Класс результата действия
 */
public interface Result<T> extends BaseEntityHeader{

  boolean isFail();

  boolean isFail(GameErrors error);

  boolean isSuccess();

  GameError getError();

  T getResult();

  <O> Result<O> map(Function<T, Result<O>> consumer);

  /**
   * выполняет с перехватом и оборачиванием исключений в ошибку
   * @param consumer
   * @param <O>
   * @return
   */
  <O> Result<O> mapSafe(Function<T, Result<O>> consumer);

  Result<T> peak(Consumer<T> consumer);

  @Deprecated
  <O> Result<T> doIfFail(Consumer<O> consumer);

  <O> Result<O> mapFail(Function<Result<T>, Result<O>> consumer);

  /**
   * Записать ошибку в лог, если таковая была и вернуть самого себя
   * @param logger
   * @return
   */
  Result<T> logIfError(Logger logger);
}
