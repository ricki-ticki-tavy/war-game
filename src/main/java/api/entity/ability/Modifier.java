package api.entity.ability;

import api.core.Context;
import api.core.IntParam;
import api.core.Result;
import api.enums.AttributeEnum;
import api.enums.TargetTypeEnum;

/**
 * модификатор атрибута
 */
public interface Modifier {

  /**
   * Название
   * @return
   */
  String getTitle();

  /**
   * Описание
   * @return
   */
  String getDescription();

  /**
   * на кого распространяется влияние
   * @return
   */
  TargetTypeEnum getTarget();

  /**
   * На какой атрибут влияет
   * @return
   */
  AttributeEnum getAttribute();

  /**
   * Вероятность успеха в процентах
   * @return
   */
  int getProbability();

  /**
   * Возвращает значение.
   * @return
   */
  Result<IntParam> getValue();

  /**
   * минимальное значение влияния. - уменьшает + увеличивает
   * @return
   */
  int getMinValue();

  /**
   * миаксимальное значение влияния. - уменьшает + увеличивает
   * @return
   */
  int getMaxValue();

  /**
   * Возвращает контекст
   * @return
   */
  Context getContext();
}
