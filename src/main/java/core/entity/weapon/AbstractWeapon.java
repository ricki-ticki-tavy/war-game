package core.entity.weapon;

import api.entity.ability.Modifier;
import api.entity.weapon.Weapon;
import api.entity.weapon.WeaponBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Абстрактное оружие. Общие методы
 */
public abstract class AbstractWeapon implements Weapon {

  protected String id = UUID.randomUUID().toString();
  protected String title;
  protected String description;
  protected int meleeMinDamage;
  protected int rangedMinDamage;
  protected int meleeMaxDamage;
  protected int rangedMaxDamage;
  protected int bitCost;
  protected Map<String, Modifier> additionalModifiers;
  protected boolean unrejectable;
  protected int useCountPerRound;
  protected int totalRangedUseCount;
  protected boolean canDealRangedDamage;
  protected int minRange;
  protected int maxRange;
  protected int fadeRangeStart;
  protected int fadeDamagePercentPerLength;
  protected int neededHandsCountToTakeWeapon;

  public AbstractWeapon(String id
          , String title
          , String description
          , int meleeMinDamage
          , int meleeMaxDamage
          , int rangedMinDamage
          , int rangedMaxDamage
          , int bitCost
          , Map<String, Modifier> additionalModifiers
          , boolean unrejectable
          , int useCountPerRound
          , int totalRangedUseCount
          , boolean canDealRangedDamage
          , int minRange
          , int maxRange
          , int fadeRangeStart
          , int fadeDamagePercentPerLength
          , int neededHandsCountToTakeWeapon) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.meleeMinDamage = meleeMinDamage;
    this.meleeMaxDamage = meleeMaxDamage;
    this.rangedMinDamage = rangedMinDamage;
    this.rangedMaxDamage = rangedMaxDamage;
    this.bitCost = bitCost;
    this.additionalModifiers = additionalModifiers;
    this.unrejectable = unrejectable;
    this.useCountPerRound = useCountPerRound;
    this.totalRangedUseCount = totalRangedUseCount;
    this.canDealRangedDamage = canDealRangedDamage;
    this.minRange = minRange;
    this.maxRange = maxRange;
    this.fadeRangeStart = fadeRangeStart;
    this.fadeDamagePercentPerLength = fadeDamagePercentPerLength;
    this.neededHandsCountToTakeWeapon = neededHandsCountToTakeWeapon;
  }

  protected AbstractWeapon(){

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
    return description;
  }

  @Override
  public int getMeleeMinDamage() {
    return meleeMinDamage;
  }

  @Override
  public int getRangedMinDamage() {
    return rangedMinDamage;
  }

  @Override
  public int getMeleeMaxDamage() {
    return meleeMaxDamage;
  }

  @Override
  public int getRangedMaxDamage() {
    return rangedMaxDamage;
  }

  @Override
  public int getBitCost() {
    return bitCost;
  }

  @Override
  public List<Modifier> getAdditionalModifiers() {
    return new ArrayList(additionalModifiers.values());
  }

  @Override
  public boolean isUnrejectable() {
    return unrejectable;
  }

  @Override
  public int getUseCountPerRound() {
    return useCountPerRound;
  }

  @Override
  public int getRangedTotalUseCount() {
    return totalRangedUseCount;
  }

  @Override
  public boolean isCanDealRangedDamage() {
    return false;
  }

  @Override
  public boolean canDealMelleDamage() {
    return canDealRangedDamage;
  }

  @Override
  public int getMinRange() {
    return minRange;
  }

  @Override
  public int getMaxRange() {
    return maxRange;
  }

  @Override
  public int getFadeRangeStart() {
    return fadeRangeStart;
  }

  @Override
  public int getFadeDamagePercentPerLength() {
    return fadeDamagePercentPerLength;
  }

  @Override
  public int getNeededHandsCountToTakeWeapon() {
    return neededHandsCountToTakeWeapon;
  }

  public static <W extends Weapon> WeaponBuilder getBuilder(Class<W> clazz) {
    return new WeaponBuilder(clazz);
  }

}
