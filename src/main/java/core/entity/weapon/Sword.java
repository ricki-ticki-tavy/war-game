package core.entity.weapon;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Обычный меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Sword extends AbstractWeapon {

  private static final String OUID = "WepSwd_002";

  public Sword() {
    super();
    this.id = OUID;
    this.title = "Простой меч";
    this.description = "Простой меч. Ближний бой ";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 5;
    this.rangedMinDamage = 0;
    this.rangedMaxDamage = 0;
    this.bitCost = 40;
    this.additionalModifiers = null;
    this.unrejectable = false;
    this.useCountPerRound = 0;
    this.totalRangedUseCount = 0;
    this.canDealRangedDamage = false;
    this.minRange = 0;
    this.maxRange = 2;
    this.fadeRangeStart = 0;
    this.fadeDamagePercentPerLength = 0;
    this.neededHandsCountToTakeWeapon = 1;
  }

}
