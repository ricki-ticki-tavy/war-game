package api.game.ability;

import api.core.Context;
import api.core.Result;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.ActorTypeEnum;
import api.enums.EventType;
import api.enums.TargetTypeEnum;
import api.game.Influencer;

import java.util.List;

/**
 * Способность. класс высшего уровня. Представляет собой способность влиять на один или несколько параметров юнита или
 * игрока при различных условиях. Этот класс формирует объекты влияния на конечные цели. Объекты влияния прикрепляются к
 * цели на определенное условиями время и оказывают влияние на его атрибуты.
 *
 * Например способностью можно назвать : поглощение доп.единиц действия при атаке или урон огнем и так далее.
 * Такие влияния можно в дальнейшем встраивать в облрудование или давать игрокам и юнитам
 */
public interface Ability extends BaseEntityHeader {

   /**
   * Отменить действие способности
   * @param context
   * @return
   */
  @Deprecated
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
  @Deprecated
  List<EventType> getAllTriggerTypes();

  /**
   * Допустимое кол-во применений за ход. 0 - бесконечность
   * @return
   */
  int getUseCountPerRound();

  /**
   * Максимальное кол-во использований за игру. 0 - бесконечность
   * @return
   */
  int getTotalUseCount();

  /**
   * Базовые классы, к которым способность не применима
   */
  List<Class<? extends WarriorBaseClass>> getUnsupportedWarriorBaseClasses();

  /**
   * Создать объект-влияние на заданного юнита согласно действию влияния. Объект-влияние может быть древовидным
   * @param target
   * @return
   */
  Result<List<Influencer>> createModifier(Warrior target);

  /**
   * Вернуть объект на который оказывается воздействие данной способностью. "Против" кого она
   * @return
   */
  TargetTypeEnum getTargetType();

  /**
   * Вернуть тип объекта, который может использовать данную способность
   * @return
   */
  ActorTypeEnum getActorType();
}
