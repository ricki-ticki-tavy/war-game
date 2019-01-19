package core.entity.warrior;


import api.WarriorSBaseAttributes;
import api.core.GameContext;
import api.entity.ability.Ability;
import api.entity.warrior.WarriorBaseClass;
import api.entity.warrior.WarriorSHand;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.game.GameEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static core.system.error.GameErrors.GAME_ERROR_BASE_WARRIOR_S_ATTRS_IS_FINAL;

public abstract class AbstractBaseWarriorClass implements WarriorBaseClass {

  protected String id = UUID.randomUUID().toString();
  protected String title;
  protected String description;
  protected WarriorSBaseAttributes warriorSBaseAttributes;
  protected List<Class<? extends Weapon>> supportedWeapons;


  public AbstractBaseWarriorClass(String title, String description) {
    this.title = title;
    this.description = description;
    this.warriorSBaseAttributes = null;
  }

  @Override
  public void setWarriorSBaseAttributes(WarriorSBaseAttributes warriorSBaseAttributes) {
    if (this.warriorSBaseAttributes != null) {
      GAME_ERROR_BASE_WARRIOR_S_ATTRS_IS_FINAL.error();
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
  public WarriorSBaseAttributes getBaseAttributes() {
    return null;
  }

  @Override
  public List<Ability> getAbilities(EventType triggerType) {
    return null;
  }

  @Override
  public boolean finishRound(GameContext context) {
    return false;
  }

  @Override
  public boolean startRound(GameContext context) {
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
  public void fireEvent(GameEvent event) {

  }

  @Override
  public List<Class<? extends Weapon>> getSupportedWeaponClasses() {
    return supportedWeapons;
  }

  @Override
  public void setSupportedWeaponClasses(List<Class<? extends Weapon>> supportedWeaponClasses) {
    this.supportedWeapons = supportedWeaponClasses;
  }
}
