package core.entity.warrior;

import api.core.Result;
import api.enums.ArmorClassEnum;
import api.game.action.InfluenceResult;
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
public class Vityaz extends AbstractBaseWarriorClass {

  public static final String CLASS_NAME = "Витязь";

  public Vityaz() {
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
