import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.game.Coords;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.wraper.GameWrapper;
import core.entity.map.GameRulesImpl;
import core.entity.warrior.Skeleton;
import core.entity.warrior.Viking;
import core.entity.warrior.Vityaz;
import core.game.CoreImpl;
import org.springframework.util.Assert;

/**
 * Класс авторизци двух пользователей, загрузка карты, создание по 2 невооруженных воина
 */
public abstract class AbstractMapTest {

  protected String player1;
  protected String player2;
  protected String gameContext;
  protected String warrior1p1;
  protected String warrior2p1;
  protected String warrior1p2;
  protected String warrior2p2;

  protected String extractError(Result result) {
    return result.isFail() ? result.getError().getMessage() : "";
  }

  protected void assertSuccess(Result operatioinResult){
    String msg = operatioinResult.isFail() ? operatioinResult.getError().getMessage() : "";
    Assert.isTrue(operatioinResult.isSuccess(), msg);
  }

  public void initMap(GameWrapper gameWrapper) {

    Result<Player> playerResult = gameWrapper.login("test");
    assertSuccess(playerResult);
    player1 = playerResult.getResult().getId();

    playerResult = gameWrapper.login("test 2");
    assertSuccess(playerResult);
    player2 = playerResult.getResult().getId();

    GameRules rules = new GameRulesImpl(2, 2, 1, 50, 200, 2, 600, 30);
    Result<Context> contextResult = gameWrapper.createGame(player1, rules, "testMap.xml", "test-game2", false);
    assertSuccess(contextResult);
    gameContext = contextResult.getResult().getContextId();

    playerResult = gameWrapper.connectToGame(player2, gameContext);
    assertSuccess(playerResult);

    Result<Warrior> warriorResult = gameWrapper.createWarrior(gameContext, player1, Vityaz.CLASS_NAME
            , new Coords(200, 400));
    assertSuccess(warriorResult);
    warrior1p1 = warriorResult.getResult().getId();

    warriorResult = gameWrapper.createWarrior(gameContext, player1, Viking.CLASS_NAME
            , new Coords(800, 400));
    assertSuccess(warriorResult);
    warrior2p1 = warriorResult.getResult().getId();

    warriorResult = gameWrapper.createWarrior(gameContext, player2, Skeleton.CLASS_NAME
            , new Coords(200, 600));
    assertSuccess(warriorResult);
    warrior1p2 = warriorResult.getResult().getId();

    warriorResult = gameWrapper.createWarrior(gameContext, player2, Skeleton.CLASS_NAME
            , new Coords(800, 600));
    assertSuccess(warriorResult);
    warrior2p2 = warriorResult.getResult().getId();

    assertSuccess(warriorResult);
  }
}
