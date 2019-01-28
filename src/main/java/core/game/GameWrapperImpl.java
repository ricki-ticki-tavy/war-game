package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.wraper.GameWrapper;
import core.entity.map.GameRulesImpl;
import core.entity.warrior.Skeleton;
import core.entity.warrior.Viking;
import core.entity.weapon.Bow;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import core.system.ResultImpl;
import core.system.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

import java.util.List;

@Component
public class GameWrapperImpl implements GameWrapper {

  private static final Logger logger = LoggerFactory.getLogger(GameWrapperImpl.class);

  @Autowired
  Core core;

  //===================================================================================================
  //===================================================================================================

  private boolean innerTest1() {
    Result<List<String>> classesResult = core.getBaseWarriorClasses();
    Assert.isTrue(classesResult.isSuccess(), "Не удалось получить список базовых классов воинов");
    Assert.isTrue(classesResult.getResult().size() > 0, "Список базовых классов воинов пуст");

    Result<List<String>> classesResult2 = core.getWeaponClasses();
    Assert.isTrue(classesResult2.isSuccess(), "Не удалось получить список базовых классов оружия");
    Assert.isTrue(classesResult.getResult().size() > 0, "Список базовых классов оружия пуст");

    Result<Class<? extends WarriorBaseClass>> baseWarriorClassResult = core
            .findWarriorBaseClassByName(Viking.CLASS_NAME);
    Assert.isTrue(baseWarriorClassResult.isSuccess(), "Не найден базовый класс воина");

    Result<Class<? extends Weapon>> baseWeaponClassResult = core
            .findWeaponByName(Bow.CLASS_NAME);
    Assert.isTrue(baseWeaponClassResult.isSuccess(), "Не найден базовый класс оружия");

    Result<Player> playerLoginResult = login("test");
    Assert.notNull(playerLoginResult.isSuccess(), "Игрок не создан");
    Player player = playerLoginResult.getResult();

    Result<Player> playerLoginResult2 = login("test 2");
    Assert.notNull(playerLoginResult2.isSuccess(), "Игрок 2 не создан");
    Player player2 = playerLoginResult2.getResult();

    GameRules rules = new GameRulesImpl(9, 2, 50, 200, 2, 600, 30);
    Result<Context> createContextResult = createGame("test2", rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult.isFail(), "Карта создана не существующим пользователем");

    rules = new GameRulesImpl(9, 2, 50, 200, 2, 600,30);
    createContextResult = createGame(player.getId(), rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult.isSuccess(), "Карта не загружена");
    Assert.isTrue(((List<Context>) getGamesList().getResult()).size() == 1, "Контекст не появился в списке контекстов");
    Context context1 = createContextResult.getResult();

    Result<Context> createContextResult2 = createGame(player2.getId(), rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult2.isSuccess(), "Карта 2 не загружена");
    Assert.isTrue(((List<Context>) getGamesList().getResult()).size() == 2, "Контекст 2 не появился в списке контекстов");

    Result<Context> createContextResult3 = createGame(player2.getId(), rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult3.isSuccess(), "Карта 3 не загружена");
    Assert.isTrue(((List<Context>) getGamesList().getResult()).size() == 2, "Контекст 2 не появился в списке контекстов");

    Result<Warrior> warriorResult = createWarrior(player.getId(), Viking.CLASS_NAME, player.getStartZone().getBottomRightConner());
    Assert.notNull(warriorResult.isSuccess(), "Воин не создан");
    Assert.isTrue(warriorResult.getResult() == player.getWarriors().get(0), "Созданный воин и воин на карте не равны");
    Warrior warrior = warriorResult.getResult();

    Result<Player> reconnectUserResult = connectToGame(player.getId(), context1.getContextId());
    Assert.isTrue(reconnectUserResult.isSuccess(), "Игрок не переподключен");
    Assert.isTrue(context1.getLevelMap().getPlayers().size() == 1, "Игрок был создан дополниткльно с теми же параметрами вместо переподключения");
    // этот плеер имеет одного воина, добавленного тестом выше
    Assert.isTrue(player.getWarriors().size() == 1, "Игрок был пересоздан на месте первого вместо переподключения");

    Result<Weapon> resultW1 = giveWeaponToWarrior(player.getId(), warrior.getId(), ShortSword.CLASS_NAME);
    Assert.isTrue(resultW1.isSuccess(), "Ошибка добавления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует добавленное оружие");
    Weapon weapon1 = resultW1.getResult();

    Result<Weapon> resultW2 = giveWeaponToWarrior(player.getId(), warrior.getId(), Sword.CLASS_NAME);
    Assert.isTrue(resultW2.isSuccess(), "Ошибка добавления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина отсутствует второе добавленное оружие");
    Weapon weapon2 = resultW2.getResult();

    Result<Weapon> resultW3 = giveWeaponToWarrior(player.getId(), warrior.getId(), Sword.CLASS_NAME);
    Assert.isTrue(resultW3.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина присутствует третье добавленное оружие");

    Result<Weapon> resultDropWeapon1 = takeWeaponFromWarrior(player.getId(), warrior.getId(), weapon1.getId());
    Assert.isTrue(resultDropWeapon1.isSuccess(), "Ошибка удаления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует удаленное первое оружие");

    resultDropWeapon1 = takeWeaponFromWarrior(player.getId(), warrior.getId(), weapon1.getId());
    Assert.isTrue(resultDropWeapon1.isFail(), "Ошибка повторного удаления ранее удаленного оружия (короткого меча (первого оружия))");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина неверное кол-во оружияпосле удаления первое оружие");

    Result<Weapon> resultDropWeapon3 = giveWeaponToWarrior(player.getId(), warrior.getId(), Bow.CLASS_NAME);
    Assert.isTrue(resultDropWeapon3.isFail(), "Ошибка добавления лука (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует лук. третье добавленное оружие");

    resultDropWeapon1 = takeWeaponFromWarrior(player.getId(), warrior.getId(), weapon2.getId());
    Assert.isTrue(resultDropWeapon1.isSuccess(), "Ошибка удаления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 0, "У воина присутствует удаленное второе оружие");

    resultDropWeapon3 = giveWeaponToWarrior(player.getId(), warrior.getId(), Bow.CLASS_NAME);
    Assert.isTrue(resultDropWeapon3.isSuccess(), "Ошибка добавления лука (единственного оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует лук");
    Weapon weapon3 = resultDropWeapon3.getResult();

    resultW2 = giveWeaponToWarrior(player.getId(), warrior.getId(), Sword.CLASS_NAME);
    Assert.isTrue(resultW2.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует не добавленное оружие");

    Result<Player> playerConnectResult2 = connectToGame(player2.getId(), context1.getContextId());
    Assert.isTrue(playerConnectResult2.isSuccess(), "Игрок не создан подключен");
    Assert.isTrue(context1.getLevelMap().getPlayers().size() == 2, "Игрок 2 не был подключен к контексту 1");
    // игрок 2 имел созданную игру. После подключения к этой игре его контекст должен былудалиться
    Assert.isTrue(((List<Context>) getGamesList().getResult()).size() == 1, "Старый контекст подключенного игрока не удалился как контекст владельца");


    core.removeGameContext(context1.getContextId());
    Assert.isTrue(core.findGameContextByUID(context1.getContextId()).isFail(), "Контекст не удален");
    return true;
  }
  //===================================================================================================

  private void innerTest2() {
    Player player1 = core.findUserByName("test").getResult();
    Player player2 = core.findUserByName("test 2").getResult();
    GameRules rules = new GameRulesImpl(1, 2, 50, 200, 2, 600, 30);
    Context gameContext = createGame(player1.getId(), rules, "level2.xml", "test-game2", false).getResult();
    connectToGame(player2.getId(), gameContext.getContextId());
    Coords player1coord = player1.getStartZone().getBottomRightConner();
    Coords player2coord = player2.getStartZone().getBottomRightConner();

    Warrior warrior1p1 = createWarrior(player1.getDescription(), Viking.CLASS_NAME
            , new Coords(player1coord.getX(), player1coord.getY()) ).getResult();
    Warrior warrior1p2 = createWarrior(player1.getDescription(), Skeleton.CLASS_NAME
            , new Coords(player2coord.getX() ,player2coord.getY())).getResult();


  }
  //===================================================================================================

  @PostConstruct
  public void testGame() {
    logger.info("Запуск внутреннего теста...");
    innerTest1();
    logger.info("1)   ПЕРВЫЙ ВНУТРЕННИЙ ТЕСТ ПРОЙДЕН");
    innerTest2();
    logger.info("2)   ВТОРОЙ ВНУТРЕННИЙ ТЕСТ ПРОЙДЕН");
  }
  //===================================================================================================

  @Override
  public Result<Player> login(String userName) {
    return core.loginPlayer(userName);
  }
  //===================================================================================================

  @Override  // TODO рулсы надо копировать при создании игры
  public Result<Context> createGame(String ownerUserName
          , GameRules gameRules
          , String mapName
          , String gameName
          , boolean hidden) {

    return core.findUserByName(ownerUserName)
            .mapFail(errorResult -> {
              core.fireEvent(new EventImpl(null, null
                      , EventType.GAME_CONTEXT_CREATE
                      , new EventDataContainer(errorResult
                      , new String[]{gameName, ownerUserName}), null));
              return errorResult;
            })
            .map(foundUser -> core.createGameContext(foundUser, new GameRulesImpl(gameRules)
                    , this.getClass().getClassLoader().getResourceAsStream(mapName)
                    , gameName, hidden));
  }
  //===================================================================================================

  @Override
  public Result<Player> connectToGame(String userName, String contextId) {
    return core.findUserByName(userName)
            .map(foundUser -> core.findGameContextByUID(contextId)
                    .map(foundContext -> foundContext.connectPlayer(foundUser))
                    .map(ctx -> ResultImpl.success(foundUser)));
  }
  //===================================================================================================

  @Override
  public Result getGamesList() {
    return ResultImpl.success(core.getContextList());
  }
  //===================================================================================================

  @Override
  public Result<Warrior> createWarrior(String userName, String className, Coords coords) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(playerContext -> core.findWarriorBaseClassByName(className)
                            .map(baseClass -> playerContext.createWarrior(foundPlayer, baseClass, coords))));
  }
//===================================================================================================

  @Override
  public Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, String weaponClassName) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> core.findWeaponByName(weaponClassName)
                            .map(baseClass -> foundPlayer.findWarriorById(warriorId)
                                    .map(foundWarrior -> foundWarrior.takeWeapon(baseClass)))));
  }
//===================================================================================================

  @Override
  public Result<Weapon> takeWeaponFromWarrior(String userName, String warriorId, String weaponId) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundPlayer.findWarriorById(warriorId)
                            .map(foundWarrior -> foundWarrior.dropWeapon(weaponId))));
  }
//===================================================================================================

  @Override
  public Result<Player> playerReady(String userName, boolean readyToPlay) {
    return core.findUserByName(userName)
            .map(foundPlayer -> foundPlayer.findContext()
                    .map(foundContext -> foundContext.setPlayerReadyToGameState(foundPlayer, readyToPlay)));
  }
  //===================================================================================================
}
