package core.entity.ability.healing;

import api.core.Owner;
import api.entity.warrior.Warrior;
import api.enums.*;
import api.game.ability.Influencer;
import core.entity.ability.base.AbstractAbilityImpl;
import core.entity.ability.base.BaseModifier;
import core.entity.warrior.base.InfluencerImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Способность лечения воина. для
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AbilityWarriorsHealthRejuvenationForArtifact extends AbstractAbilityImpl {

  private int level;
  //===================================================================================================
  //===================================================================================================

  /**
   * @param owner            владелец способности
   * @param level            не используется
   * @param useCount         оставшееся кол-во использований. -1 без ограничений
   * @param useCountPerRound кол-во использований заход. -1 без ограничений
   *
   */
  public AbilityWarriorsHealthRejuvenationForArtifact(Owner owner, int level, int useCount, int useCountPerRound) {
    super(owner, useCountPerRound, "AblRej_", "лечение", "Лечение"); // бесконечно

    this.level = level;

    ownerTypeForAbility = OwnerTypeEnum.ARTIFACT;
    targetType = TargetTypeEnum.ALLIED_WARRIOR;
    activePhases.add(PlayerPhaseType.ATACK_PHASE);
    this.useCount.set(useCount);
  }
  //===================================================================================================

  @Override
  protected List<Influencer> buildInfluencers(Warrior target) {
    List<Influencer> result = new ArrayList<>(1);
    result.add(new InfluencerImpl(target, this
            , LifeTimeUnit.JUST_NOW, 1
            , new BaseModifier(getContext(), title, description
            , targetType, ManifestationOfInfluenceEnum.POSITIVE
            , ModifierClass.PHYSICAL
            , AttributeEnum.HEALTH
            , level, level
            , 100, 0)));
    return result;
  }
  //===================================================================================================


}
