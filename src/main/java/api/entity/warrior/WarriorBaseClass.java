package api.entity.warrior;

import api.core.Owner;
import api.core.Result;
import api.game.action.InfluenceResult;
import core.entity.warrior.base.WarriorSBaseAttributesImpl;
import api.core.Context;
import api.entity.weapon.Weapon;
import api.core.Event;
import api.game.ability.Ability;
import api.entity.base.BaseEntityHeader;

import java.util.List;
import java.util.Map;

/**
 * Базовый класс воина
 */
public interface WarriorBaseClass extends Owner {
  /**
   * Возвращает значение параметроввоина
   * @return
   */
  WarriorSBaseAttributesImpl getBaseAttributes();

  /**
   * ВОзвращает способности воина
   * @return
   */
  Map<String, Ability> getAbilities();

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
  void fireEvent(Event event);

  /**
   * Задать поддерживаемое вооружение
   * @param supportedWeaponClasses
   * @return
   */
  void setSupportedWeaponClasses(List<Class<? extends Weapon>> supportedWeaponClasses);

  /**
   * Поддерживаемые классы вооружения
   * @return
   */
  List<Class<? extends Weapon>> getSupportedWeaponClasses();

  /**
   * Установить наборбазавых атрибутов
   * @param warriorSBaseAttributes
   */
  void setWarriorSBaseAttributes(WarriorSBaseAttributesImpl warriorSBaseAttributes);

  /**
   * Этот метод вызывается когда воин игрока атакуется. В этом методе происходит анализ всех нанесенныхз уронов,
   * перерасчет их (в случае наличия сопротивления, брони и прочее)
   * @param attackResult
   * @return
   */
  Result<InfluenceResult> innerWarriorUnderAttack(InfluenceResult attackResult);

  /**
   * Возвращает количество рук воина
   * @return
   */
  int getHandsCount();

  /**
   * Указывает можно ли призывать этого воина
   * @return
   */
  boolean isSummonable();

  /**
   * задать юнита, созданного с этого базового класса
   * @param owner
   * @return
   */
  WarriorBaseClass attachToWarrior(Warrior owner);

}
