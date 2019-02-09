package core.entity.weapon;

import api.entity.warrior.Warrior;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Обычный меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Sword extends AbstractWeaponImpl {

  public static final String CLASS_NAME = "Простой меч";
  private static final String OUID = "WepSwd_" + UUID.randomUUID().toString();

  public Sword() {
    super();
    this.id = OUID;
    this.title = CLASS_NAME;
    this.description = "Простой меч. Ближний бой ";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 5;
    this.rangedMinDamage = 0;
    this.rangedMaxDamage = 0;
    this.meleeAttackCost = 40;
    this.rangedAttackCost = 0;
    this.additionalModifiers = null;
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
