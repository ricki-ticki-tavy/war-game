package api.core;

import api.entity.base.BaseEntityHeader;
import core.system.error.GameError;

import java.util.Optional;

/**
 * Класс результата действия
 */
public interface Result extends BaseEntityHeader{

  boolean isFail();

  boolean isSuccess();

  GameError getError();

  Object getResult();
}
