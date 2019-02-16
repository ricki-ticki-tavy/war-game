import api.core.Context;
import api.core.Result;
import api.entity.stuff.Artifact;
import api.entity.warrior.Warrior;
import api.game.map.Player;
import api.game.wraper.GameWrapper;
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
import tests.test.artifact.TestArtifactHealinFialForPlayer;
import tests.test.artifact.TestArtifactRainbowArrowForWarrior;

import static core.system.error.GameErrors.ARTIFACT_CAN_NOT_TAKE_AT_THIS_TURN;
import static core.system.error.GameErrors.ARTIFACT_PLAYER_ALREADY_HAS_IT;
import static core.system.error.GameErrors.ARTIFACT_WARRIOR_ALREADY_HAS_IT;

// TODO тест на то, чтобы давать не свой тип артефакта
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

    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 16, "способность воина удачи не подействовала");

    // выдать артефакт удачи при стрельбе +20%. Должно все получиться
    Result<Artifact<Warrior>> artifactForWarriorResult = gameWrapper.giveArtifactToWarrior(gameContext, player1, warrior1p1, TestArtifactRainbowArrowForWarrior.CLASS_NAME);
    assertSuccess(artifactForWarriorResult);
    Assert.isTrue(warriorImpl1p1.getArtifacts().getResult().size() == 1, "У воина нет добавленного артефакта");
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 36, "Добавленный артефакт не подействовал");
    String artifact1w1p1 = artifactForWarriorResult.getResult().getId();

    // выбросим артефакт. Удача должна вернуться на прежние базовые + способность воина
    assertSuccess(artifactForWarriorResult = gameWrapper.dropArtifactByWarrior(gameContext, player1, warrior1p1, artifact1w1p1));
    Assert.isTrue(warriorImpl1p1.getArtifacts().getResult().size() == 0, "У воина не удалился артефакт");
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 16, "Удаленный артефакт не откатил свое влияние");

    // Заново выдать артефакт удачи при стрельбе +20%. Должно все получиться
    artifactForWarriorResult = gameWrapper.giveArtifactToWarrior(gameContext, player1, warrior1p1, TestArtifactRainbowArrowForWarrior.CLASS_NAME);
    assertSuccess(artifactForWarriorResult);
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 36, "Добавленный артефакт не подействовал");
    artifact1w1p1 = artifactForWarriorResult.getResult().getId();

    // выдать артефакт удачи при стрельбе +20% ЕЩЕ раз тому же воину. Так как повтор, то должен быть отказ
    artifactForWarriorResult = gameWrapper.giveArtifactToWarrior(gameContext, player1, warrior1p1, TestArtifactRainbowArrowForWarrior.CLASS_NAME);
    Assert.isTrue(artifactForWarriorResult.isFail(ARTIFACT_WARRIOR_ALREADY_HAS_IT), "Удалось дать воину 2 одинаковых артефакта");

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

    // дадим игроку 1 артефакт лечения воинов
    Result<Artifact<Player>> artifactForPlayerResult = gameWrapper.giveArtifactToPlayer(gameContext, player1, TestArtifactHealinFialForPlayer.CLASS_NAME);
    assertSuccess(artifactForPlayerResult);

    // Еще раз дадим игроку 1 артефакт лечения воинов. Не должно выйти так как можно только 1 артефакт за ход добавлять
    artifactForPlayerResult = gameWrapper.giveArtifactToPlayer(gameContext, player1, TestArtifactHealinFialForPlayer.CLASS_NAME);
    Assert.isTrue(artifactForPlayerResult.isFail(ARTIFACT_CAN_NOT_TAKE_AT_THIS_TURN), "Игрок 1 смог взять второй артифакт за один ход");

    warriorImpl1p1.getAttributes().setHealth(10);

    // переходы ходом опять до воина 1
    assertSuccess(gameWrapper.nextTurn(gameContext, player1));

    // здоровье первому воину игрока 1 не должно добавиться
    Assert.isTrue(warriorImpl1p1.getAttributes().getHealth() == 10, "Артифакт выполнил лечение не в тот момент");

    // и опять ходит первый игрок
    assertSuccess(gameWrapper.nextTurn(gameContext, player2));

    // Теперь здоровье первому воину игрока 1 должно добавиться так как этот артифакт работает раз в круг
    // то есть когда игрок получает ход
    Assert.isTrue(warriorImpl1p1.getAttributes().getHealth() == 11, "Артифакт не выполнил лечение. Не сработал");

    // И снова дадим игроку 1 артефакт лечения воинов. Не должно выйти так как можно только 1 артефакт такой уже есть
    artifactForPlayerResult = gameWrapper.giveArtifactToPlayer(gameContext, player1, TestArtifactHealinFialForPlayer.CLASS_NAME);
    Assert.isTrue(artifactForPlayerResult.isFail(ARTIFACT_PLAYER_ALREADY_HAS_IT), "Игрок 1 смог взять два одинаковых артефакта");

    assertSuccess(gameWrapper.getCore().removeGameContext(gameContext));
  }

  @Test
  public void doTest() {
    innerDoTest();
  }
}
