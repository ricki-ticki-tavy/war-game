package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.EventType;
import api.game.Coords;
import api.game.Event;
import api.game.EventDataContainer;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.system.ResultImpl;
import core.system.error.GameErrors;
import core.system.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static api.enums.EventType.GAME_CONTEXT_CREATED;
import static api.enums.EventType.GAME_CONTEXT_LOAD_MAP;
import static core.system.error.GameErrors.MAP_IS_NOT_LOADED;
import static core.system.error.GameErrors.MAP_LOAD_ERROR;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContextImpl implements Context {

  public static Context NULL_GAME_CONTEXT = new ContextImpl(null);

  private static final Logger logger = LoggerFactory.getLogger(Context.class);
  private String contextId = UUID.randomUUID().toString();

  @Autowired
  private LevelMap levelMap;

  @Autowired
  private Core core;

  @Autowired
  private BeanFactory beanFactory;

  private Player contextOwner;
  private GameRules gameRules;
  private String gameName;
  private boolean hidden;

  public ContextImpl(Player owner) {
    this.contextOwner = owner;
  }

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
  public void fireGameEvent(Event gameEvent) {
    core.fireEvent(gameEvent);
  }

  @Override
  public void fireGameEvent(Event causeEvent, EventType eventType, EventDataContainer source, Map<String, Object> params) {
    fireGameEvent(new EventImpl(this, causeEvent, eventType, source, params));
  }


  @Override
  public LevelMap getLevelMap() {
    return levelMap;
  }

  @Override
  public Result loadMap(GameRules gameRules, InputStream map, String gameName, boolean hidden) {
    Result result = null;
    LevelMapMetaDataXml mapMetadata = null;
    try {
      this.gameName = gameName;
      this.hidden = hidden;

      JAXBContext jaxbContext = JAXBContext.newInstance(LevelMapMetaDataXml.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      mapMetadata = (LevelMapMetaDataXml) jaxbUnmarshaller.unmarshal(map);

      levelMap.init(this, mapMetadata);

      this.gameRules = gameRules;
      result = ResultImpl.success(this);
    } catch (JAXBException e) {
      result = ResultImpl.fail(MAP_LOAD_ERROR.getError(e.getMessage() == null ? "NPE" : e.getMessage()));
    }
    fireGameEvent(null, GAME_CONTEXT_LOAD_MAP
            , new EventDataContainer(result, contextOwner, mapMetadata == null ? gameName : mapMetadata.name, hidden)
            , null);
    return result;
  }

  @Override
  public Result connectPlayer(Player player) {
    if (levelMap != null) {
      return levelMap.connectPlayer(player);
    } else {
      return ResultImpl.fail(MAP_IS_NOT_LOADED.getError());
    }
  }

  @Override
  public Result disconnectPlayer(Player player) {
    if (levelMap != null) {
      return levelMap.disconnectPlayer(player);
    } else {
      return ResultImpl.fail(MAP_IS_NOT_LOADED.getError());
    }
  }


  @Override
  public Result<Warrior> createWarrior(Player player, Class<? extends WarriorBaseClass> baseWarriorClass, Coords coords) {
    Warrior warrior = beanFactory.getBean(Warrior.class, this, player, beanFactory.getBean(baseWarriorClass), "", false);
    return levelMap.addWarrior(player, coords, warrior);
  }

  public Player getContextOwner() {
    return contextOwner;
  }

  public GameRules getGameRules() {
    return gameRules;
  }

  @Override
  public String subscribeEvent(Consumer<Event> consumer, EventType... eventTypes) {
    return core.subscribeEvent(this, consumer, eventTypes);
  }

  @PostConstruct
  public void firstCry() {
    fireGameEvent(null, GAME_CONTEXT_CREATED, new EventDataContainer(ResultImpl.success(this), getContextOwner()), null);
  }
}
