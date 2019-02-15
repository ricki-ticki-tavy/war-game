package core.game.action;

import api.game.ability.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.core.Event;
import api.game.action.InfluenceResult;
import api.game.map.Player;

import java.util.ArrayList;
import java.util.List;

public class InfluenceResultImpl implements InfluenceResult {

  private Player actorPlayer;
  private Player targetPlayer;
  private Warrior actor;
  private Warrior target;
  private Weapon weapon;
  private Event event;
  private int consumedActionPoints;
  private final List<Influencer> influencers = new ArrayList<>(20);

  public InfluenceResultImpl(Player actorPlayer, Warrior actor, Weapon weapon
          , Player targetPlayer, Warrior target, int consumedActionPoints){
    this.actorPlayer = actorPlayer;
    this.actor = actor;
    this.weapon = weapon;
    this.targetPlayer = targetPlayer;
    this.target = target;
    this.consumedActionPoints = consumedActionPoints;
  }
  //===================================================================================================

  @Override
  public Warrior getActor() {
    return actor;
  }
  //===================================================================================================

  @Override
  public Weapon getAttackerWeapon() {
    return weapon;
  }
  //===================================================================================================

  @Override
  public Player getActorPlayer() {
    return actorPlayer;
  }
  //===================================================================================================

  @Override
  public Player getTargetPlayer() {
    return targetPlayer;
  }
  //===================================================================================================

  @Override
  public Warrior getTarget() {
    return target;
  }
  //===================================================================================================

  @Override
  public Event getEvent() {
    return event;
  }
  //===================================================================================================

  @Override
  public List<Influencer> getInfluencers() {
    return influencers;
  }
  //===================================================================================================

  @Override
  public InfluenceResult addInfluencer(Influencer influencer) {
    influencers.add(influencer);
    return this;
  }
  //===================================================================================================

  @Override
  public int getConsumedActionPoints() {
    return consumedActionPoints;
  }
  //===================================================================================================

  public static InfluenceResult forPositive(Warrior target){
    return new InfluenceResultImpl(target.getOwner(), target, null, target.getOwner(), target, 0);
  }
  //===================================================================================================
  //===================================================================================================
}
