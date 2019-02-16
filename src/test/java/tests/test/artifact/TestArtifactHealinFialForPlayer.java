package tests.test.artifact;

import api.enums.OwnerTypeEnum;
import api.game.ability.Ability;
import api.game.map.Player;
import core.entity.ability.healing.AbilityWarriorsHealthRejuvenationForArtifact;
import core.entity.artifact.base.AbstractArtifactImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Артефакт лечения воинов. Для игрока
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestArtifactHealinFialForPlayer extends AbstractArtifactImpl<Player>{
  public static final String CLASS_NAME = "Фиал жизни";
  private int level;

  @Autowired
  BeanFactory beanFactory;

  @PostConstruct
  public void initAbilities(){
    Ability luckForRangedAttackForWarrior = beanFactory.getBean(AbilityWarriorsHealthRejuvenationForArtifact.class, this, level, -1, -1);
    this.abilities.put(luckForRangedAttackForWarrior.getTitle(), luckForRangedAttackForWarrior);

  }

  public TestArtifactHealinFialForPlayer(Player owner){
    super(owner
            , OwnerTypeEnum.PLAYER
            , "Art_WRA"
            , CLASS_NAME
            , CLASS_NAME);
    level = 1;
  }
}
