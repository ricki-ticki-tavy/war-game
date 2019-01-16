package core.game;

import api.base.i.core.Core;
import api.rule.game.GamePreparedMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Game implements Core{

  private static final Logger logger = LoggerFactory.getLogger(Game.class);

  private GamePreparedMetadata gameMetadata;

  @Autowired
  GameMetadataLoader gameMetadataLoader;

  @PostConstruct
  public void init(){
    gameMetadata = gameMetadataLoader.loadGameMetadata();

  }

  @Override
  //TODO
  public int getRandom(int min, int max) {
    return 0;
  }
}
