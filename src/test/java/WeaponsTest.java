
import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.geo.Coords;
import api.game.action.InfluenceResult;
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
import tests.test.weapons.TestBow;
import tests.test.weapons.TestSword;

import static core.system.error.GameErrors.*;

// TODO тест на дальность стрельбы
// TODO тест на кол-во боеприпасов для стрельбы
/**
 * Проверка подписывания и отписывания от событий.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WeaponsTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class WeaponsTest extends AbstractMapTest {

  @Autowired
  GameWrapper gameWrapper;

  public WeaponsTest setGameWrapper(GameWrapper gameWrapper) {
    this.gameWrapper = gameWrapper;
    return this;
  }

  public void innerDoTest() {
    initMap(gameWrapper, "WeaponsTest_user1", "WeaponsTest_User2");
    Context context = gameWrapper.getCore().findGameContextByUID(gameContext).getResult();


    // Дадим воину 1 игрока 1 лук
    Result<Weapon> weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player1, warrior1p1, TestBow.CLASS_NAME);
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

    Assert.isTrue(warriorImpl1p1.calcDistanceTo(warriorImpl1p2.getTranslatedToGameCoords()) >
                    context.getGameRules().getWarriorSize() + 2 * context.getLevelMap().getSimpleUnitSize()
            , "Расстояние от воина 1 игрока 1 до воина 1 игрока 2 должно быть более 2-х клеток");

    Assert.isTrue(warriorImpl1p1.calcDistanceTo(warriorImpl2p1.getTranslatedToGameCoords()) <
                    context.getGameRules().getWarriorSize() + 2 * context.getLevelMap().getSimpleUnitSize()
            , "Расстояние от воина 1 игрока 1 до воина 2 должно быть менее 2-х клеток");

    Assert.isTrue(warriorImpl2p1.calcDistanceTo(warriorImpl1p2.getTranslatedToGameCoords()) <
                    context.getGameRules().getWarriorSize() + 2 * context.getLevelMap().getSimpleUnitSize()
            , "Расстояние от воина 2 игрока 1 до воина 1 игрока 2 должно быть менее 2-х клеток");

    // Пробуем атаковать воином 1 игрока 2 воина 2 игрока 1. Это не должно выйти так как игра не началась
    Result<InfluenceResult> attackResult = gameWrapper.attackWarrior(gameContext, player2, warrior1p2, warrior2p1, swordWarrior1p2);
    Assert.isTrue(attackResult.isFail(CONTEXT_GAME_NOT_STARTED), "Атака при не начатой игре удалась");

    // Игрок 1 готов
    assertSuccess(gameWrapper.playerReady(player1, true));

    // Игрок 2 готов
    assertSuccess(gameWrapper.playerReady(player2, true));

    // если первым ходитигрок 2, то передаем ход игроку 1
    gameWrapper.getPlayerOwnsTheRound(gameContext)
            .peak(player -> {
              if (player.getId().equals(player2)) {
                assertSuccess(gameWrapper.nextTurn(gameContext, player2));
              }
            });

    // Пробуем атаковать воином 1 игрока 2 воина 2 игрока 1. Это не должно выйти так как ход не игрока 2
    attackResult = gameWrapper.attackWarrior(gameContext, player2, warrior1p2, warrior2p1, swordWarrior1p2);
    Assert.isTrue(attackResult.isFail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND), "Атака не в свой ход удалась");

    // Пробуем атаковать воином 2 игрока 1 воина 1 игрока 1. Это не должно выйти так как они не враги
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior2p1, warrior1p1, bowWarrior2p1);
    Assert.isTrue(attackResult.isFail(WARRIOR_ATTACK_TARGET_WARRIOR_IS_ALIED), "Атака дружественного воина удалась");

    // Пробуем атаковать воином 2 игрока 1 воина 2 игрока 2. должно выйти, но атака будет ближнеей так как слишком
    // близко стоит цель
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior2p1, warrior1p2, bowWarrior2p1);
    assertSuccess(attackResult);
    Assert.isTrue(attackResult.getResult().getInfluencers().get(0).getModifier().getTitle().equals(TestBow.SECOND_WEAPON_NAME), "Дистанционная атака должна была быть изменена на рукопашную");

    // Пробуем атаковать воином 1 игрока 1 воина 1 игрока 2 оружием воина 2 игрока 1. Это не должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior1p2, bowWarrior2p1);
    Assert.isTrue(attackResult.isFail(WARRIOR_WEAPON_NOT_FOUND), "Дистанционная атака при наличии рядом врага удалась");

    // Пробуем атаковать воином 1 игрока 1 воина 1 игрока 2. Это должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior1p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 120, "Не списаны очки за выстрел луком");

    // Пробуем атаковать воином 1 игрока 1 воина 2 игрока 2. Это должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior2p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 0, "Не списаны очки за второй выстрел луком");


    // переходы ходом опять до воина 1
    assertSuccess(gameWrapper.nextTurn(gameContext, player1));
    assertSuccess(gameWrapper.nextTurn(gameContext, player2));

    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 240, "Не восстановились очки действия");

    // Переместим немного воина 1 игрока 1, чтобы очки действия списались немного
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(460, 475));
    assertSuccess(warriorResult);
    Assert.isTrue(warriorResult.getResult().getCoords().equals(new Coords(460, 475)), "Неверные координаты перемещения воина 2 игрока 1 на втором ходе. возможно неверная стоимость перемещения");

    // Пробуем атаковать воином 1 игрока 1 воина 2 игрока 2. Это должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior2p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(warriorImpl1p1.getAttributes().getActionPoints() == 98, "Не списаны очки за выстрел луком или за предыдущее перемещение");

    // переходы ходом опять до воина 1
    assertSuccess(gameWrapper.nextTurn(gameContext, player1));
    assertSuccess(gameWrapper.nextTurn(gameContext, player2));

    // поднимем удачу до 100% воину 1 игрока 1
    warriorImpl1p1.getAttributes().setLuckRangeAtack(100);
    warriorImpl1p1.getAttributes().setLuckMeleeAtack(100);

    // Пробуем атаковать воином 1 игрока 1 воина 2 игрока 2. Это должно выйти и должна быть удача
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior2p2, bowWarrior1p1);
    assertSuccess(attackResult);
    Assert.isTrue(attackResult.getResult().getInfluencers().get(0).getModifier().isLuckyRollOfDice(), "Удача не сработала");

    // пробуем выполнить перемещение после атаки. Это не должно удаться
    warriorResult = gameWrapper.moveWarriorTo(gameContext, player1, warrior1p1, new Coords(450, 475));
    Assert.isTrue(warriorResult.isFail(WARRIOR_CAN_T_MORE_MOVE_ON_THIS_TURN), "Движение после атаки удалось.");

//    // Проверим, что воин 2
//    Assert.isTrue();
    assertSuccess(gameWrapper.getCore().removeGameContext(gameContext));
  }

  @Test
  public void doTest() {
    innerDoTest();
  }
}
