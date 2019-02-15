package tests.abstracts;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.geo.Coords;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.wraper.GameWrapper;
import core.entity.map.GameRulesImpl;
import core.game.CoreImpl;
import org.springframework.util.Assert;
import tests.test.warrior.TestSkeleton;
import tests.test.warrior.TestViking;
import tests.test.warrior.TestVityaz;
import tests.test.weapons.TestBow;
import tests.test.weapons.TestFireBowOfRejuvenation;
import tests.test.weapons.TestShortSword;
import tests.test.weapons.TestSword;

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

  public void initMap(GameWrapper gameWrapper, String player1Name, String player2Name) {

    ((CoreImpl)gameWrapper.getCore()).registerWarriorBaseClass(TestSkeleton.CLASS_NAME, TestSkeleton.class);
    ((CoreImpl)gameWrapper.getCore()).registerWarriorBaseClass(TestViking.CLASS_NAME, TestViking.class);
    ((CoreImpl)gameWrapper.getCore()).registerWarriorBaseClass(TestVityaz.CLASS_NAME, TestVityaz.class);

    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestBow.CLASS_NAME, TestBow.class);
    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestShortSword.CLASS_NAME, TestShortSword.class);
    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestFireBowOfRejuvenation.CLASS_NAME, TestFireBowOfRejuvenation.class);
    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestSword.CLASS_NAME, TestSword.class);

    Result<Player> playerResult = gameWrapper.login(player1Name);
    assertSuccess(playerResult);
    player1 = playerResult.getResult().getId();

    playerResult = gameWrapper.login(player2Name);
    assertSuccess(playerResult);
    player2 = playerResult.getResult().getId();

    GameRules rules = new GameRulesImpl(2, 2, 1, 50, 200, 2, 600, 30);
    Result<Context> contextResult = gameWrapper.createGame(player1, rules, "testMap.xml", "test-game2", false);
    assertSuccess(contextResult);
    gameContext = contextResult.getResult().getContextId();

    playerResult = gameWrapper.connectToGame(player2, gameContext);
    assertSuccess(playerResult);

    Result<Warrior> warriorResult = gameWrapper.createWarrior(gameContext, player1, TestVityaz.CLASS_NAME
            , new Coords(200, 400));
    assertSuccess(warriorResult);
    warriorResult.getResult().setTitle("Гоша 1");
    warrior1p1 = warriorResult.getResult().getId();

    warriorResult = gameWrapper.createWarrior(gameContext, player1, TestViking.CLASS_NAME
            , new Coords(800, 400));
    assertSuccess(warriorResult);
    warriorResult.getResult().setTitle("Гоша 2");
    warrior2p1 = warriorResult.getResult().getId();

    warriorResult = gameWrapper.createWarrior(gameContext, player2, TestSkeleton.CLASS_NAME
            , new Coords(200, 600));
    assertSuccess(warriorResult);
    warriorResult.getResult().setTitle("Кеша 1");
    warrior1p2 = warriorResult.getResult().getId();

    warriorResult = gameWrapper.createWarrior(gameContext, player2, TestSkeleton.CLASS_NAME
            , new Coords(800, 600));
    assertSuccess(warriorResult);
    warriorResult.getResult().setTitle("Кеша 2");
    warrior2p2 = warriorResult.getResult().getId();

    assertSuccess(warriorResult);
  }
}
