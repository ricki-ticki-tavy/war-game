package core.game;

import api.core.GameContext;
import api.core.Core;
import api.entity.base.BaseEntityHeader;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.EventType;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GameContextImpl implements GameContext {

  public static GameContext NULL_GAME_CONTEXT = new GameContextImpl();

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
  private String gameName;
  private boolean hidden;

  @Override
  public String getGameName() {
    return gameName;
  }

  @Override
  public boolean isHidden() {
    return hidden;
  }

  @Override
  public String getContextId() {
    return contextId;
  }

  @Override
  public Core getCore() {
    return core;
  }

  @Override
  public void fireGameEvent(GameEvent gameEvent) {
    core.fireEvent(gameEvent);
  }

  @Override
  public void fireGameEvent(GameEvent causeEvent, EventType eventType, BaseEntityHeader source, Map<String, Object> params) {
    fireGameEvent(new GameEvent(this, causeEvent, eventType, source, params));
  }


  @Override
  public LevelMap getLevelMap() {
    return levelMap;
  }

  @Override
  public void loadMap(String userGameCreator, GameRules gameRules, InputStream map, String gameName, boolean hidden) {
    try {
      this.gameName = gameName;
      this.hidden = hidden;

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
  public Player connectPlayer(String playerName, String playerSessionId) {
    return levelMap.connectPlayer(playerName, playerSessionId);
  }

  @Override
  public Warrior createWarrior(String playerId, Class<? extends WarriorBaseClass> baseWarriorClass) {
    return Optional.ofNullable(levelMap.getPlayer(playerId))
            .map(player -> {
              Warrior warrior = beanFactory.getBean(Warrior.class, beanFactory.getBean(baseWarriorClass), "", false);
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

  @Override
  public String subscribeEvent(List<EventType> eventTypes, Consumer<GameEvent> consumer) {
    return core.subscribeEvent(this, eventTypes, consumer);
  }
}
