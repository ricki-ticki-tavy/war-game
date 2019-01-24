package core.entity.player;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.game.EventDataContainer;
import api.game.Rectangle;
import api.game.map.Player;
import core.system.ResultImpl;
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
public class PlayerImpl implements Player {

  private Context context;
  private String playerName;
  private Rectangle startZone;
  private Map<String, Warrior> warriors = new ConcurrentHashMap();

  public PlayerImpl(String playerName) {
    this.context = null;
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

  @Override
  public Result replaceContext(Context newContext) {
    Result result;
    if (context != null) {
      if ((result = context.disconnectPlayer(this)).isFail())
        return result;
    }

    this.context = newContext;
    return ResultImpl.success(this);
  }

  @Override
  public Player replaceContextSilent(Context newContext) {
    this.context = newContext;
    return this;
  }

  @Override
  public Context getContext() {
    return context;
  }

  @Override
  public String getId() {
    return playerName;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Player) && ((Player) obj).getId().equals(getId());
  }

}
