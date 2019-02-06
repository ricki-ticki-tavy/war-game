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

import static core.system.error.GameErrors.*;

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
    Assert.isTrue(warriorResult.isFail(PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED), "Должен был быть отказ по причине исчерпания числа действий за ход");
    Assert.isTrue(gameWrapper.getCore().findGameContextByUID(gameContext).getResult()
            .findUserByName(player1).getResult()
            .findWarriorById(warrior2p1).getResult()
            .getTranslatedToGameCoords().equals(new Coords(800, 400)), "Юнит сместился при отказе движения после превышения лимита на ход юнитами(1)");

    // двигаем юнит 1 игрока 2 игроком 1.Должен быть отказ
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p2, new Coords(220, 610));
    Assert.isTrue(warriorResult.isFail(WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME), "Перемещение не мвоего юнита игроком 1 удалось");


    // Передача хода следующему игроку. Пытается выполнить игрок 2. Должна быть ошибка так как ходит игрок 1
    playerResult = gameWrapper.nextTurn(gameContext, player2);
    Assert.isTrue(playerResult.isFail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND), "Передача хода может осуществляться только владельцем хода");

    // Передача хода следующему игроку. Пытается выполнить игрок 1. Должен быть успех
    playerResult = gameWrapper.nextTurn(gameContext, player1);
    assertSuccess(playerResult);

    // двигаем юнит 1 игрока 1.Должен быть отказ
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(220, 610));
    Assert.isTrue(warriorResult.isFail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND), "Перемещение юнита не в свой ход игроком 1 удалось");

    // двигаем скелетона
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior1p2, new Coords(100, 600));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(100, 600)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 80, "Неверно рассчитана стоимость перемещения");

    // двигаем скелетона на большее расстояние, чем он может. двигаться он будет не с предыдущих координат, а с тех, на которых
    // стоял В НАЧАЛЕ ХОДА так как он еще не делал ничего, кроме перемещения и может откатиться назад
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior1p2, new Coords(900, 600));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(500, 600)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Assert.isTrue(warriorResult.getResult().getTreatedActionPointsForMove() == 240, "Неверно рассчитана стоимость перемещения");

    // двигаем вторым юнитом второго игрока. Так как лимит 1 фигура в ход, то должен быть отказ
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior2p2, new Coords(400, 600));
    Assert.isTrue(warriorResult.isFail(PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED), "Превышение лимита фигур за ход игроком 2");

    // Делаем откат движения первого юнита второго игрока

  }

  @Test
  public void startPlayTest() {
    test();
  }


}
