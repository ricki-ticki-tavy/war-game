package api.base.i.entity.warrior;

import api.base.WarriorSBaseAttributes;
import api.base.i.core.Context;
import api.base.i.entity.ability.Ability;
import api.base.i.entity.BaseEntityHeader;
import api.base.i.enums.EventEnum;

import java.util.List;

/**
 * Базовый класс воина
 */
public interface WarriorClass extends BaseEntityHeader {
  /**
   * Возвращает руки воина со всем снаряжением в них
   * @return
   */
  List<WarriorSHand> getHands();

  /**
   * Возвращает значениепараметроввоина
   * @return
   */
  WarriorSBaseAttributes getBaseAttributes();

  /**
   * ВОзвращает способности воина
   * @return
   */
  List<Ability> getAbilities(EventEnum triggerType);

  /**
   * Завершить раунд игрока
   * @param context
   * @return
   */
  boolean finishRound(Context context);

  /**
   * начать раунд игрока
   * @param context
   * @return
   */
  boolean startRound(Context context);

  /**
   * Использовался ли юнит в этом ходе
   * @return
   */
  boolean isUsedInThisRound();

  /**
   * Кол-во очков применения способностей за ход
   */
  public int getAbilityActionPoints();

}
