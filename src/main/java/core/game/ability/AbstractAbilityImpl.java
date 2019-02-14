package core.game.ability;

import api.core.Context;
import api.core.Owner;
import api.entity.warrior.Warrior;
import api.entity.warrior.WarriorBaseClass;
import api.enums.OwnerTypeEnum;
import api.enums.EventType;
import api.enums.PlayerPhaseType;
import api.enums.TargetTypeEnum;
import api.game.ability.Influencer;
import api.game.ability.Ability;
import core.entity.abstracts.AbstractOwnerImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static core.system.error.GameErrors.SYSTEM_NOT_REALIZED;

/**
 * Абстрактный класс способности. Общий для всех
 */
public abstract class AbstractAbilityImpl extends AbstractOwnerImpl implements Ability {
  protected OwnerTypeEnum ownerTypeForAbility;
  protected TargetTypeEnum targetType;
  protected final Map<String, Class<? extends WarriorBaseClass>> unsupportedWarriorBaseClasses = new ConcurrentHashMap<>(10);
  protected AtomicInteger useCount = new AtomicInteger(0);
  protected final int useCountPerRound;
  private AtomicInteger currentUseCountPerPeriod = new AtomicInteger(0);
  protected Set<PlayerPhaseType> activePhases = new HashSet<>(2);

  /**
   * @param useCountPerRound максимальное кол-во использований за ход
   * @param idPrefix         префикс для ID. допустимо пустой
   * @param title
   * @param description
   */
  protected AbstractAbilityImpl(Owner owner, int useCountPerRound, String idPrefix, String title, String description) {
    super(owner, null, idPrefix, title, description);

    this.useCountPerRound = useCountPerRound;
    currentUseCountPerPeriod.set(useCountPerRound);

  }
  //===================================================================================================
  //===================================================================================================

  /**
   * Уменьшить значения счетчика использоваия способности в этом ходе
   */
  protected int decCurrentUseCountPerPeriod() {
    return currentUseCountPerPeriod.decrementAndGet();
  }
  //===================================================================================================
  //===================================================================================================
  //===================================================================================================

  @Override
  public OwnerTypeEnum getOwnerTypeForAbility() {
    return ownerTypeForAbility;
  }
  //===================================================================================================

  @Override
  public TargetTypeEnum getTargetType() {
    return targetType;
  }
  //===================================================================================================

  @Override
  public List<Class<? extends WarriorBaseClass>> getUnsupportedWarriorBaseClasses() {
    return new ArrayList<>(unsupportedWarriorBaseClasses.values());
  }
  //===================================================================================================

  @Override
  public int getTotalUseCount() {
    return useCount.get();
  }
  //===================================================================================================

  @Override
  public int getUseCountPerRound() {
    return useCountPerRound;
  }
  //===================================================================================================

  @Override
  public boolean rollback(Context context) {
    throw SYSTEM_NOT_REALIZED.getError("depricated AbstractAbilityImpl.rollback");
  }
  //===================================================================================================

  @Override
  public List<EventType> getAllTriggerTypes() {
    throw SYSTEM_NOT_REALIZED.getError("depricated AbstractAbilityImpl.getAllTriggerTypes");
  }
  //===================================================================================================

  @Override
  public boolean isValidForEvent(EventType triggerType) {
    throw SYSTEM_NOT_REALIZED.getError("AbstractAbilityImpl.isValidForEvent");
  }
  //===================================================================================================

  @Override
  public List<Influencer> buildForTarget(Warrior target) {
    return isActive() ? buildInfluencers(target) : Collections.EMPTY_LIST;
  }
  //===================================================================================================

  /**
   * метод в котором формируются объеты влияния. Определяется наследником как более узко
   * специализированным классом
   *
   * @return
   */
  protected abstract List<Influencer> buildInfluencers(Warrior target);
  //===================================================================================================

  @Override
  public boolean isActive() {
    return currentUseCountPerPeriod.get() > 0 || currentUseCountPerPeriod.get() == -1
            &&
            // проверим нет ли общих ограничений по количеству
            useCount.get() == -1 || useCount.get() > 0;
  }
  //===================================================================================================

  @Override
  public Ability revival() {
    if (useCountPerRound > 0) {
      currentUseCountPerPeriod.set(useCountPerRound);
    }
    return this;
  }
  //===================================================================================================

  @Override
  public Set<PlayerPhaseType> getActivePhase() {
    return null;
  }
  //===================================================================================================


}
