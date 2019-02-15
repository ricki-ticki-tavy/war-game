import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.wraper.GameWrapper;
import core.entity.map.GameRulesImpl;
import core.game.CoreImpl;
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
import tests.test.warrior.TestSkeleton;
import tests.test.warrior.TestViking;
import tests.test.warrior.TestVityaz;
import tests.test.weapons.TestBow;
import tests.test.weapons.TestShortSword;
import tests.test.weapons.TestSword;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CreateWarriorsWeaponsUsersTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = tests.config.TestContextConfiguration.class)
public class CreateWarriorsWeaponsUsersTest {
  private static final Logger logger = LoggerFactory.getLogger(CreateWarriorsWeaponsUsersTest.class);

  @Autowired
  GameWrapper gameWrapper;

  public CreateWarriorsWeaponsUsersTest setGameWrapper(GameWrapper gameWrapper){
    this.gameWrapper = gameWrapper;
    return this;
  }

  public void innerDoTest(){

    ((CoreImpl)gameWrapper.getCore()).registerWarriorBaseClass(TestSkeleton.CLASS_NAME, TestSkeleton.class);
    ((CoreImpl)gameWrapper.getCore()).registerWarriorBaseClass(TestViking.CLASS_NAME, TestViking.class);
    ((CoreImpl)gameWrapper.getCore()).registerWarriorBaseClass(TestVityaz.CLASS_NAME, TestVityaz.class);

    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestBow.CLASS_NAME, TestBow.class);
    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestShortSword.CLASS_NAME, TestShortSword.class);
    ((CoreImpl)gameWrapper.getCore()).registerWeaponClass(TestSword.CLASS_NAME, TestSword.class);


    int step = 1;
    logger.info("step " + step++);
    Result<List<String>> classesResult = gameWrapper.getBaseWarriorClasses();
    Assert.isTrue(classesResult.isSuccess(), "Не удалось получить список базовых классов воинов");
    Assert.isTrue(classesResult.getResult().size() > 0, "Список базовых классов воинов пуст");

    logger.info("step " + step++);
    Result<List<String>> classesResult2 = gameWrapper.getWeaponClasses();
    Assert.isTrue(classesResult2.isSuccess(), "Не удалось получить список базовых классов оружия");
    Assert.isTrue(classesResult.getResult().size() > 0, "Список базовых классов оружия пуст");

    logger.info("step " + step++);
    Result<Class<? extends WarriorBaseClass>> baseWarriorClassResult = gameWrapper.getCore()
            .findWarriorBaseClassByName(TestViking.CLASS_NAME);
    Assert.isTrue(baseWarriorClassResult.isSuccess(), "Не найден базовый класс воина");

    logger.info("step " + step++);
    Result<Class<? extends Weapon>> baseWeaponClassResult = gameWrapper.getCore()
            .findWeaponByName(TestBow.CLASS_NAME);
    Assert.isTrue(baseWeaponClassResult.isSuccess(), "Не найден базовый класс оружия");

    logger.info("step " + step++);
    Result<Player> playerLoginResult = gameWrapper.login("test");
    Assert.notNull(playerLoginResult.isSuccess(), "Игрок не создан");
    Player player = playerLoginResult.getResult();

    logger.info("step " + step++);
    Result<Player> playerLoginResult2 = gameWrapper.login("test 2");
    Assert.notNull(playerLoginResult2.isSuccess(), "Игрок 2 не создан");
    Player player2 = playerLoginResult2.getResult();

    logger.info("step " + step++);
    GameRules rules = new GameRulesImpl(9, 2, 1, 50, 200, 2, 600, 30);
    Result<Context> createContextResult = gameWrapper.createGame("test2", rules, "testMap.xml", "test-game", false);
    Assert.isTrue(createContextResult.isFail(), "Карта создана не существующим пользователем");

    logger.info("step " + step++);
    rules = new GameRulesImpl(9, 2, 1, 50, 200, 2, 600, 30);
    createContextResult = gameWrapper.createGame(player.getId(), rules, "testMap.xml", "test-game", false);
    Assert.isTrue(createContextResult.isSuccess(), "Карта не загружена");
    Assert.isTrue(((List<Context>) gameWrapper.getGamesList().getResult()).size() == 1, "Контекст не появился в списке контекстов");
    Context context1 = createContextResult.getResult();
    Assert.isTrue(gameWrapper.getCore().findGameContextByUID(context1.getContextId()).isSuccess(), "Контекст не появился в списке контекстов");
    Assert.isTrue(context1.getLevelMap().getDescription().equals("Тестовая карта для 2-х игроков"), "Неверно загружено название карты");

    logger.info("step " + step++);
    Result<Context> createContextResult2 = gameWrapper.createGame(player2.getId(), rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult2.isSuccess(), "Карта 2 не загружена");
    Assert.isTrue(((List<Context>) gameWrapper.getGamesList().getResult()).size() == 2, "Контекст 2 не появился в списке контекстов");

    logger.info("step " + step++);
    Result<Context> createContextResult3 = gameWrapper.createGame(player2.getId(), rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult3.isSuccess(), "Карта 3 не загружена");
    Assert.isTrue(((List<Context>) gameWrapper.getGamesList().getResult()).size() == 2, "Контекст 2 не появился в списке контекстов");

    logger.info("step " + step++);
    Result<Warrior> warriorResult = gameWrapper.createWarrior(context1.getContextId(), player.getId(), TestViking.CLASS_NAME, player.getStartZone().getBottomRightConner());
    Assert.notNull(warriorResult.isSuccess(), "Воин не создан");
    Assert.isTrue(warriorResult.getResult() == player.getWarriors().get(0), "Созданный воин и воин на карте не равны");
    Warrior warrior = warriorResult.getResult();

    logger.info("step " + step++);
    Result<Player> reconnectUserResult = gameWrapper.connectToGame(player.getId(), context1.getContextId());
    Assert.isTrue(reconnectUserResult.isSuccess(), "Игрок не переподключен");
    Assert.isTrue(context1.getLevelMap().getPlayers().size() == 1, "Игрок был создан дополниткльно с теми же параметрами вместо переподключения");
    // этот плеер имеет одного воина, добавленного тестом выше
    Assert.isTrue(player.getWarriors().size() == 1, "Игрок был пересоздан на месте первого вместо переподключения");

    logger.info("step " + step++);
    Result<Weapon> resultW1 = gameWrapper.giveWeaponToWarrior(context1.getContextId(), player.getId(), warrior.getId(), TestShortSword.CLASS_NAME);
    Assert.isTrue(resultW1.isSuccess(), "Ошибка добавления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует добавленное оружие");
    Weapon weapon1 = resultW1.getResult();

    logger.info("step " + step++);
    Result<Weapon> resultW2 = gameWrapper.giveWeaponToWarrior(context1.getContextId(), player.getId(), warrior.getId(), TestSword.CLASS_NAME);
    Assert.isTrue(resultW2.isSuccess(), "Ошибка добавления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина отсутствует второе добавленное оружие");
    Weapon weapon2 = resultW2.getResult();

    logger.info("step " + step++);
    Result<Weapon> resultW3 = gameWrapper.giveWeaponToWarrior(context1.getContextId(), player.getId(), warrior.getId(), TestSword.CLASS_NAME);
    Assert.isTrue(resultW3.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина присутствует третье добавленное оружие");

    logger.info("step " + step++);
    Result<Weapon> resultDropWeapon1 = gameWrapper.dropWeaponByWarrior(context1.getContextId(), player.getId(), warrior.getId(), weapon1.getId());
    Assert.isTrue(resultDropWeapon1.isSuccess(), "Ошибка удаления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует удаленное первое оружие");

    logger.info("step " + step++);
    resultDropWeapon1 = gameWrapper.dropWeaponByWarrior(context1.getContextId(), player.getId(), warrior.getId(), weapon1.getId());
    Assert.isTrue(resultDropWeapon1.isFail(), "Ошибка повторного удаления ранее удаленного оружия (короткого меча (первого оружия))");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина неверное кол-во оружияпосле удаления первое оружие");

    logger.info("step " + step++);
    Result<Weapon> resultDropWeapon3 = gameWrapper.giveWeaponToWarrior(context1.getContextId(), player.getId(), warrior.getId(), TestBow.CLASS_NAME);
    Assert.isTrue(resultDropWeapon3.isFail(), "Ошибка добавления лука (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует лук. третье добавленное оружие");

    logger.info("step " + step++);
    resultDropWeapon1 = gameWrapper.dropWeaponByWarrior(context1.getContextId(), player.getId(), warrior.getId(), weapon2.getId());
    Assert.isTrue(resultDropWeapon1.isSuccess(), "Ошибка удаления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 0, "У воина присутствует удаленное второе оружие");

    logger.info("step " + step++);
    resultDropWeapon3 = gameWrapper.giveWeaponToWarrior(context1.getContextId(), player.getId(), warrior.getId(), TestBow.CLASS_NAME);
    Assert.isTrue(resultDropWeapon3.isSuccess(), "Ошибка добавления лука (единственного оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует лук");
    Weapon weapon3 = resultDropWeapon3.getResult();

    logger.info("step " + step++);
    resultW2 = gameWrapper.giveWeaponToWarrior(context1.getContextId(), player.getId(), warrior.getId(), TestSword.CLASS_NAME);
    Assert.isTrue(resultW2.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует не добавленное оружие");

    logger.info("step " + step++);
    Result<Player> playerConnectResult2 = gameWrapper.connectToGame(player2.getId(), context1.getContextId());
    Assert.isTrue(playerConnectResult2.isSuccess(), "Игрок не создан подключен");
    Assert.isTrue(context1.getLevelMap().getPlayers().size() == 2, "Игрок 2 не был подключен к контексту 1");
    // игрок 2 имел созданную игру. После подключения к этой игре его контекст должен былудалиться
    Assert.isTrue(((List<Context>) gameWrapper.getGamesList().getResult()).size() == 1, "Старый контекст подключенного игрока не удалился как контекст владельца");

//    warriorResult = gameWrapper.createWarrior()

    logger.info("step " + step++);
    gameWrapper.getCore().removeGameContext(context1.getContextId());
    Assert.isTrue(gameWrapper.getCore().findGameContextByUID(context1.getContextId()).isFail(), "Контекст не удален");
  }

  @Test
  public void test1() {
    innerDoTest();
  }

}
