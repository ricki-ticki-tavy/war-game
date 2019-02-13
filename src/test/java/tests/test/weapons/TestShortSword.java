package tests.test.weapons;

import core.entity.weapon.AbstractWeaponImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Короткий меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestShortSword extends AbstractWeaponImpl {

  public static final String CLASS_NAME = "Тестовый короткий меч";
  private final String OUID = "WepShSwd_" + UUID.randomUUID().toString();

  public TestShortSword(){
    super();
    this.id = OUID;
    this.title = CLASS_NAME;
    this.description = "Короткий меч. Ближний бой ";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 4;
    this.rangedMinDamage = 0;
    this.rangedMaxDamage = 0;
    this.meleeAttackCost = 30;
    this.rangedAttackCost = 0;
    this.unrejectable = false;
    this.useCountPerRound = 0;
    this.totalRangedUseCount = 0;
    this.canDealRangedDamage = false;
    this.minRangedAttackRange = 0;
    this.maxRangedAttackRange = 2;
    this.meleeAttackRange = 2;
    this.fadeRangeStart = 0;
    this.fadeDamagePercentPerLength = 0;
    this.neededHandsCountToTakeWeapon = 1;
  }
}
