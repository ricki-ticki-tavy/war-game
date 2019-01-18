package core.game;

import api.core.Context;
import api.core.Core;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.game.Coords;
import api.game.GameEvent;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.map.metadata.LevelMapMetaData;
import core.system.error.GameErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GameContext implements Context {

  private static final Logger logger = LoggerFactory.getLogger(GameContext.class);
  private String contextId = UUID.randomUUID().toString();

  @Autowired
  private LevelMap levelMap;

  @Autowired
  private Core core;

  @Autowired
  private BeanFactory beanFactory;

  private String userGameCreator;
  private GameRules gameRules;

  @Override
  public String getContextId() {
    return contextId;
  }

  @Override
  public Core getCore() {
    return core;
  }

  @Override
  public boolean fireGameEvent(GameEvent gameEvent) {
    return false;
  }

  @Override
  public LevelMap getLevelMap() {
    return null;
  }

  @Override
  public void loadMap(String userGameCreator, GameRules gameRules, InputStream map) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(LevelMapMetaData.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      LevelMapMetaData mapMetadata = (LevelMapMetaData) jaxbUnmarshaller.unmarshal(map);

      levelMap.init(this, mapMetadata);

      this.userGameCreator = userGameCreator;
      this.gameRules = gameRules;
    } catch (JAXBException e) {
      logger.error("Error load game.xml", e);
      throw new RuntimeException(e);
    }

  }

  @Override
  public Player connectPlayer(String playerSessionId) {
    return levelMap.connectPlayer(playerSessionId);
  }

  @Override
  public Warrior createWarrior(String playerId, Class<? extends WarriorBaseClass> baseWarriorClass) {
    return Optional.ofNullable(levelMap.getPlayer(playerId))
            .map(player -> {
              Warrior warrior = (Warrior)beanFactory.getBean(baseWarriorClass);
              levelMap.addWarrior(playerId, player.getStartZone().getTopLeftConner(), warrior);
              return warrior;
            })
            .orElseThrow(() -> GameErrors.GAME_ERROR_UNKNOWN_USER_UID.getError(playerId));
  }

  public String getUserGameCreator() {
    return userGameCreator;
  }

  public GameRules getGameRules() {
    return gameRules;
  }
}
