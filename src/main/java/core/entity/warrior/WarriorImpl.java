package core.entity.warrior;

import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.warrior.WarriorSHand;
import api.game.Coords;
import core.system.error.GameErrors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarriorImpl implements Warrior {

  protected Map<Integer, WarriorSHand> hands = new ConcurrentHashMap(2);
  protected WarriorBaseClass warriorBaseClass;
  protected Coords coords;
  protected final String id = UUID.randomUUID().toString();
  protected String title;
  protected boolean summoned;


  public WarriorImpl(WarriorBaseClass warriorBaseClass, String title, boolean summoned){
    this.warriorBaseClass = warriorBaseClass;
    this.title = title;
    this.summoned = summoned;
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
  public void initCoords(Coords coords) {
    if (this.coords != null) {
      GameErrors.GAME_ERROR_WARRIOR_S_COORDS_IS_FINAL.error(id);
    }
  }
}
