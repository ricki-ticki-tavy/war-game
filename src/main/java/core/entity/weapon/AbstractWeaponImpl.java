package core.entity.weapon;

import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.action.AttackResult;
import api.game.map.Player;
import core.system.ResultImpl;
import core.system.error.GameError;
import core.system.error.GameErrors;

import java.util.*;

/**
 * Абстрактное оружие. Общие методы
 */
public abstract class AbstractWeaponImpl implements Weapon {

  protected String id = UUID.randomUUID().toString();
  protected String title;
  protected String description;
  protected int meleeMinDamage;
  protected int rangedMinDamage;
  protected int meleeMaxDamage;
  protected int rangedMaxDamage;
  protected int rangedAttackCost;
  protected int meleeAttackCost;
  protected Map<String, Modifier> additionalModifiers;
  protected boolean unrejectable;
  protected int useCountPerRound;
  protected int totalRangedUseCount;
  protected boolean canDealRangedDamage;
  protected int minRangedAttackRange;
  protected int maxRangedAttackRange;
  protected int meleeAttackRange;
  protected int fadeRangeStart;
  protected int fadeDamagePercentPerLength;
  protected int neededHandsCountToTakeWeapon;

  protected Warrior owner;

  public AbstractWeaponImpl(
          Warrior owner
          , String id
          , String title
          , String description
          , int meleeMinDamage
          , int meleeMaxDamage
          , int rangedMinDamage
          , int rangedMaxDamage
          , int rangedAttackCost
          , int meleeAttackCost
          , Map<String, Modifier> additionalModifiers
          , boolean unrejectable
          , int useCountPerRound
          , int totalRangedUseCount
          , boolean canDealRangedDamage
          , int minRangedAttackRange
          , int maxRangedAttackRange
          , int meleeAttackRange
          , int fadeRangeStart
          , int fadeDamagePercentPerLength
          , int neededHandsCountToTakeWeapon) {
    this.owner = owner;
    this.id = id;
    this.title = title;
    this.description = description;
    this.meleeMinDamage = meleeMinDamage;
    this.meleeMaxDamage = meleeMaxDamage;
    this.rangedMinDamage = rangedMinDamage;
    this.rangedMaxDamage = rangedMaxDamage;
    this.rangedAttackCost = rangedAttackCost;
    this.meleeAttackCost = meleeAttackCost;
    this.additionalModifiers = additionalModifiers;
    this.unrejectable = unrejectable;
    this.useCountPerRound = useCountPerRound;
    this.totalRangedUseCount = totalRangedUseCount;
    this.canDealRangedDamage = canDealRangedDamage;
    this.minRangedAttackRange = minRangedAttackRange;
    this.maxRangedAttackRange = maxRangedAttackRange;
    this.meleeAttackRange = meleeAttackRange;
    this.fadeRangeStart = fadeRangeStart;
    this.fadeDamagePercentPerLength = fadeDamagePercentPerLength;
    this.neededHandsCountToTakeWeapon = neededHandsCountToTakeWeapon;
  }

  protected AbstractWeaponImpl() {
  }

  @Override
  public String getId() {
    return id;
  }
  //===================================================================================================

  @Override
  public String getTitle() {
    return title;
  }
  //===================================================================================================

  @Override
  public String getDescription() {
    return description;
  }
  //===================================================================================================

  @Override
  public int getMeleeMinDamage() {
    return meleeMinDamage;
  }
  //===================================================================================================

  @Override
  public int getRangedMinDamage() {
    return rangedMinDamage;
  }
  //===================================================================================================

  @Override
  public int getMeleeMaxDamage() {
    return meleeMaxDamage;
  }
  //===================================================================================================

  @Override
  public int getRangedMaxDamage() {
    return rangedMaxDamage;
  }
  //===================================================================================================

  @Override
  public int getRangedAttackCost() {
    return rangedAttackCost;
  }
  //===================================================================================================

  @Override
  public int getMeleeAttackCost() {
    return meleeAttackCost;
  }
  //===================================================================================================

  @Override
  public List<Modifier> getAdditionalModifiers() {
    return new ArrayList(additionalModifiers.values());
  }
  //===================================================================================================

