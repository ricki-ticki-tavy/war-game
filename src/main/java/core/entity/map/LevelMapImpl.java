package core.entity.map;

import api.core.Context;
import api.core.Result;
import api.entity.ability.Modifier;
import api.entity.warrior.HasCoordinates;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.enums.LifeTimeUnit;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.game.GameProcessData;
import core.system.ActiveCoords;
import core.system.ResultImpl;
import core.system.error.GameErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.*;

/**
 * Игровая карта
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LevelMapImpl implements LevelMap {

  private static final Logger logger = LoggerFactory.getLogger(LevelMap.class);

  private String name;
  private String description;
  private int simpleUnitSize;
  private int width;
  private int height;
  private int maxPlayersCount;
  private List<Rectangle> playerStartZones;
  private Map<String, Player> players;
  private Context context;
  private boolean loaded = false;

  private GameProcessData gameProcessData;

  @Autowired
  BeanFactory beanFactory;

  //===================================================================================================
  //===================================================================================================

  @Override
  public void beginGame() {
    gameProcessData = new GameProcessData();
    // сохраним список пользователей, начавших игру
    getPlayers().stream()
            .forEach(player -> {
              gameProcessData.frozenListOfPlayers.put(gameProcessData.frozenListOfPlayers.size(), player);
              // всех подготовим к защите
              player.prepareToDefensePhase();
            });
    // первого игрока готовим к атаке
    getPlayerOwnsThisTurn()
            .map(player -> player.prepareToAttackPhase());
  }
  //===================================================================================================

  @Override
  public GameProcessData getGameProcessData() {
    return gameProcessData;
  }
  //===================================================================================================

  @Override
  public String getName() {
    return name;
  }
  //===================================================================================================

  @Override
  public String getDescription() {
    return description;
  }
  //===================================================================================================

  @Override
  public void init(Context gameContext, LevelMapMetaDataXml levelMapMetaData) {
    logger.info("map initializing started \"" + levelMapMetaData.name + "\" in context " + gameContext.getContextId());
    this.context = gameContext;
    this.name = levelMapMetaData.name;
    this.description = levelMapMetaData.description;
    this.simpleUnitSize = levelMapMetaData.simpleUnitSize;
    this.width = levelMapMetaData.width;
    this.height = levelMapMetaData.height;
    this.maxPlayersCount = levelMapMetaData.maxPlayersCount;
    playerStartZones = new LinkedList();

    levelMapMetaData.playerStartZones.stream().forEach(rectangle ->
            playerStartZones.add(new Rectangle(new Coords(rectangle.topLeftConner.x, rectangle.topLeftConner.y)
                    , new Coords(rectangle.bottomRightConner.x, rectangle.bottomRightConner.y)))
    );

    players = new ConcurrentHashMap<>(maxPlayersCount);

    loaded = true;
    logger.info("map initializing succeed \"" + levelMapMetaData.name + "\" in context " + gameContext.getContextId());
  }
  //===================================================================================================

  @Override
  public List<Rectangle> getPlayerStartZone(String playerId) {
    return playerStartZones;
  }
  //===================================================================================================

  @Override
  public int getMaxPlayerCount() {
    return maxPlayersCount;
  }
  //===================================================================================================

  @Override
  public int getWidthInUnits() {
    return width;
  }
  //===================================================================================================

  @Override
  public int getHeightInUnits() {
    return height;
  }
  //===================================================================================================

  @Override
  public List<Warrior> getWarriors(Coords center, int radius) {
    return null;
  }
  //===================================================================================================

  @Override
  public Result<Warrior> createWarrior(Player player, String warriorBaseClassName, Coords coords) {
    if (player.getWarriors().size() >= context.getGameRules().getMaxStartCreaturePerPlayer()) {
      return ResultImpl.fail(GameErrors.PLAYER_UNITS_LIMIT_EXCEEDED.getError(player.getTitle()
              , String.valueOf(context.getGameRules().getMaxStartCreaturePerPlayer())));
    }
    // TODO сделать проверку координат
    return player.createWarrior(warriorBaseClassName, coords)
            .doIfSuccess(warrior -> gameProcessData.allWarriorsOnMap.put(warrior.getId(), warrior));
  }
  //===================================================================================================

  @Override
  public Result connectPlayer(Player player) {
    Result result = null;
    if (players.containsKey(player.getId())) {
      // игрок есть.
      result = ResultImpl.success(player);
      context.fireGameEvent(null, PLAYER_RECONNECTED, new EventDataContainer(player, result), null);
    } else {
      if (maxPlayersCount >= players.size()) {
        player.clear();
        if ((result = player.replaceContext(context)).isSuccess()) {
          players.put(player.getId(), player);
          player.setStartZone(playerStartZones.get(players.size() - 1));
          result = ResultImpl.success(player);
        }
      } else {
        result = ResultImpl.fail(USER_CONNECT_TO_CONTEXT_TOO_MANY_USERS.getError());
      }
      context.fireGameEvent(null, PLAYER_CONNECTED, new EventDataContainer(player, result), null);
    }
    return result;
  }
  //===================================================================================================

  @Override
  public Result disconnectPlayer(Player player) {
    Result result;
    if (players.containsKey(player.getId())) {
      players.remove(player.getId());
      player.replaceContextSilent(null);
      result = ResultImpl.success(player);
      context.fireGameEvent(null, PLAYER_DISCONNECTED, new EventDataContainer(player, result), null);

      // если это создатель игры, и контекст УЖЕ не в режиме удаления, то
      if (!context.isDeleting() && context.getContextOwner().equals(player)) {
        // выкидываем всех игроков
        players.values().stream().forEach(this::disconnectPlayer);
        // Удаляем контекст
        context.getCore().removeGameContext(context.getContextId());
      }
    } else {
      result = ResultImpl.fail(USER_DISCONNECT_NOT_CONNECTED.getError());
      context.fireGameEvent(null, PLAYER_DISCONNECTED, new EventDataContainer(player, result), null);
    }
    return result;
  }
  //===================================================================================================

  @Override
  public List<Player> getPlayers() {
    return new LinkedList(players.values());
  }
  //===================================================================================================

  @Override
  public int getSimpleUnitSize() {
    return simpleUnitSize;
  }
  //===================================================================================================

  @Override
  public Player getPlayer(String playerId) {
    return players.get(playerId);
  }
  //===================================================================================================

  @Override
  public boolean isLoaded() {
    return loaded;
  }
  //===================================================================================================

  @Override
  public Result<Warrior> removeWarrior(Player player, String warriorId) {
    return player.removeWarrior(warriorId)
            .doIfSuccess(warrior -> gameProcessData.allWarriorsOnMap.remove(warrior.getId()));
  }
  //===================================================================================================

  @Override
  public Result<Warrior> moveWarriorTo(Player player, String warriorId, Coords newCoords) {
    return player.findWarriorById(warriorId)
            .map(warrior -> whatIfMoveWarriorTo(player, warriorId, newCoords)
                    .map(coords -> {
                      // если юнита нет среди тех, которые были затронуты в этот ход, то добавить в этот список
                      gameProcessData.playerTransactionalData.computeIfAbsent(warrior.getId()
                              , id -> warrior);
                      // кинем сообщение, что юнит перемещен
                      Result result = warrior.moveWarriorTo(coords);
                      context.fireGameEvent(null, WARRIOR_MOVED
                              , new EventDataContainer(this, result), null);
                      return result;
                    }));
  }
  //===================================================================================================

  @Override
  public Result<Coords> whatIfMoveWarriorTo(Player player, String warriorId, Coords coords) {
    return player.findWarriorById(warriorId)
            .map(warrior -> innerWhatIfMoveWarriorTo(player, warrior, coords));
  }
  //===================================================================================================

  public Result<ActiveCoords> getWarriorSOriginCoords(Warrior warrior) {
    Warrior touchedWarrior = gameProcessData.playerTransactionalData.get(warrior.getId());
    return touchedWarrior == null || touchedWarrior.isMoveLocked() || !touchedWarrior.isRollbackAvailable()
            ? ResultImpl.success(new ActiveCoords(warrior.getCoords()))
            : ResultImpl.success(touchedWarrior.getOriginalCoords());
  }
  //===================================================================================================

  public Result<Player> ifPlayerIsTurnOwner(Player player) {
    return getPlayerOwnsThisTurn()
            .map(playerOwnedThisTurn -> player == playerOwnedThisTurn
                    ? ResultImpl.success(player)
                    : ResultImpl.fail(PLAYER_CAN_T_PASS_THE_TURN_PLAYER_IS_NOT_TURN_OWNER.getError(
                    player.getId(), context.getGameName(), context.getContextId(), playerOwnedThisTurn.getId())));
  }
  //===================================================================================================

  @Override
  public Result<Influencer> addInfluenceToWarrior(Player player, String warriorId, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    return player.addInfluenceToWarrior(warriorId, modifier, source, lifeTimeUnit, lifeTime);
  }
  //===================================================================================================

  @Override
  public Result<List<Influencer>> getWarriorSInfluencers(Player player, String warriorId) {
    return player.findWarriorById(warriorId)
            .map(warrior -> warrior.getWarriorSInfluencers());
  }
  //===================================================================================================


  public Result<Player> getPlayerOwnsThisTurn(){
    return ResultImpl.success(gameProcessData.frozenListOfPlayers.get(gameProcessData.indexOfPlayerOwnsTheTurn));
  }//===================================================================================================

  /**
   * Сделать следующего игрока по кругу текущим владельцем хода
   * @return
   */
  private Result<Player> switchToNextPlayerTurn(){
    if (gameProcessData.indexOfPlayerOwnsTheTurn.incrementAndGet() >= gameProcessData.frozenListOfPlayers.size()) {
      gameProcessData.indexOfPlayerOwnsTheTurn.set(0);
      context.fireGameEvent(null, ROUND_FULL, new EventDataContainer(this), null);
    }
    return getPlayerOwnsThisTurn();
  }
