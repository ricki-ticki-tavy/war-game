import api.core.Context;
import api.core.Result;
import api.entity.stuff.Artifact;
import api.entity.warrior.Warrior;
import api.game.wraper.GameWrapper;
import core.entity.artifact.ArtifactRainbowArrowForWarrior;
import core.system.ResultImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;
import tests.abstracts.AbstractMapTest;
import tests.config.TestContextConfiguration;

import static core.system.error.GameErrors.ARTIFACT_ALREADY_EXISTS;
import static core.system.error.GameErrors.PLAYER_IS_NOT_OWENER_OF_THIS_ROUND;

/**
 * Проверка подписывания и отписывания от событий.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ArtifactTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class ArtifactTest extends AbstractMapTest {

  @Autowired
  GameWrapper gameWrapper;

  public ArtifactTest setGameWrapper(GameWrapper gameWrapper) {
    this.gameWrapper = gameWrapper;
    return this;
  }

  public void innerDoTest() {
    initMap(gameWrapper, "ArtifactTest_user1", "ArtifactTest_User2");
    Context context = gameWrapper.getCore().findGameContextByUID(gameContext).getResult();

    Warrior warriorImpl1p1 = gameWrapper.getCore().findGameContextByUID(gameContext).getResult()
            .findUserByName(player1).getResult()
            .findWarriorById(warrior1p1).getResult();
    int curLuck = warriorImpl1p1.getAttributes().getLuckRangeAtack();

    // выдать артефакт удачи при стрельбе +20%. Должно все получиться
    Result<Artifact<Warrior>> artifactResult = gameWrapper.giveArtifactToWarrior(gameContext, player1, warrior1p1, ArtifactRainbowArrowForWarrior.CLASS_NAME);
    assertSuccess(artifactResult);
    Assert.isTrue(warriorImpl1p1.getArtifacts().getResult().size() == 1, "У воина нет добавленного артефакта");
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 26, "Добавленный артефакт не подействовал");

    // выдать артефакт удачи при стрельбе +20% ЕЩЕ раз тому жевоину. Так как повтор, то должен быть отказ
    artifactResult = gameWrapper.giveArtifactToWarrior(gameContext, player1, warrior1p1, ArtifactRainbowArrowForWarrior.CLASS_NAME);
    Assert.isTrue(artifactResult.isFail(ARTIFACT_ALREADY_EXISTS), "Удалось дать воину 2 одинаковых артефакта");

    // Игрок 1 готов
    assertSuccess(gameWrapper.playerReady(player1, true));

    // Игрок 2 готов
    assertSuccess(gameWrapper.playerReady(player2, true));

    // проверим, что артефакт еще работает. должно быть 6 базовых, 10 - способность персонажа и 20 - артефакт
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 36, "Добавленный артефакт после старта игры перестал действовать");

    // если первым ходитигрок 2, то передаем ход игроку 1
    gameWrapper.getPlayerOwnsTheRound(gameContext)
            .peak(player -> {
              if (player.getId().equals(player2)) {
                assertSuccess(gameWrapper.nextTurn(gameContext, player2));
              }
            });


    // переходы ходом опять до воина 1
    assertSuccess(gameWrapper.nextTurn(gameContext, player1));
    // проверим, что артефакт еще работает. см расчет выше
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 36, "Добавленный артефакт после перехода хода перестал действовать");
    // и опять ходит первый игрок
    assertSuccess(gameWrapper.nextTurn(gameContext, player2));

    // проверим, что артефакт еще работает. см расчет выше
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 36, "Добавленный артефакт после перехода хода перестал действовать");

    assertSuccess(gameWrapper.getCore().removeGameContext(gameContext));
  }

  @Test
  public void doTest() {
    innerDoTest();
  }
}
