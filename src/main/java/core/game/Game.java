package core.game;

import api.core.Context;
import api.core.Core;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Game {
  @Autowired
  Core  core;

  @PostConstruct
  public void testGame(){
    Context context = core.createGame("test"
            , new GameRules(9, 2, 50, 200, 2, 600)
            , this.getClass().getClassLoader().getResourceAsStream("level2.xml"));
    Player player = context.connectPlayer("test");
    if (player != null){

    }
    player = context.connectPlayer("1234");
    if (player != null){

    }
    player = context.connectPlayer("12345");
    if (player != null){

    }
    player = context.connectPlayer("12346");
    if (player != null){

    }
  }
}
