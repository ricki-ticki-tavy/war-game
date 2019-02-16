package core.game.wraper;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.stuff.Artifact;
import api.game.ability.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.geo.Coords;
import api.core.EventDataContainer;
import api.game.action.InfluenceResult;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.wraper.GameWrapper;
import core.entity.map.GameRulesImpl;
import core.system.ResultImpl;
import core.system.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class GameWrapperImpl implements GameWrapper {

  private static final Logger logger = LoggerFactory.getLogger(GameWrapperImpl.class);

  @Autowired
  Core core;

  //===================================================================================================

  @PostConstruct
  public void testGame() {
  }
  //===================================================================================================

  @Override
  public Result<Player> login(String userName) {
    return core.loginPlayer(userName)
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Context> createGame(String ownerUserName
          , GameRules gameRules
          , String mapName
          , String gameName
          , boolean hidden) {

    return core.findUserByName(ownerUserName)
            .mapFail(errorResult -> {
              core.fireEvent(new EventImpl(null, null
                      , EventType.GAME_CONTEXT_CREATE
                      , new EventDataContainer(errorResult
                      , new String[]{gameName, ownerUserName}), null));
              return errorResult
                      .logIfError(logger);
            })
            .mapSafe(foundUser -> core.createGameContext(foundUser, new GameRulesImpl(gameRules)
                    , this.getClass().getClassLoader().getResourceAsStream(mapName)
                    , gameName, hidden))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Player> connectToGame(String userName, String contextId) {
    return core.findUserByName(userName)
            .mapSafe(foundUser -> core.findGameContextByUID(contextId)
                    .map(foundContext -> foundContext.connectPlayer(foundUser))
                    .map(ctx -> (Result<Player>) ResultImpl.success(foundUser)))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result getGamesList() {
    return ResultImpl.success(core.getContextList());
  }
  //===================================================================================================

  @Override
  public Result<Warrior> createWarrior(String contextId, String userName, String className, Coords coords) {
    // ищем контекст
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.createWarrior(userName, className, coords))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<InfluenceResult> attackWarrior(String contextId, String userName, String attackerWarriorId, String targetWarriorId, String weaponId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> context.attackWarrior(userName, attackerWarriorId, targetWarriorId, weaponId))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(String contextId, String userName, String warriorId, Coords coords) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.moveWarriorTo(userName, warriorId, coords))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Warrior> rollbackMove(String contextId, String userName, String warriorId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.rollbackMove(userName, warriorId))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Coords> whatIfMoveWarriorTo(String contextId, String userName, String warriorId, Coords coords) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.whatIfMoveWarriorTo(userName, warriorId, coords))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Weapon> giveWeaponToWarrior(String contextId, String userName, String warriorId, String weaponClassName) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> core.findWeaponByName(weaponClassName)
                    .map(weponClass -> fineContext.giveWeaponToWarrior(userName, warriorId, weponClass)))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Artifact<Warrior>> giveArtifactToWarrior(String contextId, String userName, String warriorId, String artifactName) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> core.findArtifactForWarrior(artifactName)
                    .map(artifactClass -> fineContext.giveArtifactToWarrior(userName, warriorId, artifactClass)))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Artifact<Player>> giveArtifactToPlayer(String contextId, String userName, String artifactName) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> core.findArtifactForPlayer(artifactName)
                    .map(artifactClass -> fineContext.giveArtifactToPlayer(userName, artifactClass)))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Artifact<Warrior>> dropArtifactByWarrior(String contextId, String userName, String warriorId, String artifactInstanceId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.dropArtifactByWarrior(userName, warriorId, artifactInstanceId))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Weapon> dropWeaponByWarrior(String contextId, String userName, String warriorId, String weaponId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.dropWeaponByWarrior(userName, warriorId, weaponId))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Warrior> findWarriorById(String contextId, String warriorId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> context.findWarriorById(warriorId))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Warrior> ifWarriorCanActsAtThisTurn(String contextId, String userName, String warriorId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> context.findUserByName(userName)
                    .map(player -> context.ifWarriorCanActsAtThisTurn(userName, warriorId)))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Weapon> findWeaponById(String contextId, String userName, String warriorId, String weaponId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> core.findUserByName(userName)
                    .map(player -> context.findWeaponById(userName, warriorId, weaponId)))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Player> playerReady(String userName, boolean readyToPlay) {
    return core.findUserByName(userName)
            .mapSafe(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundContext.setPlayerReadyToGameState(foundPlayer, readyToPlay)))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Player> getPlayerOwnsTheRound(String contextId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.getPlayerOwnsThisTurn())
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> removeWarrior(String contextId, String userName, String warriorId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(fineContext -> fineContext.removeWarrior(userName, warriorId))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<List<String>> getBaseWarriorClasses() {
    return core.getBaseWarriorClasses()
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<List<String>> getWeaponClasses() {
    return core.getWeaponClasses()
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Core getCore() {
    return core;
  }
  //===================================================================================================

  @Override
  public Result<Player> nextTurn(String contextId, String userName) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> context.ifGameDeleting(false))
            .mapSafe(fineContext -> fineContext.nextTurn(userName))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<List<Influencer>> getWarriorSInfluencers(String contextId, String userName, String warriorId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> context.ifGameDeleting(false))
            .mapSafe(context -> context.getWarriorSInfluencers(userName, warriorId))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Integer> getRoundsCount(String contextId) {
    return core.findGameContextByUID(contextId)
            .mapSafe(context -> context.getRoundsCount());
  }
  //===================================================================================================

}
