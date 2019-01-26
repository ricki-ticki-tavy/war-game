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
import core.system.error.GameError;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.*;

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
  private final Map<Integer, Player> frozenListOfPlayers = new ConcurrentHashMap<>(5);
  private AtomicBoolean gameRan = new AtomicBoolean(false);
  protected AtomicBoolean deleting = new AtomicBoolean(false);

  private GameProcessData gameProcessData;

  public ContextImpl(Player owner) {
    this.contextOwner = owner;
  }

  @Override
  public Result<List<String>> getFrozenListOfPlayers() {
    return gameRan.get()
            ? ResultImpl.success(frozenListOfPlayers.values().stream().map(player -> player.getId()).collect(Collectors.toList()))
            : ResultImpl.fail(CONTEXT_GAME_NOT_STARTED.getError(getGameName(), getContextId()));
  }

  @Override
  public boolean isGameRan() {
    return gameRan.get();
  }

  @Override
  public boolean isDeleting() {
    return deleting.get();
  }

  @Override
  public Result<Context> initDelete() {
    return deleting.get()
            ? ResultImpl.fail(CONTEXT_DELETE_ALREADY_IN_PROGRESS.getError(getGameName(), getContextId()))
            : ResultImpl.success(this).doIfSuccess(context -> deleting.set(true));
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
    return ifGameRan(false).map(fineContext -> {
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
    });
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
    return ifGameRan(false)
            .map(fineContext -> {
              Warrior warrior = beanFactory.getBean(Warrior.class, this, player
                      , beanFactory.getBean(baseWarriorClass), "", false);
              return levelMap.addWarrior(player, coords, warrior);
            });
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

  public Result<Context> ifGameRan(boolean state) {
    return isGameRan() == state ? ResultImpl.success(this) : ResultImpl.fail(
            state
                    ? CONTEXT_NOT_IN_GAME_RAN_STATE.getError(getGameName(), getContextId())
                    : CONTEXT_IN_GAME_RAN_STATE.getError(getGameName(), getContextId()));
  }

  public Result<Context> ifDeleting(boolean state) {
    return isDeleting() == state ? ResultImpl.success(this) : ResultImpl.fail(
            state
                    ? CONTEXT_IS_NOT_IN_DELETING_STATE.getError(getGameName(), getContextId())
                    : CONTEXT_IS_IN_DELETING_STATE.getError(getGameName(), getContextId()));
  }

  @Override
  public Result<Player> setPlayerReadyToGameState(Player player, boolean readyToGame) {
    return ifGameRan(false)
            .map(fineContext -> fineContext.ifDeleting(false)
            .map(context -> player.setReadyToPlay(readyToGame)));
  }

  @PostConstruct
  public void firstCry() {
    // Подписаться на события изменения состава истатуса игроков
    subscribeEvent(this::checkForStartGame, PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS, PLAYER_CONNECTED
            , PLAYER_DISCONNECTED, PLAYER_RECONNECTED, GAME_CONTEXT_REMOVED);

    fireGameEvent(null, GAME_CONTEXT_CREATED, new EventDataContainer(ResultImpl.success(this), getContextOwner()), null);
  }

  /**
   * Начать игру
   * @return
   */
  private Result<Context> beginGame(){
    gameRan.set(true);
    // сохраним список пользователей, начавших игру
    getLevelMap().getPlayers().stream().forEach(player -> frozenListOfPlayers.put(frozenListOfPlayers.size(), player));
    gameProcessData = new GameProcessData();
    Result result = ResultImpl.success(this);
    fireGameEvent(null, GAME_CONTEXT_GAME_HAS_BEGAN, new EventDataContainer(this, result), null);
    return result;
  }

  private void checkForStartGame(Event event) {
    // без разницы какое событие произошло из касающихся смены состава и качества игроков - действия будут одинаковы
    boolean newState =
            getLevelMap().getPlayers().size() == getLevelMap().getMaxPlayerCount()
                    && getLevelMap().getPlayers().stream().filter(foundPlayer -> foundPlayer.isReadyToPlay())
                    .reduce(0, (acc, chg) -> acc++, (a, b) -> a + b) == getLevelMap().getMaxPlayerCount();

    // Если текущий статус игры отличен от нового, то будем его менять, если можно
    if (newState != isGameRan()) {
      (isGameRan()
              ? ResultImpl.fail(CONTEXT_IN_GAME_RAN_STATE.getError(getGameName(), getContextId()))
              : ResultImpl.success(this))
              .map(fineContext -> ((ContextImpl)fineContext).beginGame())
              .doIfFail(error -> logger.error(((GameError)error).getMessage()));
    }
  }

}
