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

  <O> Result<O> onSuccess(Function<T, Result<O>> consumer);
}
