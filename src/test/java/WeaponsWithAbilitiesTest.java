import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.ability.Influencer;
import api.game.action.InfluenceResult;
import api.game.wraper.GameWrapper;
import api.geo.Coords;
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
import tests.test.weapons.TestBow;
import tests.test.weapons.TestFireBow;
import tests.test.weapons.TestSword;

/**
 * Проверка подписывания и отписывания от событий.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WeaponsWithAbilitiesTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class WeaponsWithAbilitiesTest extends AbstractMapTest {

  @Autowired
  GameWrapper gameWrapper;

  public WeaponsWithAbilitiesTest setGameWrapper(GameWrapper gameWrapper) {
    this.gameWrapper = gameWrapper;
    return this;
  }

  public void innerDoTest() {
    initMap(gameWrapper, "WeaponsWATest_user1", "WeaponsWATest_User2");
    Context context = gameWrapper.getCore().findGameContextByUID(gameContext).getResult();


    // Дадим воину 1 игрока 1 лук
    Result<Weapon> weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player1, warrior1p1, TestFireBow.CLASS_NAME);
    assertSuccess(weaponResult);
    String bowWarrior1p1 = weaponResult.getResult().getId();

    // Дадим воину 2 игрока 1 лук
    weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player1, warrior2p1, TestBow.CLASS_NAME);
    assertSuccess(weaponResult);
    String bowWarrior2p1 = weaponResult.getResult().getId();

    // Дадим воину 1 игрока 2 меч
    weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player2, warrior1p2, TestSword.CLASS_NAME);
    assertSuccess(weaponResult);
    String swordWarrior1p2 = weaponResult.getResult().getId();

    // подвинем воина 1 игрока 1 ближе к середине
    Result<Warrior> warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(450, 490));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getTranslatedToGameCoords().equals(new Coords(450, 490)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Warrior warriorImpl1p1 = warriorResult.getResult();

    // подвинем воина 2 игрока 1 ближе к середине и вплотную к воину 1. Проверка на то, что свои, стоящие рядом
    // стрелять не помешают
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior2p1, new Coords(490, 490));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getTranslatedToGameCoords().equals(new Coords(490, 490)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Warrior warriorImpl2p1 = warriorResult.getResult();

    // подвинем воина 1 игрока 2 ближе к середине вплотную к воину 2 игрока 1, но чуть в стороне от воина 1
    // игрока 1, чтобы дать воину 1 игрока 1 возможность стрелять из лука
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior1p2, new Coords(500, 530));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getTranslatedToGameCoords().equals(new Coords(500, 530)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Warrior warriorImpl1p2 = warriorResult.getResult();

    // подвинем воина 2 игрока 2 подальше от середины и от воина 2 игрока 1, но чуть в стороне от воина 1
    // игрока 1, чтобы дать воину 1 игрока 1 возможность стрелять из лука
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player2, warrior2p2, new Coords(770, 770));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getTranslatedToGameCoords().equals(new Coords(770, 770)), "Неверные координаты перемещения. возможно неверная стоимость перемещения");
    Warrior warriorImpl2p2 = warriorResult.getResult();

    // Игрок 1 готов
    assertSuccess(gameWrapper.playerReady(player1, true));

    // Игрок 2 готов
    assertSuccess(gameWrapper.playerReady(player2, true));

    // проверим значение удачи в стрельбу у воина 1 игрока 1. Базовое значение 6. Благодаря способности удачи
    // оно должно вырасти до 16
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 16, "Способность удачливого стрелка 10-го уровня не сработала");


    // если первым ходитигрок 2, то передаем ход игроку 1
    gameWrapper.getGetPlayerOwnsTheRound(gameContext)
            .peak(player -> {
              if (player.getId().equals(player2)) {
                assertSuccess(gameWrapper.nextTurn(gameContext, player2));
              }
            });

    // Пробуем атаковать воином 1 игрока 1 воина 1 игрока 2. Это должно выйти. Заодно проверим, что огнем
    // урон нанесен тоже
    int hp = warriorImpl1p2.getAttributes().getHealth();
    Result<InfluenceResult> attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior1p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 120, "Не списаны очки за выстрел луком");
    Influencer influencer = attackResult.getResult().getInfluencers().get(0);
    Assert.isTrue(warriorImpl1p2.getAttributes().getHealth() ==
            hp - influencer.getModifier().getLastCalculatedValue()
                    - influencer.getChildren().get(0).getModifier().getLastCalculatedValue(), "Не правильно нанесен урон огненным луком");

    // Пробуем атаковать воином 1 игрока 1 воина 2 игрока 2. Это должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior2p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 0, "Не списаны очки за второй выстрел луком");

    // переходы ходом опять до воина 1
    assertSuccess(gameWrapper.nextTurn(gameContext, player1));
    assertSuccess(gameWrapper.nextTurn(gameContext, player2));

    // проверим значение удачи в стрельбу у воина 1 игрока 1. Базовое значение 6. Благодаря способности удачи
    // оно должно вырасти до 16  НО не более
    Assert.isTrue(warriorImpl1p1.getAttributes().getLuckRangeAtack() == 16, "Способность удачливого стрелка 10-го уровня не сработала");

    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 240, "Не восстановились очки действия");

    // поднимем удачу до 100% воину 1 игрока 1
    warriorImpl1p1.getAttributes().setLuckRangeAtack(100);
    warriorImpl1p1.getAttributes().setLuckMeleeAtack(100);

    // Пробуем атаковать воином 1 игрока 1 воина 2 игрока 2. Это должно выйти и должна быть удача
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior2p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(attackResult.getResult().getInfluencers().get(0).getModifier().isLuckyRollOfDice(), "Удача не сработала");

    // подлечим воина 2 игрока 2. Он нам еще нужен живым
    warriorImpl2p2.getAttributes().setHealth(20);

    assertSuccess(gameWrapper.getCore().removeGameContext(gameContext));
  }

  @Test
  public void doTest() {
    innerDoTest();
  }
}
