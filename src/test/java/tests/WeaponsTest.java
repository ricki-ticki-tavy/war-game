package tests;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Coords;
import api.game.action.AttackResult;
import api.game.map.Player;
import api.game.wraper.GameWrapper;
import core.entity.weapon.Bow;
import core.entity.weapon.Sword;
import org.springframework.util.Assert;
import tests.abstracts.AbstractMapTest;

import static core.system.error.GameErrors.*;

/**
 * Проверка подписывания и отписывания от событий.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {tests.StartGameWith2PlayersAndMovesTest.class})
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class WeaponsTest extends AbstractMapTest {

  //  @Autowired
  GameWrapper gameWrapper;

  public WeaponsTest setGameWrapper(GameWrapper gameWrapper) {
    this.gameWrapper = gameWrapper;
    return this;
  }

  public void innerDoTest() {
    initMap(gameWrapper);
    Context context = gameWrapper.getCore().findGameContextByUID(gameContext).getResult();


    // Дадим воину 1 игрока 1 лук
    Result<Weapon> weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player1, warrior1p1, Bow.CLASS_NAME);
    assertSuccess(weaponResult);
    String bowWarrior1p1 = weaponResult.getResult().getId();

    // Дадим воину 2 игрока 1 лук
    weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player1, warrior2p1, Bow.CLASS_NAME);
    assertSuccess(weaponResult);
    String bowWarrior2p1 = weaponResult.getResult().getId();

    // Дадим воину 1 игрока 2 меч
    weaponResult = gameWrapper.giveWeaponToWarrior(gameContext, player2, warrior1p2, Sword.CLASS_NAME);
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
    Result<AttackResult> attackResult = gameWrapper.attackWarrior(gameContext, player2, warrior1p2, warrior2p1, swordWarrior1p2);
    Assert.isTrue(attackResult.isFail(CONTEXT_GAME_NOT_STARTED), "Атака при не начатой игре удалась");

    // Игрок 1 готов
    Result<Player> playerResult = gameWrapper.playerReady(player1, true);
    assertSuccess(playerResult);

    // Игрок 2 готов
    playerResult = gameWrapper.playerReady(player2, true);
    assertSuccess(playerResult);

    // Пробуем атаковать воином 1 игрока 2 воина 2 игрока 1. Это не должно выйти так как ход не игрока 2
    attackResult = gameWrapper.attackWarrior(gameContext, player2, warrior1p2, warrior2p1, swordWarrior1p2);
    Assert.isTrue(attackResult.isFail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND), "Атака не в свой ход удалась");

    // Пробуем атаковать воином 2 игрока 1 воина 1 игрока 1. Это не должно выйти так как они не враги
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior2p1, warrior1p1, bowWarrior2p1);
    Assert.isTrue(attackResult.isFail(WARRIOR_ATTACK_TARGET_WARRIOR_IS_ALIED), "Атака дружественного воина удалась");

    // Пробуем атаковать воином 2 игрока 1 воина 2 игрока 2. Это не должно выйти так как слишком близко стоит враг
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior2p1, warrior1p2, bowWarrior2p1);
    Assert.isTrue(attackResult.isFail(WARRIOR_ATTACK_RANGED_NOT_POSIBLE_ENEMYS_IS_NEAR_ATTACKER), "Дистанционная атака при наличии рядом врага удалась");

    // Пробуем атаковать воином 1 игрока 1 воина 1 игрока 2 оружием воина 2 игрока 1. Это не должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior1p2, bowWarrior2p1);
    Assert.isTrue(attackResult.isFail(WARRIOR_WEAPON_NOT_FOUND), "Дистанционная атака при наличии рядом врага удалась");

    // Пробуем атаковать воином 1 игрока 1 воина 1 игрока 2. Это должно выйти
//    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior1p2, bowWarrior1p1);
//    Assert.isTrue(attackResult.isFail(WARRIOR_ATTACK_RANGED_NOT_POSIBLE_ENEMYS_IS_NEAR_ATTACKER), "Дистанционная атака при наличии рядом врага удалась");

    // Пробуем атаковать воином 1 игрока 1 воина 2 игрока 2. Это должно выйти
    attackResult = gameWrapper.attackWarrior(gameContext, player1, warrior1p1, warrior2p2, bowWarrior1p1);
    Assert.isTrue(attackResult.isFail(WARRIOR_ATTACK_RANGED_NOT_POSIBLE_ENEMYS_IS_NEAR_ATTACKER), "Дистанционная атака при наличии рядом врага удалась");

    Result r = gameWrapper.getCore().removeGameContext(gameContext);
    assertSuccess(r);
  }

//  @Test
//  public void doTest(){
//    innerDoTest();
//  }
}
