package tests.test.artifact;

import api.entity.warrior.Warrior;
import api.enums.OwnerTypeEnum;
import api.game.ability.Ability;
import core.entity.ability.luck.AbilityLuckForRangedAttackForWarrior;
import core.entity.artifact.base.AbstractArtifactImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Артефакт золотой стрелы, увеличивающий удачу в стрельбе
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestArtifactRainbowArrowForWarrior extends AbstractArtifactImpl<Warrior>{
  public static final String CLASS_NAME = "Золотая стрела удачи 2 ур";
  private int level;

  @Autowired
  BeanFactory beanFactory;

  @PostConstruct
  public void initAbilities(){
    Ability luckForRangedAttackForWarrior = beanFactory.getBean(AbilityLuckForRangedAttackForWarrior.class, this, level, -1, -1);
    this.abilities.put(luckForRangedAttackForWarrior.getTitle(), luckForRangedAttackForWarrior);

  }

  public TestArtifactRainbowArrowForWarrior(Warrior owner){
    super(owner
            , OwnerTypeEnum.WARRIOR
            , "Art_WRA"
            , CLASS_NAME
            , CLASS_NAME);
    level = 20;
  }
}
