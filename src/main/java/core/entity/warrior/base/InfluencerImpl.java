package core.entity.warrior.base;

import api.core.*;
import api.game.ability.Modifier;
import api.game.Influencer;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorSBaseAttributes;
import api.enums.EventType;
import api.enums.LifeTimeUnit;
import api.game.action.AttackResult;
import core.system.ResultImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class InfluencerImpl implements Influencer {
  private LifeTimeUnit lifeTimeUnit;
  private int lifeTime;
  private Warrior targetWarrior;
  private final Modifier modifier;
  private Owner owner;
  private String id = UUID.randomUUID().toString();
  private String consumerId = null;
  private final List<Influencer> children;
  //===================================================================================================
  //===================================================================================================

  /**
   * @param targetWarrior Тот, на кого это влияние направлено
   * @param owner         кто является источникомвлияния. Кто его наложил.
   * @param lifeTimeUnit  единицы, в которых измеряется время жизни влияния
   * @param lifeTime      время жизни влияния
   * @param modifier      модификатор влияния
   */
  public InfluencerImpl(Warrior targetWarrior, Owner owner, LifeTimeUnit lifeTimeUnit, int lifeTime, Modifier modifier) {
    this.modifier = modifier;
    this.lifeTimeUnit = lifeTimeUnit;
    this.lifeTime = lifeTime;
    this.targetWarrior = targetWarrior;
    this.owner = owner;
    this.children = new ArrayList<>(10);
    if (!lifeTimeUnit.getEventType().equals(EventType.ALWAYS)) {
      consumerId = getContext().subscribeEvent(this::onTimeEvent, lifeTimeUnit.getEventType());
    }
  }
  //===================================================================================================

  @Override
  public Context getContext() {
    return owner.getContext();
  }
  //===================================================================================================

  @Override
  public String getId() {
    return id;
  }
  //===================================================================================================

  @Override
  public String getTitle() {
    return modifier.getTitle();
  }
  //===================================================================================================

  @Override
  public String getDescription() {
    return modifier.getDescription();
  }
  //===================================================================================================

  /**
   * TODO добавить собственно действие по изменению некоторых атрибутов, как здоровье, магия и т.п., если оно есть
   * срабатывает когда наступает событие, по которому измеряется время жизни
   *
   * @param event
   */
  public void onTimeEvent(Event event) {
    if (owner == event.getSource(owner.getClass())) {
      // событие пришло от нашего источника
      if (--lifeTime <= 0) {
        // пора удаляться из списка влияния
        targetWarrior.removeInfluencerFromWarrior(this, false);
      }
    }
  }
  //===================================================================================================

  @Override
  public Warrior getTargetWarrior() {
    return targetWarrior;
  }
  //===================================================================================================

  private void unsubscribe() {
    if (consumerId != null) {
      getContext().unsubscribeEvent(consumerId, lifeTimeUnit.getEventType());
      consumerId = null;
    }
  }
  //===================================================================================================

  @Override
  public Result<Influencer> removeFromWarrior(boolean silent) {
    unsubscribe();
    ((WarriorImpl) targetWarrior).innerRemoveInfluencerFromWarrior(this, silent);
    return ResultImpl.success(this);
  }
  //===================================================================================================

  @Override
  public Influencer addChild(Influencer influencer) {
    this.children.add(influencer);
    return this;
  }
  //===================================================================================================

  @Override
  public Influencer addChildren(Collection<Influencer> influencers) {
    this.children.addAll(influencers);
    return this;
  }
  //===================================================================================================

  @Override
  public List<Influencer> getChildren() {
    return new ArrayList(children);
  }
  //===================================================================================================

  @Override
  public Modifier getModifier() {
    return modifier;
  }
  //===================================================================================================

  @Override
  public Result<Warrior> applyToWarrior(AttackResult attackResult) {
    // Уже всерассчитано. Применяем значение, рассчитанное заранее (getLastCalculatedValue())
    Result<Warrior> result;
    if (modifier.isLuckyRollOfDice() || modifier.isHitSuccess()) {
      result = modifier.apply(attackResult)
              .map(fineModifier -> {
                children.stream()
                        .forEach(child -> child.applyToWarrior(attackResult));
                return ResultImpl.success(fineModifier.getTarget());
              });

    } else {
      result = ResultImpl.success(targetWarrior);
    }
    return result;
  }
  //===================================================================================================

}
