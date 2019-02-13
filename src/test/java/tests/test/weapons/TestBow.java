package tests.test.weapons;

import core.entity.weapon.AbstractWeaponImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Обычный меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestBow extends AbstractWeaponImpl {

  public static final String CLASS_NAME = "Тестовый простой лук";
  public static final String SECOND_WEAPON_NAME = "Тестовое острие лука";
  private final String OUID = "WepBow_" + UUID.randomUUID().toString();

  public TestBow() {
    super();
    this.id = OUID;
    this.title = CLASS_NAME;
    this.description = "Простой лук. Двуручное оружие. При ближнем бое работает как кинжал";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 2;
    this.rangedMinDamage = 1;
    this.rangedMaxDamage = 8;
    this.meleeAttackCost = 40;
    this.rangedAttackCost = 120;
    this.unrejectable = false;
    this.useCountPerRound = -1;
    this.totalRangedUseCount = -1;
    this.canDealRangedDamage = true;
    this.minRangedAttackRange = 2;
    this.maxRangedAttackRange = 40;
    this.meleeAttackRange = 2;
    this.fadeRangeStart = 20;
    this.fadeDamagePercentPerLength = 3;
    this.neededHandsCountToTakeWeapon = 2;
    this.secondWeaponName = SECOND_WEAPON_NAME;
  }

}
