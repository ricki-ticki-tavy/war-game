package core.entity.warrior.base;


import api.core.Context;
import api.game.ability.Ability;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.core.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static core.system.error.GameErrors.WARRIOR_BASE_ATTRS_IS_FINAL;

public abstract class AbstractBaseWarriorClass implements WarriorBaseClass {

  protected String id = UUID.randomUUID().toString();
  protected String title;
  protected String description;
  protected WarriorSBaseAttributesImpl warriorSBaseAttributes;
  protected List<Class<? extends Weapon>> supportedWeapons;
  protected final Map<String, Ability> abilities = new ConcurrentHashMap<>(10);

  public AbstractBaseWarriorClass(String title, String description) {
    this.title = title;
    this.description = description;
    this.warriorSBaseAttributes = null;
  }

  @Override
  public void setWarriorSBaseAttributes(WarriorSBaseAttributesImpl warriorSBaseAttributes) {
    if (this.warriorSBaseAttributes != null) {
      WARRIOR_BASE_ATTRS_IS_FINAL.error();
    } else {
      this.warriorSBaseAttributes =  warriorSBaseAttributes;
    }

  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public WarriorSBaseAttributesImpl getBaseAttributes() {
    return warriorSBaseAttributes;
  }

  @Override
  public Map<String, Ability> getAbilities() {
    return new HashMap<>(abilities);
  }

  @Override
  public boolean finishRound(Context context) {
    return false;
  }

  @Override
  public boolean startRound(Context context) {
    return false;
  }

  @Override
  public boolean isUsedInThisRound() {
    return false;
  }

  @Override
  public int getAbilityActionPoints() {
    return 0;
  }

  @Override
  public void fireEvent(Event event) {

  }

  @Override
  public List<Class<? extends Weapon>> getSupportedWeaponClasses() {
    return supportedWeapons;
  }

  @Override
  public void setSupportedWeaponClasses(List<Class<? extends Weapon>> supportedWeaponClasses) {
    this.supportedWeapons = supportedWeaponClasses;
  }

  @Override
  public int getHandsCount() {
    return warriorSBaseAttributes.getHandsCount();
  }

  @Override
  public boolean isSummonable() {
    return false;
  }

}
