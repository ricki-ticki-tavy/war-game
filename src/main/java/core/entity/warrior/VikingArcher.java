package core.entity.warrior;

import api.WarriorSBaseAttributes;
import api.enums.ArmorClassEnum;
import core.entity.weapon.LongSword;
import core.entity.weapon.ShortSword;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VikingArcher extends AbstractBaseWarriorClass {

  public VikingArcher() {
    super("Викинг лучник", "Лучик с легкой броней");
    setWarriorSBaseAttributes(new WarriorSBaseAttributes(20, 0, 1, 240)
            .setMaxHealth(20)
            .setMaxManna(0)
            .setMaxActionPoints(240)
            .setMaxAbilityActionPoints(1)
            .setArmorClass(ArmorClassEnum.ARMOR_1)
            .setDeltaCostMove(0)
    );

    setSupportedWeaponClasses(Stream.of(
            ShortSword.class
            , LongSword.class
    ).collect(Collectors.toList()));
  }

}
