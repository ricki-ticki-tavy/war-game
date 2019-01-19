package core.game;

import api.core.GameContext;
import api.core.Core;
import api.entity.warrior.Warrior;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import core.entity.warrior.VikingArcher;
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
    GameContext context = core.createGameContext("test"
            , new GameRules(9, 2, 50, 200, 2, 600)
            , this.getClass().getClassLoader().getResourceAsStream("level2.xml")
    , "test-game", false);
    Assert.isTrue(context.getLevelMap().isLoaded(), "Карта не загружена");

    Player player = context.connectPlayer("test", "testSession1");
    Assert.notNull(player, "Игрок не создан");

    Warrior warrior = context.createWarrior("testSession1", VikingArcher.class);
    Assert.notNull(warrior, "Воин не создан");
    Assert.isTrue(warrior == player.getWarriors().get(0), "Созданный воин и воин на карте не равны");

    player = context.connectPlayer("test", "testSession1");
    Assert.notNull(player, "Игрок не переподключен");
    Assert.isTrue(context.getLevelMap().getPlayers().size() == 1, "Игрок был создан дополниткльно с теми же параметрами вместо переподключения");
    Assert.isTrue(player.getWarriors().size() == 1, "Игрок был пересоздан на месте первого вместо переподключения");

    player = context.connectPlayer("test2", "testSession2");
    Assert.notNull(player, "Игрок не создан");
    Assert.isTrue(context.getLevelMap().getPlayers().size() == 2, "Игрок был создан На месте первого");

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
