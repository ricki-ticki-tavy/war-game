package core.entity.map;

import api.game.Rectangle;
import api.game.map.*;
import api.entity.warrior.Warrior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.EMPTY_LIST;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerImpl<T extends Warrior> implements Player<T>{

  private String playerSessionId;
  private Rectangle startZone;
  private Map<String, T> warriors = new ConcurrentHashMap();

  public PlayerImpl(@Autowired String playerSessionId){
    this.playerSessionId = playerSessionId;
  }

  @Override
  public T addWarrior(T warrior) {
    warriors.put(warrior.getId(), warrior);
    return warrior;
  }

  @Override
  public List<T> getWarriors() {
    List<T> warriorsList = Collections.EMPTY_LIST;
    warriorsList.addAll(warriors.values());
    return warriorsList;
  }

  @Override
  public String getId() {
    return playerSessionId;
  }

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
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
