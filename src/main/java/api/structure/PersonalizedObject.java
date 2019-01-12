package api.structure;

import java.util.List;

/**
 * Объект с особыми способностями
 */
public interface PersonalizedObject {
  /**
   * Вернуть способности объекта
   * @return
   */
  List<BaseAbility> getAbilities();
}
