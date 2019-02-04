import api.core.Result;
import api.entity.warrior.Warrior;
import api.game.Coords;
import api.game.map.Player;
import api.game.wraper.GameWrapper;
import core.system.ResultImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import static core.system.error.GameErrors.SYSTEM_USER_ERROR;

/**
 * вход двумя игроками, создание карты, создание каждому игроку по 2 невооруженных воина,
 * переход игроков в режим готовности, переход игры в режим ИГРА.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartGameWith2PlayersTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class StartGameWith2PlayersTest extends MapPreparer {
  private static final Logger logger = LoggerFactory.getLogger(StartGameWith2PlayersTest.class);

  @Autowired
  GameWrapper gameWrapper;

  protected GameWrapper getGameWrapper() {
    return gameWrapper;
  }

  public void test() {
    Assert.isTrue(gameWrapper.getCore().getContextList().size() == 0, "Контексты не пусты !!!");
    initMap();

    // Двигаем воина 1 у игрока 1
    Result<Warrior> warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(400, 400));
    Assert.isTrue(warriorResult.isSuccess(), extractError(warriorResult));
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(400, 400)), extractError(warriorResult));

    // Двигаем воина 1 у игрока 1 на 1 ед
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(400, 410));
    Assert.isTrue(warriorResult.isSuccess(), extractError(warriorResult));
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(400, 410)), extractError(warriorResult));

    // Двигаем воина 1 у игрока 1 c наездом на второй юнит
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(780, 400));
    Assert.isTrue(warriorResult.isFail(), "Перемещение привело к наложению юнита на карте.");
    Assert.isTrue(gameWrapper.getCore()
            .findGameContextByUID(gameContext)
            .map(context -> context.findUserByName(player1))
            .map(player -> player.findWarriorById(warrior1p1))
            .map(warrior -> ResultImpl.success(warrior.getCoords()))
            .getResult().equals(new Coords(400, 410)), "Тем не менее воин сдвинулся");

    // Двигаем воина 1 игрока 1 на воина 1 игрока 2. Упремся в периметр идалее него не уйдет. Перемещение успешно ДО периметра
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(210, 610));
    Assert.isTrue(warriorResult.isSuccess(), extractError(warriorResult));
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(324, 490)), extractError(warriorResult));

    // игрок 1 готов
    Result<Player> playerResult = gameWrapper.playerReady(player1, true);
    Assert.isTrue(playerResult.isSuccess(), extractError(playerResult));
    Result r = gameWrapper.getCore().findGameContextByUID(gameContext)
            .map(context -> context.findUserByName(player1)
                    .map(player -> player.isReadyToPlay()
                            ? ResultImpl.success(player)
                            : ResultImpl.fail(SYSTEM_USER_ERROR.getError("Пользователь " + player.getId()
                            + " не перешел в состояние готовности"))));
    Assert.isTrue(r.isSuccess(), extractError(r));

    // игрок 2 готов
    playerResult = gameWrapper.playerReady(player2, true);
    Assert.isTrue(playerResult.isSuccess(), extractError(playerResult));
    r = gameWrapper.getCore().findGameContextByUID(gameContext)
            .map(context -> context.findUserByName(player2)
                    .map(player -> player.isReadyToPlay()
                            ? ResultImpl.success(player)
                            : ResultImpl.fail(SYSTEM_USER_ERROR.getError("Пользователь " + player.getId()
                            + " не перешел в состояние готовности"))));
    Assert.isTrue(r.isSuccess(), extractError(r));

    // игра перешла в режим ИГРА по готовности всех игроков
    r = gameWrapper.getCore().findGameContextByUID(gameContext)
            .map(context -> context.ifGameRan(true));
    Assert.isTrue(r.isSuccess(), extractError(r));

    // двигаем юнит 1 игрока 2.Должен быть отказ
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior1p2, new Coords(220, 610));
    Assert.isTrue(warriorResult.isFail(), "Перемещение юнита не в свой ход игроком 2 удалось");



  }

  @Test
  public void startPlayTest() {
    test();
  }


}
