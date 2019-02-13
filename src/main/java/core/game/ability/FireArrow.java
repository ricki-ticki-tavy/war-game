package core.game.ability;

import api.core.Owner;
import api.entity.warrior.Warrior;
import api.enums.*;
import api.game.Influencer;
import core.entity.base.BaseModifier;
import core.entity.warrior.base.InfluencerImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Способность огненной стрелы
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FireArrow extends AbstractAbilityImpl {

  private int level;
  //===================================================================================================
  //===================================================================================================

  /**
   * @param owner            владелец способности
   * @param level            уровень силы огня
   * @param useCount         оставшееся кол-во использований. -1 без ограничений
   * @param useCountPerRound кол-во использований заход. -1 без ограничений
   *
   */
  public FireArrow(Owner owner, int level, int useCount, int useCountPerRound) {
    super(owner, useCountPerRound, "AblFireArrow_", "Огненная стрела", "Урон огнем"); // бесконечно

    this.level = level;

    actorType = ActorTypeEnum.WEAPON;
    targetType = TargetTypeEnum.ENEMY_WARRIOR;
    this.useCount.set(useCount);
  }
  //===================================================================================================

  @Override
  protected List<Influencer> buildInfluencers(Warrior target) {
    List<Influencer> result = new ArrayList<>(1);
    result.add(new InfluencerImpl(target, this
            , LifeTimeUnit.JUST_NOW, 1
            , new BaseModifier(getContext(), "огонь", "огонь"
            , TargetTypeEnum.ENEMY_WARRIOR, ModifierClass.FIRE
            , AttributeEnum.HEALTH
            , level, level + 1
            , 100, 0)));
    return result;
  }
  //===================================================================================================


}
