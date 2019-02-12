package core.entity.warrior.base;

import api.core.Result;
import api.game.ability.Modifier;
import api.game.Influencer;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorSBaseAttributes;
import api.enums.EventType;
import api.enums.LifeTimeUnit;
import api.enums.TargetTypeEnum;
import api.core.Event;
import api.core.EventDataContainer;
import api.game.action.AttackResult;
import core.system.ResultImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfluencerImpl implements Influencer {
  private LifeTimeUnit lifeTimeUnit;
  private int lifeTime;
  private Warrior targetWarrior;
  private final Modifier modifier;
  private Object source;
  private String id = UUID.randomUUID().toString();
  private String consumerId = null;
  private final List<Influencer> children;
  //===================================================================================================
  //===================================================================================================

  /**
   * @param targetWarrior Тот, на кого это влияние направлено
   * @param source        кто является источникомвлияния. Кто его наложил.
   * @param lifeTimeUnit  единицы, в которых измеряется время жизни влияния
   * @param lifeTime      время жизни влияния
   * @param modifier      модификатор влияния
   */
  public InfluencerImpl(Warrior targetWarrior, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime, Modifier modifier) {
    this.modifier = modifier;
    this.lifeTimeUnit = lifeTimeUnit;
    this.lifeTime = lifeTime;
    this.targetWarrior = targetWarrior;
    this.source = source;
    this.children = new ArrayList<>(10);
    if (!lifeTimeUnit.getEventType().equals(EventType.ALWAYS)) {
      consumerId = targetWarrior.getOwner().findContext().getResult().subscribeEvent(this::onTimeEvent, lifeTimeUnit.getEventType());
    }
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
    if (source == event.getSource(source.getClass())) {
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
      targetWarrior.getOwner().findContext().getResult().unsubscribeEvent(consumerId, lifeTimeUnit.getEventType());
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
  public Influencer addChildren(Influencer influencer) {
    this.children.add(influencer);
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
    // TODO Реализовать применение влияния на воина
    WarriorSBaseAttributes attributes = targetWarrior.getAttributes();
    // Уже всерассчитано. Применяем значение, рассчитанное заранее (getLastCalculatedValue())
    if (modifier.getTarget() == TargetTypeEnum.ENEMY_WARRIOR) {
      switch (modifier.getAttribute()) {
        case HEALTH:
          attributes.addHealth(-modifier.getLastCalculatedValue());
          // Отправим сообщение
          targetWarrior.getContext()
                  .fireGameEvent(null
                          , EventType.WARRIOR_WAS_ATTACKED_BY_ENEMY
                          , new EventDataContainer(attackResult, modifier)
                          , null);
          break;
      }
    }
    return ResultImpl.success(targetWarrior);
  }
  //===================================================================================================

}
