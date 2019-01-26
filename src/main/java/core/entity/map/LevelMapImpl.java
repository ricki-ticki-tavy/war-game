package core.entity.map;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.enums.EventType;
import api.game.Coords;
import api.game.Event;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.LevelMapMetaDataXml;
import core.system.ResultImpl;
import core.system.error.GameErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static api.enums.EventType.PLAYER_CONNECTED;
import static api.enums.EventType.PLAYER_RECONNECTED;
import static api.enums.EventType.PLAYER_DISCONNECTED;
import static core.system.error.GameErrors.USER_CONNECT_TO_CONTEXT_TOO_MANY_USERS;
import static core.system.error.GameErrors.USER_DISCONNECT_NOT_CONNECTED;

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
  private volatile AtomicBoolean ready = new AtomicBoolean(false);

  @Autowired
  BeanFactory beanFactory;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

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

    context.subscribeEvent(this::checkForReady, EventType.WARRIOR_ADDED, EventType.PLAYER_DISCONNECTED);

    loaded = true;
    logger.info("map initializing succeed \"" + levelMapMetaData.name + "\" in context " + gameContext.getContextId());
  }

  @Override
  public List<Rectangle> getPlayerStartZone(String playerId) {
    return playerStartZones;
  }

  @Override
  public int getMaxPlayerCount() {
    return maxPlayersCount;
  }

  @Override
  public int getWidthInUnits() {
    return width;
  }

  @Override
  public int getHeightInUnits() {
    return height;
  }

  @Override
  public List<Warrior> getWarriors(Coords center, int radius) {
    return null;
  }

  @Override
  public Result<Warrior> addWarrior(Player player, Coords coords, Warrior warrior) {
    if (player.getWarriors().size() >= context.getGameRules().getMaxStartCreaturePerPlayer()) {
      return ResultImpl.fail(GameErrors.PLAYER_UNITS_LIMIT_EXCEEDED.getError(player.getTitle()
              , String.valueOf(context.getGameRules().getMaxStartCreaturePerPlayer())));
    }
    return player.addWarrior(warrior)
            .map(addedWarrior -> addedWarrior.moveTo(coords));
  }

  @Override
  public Result connectPlayer(Player player) {
    Result result = null;
    if (players.containsKey(player.getId())) {
      // игрок есть.
      result = ResultImpl.success(player);
      context.fireGameEvent(null, PLAYER_RECONNECTED, new EventDataContainer(player, result), null);
    } else {
      if (maxPlayersCount >= players.size()) {
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

  @Override
  public List<Player> getPlayers() {
    return new LinkedList(players.values());
  }

  @Override
  public int getSimpleUnitSize() {
    return simpleUnitSize;
  }

  @Override
  public Player getPlayer(String playerId) {
    return players.get(playerId);
  }

  @Override
  public boolean isLoaded() {
    return loaded;
  }

  @Override
  public boolean isReady() {
    return ready.get();
  }

  private void checkForReady(Event event) {
    ready.set(players.size() == maxPlayersCount
            && players.values().stream()
            .reduce(true, (rd, player) -> rd &= player.getWarriors().size() == context.getGameRules().getMaxStartCreaturePerPlayer()
                    , (rd2, rd3) -> rd2));
  }
}
