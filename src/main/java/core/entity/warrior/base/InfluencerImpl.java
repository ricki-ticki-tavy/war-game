package core.entity.warrior.base;

import api.entity.ability.Modifier;
import api.entity.warrior.Influencer;
import api.entity.warrior.Warrior;
import api.enums.LifeTimeUnit;
import api.game.Event;

import java.util.UUID;

public class InfluencerImpl implements Influencer {
  private LifeTimeUnit lifeTimeUnit;
  private int lifeTime;
  private Warrior warrior;
  private Modifier modifier;
  private Object source;
  private String id = UUID.randomUUID().toString();

  /**
   *
   * @param warrior    владелец. Тот, на кого это влияние направлено
   * @param modifier   модификатор влияния
   * @param source         кто является источникомвлияния. Кто его наложил.
   * @param lifeTimeUnit единицы, в которых измеряется время жизни влияния
   * @param lifeTime     время жизни влияния
   */
  public InfluencerImpl(Warrior warrior, Modifier modifier, Object source, LifeTimeUnit lifeTimeUnit, int lifeTime) {
    this.modifier = modifier;
    this.lifeTimeUnit = lifeTimeUnit;
    this.lifeTime = lifeTime;
    this.warrior = warrior;
    this.source= source;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return modifier.getTitle();
  }

  @Override
  public String getDescription() {
    return modifier.getDescription();
  }

  /**
   * срабатывает когда наступает событие, по которому измеряется время жизни
   * @param event
   */
  public void onTimeEvent(Event event){

  }

}