//===================================================================================================

  @Override
  public Result<Player> nextTurn(Player player) {
    return
            // проверим этот ли игрок сейчас владеет ходом
            ifPlayerIsTurnOwner(player)
                    // переведем в защиту уходящего игрока
                    .map(finePlayer -> finePlayer.prepareToDefensePhase())
                    // сменим игрока
                    .map(oldPlayer -> switchToNextPlayerTurn())
                    .map(newPlayer -> {
                      // зачистить транзакционные данные игрока
                      gameProcessData.playerTransactionalData.clear();
                      // подготовим нового игрока к ходу
                      return newPlayer.prepareToAttackPhase();
                    });
  }
  //===================================================================================================

  @Override
  public int getWarriorSMoveCost(Warrior warrior) {
    // TODO ВОЗМОЖНО перенести в класс юнита
    return warrior.getAttributes().getResult().getArmorClass().getMoveCost() + warrior.getAttributes().getResult().getDeltaCostMove();
  }
  //===================================================================================================

  /**
   * Собирает всех юнитов карты.
   * @param excludedWarrior
   * @return
   */
  private List<HasCoordinates> getAllUnitsExcludeOneThis(Warrior excludedWarrior) {
    List<HasCoordinates> allWarriors = new ArrayList(gameProcessData.allWarriorsOnMap.values());
    allWarriors.remove(excludedWarrior);
    return allWarriors;
  }
  //===================================================================================================

  private Result<Coords> innerWhatIfMoveWarriorTo(Player player, Warrior warrior, Coords coords) {
    // режим расстановки. ставим не выходя за периметр
    return getWarriorSOriginCoords(warrior) // получаем координаты от которых будем двигаться
            .map(activeCoords -> activeCoords.tryToMove(coords, getAllUnitsExcludeOneThis(warrior), context.getGameRules().getWarriorSize()
                    // очки действия, оставшиеся в данном ходу для перемещения
                    , warrior.getWarriorSActionPoints(true)
                            // делим на стоимость
                            * simpleUnitSize / getWarriorSMoveCost(warrior)
                    , context.isGameRan() ? null : player.getStartZone()));
  }
  //===================================================================================================

  // TODO not implemented. !!!!!!!   correct it
  @Override
  public Result<Context> ifNewWarriorSCoordinatesAreAvailable(Warrior warrior, Coords newCoords) {
    return ResultImpl.success(this);
  }
  //===================================================================================================


}
