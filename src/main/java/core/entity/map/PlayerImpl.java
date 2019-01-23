package core.entity.map;

import api.core.Context;
import api.entity.warrior.Warrior;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.map.Player;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static api.enums.EventType.WARRIOR_ADDED;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerImpl implements Player{

  private Context context;
  private String playerSessionId;
  private String playerName;
  private Rectangle startZone;
  private Map<String, Warrior> warriors = new ConcurrentHashMap();

  public PlayerImpl(Context context, String playerName, String playerSessionId){
    this.context = context;
    this.playerSessionId = playerSessionId;
    this.playerName = playerName;
  }

  @Override
  public Warrior addWarrior(Warrior warrior) {
    warriors.put(warrior.getId(), warrior);
    context.fireGameEvent(null, WARRIOR_ADDED, new EventDataContainer(warrior), Collections.EMPTY_MAP);
    return warrior;
  }

  @Override
  public List<Warrior> getWarriors() {
    return new ArrayList(warriors.values());
  }

  @Override
  public String getId() {
    return playerSessionId;
  }

  @Override
  public String getTitle() {
    return playerName;
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public void setStartZone(Rectangle startZone) {
    this.startZone = startZone;
  }

  @Override
  public Rectangle getStartZone() {
    return startZone;
  }
}
