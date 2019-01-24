package core.entity.weapon;

import api.entity.ability.Modifier;
import api.entity.weapon.Weapon;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Map;
import java.util.UUID;

/**
 * билдер класса вооружения
 */
public final class WeaponBuilder<W extends Weapon> {
  private String id = UUID.randomUUID().toString();
  private String title;
  private String description;
  private int meleeMinDamage;
  private int rangedMinDamage;
  private int meleeMaxDamage;
  private int rangedMaxDamage;
  private int bitCost;
  private Map<String, Modifier> additionalModifiers;
  private boolean unrejectable;
  private int useCountPerRound;
  private int totalRangedUseCount;
  private boolean canDealRangedDamage;
  private int minRange;
  private int maxRange;
  private int fadeRangeStart;
  private int fadeDamagePercentPerLength;
  private int neededHandsCountToTakeWeapon;
  private Class<W> clazz;

  @Autowired
  private BeanFactory beanFactory;

  public WeaponBuilder(Class<W> clazz){
    this.clazz = clazz;
  }

  public WeaponBuilder<W> setTitle(String title){
    this.title = title;
    return this;
  }

  public WeaponBuilder<W> setDescription(String description){
    this.description = description;
    return this;
  }


  public WeaponBuilder<W> setId(String id) {
    this.id = id;
    return this;
  }

  public WeaponBuilder<W> setMeleeMinDamage(int meleeMinDamage) {
    this.meleeMinDamage = meleeMinDamage;
    return this;
  }

  public WeaponBuilder<W> setRangedMinDamage(int rangedMinDamage) {
    this.rangedMinDamage = rangedMinDamage;
    return this;
  }

  public WeaponBuilder<W> setMeleeMaxDamage(int meleeMaxDamage) {
    this.meleeMaxDamage = meleeMaxDamage;
    return this;
  }

  public WeaponBuilder<W> setRangedMaxDamage(int rangedMaxDamage) {
    this.rangedMaxDamage = rangedMaxDamage;
    return this;
  }

  public WeaponBuilder<W> setBitCost(int bitCost) {
    this.bitCost = bitCost;
    return this;
  }

  public WeaponBuilder<W> setAdditionalModifiers(Map<String, Modifier> additionalModifiers) {
    this.additionalModifiers = additionalModifiers;
    return this;
  }

  public WeaponBuilder<W> setUnrejectable(boolean unrejectable) {
    this.unrejectable = unrejectable;
    return this;
  }

  public WeaponBuilder<W> setUseCountPerRound(int useCountPerRound) {
    this.useCountPerRound = useCountPerRound;
    return this;
  }

  public WeaponBuilder<W> setTotalRangedUseCount(int totalRangedUseCount) {
    this.totalRangedUseCount = totalRangedUseCount;
    return this;
  }

  public WeaponBuilder<W> setCanDealRangedDamage(boolean canDealRangedDamage) {
    this.canDealRangedDamage = canDealRangedDamage;
    return this;
  }

  public WeaponBuilder<W> setMinRange(int minRange) {
    this.minRange = minRange;
    return this;
  }

  public WeaponBuilder<W> setMaxRange(int maxRange) {
    this.maxRange = maxRange;
    return this;
  }

  public WeaponBuilder<W> setFadeRangeStart(int fadeRangeStart) {
    this.fadeRangeStart = fadeRangeStart;
    return this;
  }

  public WeaponBuilder<W> setFadeDamagePercentPerLength(int fadeDamagePercentPerLength) {
    this.fadeDamagePercentPerLength = fadeDamagePercentPerLength;
    return this;
  }

  public WeaponBuilder<W> setNeededHandsCountToTakeWeapon(int neededHandsCountToTakeWeapon) {
    this.neededHandsCountToTakeWeapon = neededHandsCountToTakeWeapon;
    return this;
  }

  public Weapon build(){
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    return beanFactory.getBean(clazz, id, title, description, meleeMinDamage, meleeMaxDamage
          , rangedMinDamage, rangedMaxDamage, bitCost, additionalModifiers, unrejectable, useCountPerRound
            , totalRangedUseCount, canDealRangedDamage, minRange, maxRange, fadeRangeStart, fadeDamagePercentPerLength
          , neededHandsCountToTakeWeapon);
  }
}
