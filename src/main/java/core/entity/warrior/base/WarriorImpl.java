package core.entity.warrior.base;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.entity.warrior.WarriorSHand;
import api.entity.weapon.Weapon;
import api.game.Coords;
import api.game.EventDataContainer;
import api.game.map.Player;
import core.system.ResultImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static api.enums.EventType.*;
import static core.system.error.GameErrors.WARRIOR_HANDS_NO_FREE_SLOTS;
import static core.system.error.GameErrors.WARRIOR_WEAPON_NOT_FOUND;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WarriorImpl implements Warrior {

  protected Map<Integer, WarriorSHand> hands;
  protected WarriorBaseClass warriorBaseClass;
  protected Coords coords;
  protected final String id = UUID.randomUUID().toString();
  protected String title;
  protected boolean summoned;
  protected Context gameContext;
  protected Player player;

  @Autowired
  private BeanFactory beanFactory;

  public WarriorImpl(Context gameContext, Player player, WarriorBaseClass warriorBaseClass, String title, boolean summoned) {
    this.warriorBaseClass = warriorBaseClass;
    this.title = title;
    this.summoned = summoned;
    this.gameContext = gameContext;
    this.player = player;
    int handsCount = warriorBaseClass.getHandsCount();
    hands = new ConcurrentHashMap(2);
    while (handsCount-- > 0) {
      hands.put(hands.size(), new WarriorSHandImpl());
    }
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
  public List<Weapon> getWeapons() {
    Set<Weapon> weaponSet = new HashSet<>(5);
    hands.values().stream().forEach(hand ->
            hand.getWeapons().stream().forEach(weapon -> weaponSet.add(weapon)));

    return new ArrayList<>(weaponSet);
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
    gameContext.fireGameEvent(null, WARRIOR_MOVED, new EventDataContainer(this), null);
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

  @Override
  public Result takeWeapon(Class<? extends Weapon> weaponClass) {
    Result result = null;
    Weapon weapon = beanFactory.getBean(weaponClass);
    try {
      if (weapon.getNeededHandsCountToTakeWeapon() > 0) {
        int freePoints = 2 - hands.values().stream().map(hand -> hand.isFree() ? 0 : 1).reduce(0, (acc, chg) -> acc += chg);
        if (freePoints < weapon.getNeededHandsCountToTakeWeapon()) {
          result = ResultImpl.fail(WARRIOR_HANDS_NO_FREE_SLOTS.getError(String.valueOf(freePoints)
                  , weapon.getTitle()
                  , String.valueOf(weapon.getNeededHandsCountToTakeWeapon())));
          return result;
        }
      }

      AtomicInteger points = new AtomicInteger(weapon.getNeededHandsCountToTakeWeapon());
      // место есть в руках. Ищем свободную руку
      hands.values().stream()
              .filter(hand -> hand.isFree() && points.get() > 0)
              .forEach(hand -> {
                points.decrementAndGet();
                hand.addWeapon(weapon);
              });
      result = ResultImpl.success(weapon);
      return result;
    } finally {
      gameContext.fireGameEvent(null, WEAPON_TAKEN, new EventDataContainer(this, weapon, result), null);
    }
  }

  @Override
  public Result dropWeapon(String weaponInstanceId) {
    Result result = hands.values().stream().filter(hand -> hand.hasWeapon(weaponInstanceId)).findFirst()
            .map(warriorSHand -> ResultImpl.success(warriorSHand.removeWeapon(weaponInstanceId)))
            .orElse(ResultImpl.fail(WARRIOR_WEAPON_NOT_FOUND.getError(weaponInstanceId)));

    gameContext.fireGameEvent(null
            , result.isSuccess() ? WEAPON_DROPED : WEAPON_TRY_TO_DROP
            , new EventDataContainer(this, result.isSuccess() ? result.getResult() : weaponInstanceId, result)
            , null);

    return result;
  }
}
