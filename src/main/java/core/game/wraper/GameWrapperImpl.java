package core.game.wraper;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.game.Coords;
import api.game.EventDataContainer;
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
            .map(foundUser -> core.createGameContext(foundUser, new GameRulesImpl(gameRules)
                    , this.getClass().getClassLoader().getResourceAsStream(mapName)
                    , gameName, hidden))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Player> connectToGame(String userName, String contextId) {
    return core.findUserByName(userName)
            .map(foundUser -> core.findGameContextByUID(contextId)
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
            .map(fineContext -> fineContext.createWarrior(userName, className, coords))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(String contextId, String userName, String warriorId, Coords coords) {
    return core.findGameContextByUID(contextId)
            .map(fineContext -> fineContext.moveWarriorTo(userName, warriorId, coords))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Coords> whatIfMoveWarriorTo(String contextId, String userName, String warriorId, Coords coords) {
    return core.findGameContextByUID(contextId)
            .map(fineContext -> fineContext.whatIfMoveWarriorTo(userName, warriorId, coords))
            .logIfError(logger);
  }
//===================================================================================================

  // TODO перевести на параметр контекста
  @Override
  public Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, String weaponClassName) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> core.findWeaponByName(weaponClassName)
                            .map(baseClass -> foundPlayer.findWarriorById(warriorId)
                                    .map(foundWarrior -> foundWarrior.takeWeapon(baseClass)))))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Weapon> takeWeaponFromWarrior(String userName, String warriorId, String weaponId) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundPlayer.findWarriorById(warriorId)
                            .map(foundWarrior -> foundWarrior.dropWeapon(weaponId))))
            .logIfError(logger);
  }
//===================================================================================================

  @Override
  public Result<Player> playerReady(String userName, boolean readyToPlay) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundContext.setPlayerReadyToGameState(foundPlayer, readyToPlay)))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Player> getGetPlayerOwnsTheRound(String contextId) {
    return core.findGameContextByUID(contextId)
            .map(fineContext -> fineContext.getPlayerOwnsThisTurn())
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> removeWarrior(String contextId, String userName, String warriorId) {
    return core.findGameContextByUID(contextId)
            .map(fineContext -> fineContext.removeWarrior(userName, warriorId));
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
            .map(context -> context.ifGameDeleting(false))
            .map(fineContext -> fineContext.nextTurn(userName))
            .logIfError(logger);
  }
  //===================================================================================================

  @Override
  public Result<List<Influencer>> getWarriorSInfluencers(String contextId, String userName, String warriorId) {
    return core.findGameContextByUID(contextId)
            .map(context -> context.ifGameDeleting(false))
            .map(context -> context.getWarriorSInfluencers(userName, warriorId));
  }
  //===================================================================================================

}
