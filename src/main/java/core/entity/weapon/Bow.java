package core.entity.weapon;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Обычный меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Bow extends AbstractWeapon {

  public static final String CLASS_NAME = "Простой лук";
  private static final String OUID = "WepBow_" + UUID.randomUUID().toString();

  public Bow() {
    super();
    this.id = OUID;
    this.title = CLASS_NAME;
    this.description = "Простой лук. Двуручное оружие. При ближнем бое работает как кинжал";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 3;
    this.rangedMinDamage = 1;
    this.rangedMaxDamage = 7;
    this.bitCost = 120;
    this.additionalModifiers = null;
    this.unrejectable = false;
    this.useCountPerRound = 0;
    this.totalRangedUseCount = 0;
    this.canDealRangedDamage = true;
    this.minRange = 2;
    this.maxRange = 40;
    this.fadeRangeStart = 20;
    this.fadeDamagePercentPerLength = 3;
    this.neededHandsCountToTakeWeapon = 2;
  }

}
