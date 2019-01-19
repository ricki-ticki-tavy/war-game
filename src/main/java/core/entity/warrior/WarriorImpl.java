package core.entity.warrior;

import api.core.GameContext;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.warrior.WarriorSHand;
import api.game.Coords;
import api.game.map.Player;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static api.enums.EventType.WARRIOR_MOVED;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarriorImpl implements Warrior {

  protected Map<Integer, WarriorSHand> hands = new ConcurrentHashMap(2);
  protected WarriorBaseClass warriorBaseClass;
  protected Coords coords;
  protected final String id = UUID.randomUUID().toString();
  protected String title;
  protected boolean summoned;
  protected GameContext gameContext;
  protected Player player;

  public WarriorImpl(GameContext gameContext, Player player, WarriorBaseClass warriorBaseClass, String title, boolean summoned) {
    this.warriorBaseClass = warriorBaseClass;
    this.title = title;
    this.summoned = summoned;
    this.gameContext = gameContext;
    this.player = player;
  }

  @Override
  public WarriorBaseClass getWarriorBaseClass() {
    return warriorBaseClass;
  }

  @Override
  public boolean isSummoned() {
    return summoned;
  }

  @Override
  public List<WarriorSHand> getHands() {
    return new LinkedList(hands.values());
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public Warrior moveTo(Coords coords) {
    this.coords = new Coords(coords);
    gameContext.fireGameEvent(null, WARRIOR_MOVED, this, Collections.EMPTY_MAP);
    return this;
  }

  @Override
  public Player getOwner() {
    return player;
  }

  @Override
  public Coords getCoords() {
    return new Coords(this.coords);
  }
}
