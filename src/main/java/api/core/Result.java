package api.core;

import api.entity.base.BaseEntityHeader;
import core.system.error.GameError;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Класс результата действия
 */
public interface Result<T> extends BaseEntityHeader{

  boolean isFail();

  boolean isSuccess();

  GameError getError();

  T getResult();

  <O> Result<O> map(Function<T, Result<O>> consumer);

  @Deprecated
  Result<T> doIfSuccess(Consumer<T> consumer);

  @Deprecated
  <O> Result<T> doIfFail(Consumer<O> consumer);

  <O> Result<O> mapFail(Function<Result<T>, Result<O>> consumer);
}
