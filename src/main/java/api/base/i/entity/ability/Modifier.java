package api.base.i.entity.ability;

import api.base.i.enums.AttributeEnum;
import api.base.i.enums.TargetTypeEnum;

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
  int getValue();

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

}
