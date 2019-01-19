package core.entity.map;

import api.core.GameContext;
import api.entity.warrior.Warrior;
import api.enums.EventType;
import api.game.Coords;
import api.game.GameEvent;
import api.game.Rectangle;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.LevelMapMetaData;
import core.system.error.GameErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static api.enums.EventParamNames.WARRIOR_PARAM;
import static api.enums.TargetTypeEnum.WARRIOR;

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
  private GameContext context;
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
  public void init(GameContext gameContext, LevelMapMetaData levelMapMetaData) {
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

    context.subscribeEvent(this::checkForReady, EventType.WARRIOR_ADDED, EventType.PLAYER_REMOVED);

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
  public Warrior addWarrior(String playerId, Coords coords, Warrior warrior) {
    return Optional.ofNullable(getPlayer(playerId))
            .map(player -> {
              if (player.getWarriors().size() >= context.getGameRules().getMaxStartCreaturePerPlayer()) {
                GameErrors.GAME_ERROR_TOO_MANY_UNITS_FOR_PLAYER.error(playerId, String.valueOf(context.getGameRules().getMaxStartCreaturePerPlayer()));
              }
              player.addWarrior(warrior);
              warrior.moveTo(coords);
              return warrior;
            }).orElseThrow(() -> GameErrors.GAME_ERROR_UNKNOWN_USER_UID.getError(playerId));
  }

  private Player createNewPlayer(String playerName, String playerSessionId) {
    Player player = players.get(playerSessionId);
    if (player == null) {
      logger.info(String.format("creating new player %s in context %s", playerSessionId, context.getContextId()));
      if (players.size() < maxPlayersCount) {
        player = beanFactory.getBean(Player.class, context, playerName, playerSessionId);
        player.setStartZone(playerStartZones.get(players.size()));
        players.put(playerSessionId, player);
        logger.info(String.format("New player %s was created in context %s. GameContext now contains %s player(s)"
                , playerSessionId, context.getContextId(), players.size()));
      } else {
        logger.info(String.format("player %s can't be created because all player's slots in context %s are busy"
                , playerSessionId, context.getContextId()));
      }
    } else {
      logger.info(String.format("player %s was reconnected to context %s", playerSessionId, context.getContextId()));
    }
    return player;
  }

  @Override
  public Player connectPlayer(String playerName, String playerSessionId) {
    logger.info(String.format("connecting player %s with sessionId %s to context %s", playerName, playerSessionId, context.getContextId()));
    return createNewPlayer(playerName, playerSessionId);
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

  private void checkForReady(GameEvent event){
    ready.set(players.size() == maxPlayersCount
            && players.values().stream()
            .reduce(true, (rd, player) ->  rd &= player.getWarriors().size() == context.getGameRules().getMaxStartCreaturePerPlayer()
            , (rd2, rd3) -> rd2));
  }
}
