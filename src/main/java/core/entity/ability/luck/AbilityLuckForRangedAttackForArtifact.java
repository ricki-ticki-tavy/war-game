package core.entity.ability.luck;

import api.core.Owner;
import api.entity.warrior.Warrior;
import api.enums.*;
import api.game.ability.Influencer;
import core.entity.ability.base.BaseModifier;
import core.entity.warrior.base.InfluencerImpl;
import core.entity.ability.base.AbstractAbilityImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Способность повышать удачу дистанционной атаки для применения в артефакте
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AbilityLuckForRangedAttackForArtifact extends AbstractAbilityImpl {

  private int level;
  //===================================================================================================
  //===================================================================================================

  /**
   * @param owner            владелец способности
   * @param level            уровень силы удачи
   * @param useCount         оставшееся кол-во использований. -1 без ограничений
   * @param useCountPerRound кол-во использований заход. -1 без ограничений
   *
   */
  public AbilityLuckForRangedAttackForArtifact(Owner owner, int level, int useCount, int useCountPerRound) {
    super(owner, useCountPerRound, "AblLuckR_", "Удачливый стрелок", "Удача в стрельбе"); // бесконечно

    this.level = level;

    ownerTypeForAbility = OwnerTypeEnum.ARTIFACT;
    targetType = TargetTypeEnum.THIS_WARRIOR;
    activePhases.add(PlayerPhaseType.DEFENSE_PHASE);
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
            , AttributeEnum.RANGED_ATTACK_LUCK
            , level, level
            , 100, 0)));
    return result;
  }
  //===================================================================================================


}
