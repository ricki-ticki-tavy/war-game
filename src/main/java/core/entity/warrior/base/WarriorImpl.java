package core.entity.warrior.base;

import api.core.Context;
import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.*;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.enums.PlayerPhaseType;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.map.Player;
import core.system.ResultImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.WARRIOR_HANDS_NO_FREE_SLOTS;
import static core.system.error.GameErrors.WARRIOR_WEAPON_NOT_FOUND;

// TODO добавить поддержку ограничения оружия по допустимому списку
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarriorImpl implements Warrior {

  protected Map<Integer, WarriorSHand> hands;
  protected WarriorBaseClass warriorBaseClass;
  protected Coords coords;
  protected final String id = UUID.randomUUID().toString();
  protected String title;
  protected boolean summoned;
  protected Context gameContext;
  protected Player owner;
  protected WarriorSBaseAttributes attributes;
  protected Map<String, Influencer> influencers = new ConcurrentHashMap<>(50);

  @Autowired
  private BeanFactory beanFactory;
  //===================================================================================================
  //===================================================================================================
  //===================================================================================================

  public WarriorImpl(Context gameContext, Player owner, WarriorBaseClass warriorBaseClass, String title, Coords coords, boolean summoned) {
    this.warriorBaseClass = warriorBaseClass;
    this.attributes = warriorBaseClass.getBaseAttributes().clone();
    this.title = title;
    this.summoned = summoned;
    this.gameContext = gameContext;
    this.owner = owner;
    this.coords = new Coords(coords);
    int handsCount = warriorBaseClass.getHandsCount();
    hands = new ConcurrentHashMap(2);
    while (handsCount-- > 0) {
      hands.put(hands.size(), new WarriorSHandImpl());
    }
  }
  //===================================================================================================

  @Override
  public WarriorBaseClass getWarriorBaseClass() {
    return warriorBaseClass;
  }
  //===================================================================================================

  @Override
  public boolean isSummoned() {
    return summoned;
  }
  //===================================================================================================

  @Override
  public List<WarriorSHand> getHands() {
    return new LinkedList(hands.values());
  }
  //===================================================================================================

  @Override
  public List<Weapon> getWeapons() {
    Set<Weapon> weaponSet = new HashSet<>(5);
    hands.values().stream().forEach(hand ->
            hand.getWeapons().stream().forEach(weapon -> weaponSet.add(weapon)));

    return new ArrayList<>(weaponSet);
  }
  //===================================================================================================

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
    return "";
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(Coords coords) {
    this.coords = new Coords(coords);
    Result result = ResultImpl.success(this);
    return result;
  }
  //===================================================================================================

  @Override
  public Player getOwner() {
    return owner;
  }
  //===================================================================================================

  @Override
  public Coords getCoords() {
    return new Coords(this.coords);
  }
  //===================================================================================================

  @Override
  public Result takeWeapon(Class<? extends Weapon> weaponClass) {
    Result result = null;
    Weapon weapon = beanFactory.getBean(weaponClass);
    try {
      if (weapon.getNeededHandsCountToTakeWeapon() > 0) {
        int freePoints = 2 - hands.values().stream().map(hand -> hand.isFree() ? 0 : 1).reduce(0, (acc, chg) -> acc += chg);
        if (freePoints < weapon.getNeededHandsCountToTakeWeapon()) {
          result = ResultImpl.fail(WARRIOR_HANDS_NO_FREE_SLOTS.getError(String.valueOf(freePoints)
                  , weapon.getTitle()
                  , String.valueOf(weapon.getNeededHandsCountToTakeWeapon())));
          return result;
        }
      }

      AtomicInteger points = new AtomicInteger(weapon.getNeededHandsCountToTakeWeapon());
      // место есть в руках. Ищем свободную руку
      hands.values().stream()
              .filter(hand -> hand.isFree() && points.get() > 0)
              .forEach(hand -> {
                points.decrementAndGet();
                hand.addWeapon(weapon);
              });
      result = ResultImpl.success(weapon);
      return result;
    } finally {
      gameContext.fireGameEvent(null, WEAPON_TAKEN, new EventDataContainer(this, weapon, result), null);
    }
  }
  //===================================================================================================

  @Override
  public Result<Weapon> dropWeapon(String weaponInstanceId) {
    Result result = hands.values().stream().filter(hand -> hand.hasWeapon(weaponInstanceId)).findFirst()
            .map(warriorSHand -> ResultImpl.success(warriorSHand.removeWeapon(weaponInstanceId)))
            .orElse(ResultImpl.fail(WARRIOR_WEAPON_NOT_FOUND.getError(weaponInstanceId)));

    gameContext.fireGameEvent(null
            , result.isSuccess() ? WEAPON_DROPED : WEAPON_TRY_TO_DROP
            , new EventDataContainer(this, result.isSuccess() ? result.getResult() : weaponInstanceId, result)
            , null);

    return result;
  }
  //===================================================================================================

  @Override
  public Result<WarriorSBaseAttributes> getAttributes() {
    return ResultImpl.success(attributes);
  }
  //===================================================================================================

  /**
   * Применяет все влияния, оказываемые на юнит
   *
   * @param playerPhaseType
   * @return
   */
  private Result<Warrior> applayInfluences(PlayerPhaseType playerPhaseType) {
    // TODO соберем все влияния, что наложены на воина.
    // сначала соберем его личные способности
    return ResultImpl.success(this);

  }
  //===================================================================================================

  /**
   * Восстанавливает значения атрибутов, которые могут быть восстановлены на заданный режим.
   * Например кол-во очков действия для режима хода или защиты, максимальный запас здоровья и прочее
   */
  private void restoreAttributesAvailableForRestoration(PlayerPhaseType playerPhaseType) {
    attributes.setAbilityActionPoints(attributes.getMaxAbilityActionPoints());
    attributes.setActionPoints(playerPhaseType == PlayerPhaseType.PLAYER_PHASE_TYPE_ATACK ? attributes.getMaxActionPoints() : attributes.getMaxDefenseActionPoints());
    attributes.setLuckMeleeAtack(getWarriorBaseClass().getBaseAttributes().getLuckMeleeAtack());
    attributes.setLuckRangeAtack(getWarriorBaseClass().getBaseAttributes().getLuckRangeAtack());
    attributes.setLuckDefense(getWarriorBaseClass().getBaseAttributes().getLuckDefense());
    attributes.setDeltaCostMove(getWarriorBaseClass().getBaseAttributes().getDeltaCostMove());
  }
  //===================================================================================================

  /**
   * Плдготовить воина к одной из двух фаз
   *
   * @param playerPhaseType
   */
  private Result<Warrior> prepareToPhase(PlayerPhaseType playerPhaseType) {
    restoreAttributesAvailableForRestoration(playerPhaseType);
    return applayInfluences(playerPhaseType)
            .doIfSuccess(warrior -> warrior.getOwner().findContext()
                    .doIfSuccess(context -> context.fireGameEvent(null
                            , playerPhaseType == PlayerPhaseType.PLAYER_PHASE_TYPE_DEFENSE ? WARRIOR_PREPARED_TO_DEFENCE : WARRIOR_PREPARED_TO_ATTACK
                            , new EventDataContainer(this), null)));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> prepareToDefensePhase() {
    return prepareToPhase(PlayerPhaseType.PLAYER_PHASE_TYPE_DEFENSE);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> prepareToAttackPhase() {
    return prepareToPhase(PlayerPhaseType.PLAYER_PHASE_TYPE_ATACK);
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    Influencer influencer = new InfluencerImpl(this, modifier, source, lifeTimeUnit, lifeTime);
    influencers.put(influencer.getId(), influencer);
    getOwner().findContext().getResult().fireGameEvent(null, WARRIOR_INFLUENCER_ADDED
            , new EventDataContainer(influencer, this), null);
    return ResultImpl.success(influencer);
  }
  //===================================================================================================

  @Override
  public Result<List<Influencer>> getWarriorSInfluencers() {
    return ResultImpl.success(new ArrayList<>(influencers.values()));
  }
  //===================================================================================================

  /**
   * Удаление
   *
   * @param influencer
   * @param silent
   */
  void innerRemoveInfluencerFromWarrior(Influencer influencer, boolean silent) {
    influencers.remove(influencer.getId());
    if (!silent) {
      getOwner().findContext().getResult().fireGameEvent(null, WARRIOR_INFLUENCER_REMOVED
              , new EventDataContainer(influencer, this), null);
    }
  }
  //===================================================================================================

  @Override
  public Result<Influencer> removeInfluencerFromWarrior(Influencer influencer, boolean silent) {
    influencer.removeFromWarrior(silent);
    innerRemoveInfluencerFromWarrior(influencer, silent);
    return ResultImpl.success(influencer);
  }
  //===================================================================================================

}
