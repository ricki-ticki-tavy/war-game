package core.entity.map;

import api.game.Rectangle;
import api.game.map.*;
import api.entity.warrior.Warrior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerImpl implements Player{

  private String playerSessionId;
  private Rectangle startZone;

  public PlayerImpl(@Autowired String playerSessionId){
    this.playerSessionId = playerSessionId;
  }

  @Override
  public Warrior addWarrior(Warrior warrior) {
    return null;
  }

  @Override
  public List<Warrior> getWarriors() {
    return null;
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
