package api.game.ability;

import api.core.Context;
import api.core.Owner;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.OwnerTypeEnum;
import api.enums.EventType;
import api.enums.PlayerPhaseType;
import api.enums.TargetTypeEnum;

import java.util.List;
import java.util.Set;

/**
 * Способность. класс высшего уровня. Представляет собой способность влиять на один или несколько параметров юнита или
 * игрока при различных условиях. Этот класс формирует объекты влияния на конечные цели. Объекты влияния прикрепляются к
 * цели на определенное условиями время и оказывают влияние на его атрибуты.
 * <p>
 * Например способностью можно назвать : поглощение доп.единиц действия при атаке или урон огнем и так далее.
 * Такие влияния можно в дальнейшем встраивать в облрудование или давать игрокам и юнитам
 */
public interface Ability extends Owner {

  /**
   * Отменить действие способности
   *
   * @param context
   * @return
   */
  @Deprecated
  boolean rollback(Context context);

  /**
   * Получить события, при которых действует артифакт
   *
   * @return
   */
  List<EventType> getEventsWhenActive();

  /**
   * Допустимое кол-во применений за ход. 0 - бесконечность
   *
   * @return
   */
  int getUseCountPerRound();

  /**
   * Максимальное кол-во использований за игру. 0 - бесконечность
   *
   * @return
   */
  int getTotalUseCount();

  /**
   * Базовые классы, к которым способность не применима
   */
  List<Class<? extends WarriorBaseClass>> getUnsupportedWarriorBaseClasses();

  /**
   * Создать объект-влияние на заданного юнита согласно действию влияния. Объект-влияние может быть древовидным
   *
   * @param target
   * @return
   */
  List<Influencer> buildForTarget(Warrior target);

  /**
   * возвращает активна ли сейчас способность
   *
   * @return
   */
  boolean isActive();

  /**
   * Вернуть объект на который оказывается воздействие данной способностью. "Против" кого она
   *
   * @return
   */
  TargetTypeEnum getTargetType();

  /**
   * Вернуть тип объекта, который может использовать данную способность
   *
   * @return
   */
  OwnerTypeEnum getOwnerTypeForAbility();

  /**
   * Восстановиться после хода
   *
   * @return
   */
  Ability revival();

  /**
   * Возвращает фазы, когда способность активна. Актуально для способностей юнита.
   * @return
   */
  Set<PlayerPhaseType> getActivePhase();
}
