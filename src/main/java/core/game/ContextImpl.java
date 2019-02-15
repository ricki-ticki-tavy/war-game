package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.stuff.Artifact;
import api.game.ability.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.enums.EventType;
import api.geo.Coords;
import api.core.Event;
import api.core.EventDataContainer;
import api.game.action.InfluenceResult;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.GameRules;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.system.ResultImpl;
import core.system.error.GameError;
import core.system.event.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

  private Player contextCreator;
  private GameRules gameRules;
  private String gameName;
  private boolean hidden;
  private AtomicBoolean gameRan = new AtomicBoolean(false);
  protected AtomicBoolean deleting = new AtomicBoolean(false);

  //===================================================================================================
  //===================================================================================================

  public ContextImpl(Player creator) {
    this.contextCreator = creator;
  }
  //===================================================================================================

  @Override
  public Result<List<String>> getFrozenListOfPlayers() {
    return gameRan.get()
            ? ResultImpl.success(getLevelMap().getGameProcessData().frozenListOfPlayers.values().stream()
            .map(player -> player.getId()).collect(Collectors.toList()))
            : ResultImpl.fail(CONTEXT_GAME_NOT_STARTED.getError(getGameName(), getContextId()));
  }
  //===================================================================================================

  @Override
  public boolean isGameRan() {
    return gameRan.get();
  }
  //===================================================================================================

  @Override
  public boolean isDeleting() {
    return deleting.get();
  }
  //===================================================================================================

  @Override
  public Result<Context> initDelete() {
    return deleting.get()
            ? ResultImpl.fail(CONTEXT_DELETE_ALREADY_IN_PROGRESS.getError(getGameName(), getContextId()))
            : ResultImpl.success(this).peak(context -> deleting.set(true));
  }
  //===================================================================================================

  @Override
  public String getGameName() {
    return gameName;
  }
  //===================================================================================================

  @Override
  public boolean isHidden() {
    return hidden;
  }
  //===================================================================================================

  @Override
  public String getContextId() {
    return contextId;
  }
  //===================================================================================================

  @Override
  public Core getCore() {
    return core;
  }
  //===================================================================================================

  @Override
  public void fireGameEvent(Event gameEvent) {
    core.fireEvent(gameEvent);
  }
  //===================================================================================================

  @Override
  public void fireGameEvent(Event causeEvent, EventType eventType, EventDataContainer source, Map<String, Object> params) {
    fireGameEvent(new EventImpl(this, causeEvent, eventType, source, params));
  }
  //===================================================================================================


  @Override
  public LevelMap getLevelMap() {
    return levelMap;
  }
  //===================================================================================================

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
              , new EventDataContainer(result, contextCreator, mapMetadata == null ? gameName : mapMetadata.name, hidden)
              , null);
      return result;
    });
  }
  //===================================================================================================

  @Override
  public Result connectPlayer(Player player) {
    if (levelMap != null) {
      return levelMap.connectPlayer(player);
    } else {
      return ResultImpl.fail(MAP_IS_NOT_LOADED.getError());
    }
  }
  //===================================================================================================

  @Override
  public Result disconnectPlayer(Player player) {
    if (levelMap != null) {
      return levelMap.disconnectPlayer(player);
    } else {
      return ResultImpl.fail(MAP_IS_NOT_LOADED.getError());
    }
  }
  //===================================================================================================

  @Override
  public Result<Warrior> findWarriorById(String warriorId) {
    return ifGameDeleting(false)
            .map(fineContext -> getLevelMap().findWarriorById(warriorId));
  }
  //===================================================================================================


  @Override
  public Result<Warrior> createWarrior(String userName, String warriorClassName, Coords coords) {
    return ifGameRan(false)
            .map(thisContext1 -> thisContext1.ifGameDeleting(false))
            .map(fineContext -> fineContext.findUserByName(userName))
            .map(player -> getLevelMap().createWarrior(player, warriorClassName, coords));
  }
  //===================================================================================================

  @Override
  public Result<InfluenceResult> attackWarrior(String userName, String attackerWarriorId, String targetWarriorId, String weaponId) {
    return ifGameDeleting(false)
            .map(fineContext -> fineContext.findUserByName(userName))
            .map(player -> getLevelMap().attackWarrior(player, attackerWarriorId, targetWarriorId, weaponId));
  }
  //===================================================================================================

  public Player getContextCreator() {
    return contextCreator;
  }
  //===================================================================================================

  public GameRules getGameRules() {
    return gameRules;
  }
  //===================================================================================================

  @Override
  public String subscribeEvent(Consumer<Event> consumer, EventType... eventTypes) {
    return core.subscribeEvent(this, consumer, eventTypes);
  }
  //===================================================================================================

  @Override
  public Result<Context> unsubscribeEvent(String consumerId, EventType... eventTypes) {
    return core.unsubscribeEvent(this, consumerId, eventTypes);
  }
  //===================================================================================================

  public Result<Context> ifGameRan(boolean state) {
    return isGameRan() == state ? ResultImpl.success(this) : ResultImpl.fail(
            state
                    ? CONTEXT_GAME_NOT_STARTED.getError(getGameName(), getContextId())
                    : CONTEXT_IN_GAME_RAN_STATE.getError(getGameName(), getContextId()));
  }
  //===================================================================================================

  public Result<Context> ifGameDeleting(boolean state) {
    return isDeleting() == state ? ResultImpl.success(this) : ResultImpl.fail(
            state
                    ? CONTEXT_IS_NOT_IN_DELETING_STATE.getError(getGameName(), getContextId())
                    : CONTEXT_IS_IN_DELETING_STATE.getError(getGameName(), getContextId()));
  }
  //===================================================================================================

  @Override
  public Result<Player> setPlayerReadyToGameState(Player player, boolean readyToGame) {
    return ifGameRan(false)
            .map(fineContext -> fineContext.ifGameDeleting(false)
                    .map(context -> player.setReadyToPlay(readyToGame)));
  }
  //===================================================================================================

  @PostConstruct
  public void firstCry() {
    // Подписаться на события изменения состава истатуса игроков
    subscribeEvent(this::checkForStartGame, PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS, PLAYER_CONNECTED
            , PLAYER_DISCONNECTED, PLAYER_RECONNECTED, GAME_CONTEXT_REMOVED);

    fireGameEvent(null, GAME_CONTEXT_CREATED, new EventDataContainer(ResultImpl.success(this), getContextCreator()), null);
  }
  //===================================================================================================

  /**
   * Начать игру
   *
   * @return
   */
  private Result<Context> beginGame() {
    gameRan.set(true);
    getLevelMap().beginGame();
    Result result = ResultImpl.success(this);
    fireGameEvent(null, GAME_CONTEXT_GAME_HAS_BEGAN, new EventDataContainer(this, result), null);
    return result;
  }
  //===================================================================================================

  private void checkForStartGame(Event event) {
    // без разницы какое событие произошло из касающихся смены состава и качества игроков - действия будут одинаковы
    boolean newState =
            getLevelMap().getPlayers().size() == getLevelMap().getMaxPlayerCount()
                    && getLevelMap().getPlayers().stream().filter(foundPlayer -> foundPlayer.isReadyToPlay())
                    .count() == getLevelMap().getMaxPlayerCount();

    // Если текущий статус игры отличен от нового, то будем его менять, если можно
    if (newState != isGameRan()) {
      (isGameRan()
              ? ResultImpl.fail(CONTEXT_IN_GAME_RAN_STATE.getError(getGameName(), getContextId()))
              : ResultImpl.success(this))
              .map(fineContext -> ((ContextImpl) fineContext).beginGame())
              .doIfFail(error -> logger.error(((GameError) error).getMessage()));
    }
  }
  //===================================================================================================


  // TODO not implemented. !!!!!!!   correct it
  @Override
  public Result<Context> ifNewWarriorSCoordinatesAreAvailable(Warrior warrior, Coords newCoords) {
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> ifWarriorCanActsAtThisTurn(String userName, String warriorId) {
    return ifGameDeleting(false)
            .map(fineContext -> fineContext.findUserByName(userName))
            .map(player -> getLevelMap().ifWarriorCanActsAtThisTurn(player, warriorId));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(String userName, String warriorId, Coords coords) {
    return ifGameDeleting(false)
            .map(fineContext -> fineContext.findUserByName(userName)
                    .map(player -> fineContext.getLevelMap().moveWarriorTo(player, warriorId, coords)));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> rollbackMove(String userName, String warriorId) {
    return ifGameDeleting(false)
            .map(context -> findUserByName(userName))
            .map(player -> getLevelMap().rollbackMove(player, warriorId));
  }
  //===================================================================================================

  @Override
  public Result<Coords> whatIfMoveWarriorTo(String userName, String warriorId, Coords coords) {
    return ifGameDeleting(false)
            .map(fineContext -> fineContext.findUserByName(userName)
                    // если игра запущена, то двигать фигуры можно только в свой ход
                    .map(player -> !fineContext.isGameRan() || getPlayerOwnsThisTurn() == player
                            ? (Result<Coords>) getLevelMap().whatIfMoveWarriorTo(player, warriorId, coords)
                            : ResultImpl.fail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND.getError(userName, "перемещение юнита " + warriorId))));
  }
  //===================================================================================================

  // TODO перевести все поиски пользователя на этот метод
  @Override
  public Result<Player> findUserByName(String userName) {
    return Optional.ofNullable(getLevelMap().getPlayer(userName))
            .map(player -> ResultImpl.success(player))
            .orElse(ResultImpl.fail(USER_NOT_CONNECTED_TO_THIS_GAME.getError(userName, getGameName(), getContextId())));
  }
  //===================================================================================================

  @Override
  public Result<Player> getPlayerOwnsThisTurn() {
    return ifGameDeleting(false)
            .map(context -> context.ifGameRan(true))
            .map(context -> getLevelMap().getPlayerOwnsThisTurn());
  }
  //===================================================================================================

  @Override
  public Result<Player> ifPlayerOwnsTheTurnEqualsTo(Player player, String... args) {
    return getLevelMap().ifPlayerOwnsThisTurn(player, args);
  }
  //===================================================================================================

  @Override
  public Result<Warrior> removeWarrior(String userName, String warriorId) {
    return ifGameDeleting(false)
            .map(context -> context.ifGameRan(false))
            .map(context -> context.findUserByName(userName)
                    .map(player -> context.getLevelMap().removeWarrior(player, warriorId)));
  }
  //===================================================================================================

  @Override
  public Result<Weapon> giveWeaponToWarrior(String userName, String warriorId, Class<? extends Weapon> weaponClass) {
    return ifGameDeleting(false)
            .map(fineContext -> findUserByName(userName))
            .map(player -> ifGameRan(false)
                    .map(context -> context.getLevelMap().giveWeaponToWarrior(player, warriorId, weaponClass)));
  }
  //===================================================================================================

  @Override
  public Result<Artifact<Warrior>> giveArtifactToWarrior(String userName, String warriorId, Class<? extends Artifact<Warrior>> artifactClass) {
    return ifGameDeleting(false)
            .map(fineContext -> findUserByName(userName))
            .map(player -> ifGameRan(false)
                    .map(context -> context.getLevelMap().giveArtifactToWarrior(player, warriorId, artifactClass)));
  }
  //===================================================================================================

  @Override
  public Result<Artifact<Warrior>> dropArtifactByWarrior(String userName, String warriorId, String artifactInstanceId) {
    return ifGameDeleting(false)
            .map(fineContext -> findUserByName(userName))
            .map(player -> ifGameRan(false)
                    .map(context -> context.getLevelMap().dropArtifactByWarrior(player, warriorId, artifactInstanceId)));
  }
  //===================================================================================================

  @Override
  public Result<Weapon> dropWeaponByWarrior(String userName, String warriorId, String weaponId) {
    return ifGameDeleting(false)
            .map(fineContext -> findUserByName(userName))
            .map(player -> ifGameRan(false)
                    .map(context -> context.getLevelMap().dropWeaponByWarrior(player, warriorId, weaponId)));
  }
  //===================================================================================================

  @Override
  public Result<Weapon> findWeaponById(String userName, String warriorId, String weaponId) {
    return ifGameDeleting(false)
            .map(context -> context.findUserByName(userName))
            .map(player -> getLevelMap().findWeaponById(player, warriorId, weaponId));
  }
  //===================================================================================================

  @Override
  public Result<Player> nextTurn(String userName) {
    return ifGameRan(true)
            .map(fineContext -> fineContext.findUserByName(userName))
            .map(player -> getLevelMap().nextTurn(player));
  }
  //===================================================================================================

  @Override
  public Result<List<Influencer>> getWarriorSInfluencers(String userName, String warriorId) {
    return findUserByName(userName)
            .map(player -> getLevelMap().getWarriorSInfluencers(player, warriorId));
  }
  //===================================================================================================

  @Override
  public int calcDistanceTo(Coords from, Coords to) {
    return (int) Math.round(Math.sqrt((double) ((from.getX() - to.getX()) * (from.getX() - to.getX())
            + (from.getY() - to.getY()) * (from.getY() - to.getY()))));
  }
  //===================================================================================================
}
