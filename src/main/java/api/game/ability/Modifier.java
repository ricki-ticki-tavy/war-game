package api.game.ability;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.enums.AttributeEnum;
import api.enums.ManifestationOfInfluenceEnum;
import api.enums.ModifierClass;
import api.enums.TargetTypeEnum;
import api.game.action.InfluenceResult;

/**
 * модификатор атрибута
 */
public interface Modifier {

  /**
   * Название
   *
   * @return
   */
  String getTitle();

  /**
   * Описание
   *
   * @return
   */
  String getDescription();

  /**
   * на кого распространяется влияние
   *
   * @return
   */
  TargetTypeEnum getTargetType();

  /**
   * На какой атрибут влияет
   *
   * @return
   */
  AttributeEnum getAttribute();

  /**
   * Вероятность успеха в процентах
   *
   * @return
   */
  int getProbability();

  /**
   * Возвращает значение.
   *
   * @return
   */
  Result<Integer> getValue();

  /**
   * минимальное значение влияния. - уменьшает + увеличивает
   *
   * @return
   */
  int getMinValue();

  /**
   * миаксимальное значение влияния. - уменьшает + увеличивает
   *
   * @return
   */
  int getMaxValue();

  /**
   * Возвращает контекст
   *
   * @return
   */
  Context getContext();

  /**
   * Получить фиксированное значение последнего результата вычисления размера влияния модификатора
   *
   * @return
   */
  int getLastCalculatedValue();

  /**
   * Установить фиксированное значение последнего результата вычисления размера влияния модификатора
   *
   * @param value
   * @return
   */
  Modifier setLastCalculatedValue(int value);

  /**
   * Возвращает класс воздействия
   *
   * @return
   */
  ModifierClass getModifierClass();

  /**
   * Получить процент удачи
   *
   * @return
   */
  int getLuck();

  /**
   * Возвращает улыбнулась ли удача при броске
   *
   * @return
   */
  boolean isLuckyRollOfDice();

  /**
   * Успех попадания, если вероятность попасть менее 100%
   * @return
   */
  boolean isHitSuccess();

  /**
   * изменить успех
   * контроля по переходу за 100 или менее 0 не делаем чтобы можно было правильно учесть ситуацию :
   * 10 - 12 + 2 = ?. если считать с контролем, то после -12  результат станет 0, так как не может
   * быть менее 0, а после +2 станет 2, что не верно ибо должно выйти 0 в итоге. На самом деле на
   * успех переход за 100 или уменьшение менее 0 не влияет
   * @param delta
   * @return
   */
  Modifier addLuck(int delta);

  /**
   * Применить в соответствие с атакой.
   * @param influenceResult
   * @return
   */
  Result<Modifier> applyModifier(InfluenceResult influenceResult);

  /**
   * Возвращает тип влияния: положительный или отрицательный
   * @return
   */
  ManifestationOfInfluenceEnum getManifestationOfInfluence();

}
