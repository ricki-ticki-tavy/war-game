package core.entity.map;

import api.entity.warrior.Warrior;
import api.game.Coords;
import api.game.Rectangle;
import api.game.map.LevelMap;
import api.game.map.Player;
import api.game.map.metadata.LevelMapMetaData;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Игровая карта
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LevelMapImpl implements LevelMap {

  private String name;
  private String description;
  private int simpleUnitSize;
  private int width;
  private int height;
  private int maxPlayersCount;
  private List<Rectangle> playerStartZones;
  private Map<String, Player> players;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void init(LevelMapMetaData levelMapMetaData) {
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
  public Warrior addWarrior(Coords coords, Warrior warrior) {
    return null;
  }

  @Override
  public Player addPlayer(Player player) {
    return null;
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
}
