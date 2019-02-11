package core.entity.player;

import api.core.Context;
import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.LifeTimeUnit;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.action.AttackResult;
import api.game.map.Player;
import core.system.ResultImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerImpl implements Player {

  @Autowired
  BeanFactory beanFactory;

  private Context context;
  private String userName;
  private Rectangle startZone;
  private Map<String, Warrior> warriors = new ConcurrentHashMap();
  private volatile boolean readyToPlay;

  public PlayerImpl(String userName) {
    this.context = null;
    this.userName = userName;
    this.readyToPlay = false;
  }
  //===================================================================================================
  //===================================================================================================

  @Override
  public Result<Warrior> createWarrior(String warriorBaseClassName, Coords coords) {
    if (isReadyToPlay()) {
      // игрок уже готов к игре. Следовательно добавить юнит не может
      return ResultImpl.fail(USER_IS_READY_TO_PLAY.getError(getId(), context.getGameName(), context.getContextId()));
    }
    //Создадим нового воина
    return findContext()
            .map(fineContext -> fineContext.getCore().findWarriorBaseClassByName(warriorBaseClassName)
                    .map(aClass -> {
                      Warrior warrior = beanFactory.getBean(Warrior.class, fineContext, this
                              , beanFactory.getBean(aClass), "", coords, false);
                      // поместим в массив
                      warriors.put(warrior.getId(), warrior);
                      Result result = ResultImpl.success(warrior);
                      context.fireGameEvent(null, WARRIOR_ADDED, new EventDataContainer(warrior, result), Collections.EMPTY_MAP);
                      return result;
                    }));
  }
  //===================================================================================================

  @Override
  public Map<String, Warrior> getOriginWarriors() {
    return warriors;
  }
  //===================================================================================================

  @Override
  public List<Warrior> getWarriors() {
    return new ArrayList(warriors.values());
  }
  //===================================================================================================

  @Override
  public String getTitle() {
    return userName;
  }
  //===================================================================================================

  @Override
  public String getDescription() {
    return "";
  }
  //===================================================================================================

  @Override
  public void setStartZone(Rectangle startZone) {
    this.startZone = startZone;
  }
  //===================================================================================================

  @Override
  public Rectangle getStartZone() {
    return startZone;
  }
  //===================================================================================================

  @Override
  public Result replaceContext(Context newContext) {
    Result result;
    if (this.context != null && (newContext == null || !this.context.getContextId().equals(newContext.getContextId()))) {
      if ((result = this.context.disconnectPlayer(this)).isFail())
        return result;
    }

    replaceContextSilent(newContext);
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<Player> replaceContextSilent(Context newContext) {
    this.context = newContext;
    this.readyToPlay = false;
    this.warriors.clear();
    this.startZone = null;
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<Context> findContext() {
    return context == null
            ? ResultImpl.fail(USER_NOT_CONNECTED_TO_ANY_GAME.getError(getId()))
            : ResultImpl.success(context);
  }
  //===================================================================================================

  @Override
  public String getId() {
    return userName;
  }
  //===================================================================================================

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Player) && ((Player) obj).getId().equals(getId());
  }
  //===================================================================================================

  @Override
  public Result<Warrior> findWarriorById(String warriorId) {
    return Optional.ofNullable(warriors.get(warriorId))
            .map(foundWarrior -> ResultImpl.success(foundWarrior))
            .orElse(ResultImpl.fail(WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME.getError(
                    context.getGameName()
                    , context.getContextId()
                    , getId()
                    , warriorId)));
  }
  //===================================================================================================

  @Override
  public Result<Player> setReadyToPlay(boolean ready) {
    this.readyToPlay = ready;
    Result result = ResultImpl.success(this);
    context.fireGameEvent(null, PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS
            , new EventDataContainer(context, this, result), null);
    return result;
  }
  //===================================================================================================

  @Override
  public boolean isReadyToPlay() {
    return readyToPlay;
  }
  //===================================================================================================

  // TODO этот метод должен вызываться со слоя карты. Карта не должна двигать юнит сама
  @Override
  public Result<Warrior> moveWarriorTo(Warrior warrior, Coords to) {
    return warrior.moveWarriorTo(to);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> rollbackMove(String warriorId) {
    return findWarriorById(warriorId)
            .map(warrior -> warrior.rollbackMove());
  }
  //===================================================================================================

  @Override
  public Result<Warrior> ifWarriorCanMoveAtThisTurn(Warrior warrior) {
    return context.isGameRan()
            // и этим юнитом не делалось движений
            && !warrior.isTouchedAtThisTurn()
            // и предел используемых за ход юнитов достигнут
            && getWarriorsTouchedAtThisTurn().getResult().size() >= context.getGameRules().getMovesCountPerTurnForEachPlayer()
            ? ResultImpl.fail(PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED
            .getError(warrior.getOwner().getId(), String.valueOf(context.getGameRules().getMovesCountPerTurnForEachPlayer())))
            : ResultImpl.success(warrior);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> ifWarriorCanActsAtThisTurn(Warrior warrior) {
    return context.ifGameRan(true)
            .map(fineContext ->
                    // и этим юнитом не делалось движений
                    !warrior.isTouchedAtThisTurn()
                            // и предел используемых за ход юнитов достигнут
                            && getWarriorsTouchedAtThisTurn().getResult().size() < context.getGameRules().getMovesCountPerTurnForEachPlayer()
                            // или юнит уже задействован в этом ходе
                            || warrior.isTouchedAtThisTurn()
                            ? ResultImpl.success(warrior)
                            : ResultImpl.fail(PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED
                            .getError(
                                    warrior.getOwner().getId()
                                    , String.valueOf(context.getGameRules().getMovesCountPerTurnForEachPlayer()))));
  }
  //===================================================================================================

  @Override
  public Result<Player> clear() {
    List<Warrior> warriorsList = new ArrayList<>(warriors.values());
    warriors.clear();
    if (context != null) {
      warriorsList.stream().forEach(warrior -> innerRemoveWarrior(warrior));
    }
    return ResultImpl.success(this);
  }
  //===================================================================================================

  private Result<Warrior> innerRemoveWarrior(Warrior warrior) {
    warriors.remove(warrior.getId());
    // отписаться ото всех событий котрые должны удалять влияния на юнит
    warrior.getWarriorSInfluencers()
            .peak(influencers -> influencers.stream().forEach(influencer -> influencer.removeFromWarrior(true)));
    //  пошлем событие
    Result<Warrior> result = ResultImpl.success(warrior);
    context.fireGameEvent(null, WARRIOR_REMOVED, new EventDataContainer(warrior, result), null);
    return result;

  }
  //===================================================================================================

  @Override
  public Result<Warrior> removeWarrior(String warriorId) {
    return readyToPlay ? ResultImpl.fail(USER_IS_READY_TO_PLAY.getError(getId(), context.getGameName(), context.getContextId()))
            : findWarriorById(warriorId)
            .map(warrior -> innerRemoveWarrior(warrior));
  }
  //===================================================================================================

  @Override
  public Result<Weapon> giveWeaponToWarrior(String warriorId, Class<? extends Weapon> weaponClass) {
    return findWarriorById(warriorId)
            .map(foundWarrior -> foundWarrior.takeWeapon(weaponClass));
  }
  //===================================================================================================

  @Override
  public Result<Weapon> findWeaponById(String warriorId, String weaponId) {
    return findWarriorById(warriorId)
            .map(foundWarrior -> foundWarrior.findWeaponById(weaponId));
  }
  //===================================================================================================

  @Override
  public Result<Player> prepareToDefensePhase() {
    Result<Warrior> warriorResult;
    // восстановим значения воинов. свои влияния воин собирает сам
    for (Warrior warrior : warriors.values())
      if ((warriorResult = warrior.prepareToDefensePhase()).isFail()) {
        return (Result<Player>) ResultImpl.fail(warriorResult.getError());
      }

    // TODO применить артефакты и способности игрока

    // Отправим сообщение о завершении хода
    context.fireGameEvent(null, PLAYER_LOOSE_TURN, new EventDataContainer(this), null);

    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<AttackResult> attackWarrior(String attackerWarriorId, String targetWarriorId, String weaponId) {
    // Найти юнит, который будем атаковать
    return context.getLevelMap().findWarriorById(targetWarriorId)
            // найти юнит которым будем атаковать
            .map(targetWarrior -> findWarriorById(attackerWarriorId)
                    // проверить, что этот юнит может атаковать в этом ходу
                    .map(attackerWarrior -> ifWarriorCanActsAtThisTurn(attackerWarrior)
                            // Атаковать
                            .map(fineAttackerWarrior -> fineAttackerWarrior.attackWarrior(targetWarrior, weaponId))));
  }
  //===================================================================================================

  @Override
  public Result<AttackResult> innerAttachToAttackToWarrior(AttackResult attackResult) {
    return ResultImpl.success(attackResult); // TODO добавить сбор с артефактов всяких полезностей
  }
  //===================================================================================================

  @Override
  public Result<AttackResult> innerWarriorUnderAttack(AttackResult attackResult) {
    // возможность воину отбить удары и / или ослабить воздействие вредных влияний
    return attackResult.getTarget().innerWarriorUnderAttack(attackResult)
            // теперь рпименим оставшиеся влияния к воину
            .map(fineAttackResult -> {
              // собственно применим полученные поражения
              fineAttackResult.getInfluencers().stream()
                      // считаем, что уже все значения в модифиерах расчитаны жестко и не изменяются от запроса к запросу
//                      .filter(influencer -> influencer.getModifier().getLastCalculatedValue() > 0)
                      .forEach(influencer -> influencer.applyToWarrior(attackResult));
              return ResultImpl.success(attackResult);
            });
  }
  //===================================================================================================

  @Override
  public Result<Player> prepareToAttackPhase() {
    Result<Warrior> warriorResult;
    // восстановим значения воинов. свои влияния воин собирает сам
    for (Warrior warrior : warriors.values())
      if ((warriorResult = warrior.prepareToAttackPhase()).isFail()) {
        return (Result<Player>) ResultImpl.fail(warriorResult.getError());
      }

    // TODO применить артефакты и способности игрока

    // Отправим сообщение о завершении хода
    context.fireGameEvent(null, PLAYER_TAKES_TURN, new EventDataContainer(this), null);

    return ResultImpl.success(true);
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(String warriorId, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    return findWarriorById(warriorId)
            .map(warrior -> warrior.addInfluenceToWarrior(modifier, source, lifeTimeUnit, lifeTime))
            // добавить слушатель события
            .peak(influencer -> {
            });
  }
  //===================================================================================================

  @Override
  public Result<List<Warrior>> getWarriorsTouchedAtThisTurn() {
    return ResultImpl.success(warriors.values()
            .stream()
            .filter(warrior -> warrior.isTouchedAtThisTurn())
            .collect(Collectors.toList()));
  }
  //===================================================================================================

}
