package core.entity.weapon;

import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.AttributeEnum;
import api.enums.LifeTimeUnit;
import api.enums.TargetTypeEnum;
import api.game.action.AttackResult;
import api.game.map.Player;
import core.entity.base.BaseModifier;
import core.entity.warrior.base.InfluencerImpl;
import core.game.action.AttackResultImpl;
import core.system.ResultImpl;
import core.system.error.GameErrors;

import java.util.*;

import static api.enums.AttributeEnum.HEALTH;
import static api.enums.TargetTypeEnum.ENEMY_WARRIOR;
import static core.system.error.GameErrors.*;

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
  protected String secondWeaponName;

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
  public String getSecondWeaponName() {
    return secondWeaponName;
  }
  //===================================================================================================

  @Override
  public Warrior getOwner() {
    return owner;
  }
  //===================================================================================================

  private Result generateAttackError(GameErrors gameError, Warrior targetWarrior) {
    return ResultImpl.fail(gameError.getError(
            owner.getContext().getGameName()
            , owner.getContext().getContextId()
            , owner.getWarriorBaseClass().getTitle()
            , owner.getTitle()
            , owner.getId()
            , owner.getOwner().getId()
            , targetWarrior.getWarriorBaseClass().getTitle()
            , targetWarrior.getTitle()
            , targetWarrior.getId()
            , targetWarrior.getOwner().getId()));
  }
  //===================================================================================================

  @Override
  public Weapon setOwner(Warrior owner) {
    if (this.owner != null && this.owner != owner) {
      throw GameErrors.SYSTEM_OBJECT_ALREADY_INITIALIZED.getError("warrior.owner", owner.toString());
    }
    this.owner = owner;
    return this;
  }
  //===================================================================================================

  // TODO Учесть кол-во потраченных единиц действия.
  // TODO разбить единицы действия по 2м (только двум !!!) рукам
  @Override
  public Result<AttackResult> attack(Warrior targetWarrior) {

    Result<AttackResult> attackResult = null;

    // для начала получим свободные очки действия.
    int availableActionPoints = owner.getWarriorSActionPoints(false);

    // размер ячейки карты
    int mapUnitSize = owner.getContext().getLevelMap().getSimpleUnitSize();

    // Размер воина (в "пикселях")
    int warriorSize = owner.getContext().getGameRules().getWarriorSize();

    // расстояние до цели. при этом считаем не расстояние между центрами юнитов, а расстояния от края юнита до края
    // юнита. При условии, что они круглые расстояние сократится с расстояния между центрами на два радиуса фигур
    // или, что равнозначно на размер юнита
    int distanceToTarget = owner.calcDistanceTo(targetWarrior.getCoords()) - warriorSize;

    // название оружия, которым нанесено поражение
    String weaponName = "";

    int maxDmg = 0, minDmg = 0;

    // если есть у оружия дистанционная атака и на нее хватает действия, то пробуем применить ее
    if (canDealRangedDamage && availableActionPoints >= rangedAttackCost) {
      // вроде можно и очков хватает. дистанционное оружие дествует только на дистанции. Если расстояние
      // менее дальности рукопашной атаки, то наносится будет ближняя атака, если доступна

      if (distanceToTarget >= minRangedAttackRange * mapUnitSize) {
        // расстояние более минимальной дистанции дальней атаки. Проверим, что дальность до цели не
        // превышает допустимую
        if (distanceToTarget > maxRangedAttackRange * mapUnitSize) {
          // "В игре %s (id %s)  воин '%s %s' (id %s) игрока %s не может атаковать воина '%s %s' (id %s) игрока %s : %s"
          attackResult = generateAttackError(WARRIOR_ATTACK_TARGET_IS_OUT_OF_RANGE, targetWarrior);
        } else {
          // вроде с целью все в норме. Проверим сколько осталось выстрелов. -1 - бесконечно
          if (totalRangedUseCount <= 0 && totalRangedUseCount != -1) {
            attackResult = generateAttackError(WARRIOR_ATTACK_WEAPON_IS_OUT_OF_CHARGES, targetWarrior);
          } else {
            // выстрелов тоже хватает. Проверим, что нету рядом врагов на расстоянии равном или меньшем
            // минимальной дистанции стрельбы. при расчете учитываем размер воина как и при расчете дистанции до цели
            // (см выше)
            List<Warrior> nearMeWarriorsList = owner.getContext().getLevelMap()
                    .getWarriors(owner.getTranslatedToGameCoords(), minRangedAttackRange * mapUnitSize + warriorSize, ENEMY_WARRIOR, owner.getOwner());
            if (nearMeWarriorsList.size() > 0) {
              // есть рядом противники. дистанционная атака невозможна, а выбранный для атаки противник находится на
              // расстоянии более минимальной для дистанционной. Тут ближняя атака невозможна, даже если это допускает
              // оружие
              attackResult = generateAttackError(WARRIOR_ATTACK_RANGED_NOT_POSIBLE_ENEMYS_IS_NEAR_ATTACKER, targetWarrior);
            }
            // проверки пройдены. Можно атаковать дистанционно
            // если расстояние до цели более расстояния, после которого урон спадает, то пересчитаем максимальный и
            // минимальный уроны
            if (distanceToTarget > fadeRangeStart * mapUnitSize) {
              // да. Надо пересчитывать макс и мин уроны
              int fadeCoef = 100 - ((distanceToTarget - fadeRangeStart * mapUnitSize) * fadeDamagePercentPerLength + mapUnitSize / 2 + 1)
                      / mapUnitSize;
              maxDmg = (rangedMaxDamage * fadeCoef) / 100;
              minDmg = (rangedMinDamage * fadeCoef) / 100;
            } else {
              maxDmg = rangedMaxDamage;
              minDmg = rangedMinDamage;
            }
            attackResult = ResultImpl.success(new AttackResultImpl(owner.getOwner(), owner
                    , this, targetWarrior.getOwner(), targetWarrior, rangedAttackCost));
            weaponName = title;
          }
        }
      }
    }
    // если мы тут , то либо не вышло с дистанционкой, либо ее нет у этого оружия
    if (attackResult == null) {
      if (canDealRangedDamage) {
        // нет рукопашной атаки или не хватает единиц действия
        // если оружие дистанционное, то дистанцией для рукопашки берем не параметры рукопашной атаки, а минимальную
        // дистанцию дистанционно атаки.
        int innerMeleeAttackRange = canDealRangedDamage ? minRangedAttackRange : meleeAttackRange;

        // проверим, что очков действия для атаки хватает
        if (availableActionPoints < meleeAttackCost) {
          attackResult = generateAttackError(WARRIOR_ATTACK_THERE_IS_NOT_ENOUGH_ACTION_POINTS, targetWarrior);
        } else {
          // очков хватает. Проверим расстояние до цели
          if (distanceToTarget >= innerMeleeAttackRange * mapUnitSize) {
            // расстояние велико для рукопашной атаки
            attackResult = generateAttackError(WARRIOR_ATTACK_TARGET_IS_OUT_OF_RANGE, targetWarrior);
          } else {
            // проверки пройдены. Можно атаковать
            maxDmg = rangedMaxDamage;
            minDmg = rangedMinDamage;
            attackResult = ResultImpl.success(new AttackResultImpl(owner.getOwner(), owner
                    , this, targetWarrior.getOwner(), targetWarrior, meleeAttackCost));
            weaponName = canDealRangedDamage ? secondWeaponName : title;
          }
        }
      } else if (canDealRangedDamage) {
        // если мы тут, то значит не хватило очков для дистанционки и нет рукопашной атаки
        attackResult = generateAttackError(WARRIOR_ATTACK_THERE_IS_NOT_ENOUGH_ACTION_POINTS, targetWarrior);
      } else {
        attackResult = generateAttackError(WARRIOR_ATTACK_UNKNOW_REASON_SHAISE, targetWarrior);
      }
    }

    if (attackResult.isSuccess()) {
      // если не было никаких ошибок, то физический урон подсчитан. Поместим его в итог атаки
      attackResult.getResult().addInfluencer(new InfluencerImpl(
              owner, this, LifeTimeUnit.JUST_NOW, 1
              , new BaseModifier(
              owner.getContext()
              , weaponName
              , weaponName
              , ENEMY_WARRIOR
              , HEALTH
              , minDmg < 1 ? 1 : minDmg
              , maxDmg < 1 ? 1 : maxDmg
              , 100)));

      // отправим своему плееру на возможное добавление влияний
      attackResult = owner.getOwner().innerAttachToAttackToWarrior(attackResult.getResult());

      // теперь отправим игроку-владельцу атакуемого юнита результат атаки, чтобы там уже и
      // произошел разбор всех влияний в том числе и физического урона

      // отправить атаку плееру атакуемого воина
      attackResult = targetWarrior.getOwner().innerWarriorUnderAttack(attackResult.getResult())
              .map(proceededAttackResult -> {
                // спишем очки, затраченные на атаку
                owner.getAttributes().addActionPoints(-proceededAttackResult.getConsumedActionPoints());
                // заблокируем откат
                owner.lockRollback();
                // заблокируем перемещение
                owner.lockMove();
                return ResultImpl.success(proceededAttackResult);
              });
    }
    return attackResult;

  }
  //===================================================================================================
}
