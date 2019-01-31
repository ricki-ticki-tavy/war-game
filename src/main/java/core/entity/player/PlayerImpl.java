package core.entity.player;

import api.core.Context;
import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.LifeTimeUnit;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.map.Player;
import core.system.ResultImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerImpl implements Player {

  @Autowired
  BeanFactory beanFactory;

  private Context context;
  private String playerName;
  private Rectangle startZone;
  private Map<String, Warrior> warriors = new ConcurrentHashMap();
  private volatile boolean readyToPlay;

  public PlayerImpl(String playerName) {
    this.context = null;
    this.playerName = playerName;
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
  public List<Warrior> getWarriors() {
    return new ArrayList(warriors.values());
  }
  //===================================================================================================

  @Override
  public String getTitle() {
    return playerName;
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

    this.context = newContext;
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<Player> replaceContextSilent(Context newContext) {
    this.context = newContext;
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
    return playerName;
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
            .orElse(ResultImpl.fail(WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME.getError(getTitle(), warriorId)));
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

  @Override
  public Result<Warrior> moveWarriorTo(String warriorId, Coords newCoords) {
    return findWarriorById(warriorId)
            // ищем юнит
            .map(warrior -> null);
  }
  //===================================================================================================

  @Override
  public Result<Player> clear() {
    List<Warrior> warriorsList = new ArrayList<>(warriors.values());
    warriors.clear();
    warriorsList.stream().forEach(warrior -> innerRemoveWarrior(warrior));
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(Player player, String warriorId, Coords newCoords) {
    return null;
  }
  //===================================================================================================

  private Result<Warrior> innerRemoveWarrior(Warrior warrior) {
    warriors.remove(warrior.getId());
    // отписаться ото всех событий котрые должны удалять влияния на юнит
    warrior.getWarriorSInfluencers()
            .doIfSuccess(influencers -> influencers.stream().forEach(influencer -> influencer.unsubscribe()));
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

    return ResultImpl.success(true);
  }
  //===================================================================================================

  @Override
  public Result<Player> prepareToAttackPhase() {
    return ResultImpl.fail(SYSTEM_NOT_REALIZED.getError("prepareToAttackPhase"));
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(String warriorId, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    return findWarriorById(warriorId)
            .map(warrior -> warrior.addInfluenceToWarrior(modifier, source, lifeTimeUnit, lifeTime))
            // добавить слушатель события
            .doIfSuccess(influencer -> {});
  }
  //===================================================================================================

//  @Override
//  public Result<Influencer> removeInfluencerFromWarrior(String warriorId, Influencer influencer) {
//    return findWarriorById(warriorId)
//            .map(warrior -> warrior.removeInfluencerFromWarrior(influencer, ));
//  }
//  //===================================================================================================
}
