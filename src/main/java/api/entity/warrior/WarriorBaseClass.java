package api.entity.warrior;

import api.WarriorSBaseAttributes;
import api.core.Context;
import api.game.GameEvent;
import api.entity.ability.Ability;
import api.entity.base.BaseEntityHeader;
import api.enums.EventType;

import java.util.List;

/**
 * Базовый класс воина
 */
public interface WarriorBaseClass extends BaseEntityHeader {
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
  List<Ability> getAbilities(EventType triggerType);

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
   * Кол-во оставшихся очков применения способностей в ходe
   */
  int getAbilityActionPoints();

  /**
   * Обработка события игры
   * @param event
   * @return
   */
  void fireEvent(GameEvent event);
}
