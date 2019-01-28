package core.game.integration.test;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.weapon.Weapon;
import api.game.Coords;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.wraper.GameWrapper;
import core.entity.map.GameRulesImpl;
import core.entity.warrior.Skeleton;
import core.entity.warrior.Viking;
import core.entity.weapon.Bow;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Временно. первый тест
 */
public class InnerTest2 {
  public void test(GameWrapper wrapper) {
    Player player1 = wrapper.login("test").getResult();
    Player player2 = wrapper.login("test 2").getResult();
    GameRules rules = new GameRulesImpl(1, 2, 50, 200, 2, 600, 30);
    Context gameContext = wrapper.createGame(player1.getId(), rules, "level2.xml", "test-game2", false).getResult();
    wrapper.connectToGame(player2.getId(), gameContext.getContextId());
    Coords player1coord = player1.getStartZone().getBottomRightConner();
    Coords player2coord = player2.getStartZone().getBottomRightConner();

    Warrior warrior1p1 = wrapper.createWarrior(gameContext.getContextId(), player1.getDescription(), Viking.CLASS_NAME
            , new Coords(player1coord.getX(), player1coord.getY())).getResult();
    Warrior warrior1p2 = wrapper.createWarrior(gameContext.getContextId(), player1.getDescription(), Skeleton.CLASS_NAME
            , new Coords(player2coord.getX(), player2coord.getY())).getResult();


  }
}
