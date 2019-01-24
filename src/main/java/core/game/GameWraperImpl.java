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
import api.game.wraper.GameWraper;
import core.entity.map.GameRulesImpl;
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
import java.util.Optional;

import static core.system.error.GameErrors.USER_NOT_CONNECTED_TO_ANY_GAME;
import static core.system.error.GameErrors.USER_NOT_LOGGED_IN;

@Component
public class GameWraperImpl implements GameWraper {

  private static final Logger logger = LoggerFactory.getLogger(GameWraperImpl.class);

  @Autowired
  Core core;

  private boolean innerTest() {
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

    GameRules rules = new GameRulesImpl(9, 2, 50, 200, 2, 600);
    Result<Context> createContextResult = createGame("test2", rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult.isFail(), "Карта создана не существующим пользователем");

    rules = new GameRulesImpl(9, 2, 50, 200, 2, 600);
    createContextResult = createGame(player.getId(), rules, "level2.xml", "test-game", false);
    Assert.isTrue(createContextResult.isSuccess(), "Карта не загружена");
    Assert.isTrue(((List<Context>) getGamesList().getResult()).size() == 1, "Контекст не появился в списке контекстов");

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

    Result resultW1 = giveWeaponToWarrior(player.getId(), warrior.getId(), ShortSword.CLASS_NAME);
    Assert.isTrue(resultW1.isSuccess(), "Ошибка добавления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует добавленное оружие");

    Result resultW2 = giveWeaponToWarrior(player.getId(), warrior.getId(), Sword.CLASS_NAME);
    Assert.isTrue(resultW2.isSuccess(), "Ошибка добавления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина отсутствует второе добавленное оружие");

    Result resultW3 = giveWeaponToWarrior(player.getId(), warrior.getId(), Sword.CLASS_NAME);
    Assert.isTrue(resultW3.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина присутствует третье добавленное оружие");

//    result = warrior.dropWeapon(((Weapon) resultW1.getResult()).getId());
//    Assert.isTrue(result.isSuccess(), "Ошибка удаления короткого меча (первого оружия)");
//    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует удаленное первое оружие");

//    result = warrior.dropWeapon(((Weapon) resultW1.getResult()).getId());
//    Assert.isTrue(result.isFail(), "Ошибка повторного удаления ранее удаленного оружия (короткого меча (первого оружия))");
//    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина неверное кол-во оружияпосле удаления первое оружие");
//
//    result = warrior.takeWeapon(Bow.class);
//    Assert.isTrue(result.isFail(), "Ошибка добавления лука (третьего оружия). Оружие не должно быть добавлено ");
//    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует лук. третье добавленное оружие");
//
//    result = warrior.dropWeapon(((Weapon) resultW2.getResult()).getId());
//    Assert.isTrue(result.isSuccess(), "Ошибка удаления меча (второго оружия)");
//    Assert.isTrue(warrior.getWeapons().size() == 0, "У воина присутствует удаленное второе оружие");
//
//    Result resultBow = warrior.takeWeapon(Bow.class);
//    Assert.isTrue(resultBow.isSuccess(), "Ошибка добавления лука (единственного оружия)");
//    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует лук");
//
//    resultW2 = warrior.takeWeapon(Sword.class);
//    Assert.isTrue(resultW2.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
//    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует не добавленное оружие");

//    player = context.connectPlayer("test", "testSession1");
//    Assert.notNull(player, "Игрок не переподключен");
//    Assert.isTrue(context.getLevelMap().getPlayers().size() == 1, "Игрок был создан дополниткльно с теми же параметрами вместо переподключения");
//    Assert.isTrue(player.getWarriors().size() == 1, "Игрок был пересоздан на месте первого вместо переподключения");
//
//    player = context.connectPlayer("test2", "testSession2");
//    Assert.notNull(player, "Игрок не создан");
//    Assert.isTrue(context.getLevelMap().getPlayers().size() == 2, "Игрок был создан На месте первого");
//
    core.removeGameContext(createContextResult.getResult());
    Assert.isTrue(core.findGameContextByUID(createContextResult.getResult().getContextId()) == null, "Контекст не удален");
    return true;
  }

  @PostConstruct
  public void testGame() {
    logger.info("Запуск внутреннего теста...");
    if (innerTest()) {
      logger.info("ВНУТРЕННИЙ ТЕСТ ПРОЙДЕН");
    } else {
      logger.error("ВНУТРЕННИЙ ТЕСТ       НЕ   ПРОЙДЕН");
    }
  }

  @Override
  public Result<Player> login(String userName) {
    return core.loginPlayer(userName);
  }

  @Override  // TODO рулсы надо копировать при создании игры
  public Result createGame(String ownerUserName
          , GameRules gameRules
          , String mapName
          , String gameName
          , boolean hidden) {
    Result<Player> playerResult;
    if ((playerResult = core.findPlayer(ownerUserName)).isFail()) {
      core.fireEvent(new EventImpl(null, null, EventType.GAME_CONTEXT_CREATE
              , new EventDataContainer(playerResult, new String[]{gameName, ownerUserName}), null));
      return playerResult;
    } else {

      return core.createGameContext(playerResult.getResult(), gameRules
              , this.getClass().getClassLoader().getResourceAsStream(mapName)
              , gameName, hidden);
    }
  }

  @Override
  public Result getGamesList() {
    return ResultImpl.success(core.getContextList());
  }

  @Override
  public Result<Warrior> createWarrior(String userName, String className, Coords coords) {
    return core.findPlayer(userName)
            .onSuccess(foundPlayer -> {
              Context context = foundPlayer.getContext();
              if (context != null) {
                return core.findWarriorBaseClassByName(className)
                        .onSuccess(baseClass -> context.createWarrior(foundPlayer, baseClass, coords));
              } else {
                return ResultImpl.fail(USER_NOT_CONNECTED_TO_ANY_GAME.getError(foundPlayer.getTitle()));
              }
            });
  }

  @Override
  public Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, String weaponClassName) {
    return core.findPlayer(userName)
            .onSuccess(foundPlayer -> {
              Context context = foundPlayer.getContext();
              if (context != null) {
                return core.findWeaponByName(weaponClassName)
                        .onSuccess(baseClass -> foundPlayer.findWarriorById(warriorId)
                                .onSuccess(foundWarrior -> foundWarrior.takeWeapon(baseClass)));
              } else {
                return ResultImpl.fail(USER_NOT_CONNECTED_TO_ANY_GAME.getError(foundPlayer.getTitle()));
              }
            });
  }
}
