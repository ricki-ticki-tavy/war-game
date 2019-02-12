package api.game.ability;

import api.core.Context;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.WarriorBaseClass;
import api.enums.EventType;

import java.util.List;

/**
 * Способность
 */
public interface Ability extends BaseEntityHeader {
  /**
   * применить способность
   * @return
   */
  boolean applay(Context context);

  /**
   * Отменить действие способности
   * @param context
   * @return
   */
  boolean rollback(Context context);

  /**
   * Доступна ли способность в данном собитии
   * @param triggerType
   * @return
   */
  boolean isValidForEvent(EventType triggerType);

  /**
   * Вернуть все события на которые способность оказывает влияние
   * @return
   */
  List<EventType> getAllTriggerTypes();

  /**
   * Допустимое кол-во применений за ход. 0 - бесконечность
   * @return
   */
  int getUseCoountPerRound();

  /**
   * Максимальное кол-во использований за игру. 0 - бесконечность
   * @return
   */
  int getTotalUseCount();

  /**
   * Базовые классы, к которым способность не применима
   */
  List<WarriorBaseClass> getNotSupportedWarriorBaseClasses();

}
