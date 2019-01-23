package core.entity.warrior.base;

import api.entity.warrior.WarriorSHand;
import api.entity.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Рука воина. Вернее то, что в ней содержится
 */
public class WarriorSHandImpl implements WarriorSHand{
  private Map<Integer, Weapon> weapons = new ConcurrentHashMap<>(3);

  @Override
  public List<Weapon> getWeapons() {
    return new ArrayList<>(weapons.values());
  }

  @Override
  public boolean isFree() {
    int busyPoints = weapons.values().stream()
            .map(weapon -> weapon.getNeededHandsCountToTakeWeapon())
            .reduce(0, (acc, chg) -> acc += chg);
    return busyPoints == 0;
  }

  @Override
  public void addWeapon(Weapon weapon) {
    weapons.put(weapons.size(), weapon);
  }

  @Override
  public boolean removeWeapon(String weaponInstanceId) {
    return false;
  }
}
