package api.entity.warrior;

import api.entity.weapon.Weapon;

import java.util.List;

/**
 * Рука воина.
 */
public interface WarriorSHand {
  /**
   * Получить оружие, находящееся в руке
   * @return
   */
  List<Weapon> getWeapons();

  /**
   * добавить в коллекцию оружие
   * @param weapon
   * @return
   */
  void addWeapon(Weapon weapon);

  /**
   * Удалить из коллекции оружие
   * @param weaponInstanceId
   * @return
   */
  Weapon removeWeapon(String weaponInstanceId);

  /**
   * Проверить наличия у юнита оружия с заданным id
   * @param weaponInstanceId
   * @return
   */
  boolean hasWeapon(String weaponInstanceId);

  /**
   * Признак, что рука своодна
   * @return
   */
  boolean isFree();


}
