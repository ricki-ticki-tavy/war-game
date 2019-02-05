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
@SpringBootTest(classes = {StartGameWith2PlayersAndMovesTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class StartGameWith2PlayersAndMovesTest extends AbstractMapTest {
  private static final Logger logger = LoggerFactory.getLogger(StartGameWith2PlayersAndMovesTest.class);

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
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(400, 400)), extractError(warriorResult));
    // проверка стоимости перемещения. Витязь, броня 2 - стоимость 12 очков за 1 ед дли. длина у нас по карте 10 точек
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 240, "Неверно расчитана стоимость перемещения");

    // Двигаем воина 1 у игрока 1
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(200, 100));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(200, 100)), extractError(warriorResult));
    // проверка стоимости перемещения.
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 433, "Неверно расчитана стоимость перемещения");

    // Двигаем воина 1 у игрока 1 на 1 ед
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(400, 410));
    assertSuccess(warriorResult);
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
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(324, 490)), extractError(warriorResult));

    // ставим воина в исходную позицию
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(200, 400));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(200, 400)), extractError(warriorResult));

    // игрок 1 готов
    Result<Player> playerResult = gameWrapper.playerReady(player1, true);
    assertSuccess(playerResult);
    Result r = gameWrapper.getCore().findGameContextByUID(gameContext)
            .map(context -> context.findUserByName(player1)
                    .map(player -> player.isReadyToPlay()
                            ? ResultImpl.success(player)
                            : ResultImpl.fail(SYSTEM_USER_ERROR.getError("Пользователь " + player.getId()
                            + " не перешел в состояние готовности"))));
    assertSuccess(r);

    // игрок 2 готов
    playerResult = gameWrapper.playerReady(player2, true);
    assertSuccess(playerResult);
    r = gameWrapper.getCore().findGameContextByUID(gameContext)
            .map(context -> context.findUserByName(player2)
                    .map(player -> player.isReadyToPlay()
                            ? ResultImpl.success(player)
                            : ResultImpl.fail(SYSTEM_USER_ERROR.getError("Пользователь " + player.getId()
                            + " не перешел в состояние готовности"))));
    assertSuccess(r);

    // игра перешла в режим ИГРА по готовности всех игроков
    r = gameWrapper.getCore().findGameContextByUID(gameContext)
            .map(context -> context.ifGameRan(true));
    assertSuccess(r);

    // двигаем юнит 1 игрока 2.Должен быть отказ
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior1p2, new Coords(220, 610));
    Assert.isTrue(warriorResult.isFail(), "Перемещение юнита не в свой ход игроком 2 удалось");

    // двигаем витязя на большее расстояние, чем он может
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(400, 600));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(341, 541)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 239, "Неверно рассчитана стоимость перемещения");

    // двигаем витязя на большее расстояние, чем он может, но на другую координату
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(600, 600));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(378, 489)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 239, "Неверно рассчитана стоимость перемещения");

    // двигаем вторым юнитом первого игрока. Так как лимит 1 фигура в ход, то должен быть отказ
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior2p1, new Coords(300, 300));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(378, 489)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 239, "Неверно рассчитана стоимость перемещения");


  }

  @Test
  public void startPlayTest() {
    test();
  }


}
