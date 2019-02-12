package tests.test.warrior;

import api.core.Result;
import api.enums.ArmorClassEnum;
import api.game.action.AttackResult;
import core.entity.warrior.base.AbstractBaseWarriorClass;
import core.entity.warrior.base.WarriorSBaseAttributesImpl;
import core.entity.weapon.ShortSword;
import core.entity.weapon.Sword;
import core.system.ResultImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestViking extends AbstractBaseWarriorClass {

  public static final String CLASS_NAME = "Тестовый викинг";

  public TestViking() {
    super(CLASS_NAME, "Воин с легкой броней");
    setWarriorSBaseAttributes(new WarriorSBaseAttributesImpl(20, 0, 1, 240, 2)
            .setMaxHealth(20)
            .setMaxManna(0)
            .setMaxActionPoints(240)
            .setMaxAbilityActionPoints(1)
            .setArmorClass(ArmorClassEnum.ARMOR_1)
            .setDeltaCostMove(0)
            .setMaxDefenseActionPoints(60)
            .setSummonable(false)
    );

    setSupportedWeaponClasses(Stream.of(
            ShortSword.class
            , Sword.class
    ).collect(Collectors.toList()));
  }
  //===================================================================================================

  @Override
  public Result<AttackResult> innerWarriorUnderAttack(AttackResult attackResult) {
    return ResultImpl.success(attackResult);
  }

}
