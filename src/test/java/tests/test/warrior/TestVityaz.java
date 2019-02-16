package tests.test.warrior;

import api.core.Result;
import api.enums.ArmorClassEnum;
import api.game.ability.Ability;
import api.game.action.InfluenceResult;
import core.entity.warrior.base.AbstractBaseWarriorClass;
import core.entity.warrior.base.WarriorSBaseAttributesImpl;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import core.entity.ability.luck.AbilityLuckForRangedAttackForWarrior;
import core.system.ResultImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestVityaz extends AbstractBaseWarriorClass {

  @Autowired
  BeanFactory beanFactory;

  public static final String CLASS_NAME = "Тестовый  витязь";

  @PostConstruct
  private void registerAbilities(){
    Ability luck = beanFactory.getBean(AbilityLuckForRangedAttackForWarrior.class, this, 10, -1, -1);
    abilities.put(luck.getTitle(), luck);
  }

  public TestVityaz() {
    super(CLASS_NAME, "Воин со средней броней");
    setWarriorSBaseAttributes(new WarriorSBaseAttributesImpl(20, 0, 1, 240, 2)
            .setMaxHealth(20)
            .setMaxManna(0)
            .setMaxActionPoints(240)
            .setMaxAbilityActionPoints(1)
            .setArmorClass(ArmorClassEnum.ARMOR_2)
            .setDeltaCostMove(0)
            .setMaxDefenseActionPoints(60)
            .setSummonable(false)
            .setLuckMeleeAtack(6)
            .setLuckRangeAtack(6)
            .setLuckDefense(6)
    );

    setSupportedWeaponClasses(Stream.of(
            ShortSword.class
            , Sword.class
    ).collect(Collectors.toList()));

  }
  //===================================================================================================

  @Override
  public Result<InfluenceResult> innerWarriorUnderAttack(InfluenceResult attackResult) {
    return ResultImpl.success(attackResult);
  }

}
