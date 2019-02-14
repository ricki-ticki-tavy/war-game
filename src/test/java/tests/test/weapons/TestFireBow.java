package tests.test.weapons;

import api.game.ability.Ability;
import core.entity.weapon.AbstractWeaponImpl;
import core.entity.ability.AbilityFireArrowForWeapon;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Обычный меч
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestFireBow extends AbstractWeaponImpl {

  @Autowired
  BeanFactory beanFactory;

  public static final String CLASS_NAME = "Тестовый Огненный лук";
  public static final String SECOND_WEAPON_NAME = "Тестовое острие огненного лука";
  private final String OUID = "WepFireBow_" + UUID.randomUUID().toString();

  @PostConstruct
  private void initAbilities(){
    Ability fireArrowAbility = beanFactory.getBean(AbilityFireArrowForWeapon.class, this, 1, -1, -1);
    this.abilities.put("Огненная стрела", fireArrowAbility);
  }

  public TestFireBow() {
    super();
    this.id = OUID;
    this.title = CLASS_NAME;
    this.description = "Огненный лук. Двуручное оружие. При ближнем бое работает как кинжал";
    this.meleeMinDamage = 1;
    this.meleeMaxDamage = 3;
    this.rangedMinDamage = 1;
    this.rangedMaxDamage = 7;
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
