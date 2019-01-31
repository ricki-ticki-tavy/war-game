package core.entity.warrior.base;

import api.entity.ability.Modifier;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.enums.EventType;
import api.enums.LifeTimeUnit;
import api.game.Event;

import java.util.UUID;

public class InfluencerImpl implements Influencer {
  private LifeTimeUnit lifeTimeUnit;
  private int lifeTime;
  private Warrior targetWarrior;
  private Modifier modifier;
  private Object source;
  private String id = UUID.randomUUID().toString();
  private String consumerId = null;
  //===================================================================================================
  //===================================================================================================

  /**
   * @param targetWarrior владелец. Тот, на кого это влияние направлено
   * @param modifier      модификатор влияния
   * @param source        кто является источникомвлияния. Кто его наложил.
   * @param lifeTimeUnit  единицы, в которых измеряется время жизни влияния
   * @param lifeTime      время жизни влияния
   */
  public InfluencerImpl(Warrior targetWarrior, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    this.modifier = modifier;
    this.lifeTimeUnit = lifeTimeUnit;
    this.lifeTime = lifeTime;
    this.targetWarrior = targetWarrior;
    this.source = source;
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

  @Override
  public void unsubscribe() {
    if (consumerId != null) {
      targetWarrior.getOwner().findContext().getResult().unsubscribeEvent(consumerId, lifeTimeUnit.getEventType());
      consumerId = null;
    }
  }
  //===================================================================================================

}
