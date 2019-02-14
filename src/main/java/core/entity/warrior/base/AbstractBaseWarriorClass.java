package core.entity.warrior.base;


import api.core.Context;
import api.core.Owner;
import api.entity.warrior.Warrior;
import api.enums.OwnerTypeEnum;
import api.game.ability.Ability;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.core.Event;
import api.game.map.Player;
import core.entity.abstracts.AbstractOwnerImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static core.system.error.GameErrors.WARRIOR_BASE_ATTRS_IS_FINAL;

public abstract class AbstractBaseWarriorClass extends AbstractOwnerImpl<Warrior> implements WarriorBaseClass {

  protected WarriorSBaseAttributesImpl warriorSBaseAttributes;
  protected List<Class<? extends Weapon>> supportedWeapons;
  protected final Map<String, Ability> abilities = new ConcurrentHashMap<>(10);
  //===================================================================================================
  //===================================================================================================

  public AbstractBaseWarriorClass( String title, String description) {
    super(null, OwnerTypeEnum.WARRIOR, "wrr", title, description);
    this.warriorSBaseAttributes = null;
  }
  //===================================================================================================

  @Override
  public void setWarriorSBaseAttributes(WarriorSBaseAttributesImpl warriorSBaseAttributes) {
    if (this.warriorSBaseAttributes != null) {
      WARRIOR_BASE_ATTRS_IS_FINAL.error();
    } else {
      this.warriorSBaseAttributes =  warriorSBaseAttributes;
    }

  }
  //===================================================================================================

  @Override
  public WarriorSBaseAttributesImpl getBaseAttributes() {
    return warriorSBaseAttributes;
  }
  //===================================================================================================

  @Override
  public Map<String, Ability> getAbilities() {
    return new HashMap<>(abilities);
  }
  //===================================================================================================

  @Override
  public boolean finishRound(Context context) {
    return false;
  }
  //===================================================================================================

  @Override
  public boolean startRound(Context context) {
    return false;
  }
  //===================================================================================================

  @Override
  public boolean isUsedInThisRound() {
    return false;
  }
  //===================================================================================================

  @Override
  public int getAbilityActionPoints() {
    return 0;
  }
  //===================================================================================================

  @Override
  public void fireEvent(Event event) {

  }
  //===================================================================================================

  @Override
  public List<Class<? extends Weapon>> getSupportedWeaponClasses() {
    return supportedWeapons;
  }
  //===================================================================================================

  @Override
  public void setSupportedWeaponClasses(List<Class<? extends Weapon>> supportedWeaponClasses) {
    this.supportedWeapons = supportedWeaponClasses;
  }
  //===================================================================================================

  @Override
  public int getHandsCount() {
    return warriorSBaseAttributes.getHandsCount();
  }
  //===================================================================================================

  @Override
  public boolean isSummonable() {
    return false;
  }
  //===================================================================================================

  @Override
  public WarriorBaseClass attachToWarrior(Warrior owner) {
    this.owner = owner;
    this.setContext(null);
    return this;
  }
  //===================================================================================================

}
