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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static api.enums.EventType.WARRIOR_ADDED;
import static core.system.error.GameErrors.WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME;

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
  public Result<Warrior> addWarrior(Warrior warrior) {
    warriors.put(warrior.getId(), warrior);
    Result result = ResultImpl.success(warrior);
    context.fireGameEvent(null, WARRIOR_ADDED, new EventDataContainer(warrior, result), Collections.EMPTY_MAP);
    return result;
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
    if (this.context != null && (newContext == null || !this.context.getContextId().equals(newContext.getContextId()))) {
      if ((result = this.context.disconnectPlayer(this)).isFail())
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

  @Override
  public Result<Warrior> findWarriorById(String warriorId) {
    return Optional.ofNullable(warriors.get(warriorId))
            .map(foundWarrior -> ResultImpl.success(foundWarrior))
            .orElse(ResultImpl.fail(WARRIOR_NOT_FOUND_AT_PLAYER_BY_NAME.getError(getTitle(), warriorId)));
  }
}
