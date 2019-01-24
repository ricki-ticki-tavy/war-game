package core.entity.warrior;

import api.enums.ArmorClassEnum;
import core.entity.warrior.base.AbstractBaseWarriorClass;
import core.entity.warrior.base.WarriorSBaseAttributesImpl;
import core.entity.weapon.Sword;
import core.entity.weapon.ShortSword;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Viking extends AbstractBaseWarriorClass {

  public Viking() {
    super("Викинг", "Воин с легкой броней");
    setWarriorSBaseAttributes(new WarriorSBaseAttributesImpl(20, 0, 1, 240, 2)
            .setMaxHealth(20)
            .setMaxManna(0)
            .setMaxActionPoints(240)
            .setMaxAbilityActionPoints(1)
            .setArmorClass(ArmorClassEnum.ARMOR_1)
            .setDeltaCostMove(0)
    );

    setSupportedWeaponClasses(Stream.of(
            ShortSword.class
            , Sword.class
    ).collect(Collectors.toList()));
  }

}