  @Override
  public boolean isUnrejectable() {
    return unrejectable;
  }
  //===================================================================================================

  @Override
  public int getUseCountPerRound() {
    return useCountPerRound;
  }
  //===================================================================================================

  @Override
  public int getRangedTotalUseCount() {
    return totalRangedUseCount;
  }
  //===================================================================================================

  @Override
  public boolean isCanDealRangedDamage() {
    return false;
  }
  //===================================================================================================

  @Override
  public boolean canDealMelleDamage() {
    return canDealRangedDamage;
  }
  //===================================================================================================

  @Override
  public int getRangedAttackMinRange() {
    return minRangedAttackRange;
  }
  //===================================================================================================

  @Override
  public int getRangedAttackMaxRange() {
    return maxRangedAttackRange;
  }
  //===================================================================================================

  @Override
  public int getMeleeAttackRange() {
    return meleeAttackRange;
  }
  //===================================================================================================

  @Override
  public int getFadeRangeStart() {
    return fadeRangeStart;
  }
  //===================================================================================================

  @Override
  public int getFadeDamagePercentPerLength() {
    return fadeDamagePercentPerLength;
  }
  //===================================================================================================

  @Override
  public int getNeededHandsCountToTakeWeapon() {
    return neededHandsCountToTakeWeapon;
  }
  //===================================================================================================

  @Override
  public Warrior getOwner() {
    return owner;
  }
  //===================================================================================================

  private Result generateAttackError(Warrior targetWarrior, String message) {
    return ResultImpl.fail(GameErrors.WEAPON_ATTACK_TARGET_IS_OUT_OF_RANGE.getError(
            owner.getContext().getGameName()
            , owner.getContext().getContextId()
            , owner.getWarriorBaseClass().getTitle()
            , owner.getTitle()
            , owner.getId()
            , owner.getOwner().getId()
            , targetWarrior.getWarriorBaseClass().getTitle()
            , targetWarrior.getTitle()
            , targetWarrior.getId()
            , targetWarrior.getOwner().getId()
            , " так как дистанция для выстрела велика"));
  }
  //===================================================================================================

  @Override
  public Weapon setOwner(Warrior owner) {
    if (this.owner != null && this.owner != owner) {
      throw GameErrors.SYSTEM_OBJECT_NOT_NULL.getError("warrior.owner", owner.toString());
    }
    this.owner = owner;
    return this;
  }
  //===================================================================================================

  @Override
  public Result<AttackResult> attack(Warrior targetWarrior) {

    Result<AttackResult> attackResult = null;

    // для начала получим свободные очки действия.
    int availableActionPoints = owner.getWarriorSActionPoints(false);

    // расстояние до цели
    int distanceToTarget = owner.calcDistanceTo(targetWarrior.getCoords());

    // если есть у оружия дистанционная атака и на нее хватает действия, то пробуем применить ее
    if (canDealRangedDamage && availableActionPoints >= rangedAttackCost) {
      // вроде можно и очков хватает. дистанционное оружие дествует только на дистанции. Если расстояние
      // менее дальности рукопашной атаки, то наносится будет ближняя атака, если доступна

      if (distanceToTarget >= minRangedAttackRange) {
        // расстояние более минимальной дистанции дальней атаки. Проверим, что дальность до цели не
        // превышает допустимую
        if (distanceToTarget > maxRangedAttackRange) {
          // "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может атаковать воина '%s %s' (id %s) игрока %s : %s"
          attackResult = generateAttackError(targetWarrior, " так как дистанция для выстрела велика");
        } else {
          // вроде с целью все в норме. Проверим сколько осталось выстрелов
          if (totalRangedUseCount <= 0){
            attackResult = generateAttackError(targetWarrior, " Не осталось выстрелов");
          } else {
            // выстрелов тоже хватает. Проверим, что нету рядом врагов на расстоянии равном или меньшем
            // минимальной дистанции стрельбы
            List<Warrior> nearWarriorList = owner.getContext().getLevelMap()
                    .getWarriors(owner.getTranslatedToGameCoords(), minRangedAttackRange);

          }
        }
      }
    }

    return attackResult;

  }
  //===================================================================================================
}
