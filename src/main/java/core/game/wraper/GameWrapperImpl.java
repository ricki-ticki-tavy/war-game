package core.game.wraper;

import api.core.Context;
import api.core.Core;
import api.core.Result;
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
//    logger.info("Запуск внутреннего теста...");
//    new InnerTest1().test(this);
//    logger.info("1)   ПЕРВЫЙ ВНУТРЕННИЙ ТЕСТ ПРОЙДЕН");
//    new InnerTest2().test(this);
//    logger.info("2)   ВТОРОЙ ВНУТРЕННИЙ ТЕСТ ПРОЙДЕН");
  }
  //===================================================================================================

  @Override
  public Result<Player> login(String userName) {
    return core.loginPlayer(userName);
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
              return errorResult;
            })
            .map(foundUser -> core.createGameContext(foundUser, new GameRulesImpl(gameRules)
                    , this.getClass().getClassLoader().getResourceAsStream(mapName)
                    , gameName, hidden));
  }
  //===================================================================================================

  @Override
  public Result<Player> connectToGame(String userName, String contextId) {
    return core.findUserByName(userName)
            .map(foundUser -> core.findGameContextByUID(contextId)
                    .map(foundContext -> foundContext.connectPlayer(foundUser))
                    .map(ctx -> ResultImpl.success(foundUser)));
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
            .map(fineContext -> fineContext.createWarrior(userName, className, coords));
  }
//===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(String contextId, String userName, String warriorId, Coords coords) {
    return core.findGameContextByUID(contextId)
            .map(fineContext -> fineContext.moveWarriorTo(userName, warriorId, coords));
  }
//===================================================================================================

  @Override
  public Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, String weaponClassName) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> core.findWeaponByName(weaponClassName)
                            .map(baseClass -> foundPlayer.findWarriorById(warriorId)
                                    .map(foundWarrior -> foundWarrior.takeWeapon(baseClass)))));
  }
//===================================================================================================

  @Override
  public Result<Weapon> takeWeaponFromWarrior(String userName, String warriorId, String weaponId) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundPlayer.findWarriorById(warriorId)
                            .map(foundWarrior -> foundWarrior.dropWeapon(weaponId))));
  }
//===================================================================================================

  @Override
  public Result<Player> playerReady(String userName, boolean readyToPlay) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundContext.setPlayerReadyToGameState(foundPlayer, readyToPlay)));
  }
  //===================================================================================================

  @Override
  public Result<Player> getGetPlayerOwnsTheRound(String contextId) {
    return core.findGameContextByUID(contextId)
            .map(fineContext -> fineContext.getPlayerOwnsTheTurn());
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
    return core.getBaseWarriorClasses();
  }
  //===================================================================================================

  @Override
  public Result<List<String>> getWeaponClasses() {
    return core.getWeaponClasses();
  }
  //===================================================================================================

  @Override
  public Core getCore() {
    return core;
  }

  //===================================================================================================

}
