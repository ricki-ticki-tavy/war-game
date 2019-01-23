package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.warrior.Viking;
import core.entity.weapon.Bow;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Component
public class Game {

  private static final Logger logger = LoggerFactory.getLogger(Game.class);

  @Autowired
  Core  core;

  private boolean innerTest(){
    logger.info("Создание тестового контекста...");
    Context context = core.createGameContext("test"
            , new GameRules(9, 2, 50, 200, 2, 600)
            , this.getClass().getClassLoader().getResourceAsStream("level2.xml")
    , "test-game", false);
    Assert.isTrue(context.getLevelMap().isLoaded(), "Карта не загружена");

    Player player = context.connectPlayer("test", "testSession1");
    Assert.notNull(player, "Игрок не создан");

    Warrior warrior = context.createWarrior("testSession1", Viking.class);
    Assert.notNull(warrior, "Воин не создан");
    Assert.isTrue(warrior == player.getWarriors().get(0), "Созданный воин и воин на карте не равны");

    Result resultW1 = warrior.takeWeapon(ShortSword.class);
    Assert.isTrue(resultW1.isSuccess(), "Ошибка добавления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует добавленное оружие");

    Result resultW2 = warrior.takeWeapon(Sword.class);
    Assert.isTrue(resultW2.isSuccess(), "Ошибка добавления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина отсутствует второе добавленное оружие");

    Result result = warrior.takeWeapon(Sword.class);
    Assert.isTrue(result.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 2, "У воина присутствует третье добавленное оружие");

    result = warrior.dropWeapon(((Weapon)resultW1.getResult()).getId());
    Assert.isTrue(result.isSuccess(), "Ошибка удаления короткого меча (первого оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует удаленное первое оружие");

    result = warrior.dropWeapon(((Weapon)resultW1.getResult()).getId());
    Assert.isTrue(result.isFail(), "Ошибка повторного удаления ранее удаленного оружия (короткого меча (первого оружия))");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина неверное кол-во оружияпосле удаления первое оружие");

    result = warrior.takeWeapon(Bow.class);
    Assert.isTrue(result.isFail(), "Ошибка добавления лука (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует лук. третье добавленное оружие");

    result = warrior.dropWeapon(((Weapon)resultW2.getResult()).getId());
    Assert.isTrue(result.isSuccess(), "Ошибка удаления меча (второго оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 0, "У воина присутствует удаленное второе оружие");

    Result resultBow = warrior.takeWeapon(Bow.class);
    Assert.isTrue(result.isSuccess(), "Ошибка добавления лука (единственного оружия)");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина отсутствует лук");

    resultW2 = warrior.takeWeapon(Sword.class);
    Assert.isTrue(resultW2.isFail(), "Ошибка добавления меча (третьего оружия). Оружие не должно быть добавлено ");
    Assert.isTrue(warrior.getWeapons().size() == 1, "У воина присутствует не добавленное оружие");

    player = context.connectPlayer("test", "testSession1");
    Assert.notNull(player, "Игрок не переподключен");
    Assert.isTrue(context.getLevelMap().getPlayers().size() == 1, "Игрок был создан дополниткльно с теми же параметрами вместо переподключения");
    Assert.isTrue(player.getWarriors().size() == 1, "Игрок был пересоздан на месте первого вместо переподключения");

    player = context.connectPlayer("test2", "testSession2");
    Assert.notNull(player, "Игрок не создан");
    Assert.isTrue(context.getLevelMap().getPlayers().size() == 2, "Игрок был создан На месте первого");

    core.removeGameContext(context);
    Assert.isTrue(core.findGameContextByUID(context.getContextId()) == null, "Контекст не удален");
    return true;
  }

  @PostConstruct
  public void testGame(){
    logger.info("Запуск внутреннего теста...");
    if (innerTest()){
      logger.info("ВНУТРЕННИЙ ТЕСТ ПРОЙДЕН");
    } else {
      logger.error("ВНУТРЕННИЙ ТЕСТ       НЕ   ПРОЙДЕН");
    }
  }
}
