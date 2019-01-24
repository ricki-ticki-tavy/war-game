package core.entity.weapon;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Короткий меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShortSword extends AbstractWeapon{

  private static final String OUID = "WepShSwd_" + UUID.randomUUID().toString();

  public ShortSword(){
    super();
    this.id = OUID;
    this.title = "Короткий меч";
    this.description = "Короткий меч. Ближний бой ";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 4;
    this.rangedMinDamage = 0;
    this.rangedMaxDamage = 0;
    this.bitCost = 30;
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
