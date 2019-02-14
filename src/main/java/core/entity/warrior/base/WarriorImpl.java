package core.entity.warrior.base;

import api.core.Context;
import api.core.Owner;
import api.entity.stuff.Artifact;
import api.enums.OwnerTypeEnum;
import api.enums.TargetTypeEnum;
import api.game.ability.Influencer;
import api.core.Result;
import api.game.ability.Ability;
import api.game.ability.Modifier;
import api.entity.warrior.*;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.enums.PlayerPhaseType;
import api.geo.Coords;
import api.core.EventDataContainer;
import api.game.action.InfluenceResult;
import api.game.map.Player;
import core.entity.abstracts.AbstractOwnerImpl;
import core.game.action.InfluenceResultImpl;
import core.system.ResultImpl;
import core.system.error.GameError;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.*;

// TODO добавить поддержку ограничения оружия по допустимому списку
// TODO способности воина долждны действовать даже на этапе расстановки войск
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarriorImpl extends AbstractOwnerImpl<Player> implements Warrior {

  protected Map<Integer, WarriorSHand> hands;
  protected WarriorBaseClass warriorBaseClass;
  protected volatile Coords coords;
  protected boolean summoned;
  protected WarriorSBaseAttributes attributes;
  protected final Map<String, Influencer> influencers = new ConcurrentHashMap<>(50);

  protected volatile boolean touchedAtThisTurn = false;

  protected volatile Coords originalCoords;
  protected volatile boolean moveLocked;
  protected volatile boolean rollbackAvailable;
  protected volatile int treatedActionPointsForMove;
  protected final Map<String, Artifact<Warrior>> artifacts = new ConcurrentHashMap<>(10);

  protected final Map<String, Class<? extends Ability>> unsupportedAbilities = new ConcurrentHashMap<>(20);


  @Autowired
  private BeanFactory beanFactory;
  //===================================================================================================
  //===================================================================================================
  //===================================================================================================

  public WarriorImpl(Player owner, WarriorBaseClass warriorBaseClass, String title, Coords coords, boolean summoned) {
    super(owner, OwnerTypeEnum.WARRIOR, "wrr", title, title);
    this.warriorBaseClass = warriorBaseClass;
    this.attributes = warriorBaseClass.getBaseAttributes().clone();
    this.summoned = summoned;
    this.coords = new Coords(coords);
    int handsCount = warriorBaseClass.getHandsCount();
    hands = new ConcurrentHashMap(2);
    while (handsCount-- > 0) {
      hands.put(hands.size(), new WarriorSHandImpl());
    }
    this.warriorBaseClass.attachToWarrior(this);
  }
  //===================================================================================================

  @Override
  public Player getOwner() {
    return super.getOwner();
  }
  //===================================================================================================

  @Override
  public Result<Artifact<Warrior>> giveArtifactToWarrior(Class<? extends Artifact<Warrior>> artifactClass) {
    Result<Artifact<Warrior>> result = ResultImpl.success(null);
    return result.mapSafe(nullArt -> {
      Artifact<Warrior> artifact = beanFactory.getBean(artifactClass
              , this);
      return attachArtifact(artifact);
    });
  }
  //===================================================================================================

  @Override
  public Result<Artifact<Warrior>> attachArtifact(Artifact<Warrior> artifact) {
    Result<Artifact<Warrior>> result;
    if (artifacts.get(artifact.getTitle()) != null) {
      // уже есть такой артефакт. даем ошибку
      // "В игре %s. воин '%s %s' игрока '%s' уже владеет артефактом '%s'."
      result = ResultImpl.fail(ARTIFACT_ALREADY_EXISTS.getError(
              getContext().getGameName()
              , warriorBaseClass.getTitle()
              , title
              , owner.getId()
              , artifact.getTitle()));
    } else {
      artifact.attachToOwner(this);
      artifacts.put(artifact.getTitle(), artifact);
      // применить сразу действие артефакта
      artifact.applyToOwner();

      result = ResultImpl.success(artifact);
    }
    return result;
  }
  //===================================================================================================

  @Override
  public Result<List<Artifact<Warrior>>> getArtifacts() {
    return ResultImpl.success(new ArrayList<Artifact<Warrior>>(artifacts.values()));
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
  public Result<Weapon> findWeaponById(String weaponId) {
    return hands.values().stream()
            .filter(warriorSHand -> warriorSHand.hasWeapon(weaponId))
            .findFirst().map(warriorSHand -> warriorSHand.getWeaponById(weaponId))
            .map(weapon -> ResultImpl.success(weapon))
            .orElse(ResultImpl.fail(generateWeapoNotFoundError(weaponId)));
  }
  //===================================================================================================

  private Coords innerGetTranslatedToGameCoords() {
    // если игра уже в стадии игры, а не расстановки, юнит не трогали или если не заблокирована возможность отката, то
    // берем OriginalCoords в противном случае берем Coords
    return getContext().isGameRan()
            // игра идет
            && isRollbackAvailable() && !isMoveLocked()
            ? originalCoords
            : coords;
  }
  //===================================================================================================

  @Override
  public Coords getTranslatedToGameCoords() {
    return new Coords(innerGetTranslatedToGameCoords());
  }
  //===================================================================================================

  @Override
  public int calcMoveCost(Coords to) {
    Coords from = innerGetTranslatedToGameCoords();

    return (int) Math.round((double) getWarriorSMoveCost() * Math.sqrt((double) ((from.getX() - to.getX()) * (from.getX() - to.getX())
            + (from.getY() - to.getY()) * (from.getY() - to.getY()))) / (double) getContext().getLevelMap().getSimpleUnitSize());
  }
  //===================================================================================================

  @Override
  public int calcDistanceTo(Coords to) {
    return getContext().calcDistanceTo(innerGetTranslatedToGameCoords(), to);
  }
  //===================================================================================================

  @Override
  public int calcDistanceToTarget(Coords to) {
    return getContext().calcDistanceTo(coords, to);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(Coords to) {
    Result result;
    if (!moveLocked) {
      treatedActionPointsForMove = calcMoveCost(to);
      setTouchedOnThisTurn(true);
      this.coords = new Coords(to);
      result = ResultImpl.success(this);
    } else {
      // новые координаты воина уже заморожены и не могут быть отменены
      // Воин %s (id %s) игрока %s в игре %s (id %s) не может более выполнить перемещение в данном ходе
      result = ResultImpl
              .fail(WARRIOR_CAN_T_MORE_MOVE_ON_THIS_TURN.getError(
                      getWarriorBaseClass().getTitle()
                      , getId()
                      , getOwner().getId()
                      , getContext().getGameName()
                      , getContext().getContextId()));
    }
    return result;
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
    if (weapon.getNeededHandsCountToTakeWeapon() > 0) {
      int freePoints = 2 - hands.values().stream().map(hand -> hand.isFree() ? 0 : 1).reduce(0, (acc, chg) -> acc += chg);
      if (freePoints < weapon.getNeededHandsCountToTakeWeapon()) {
        result = ResultImpl.fail(WARRIOR_HANDS_NO_FREE_SLOTS.getError(String.valueOf(freePoints)
                , weapon.getTitle()
                , String.valueOf(weapon.getNeededHandsCountToTakeWeapon())));
      }
    }

    if (result == null) {
      AtomicInteger points = new AtomicInteger(weapon.getNeededHandsCountToTakeWeapon());
      // место есть в руках. Ищем свободную руку
      hands.values().stream()
              .filter(hand -> hand.isFree() && points.get() > 0)
              .forEach(hand -> {
                points.decrementAndGet();
                hand.addWeapon(weapon);
                weapon.setOwner(this);
              });
      result = ResultImpl.success(weapon);
    }
    getContext().fireGameEvent(null, WEAPON_TAKEN, new EventDataContainer(this, weapon, result), null);
    return result;
  }
  //===================================================================================================

  @Override
  public Result<Weapon> dropWeapon(String weaponInstanceId) {
    Result result = hands.values().stream().filter(hand -> hand.hasWeapon(weaponInstanceId)).findFirst()
            .map(warriorSHand -> ResultImpl.success(warriorSHand.removeWeapon(weaponInstanceId)))
            .orElse(ResultImpl.fail(generateWeapoNotFoundError(weaponInstanceId)));

    getContext().fireGameEvent(null
            , result.isSuccess() ? WEAPON_DROPED : WEAPON_TRY_TO_DROP
            , new EventDataContainer(this, result.isSuccess() ? result.getResult() : weaponInstanceId, result)
            , null);

    return result;
  }
  //===================================================================================================

  public Result<Warrior> ifWarriorAlied(Warrior warrior, boolean isAllied) {
    Result<Warrior> warriorResult = owner.findWarriorById(warrior.getId());
    if (warriorResult.isSuccess() == isAllied) {
      // утверждение совпало
      warriorResult = ResultImpl.success(warrior);
    } else {
      warriorResult = isAllied
              // ждали дружественного, а он - враг
              // "В игре %s (id %s)  воин '%s %s' (id %s) не является врагом для воина '%s %s' (id %s) игрока %s %s"
              ? ResultImpl.fail(WARRIOR_ATTACK_TARGET_WARRIOR_IS_NOT_ALIED.getError(
              getContext().getGameName()
              , getContext().getContextId()
              , warrior.getWarriorBaseClass().getTitle()
              , warrior.getTitle()
              , warrior.getId()
              , getWarriorBaseClass().getTitle()
              , getTitle()
              , getId()
              , getOwner().getId()
              , ""))
              // "В игре %s (id %s)  воин '%s %s' (id %s) является враждебным для воина '%s %s' (id %s) игрока %s %s"
              : ResultImpl.fail(WARRIOR_ATTACK_TARGET_WARRIOR_IS_ALIED.getError(
              getContext().getGameName()
              , getContext().getContextId()
              , warrior.getWarriorBaseClass().getTitle()
              , warrior.getTitle()
              , warrior.getId()
              , getWarriorBaseClass().getTitle()
              , getTitle()
              , getId()
              , getOwner().getId()
              , ""));
    }
    return warriorResult;
  }
  //===================================================================================================

  public Result<InfluenceResult> attackWarrior(Warrior targetWarrior, String weaponId) {
    // проверим, что это не дружественный воин
    return ifWarriorAlied(targetWarrior, false)
            // Найдем у своего воинаоружие
            .map(fineTargetWarrior -> findWeaponById(weaponId)
                    // вдарим
                    .map(weapon -> weapon.attack(fineTargetWarrior)));
  }
  //===================================================================================================

  @Override
  public Result<InfluenceResult> defenceWarrior(InfluenceResult attackResult) {
    // TODO реализовать рассчет защиты и особенностей воина

    return ResultImpl.success(attackResult);
  }
  //===================================================================================================

  private GameError generateWeapoNotFoundError(String weaponId) {
    //"В игре %s (id %s) у игрока %s воин '%s %s' (id %s) не имеет оружия с id '%s'"
    return WARRIOR_WEAPON_NOT_FOUND.getError(
            getContext().getGameName()
            , getContext().getContextId()
            , getOwner().getId()
            , getWarriorBaseClass().getTitle()
            , getTitle()
            , getId()
            , weaponId);

  }
  //===================================================================================================

  @Override
  public WarriorSBaseAttributes getAttributes() {
    return attributes;
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
    attributes.setActionPoints(playerPhaseType == PlayerPhaseType.ATACK_PHASE ? attributes.getMaxActionPoints() : attributes.getMaxDefenseActionPoints());
    attributes.setLuckMeleeAtack(getWarriorBaseClass().getBaseAttributes().getLuckMeleeAtack());
    attributes.setLuckRangeAtack(getWarriorBaseClass().getBaseAttributes().getLuckRangeAtack());
    attributes.setLuckDefense(getWarriorBaseClass().getBaseAttributes().getLuckDefense());
    attributes.setDeltaCostMove(getWarriorBaseClass().getBaseAttributes().getDeltaCostMove());
    // движение не заблокировано
    moveLocked = false;
    // на перемещение не использованио ничего
    treatedActionPointsForMove = 0;
    // возврат в начальную координату возможен
    rollbackAvailable = true;
    // начальные координаты
    originalCoords = new Coords(coords);
    // не использовался в этом ходе / защите
    setTouchedOnThisTurn(false);

    // обновить спосоности
    warriorBaseClass.getAbilities().values().stream().forEach(ability -> ability.revival());

    // восстановить оружие. На двуручном и более-ручном оружии могут быть кратные срабатывания восстановления. TODO поправить
    hands.values().stream()
            .forEach(warriorSHand -> warriorSHand.getWeapons()
                    .stream().forEach(weapon -> weapon.revival()));

    // применить способности класса воина
    InfluenceResult influenceResult = new InfluenceResultImpl(this.getOwner(), this, null, this.getOwner(), this, 0);
    warriorBaseClass.getAbilities().values().stream()
            .filter(ability -> ability.getOwnerTypeForAbility().equals(OwnerTypeEnum.WARRIOR))
            .forEach(ability -> ability.buildForTarget(this).stream()
                    .forEach(influencer -> influencer.applyToWarrior(influenceResult)));

    // применить влияния артефактов
    artifacts.values().stream()
            .forEach(artifact -> artifact.applyToOwner());
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
            .peak(warrior -> warrior.getOwner().findContext()
                    .peak(context -> getContext().fireGameEvent(null
                            , playerPhaseType == PlayerPhaseType.DEFENSE_PHASE ? WARRIOR_PREPARED_TO_DEFENCE : WARRIOR_PREPARED_TO_ATTACK
                            , new EventDataContainer(this), null)));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> prepareToDefensePhase() {
    return prepareToPhase(PlayerPhaseType.DEFENSE_PHASE);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> prepareToAttackPhase() {
    return prepareToPhase(PlayerPhaseType.ATACK_PHASE);
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(Modifier modifier, Owner source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    Influencer influencer = new InfluencerImpl(this, source, lifeTimeUnit, lifeTime, modifier);
    influencers.put(influencer.getId(), influencer);
    getContext().fireGameEvent(null, WARRIOR_INFLUENCER_ADDED
            , new EventDataContainer(influencer, this), null);
    return ResultImpl.success(influencer);
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(Influencer influencer) {
    influencer.attachToOwner(this);
    influencers.put(influencer.getId(), influencer);
    getContext().fireGameEvent(null, WARRIOR_INFLUENCER_ADDED
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
   * Удаление влияния
   *
   * @param influencer
   * @param silent     не отправлять уведомления о действии
   */
  void innerRemoveInfluencerFromWarrior(Influencer influencer, boolean silent) {
    influencers.remove(influencer.getId());
    if (!silent) {
      getContext().fireGameEvent(null, WARRIOR_INFLUENCER_REMOVED
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

  public int getWarriorSActionPoints(boolean forMove) {
    return forMove  // для перемещения (либо у юнита остатки после атаки и прочего, либо он свежак)
            // он свежак. Им еще не действовали в этом ходу
            || !isMoveLocked()
            // им действовали но есть возможности откатиться
            || isRollbackAvailable()
            // для движения
            ? attributes.getActionPoints()
            // для нанесения атаки
            : (attributes.getActionPoints() - treatedActionPointsForMove);
  }
  //===================================================================================================

  @Override
  public Coords getOriginalCoords() {
    return originalCoords;
  }
  //===================================================================================================

  @Override
  public int getWarriorSMoveCost() {
    return getAttributes().getArmorClass().getMoveCost()
            + getAttributes().getDeltaCostMove();
  }
  //===================================================================================================

  @Override
  public boolean isMoveLocked() {
    return moveLocked;
  }
  //===================================================================================================

  @Override
  public void lockMove() {
    this.moveLocked = true;
  }
  //===================================================================================================

  @Override
  public void lockRollback() {
    this.rollbackAvailable = false;
    // спишем очки за еремещение
    attributes.addActionPoints(-treatedActionPointsForMove);
    treatedActionPointsForMove = 0;
    this.originalCoords = new Coords(coords);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> rollbackMove() {
    Result<Warrior> result;
    // если откат не заблокирован
    if (isRollbackAvailable()) {
      coords = new Coords(originalCoords);
      // снять признак задействованности в данном ходе
      setTouchedOnThisTurn(false);
      // обнулить использованные очки
      setTreatedActionPointsForMove(0);

      result = ResultImpl.success(this);
      // уведомление
      getContext().fireGameEvent(null
              , WARRIOR_MOVE_ROLLEDBACK
              , new EventDataContainer(this, result)
              , null);
    } else {
      // В игре %s (id %s) игрок %s не может откатить перемещение воина '%s %s' (id %s) так как откат
      // заблокирован последующими действиями
      result = ResultImpl.fail(WARRIOR_CAN_T_ROLLBACK_MOVE.getError(
              getContext().getGameName()
              , getContext().getContextId()
              , getOwner().getId()
              , getWarriorBaseClass().getTitle()
              , getTitle()
              , getId()));
    }
    return result;
  }
  //===================================================================================================

  @Override
  public boolean isTouchedAtThisTurn() {
    return touchedAtThisTurn;
  }
  //===================================================================================================

  @Override
  public Warrior setTouchedOnThisTurn(boolean touchedAtThisTurn) {
    this.touchedAtThisTurn = touchedAtThisTurn;
    return this;
  }
  //===================================================================================================

  @Override
  public boolean isRollbackAvailable() {
    return rollbackAvailable;
  }
  //===================================================================================================

  @Override
  public int getTreatedActionPointsForMove() {
    return treatedActionPointsForMove;
  }
  //===================================================================================================

  @Override
  public Warrior setTitle(String title) {
    this.title = title;
    return this;
  }
  //===================================================================================================

  @Override
  public Map<String, Class<? extends Ability>> getUnsupportedAbilities() {
    return new HashMap<>(unsupportedAbilities);
  }
  //===================================================================================================

  @Override
  public void setTreatedActionPointsForMove(int treatedActionPointsForMove) {
    this.treatedActionPointsForMove = treatedActionPointsForMove;
  }
  //===================================================================================================

}
