package api.core;

import api.entity.base.BaseEntityHeader;
import core.system.error.GameError;

import java.util.Optional;

/**
 * Класс результата действия
 */
public interface Result<T> extends BaseEntityHeader{

  boolean isFail();

  boolean isSuccess();

  GameError getError();

  T getResult();
}
