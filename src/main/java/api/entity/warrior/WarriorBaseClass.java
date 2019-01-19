package api.entity.warrior;

import api.WarriorSBaseAttributes;
import api.core.GameContext;
import api.entity.weapon.Weapon;
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
   * Возвращает значение параметроввоина
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
  boolean finishRound(GameContext context);

  /**
   * начать раунд игрока
   * @param context
   * @return
   */
  boolean startRound(GameContext context);

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
  void setWarriorSBaseAttributes(WarriorSBaseAttributes warriorSBaseAttributes);
}
