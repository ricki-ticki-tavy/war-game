package core.entity.weapon;

import api.entity.ability.Modifier;
import api.entity.weapon.Weapon;

import java.util.List;

public class AbstractWeapon implements Weapon {
  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public int getMinDamage() {
    return 0;
  }

  @Override
  public int getMaxDamage() {
    return 0;
  }

  @Override
  public int getCost() {
    return 0;
  }

  @Override
  public List<Modifier> getAdditionalModifiers() {
    return null;
  }

  @Override
  public boolean isUnrejectable() {
    return false;
  }

  @Override
  public int getUseCoountPerRound() {
    return 0;
  }

  @Override
  public int getTotalUseCount() {
    return 0;
  }
}
