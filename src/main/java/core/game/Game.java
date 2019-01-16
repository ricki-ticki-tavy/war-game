package core.game;

import api.core.Context;
import api.core.Core;
import api.enums.MapTypeEnum;
import api.game.map.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Game {
  @Autowired
  Core  core;

  @PostConstruct
  public void testGame(){
    Context context = core.createGame(MapTypeEnum.TWO_PLAYERS);
    Player player = context.connectPlayer("1234");
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
