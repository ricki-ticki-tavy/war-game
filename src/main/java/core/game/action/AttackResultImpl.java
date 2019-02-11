package core.game.action;

import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Event;
import api.game.action.AttackResult;
import api.game.map.Player;

import java.util.ArrayList;
import java.util.List;

public class AttackResultImpl implements api.game.action.AttackResult {

  private Player attackerPlayer;
  private Player targetPlayer;
  private Warrior attacker;
  private Warrior target;
  private Weapon weapon;
  private Event event;
  private final List<Influencer> influencers = new ArrayList<>(20);

  public AttackResultImpl(Player attackerPlayer, Warrior attacker, Weapon weapon
          , Player targetPlayer, Warrior target, Event event){
    this.attackerPlayer = attackerPlayer;
    this.attacker = attacker;
    this.weapon = weapon;
    this.targetPlayer = targetPlayer;
    this.target = target;
    this.event = event;
  }

  @Override
  public Warrior getAttacker() {
    return attacker;
  }

  @Override
  public Weapon getAttackerWeapon() {
    return weapon;
  }

  @Override
  public Player getAttackerPlayer() {
    return attackerPlayer;
  }

  @Override
  public Player getTargetPlayer() {
    return targetPlayer;
  }

  @Override
  public Warrior getTarget() {
    return target;
  }

  @Override
  public Event getEvent() {
    return event;
  }

  @Override
  public List<Influencer> getInfluencers() {
    return influencers;
  }

  @Override
  public AttackResult addInfluencer(Influencer influencer) {
    influencers.add(influencer);
    return this;
  }
}
