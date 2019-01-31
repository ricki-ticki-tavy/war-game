package core.game;

import api.core.Context;
import api.core.Core;
import api.core.Result;
import api.entity.warrior.Warrior;
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
import core.system.game.WarriorHeapElement;
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
  private AtomicBoolean gameRan = new AtomicBoolean(false);
  protected AtomicBoolean deleting = new AtomicBoolean(false);

  //===================================================================================================
  //===================================================================================================

  public ContextImpl(Player owner) {
    this.contextOwner = owner;
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
            : ResultImpl.success(this).doIfSuccess(context -> deleting.set(true));
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
              , new EventDataContainer(result, contextOwner, mapMetadata == null ? gameName : mapMetadata.name, hidden)
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
  public Result<Warrior> createWarrior(String userName, String warriorClassName, Coords coords) {
    return ifGameRan(false)
            .map(thisContext1 -> thisContext1.ifGameDeleting(false))
            .map(fineContext -> fineContext.findUserByName(userName))
            .map(player -> getLevelMap().createWarrior(player, warriorClassName, coords));
  }
  //===================================================================================================

  public Player getContextOwner() {
    return contextOwner;
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

  public Result<Context> ifGameRan(boolean state) {
    return isGameRan() == state ? ResultImpl.success(this) : ResultImpl.fail(
            state
                    ? CONTEXT_NOT_IN_GAME_RAN_STATE.getError(getGameName(), getContextId())
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

    fireGameEvent(null, GAME_CONTEXT_CREATED, new EventDataContainer(ResultImpl.success(this), getContextOwner()), null);
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
                    .reduce(0, (acc, chg) -> acc++, (a, b) -> a + b) == getLevelMap().getMaxPlayerCount();

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

  // TODO пополняется

  /**
   * Передать ход следующему игроку
   */
  private void nextPlayerRound() {
    // зачистить транзакционные данные игрока
    getLevelMap().getGameProcessData().playerTransactionalData.clear();
  }
  //===================================================================================================


  /**
   * проверяет может ли ходящий сейчас игрок переместить данный юнит
   *
   * @param warrior
   */
  public Result<Warrior> ifUnitCanMove(Warrior warrior) {
    Result result;
    // если нет этого юнита в списке юнитов, уже задействованных в данном ходе и в нем еще есть место, то можно ходить
    // новыйм юнитом
    WarriorHeapElement warriorHeapElement = getLevelMap().getGameProcessData().playerTransactionalData.get(warrior.getId());
    if (warriorHeapElement == null) {
      result = getLevelMap().getGameProcessData().playerTransactionalData.size() < getLevelMap().getMaxPlayerCount()
              // это новый юнит в данном ходу, но двигать его можно
              ? ResultImpl.success(warrior)
              : ResultImpl.fail(PLAYER_UNIT_MOVES_ON_THIS_TURN_ARE_EXCEEDED.getError(warrior.getOwner().getId(), String.valueOf(getLevelMap().getMaxPlayerCount())));
    } else {
      // юнит уже в списке тех, что делали движение  данном ходе
      result = warriorHeapElement.isMoveLocked()
              // новые координаты воина уже заморожены и не могут быть отменены
              // Воин %s (id %s) игрока %s в игре %s (id %s) не может более выполнить перемещение в данном ходе
              ? ResultImpl
              .fail(WARRIOR_CAN_T_MORE_MOVE_ON_THIS_TURN.getError(
                      warrior.getWarriorBaseClass().getTitle()
                      , warrior.getId()
                      , warrior.getOwner().getId()
                      , getGameName()
                      , getContextId()))
              : ResultImpl.success(warrior);
    }
    return result;
  }
  //===================================================================================================

  // TODO пополняется

  /**
   * возвращает имеет ли возможность текущий игрок двигать определенный юнит
   */
  private Result<Warrior> ifUserCanMoveWarriorAtThisTurn(Warrior warrior) {
    // если игра не запущена, то может
    return ifGameRan(false)
            .map(context -> ResultImpl.success(warrior))
            // игра запущена. Допустимо ходить только в свой ход и только доступным юнитом
            .mapFail(result -> ifPlayerOwnsThisTurn(warrior.getOwner())
                    // проверим всеми ли юнитами, которыми допустимо, ходил игрок или есть еще ходы
                    .map(player -> ifUnitCanMove(warrior)));
  }
  //===================================================================================================

  private Result<Player> ifPlayerOwnsThisTurn(Player player) {
    return player == getPlayerOwnsTheTurn() ? ResultImpl.success(player) : ResultImpl.fail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND.getError(player.getId(), getContextId()));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(String userName, String warriorId, Coords coords) {
    return ifGameDeleting(false)
            .map(fineContext -> fineContext.findUserByName(userName)
                    // если игра запущена, то двигать фигуры можно только в свой ход
                    .map(player -> !fineContext.isGameRan() || getPlayerOwnsTheTurn() == player
                            ? getLevelMap().moveWarriorTo(player, warriorId, coords)
                            : ResultImpl.fail(PLAYER_IS_NOT_OWENER_OF_THIS_ROUND.getError(userName, "перемещение юнита " + warriorId))))
            ;
  }
  //===================================================================================================

  @Override
  public Result<Coords> whatIfMoveWarriorTo(String userName, String warriorId, Coords coords) {
    return ifGameDeleting(false)
            .map(fineContext -> fineContext.findUserByName(userName)
                    // если игра запущена, то двигать фигуры можно только в свой ход
                    .map(player -> !fineContext.isGameRan() || getPlayerOwnsTheTurn() == player
                            ? getLevelMap().whatIfMoveWarriorTo(player, warriorId, coords)
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
  public Result<Player> getPlayerOwnsTheTurn() {
    return ifGameDeleting(false)
            .map(context -> context.ifGameRan(true))
            .map(context -> getLevelMap().getGameProcessData().getPlayerOwnsTheThisTurn());
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
  public Result<Player> nextTurn(String userName) {
    return             ifGameRan(true)
            .map(fineContext ->  fineContext.findUserByName(userName))
            .map(player -> getLevelMap().nextTurn(player));
  }
  //===================================================================================================
}
