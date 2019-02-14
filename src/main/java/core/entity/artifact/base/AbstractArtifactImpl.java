package core.entity.artifact.base;

import api.core.Owner;
import api.entity.stuff.Artifact;
import api.entity.warrior.Warrior;
import api.enums.OwnerTypeEnum;
import api.enums.TargetTypeEnum;
import api.game.ability.Ability;
import core.entity.abstracts.AbstractOwnerImpl;
import core.game.action.InfluenceResultImpl;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static core.system.error.GameErrors.ARTIFACT_WRONG_ABILITY;

/**
 * базовый класс артефакта
 */
public abstract class AbstractArtifactImpl<T extends Owner> extends AbstractOwnerImpl<T> implements Artifact {
  protected final Map<String, Ability> abilities = new ConcurrentHashMap<>(10);
  protected OwnerTypeEnum ownerTypeForArtifact;

  /**
   * @param owner                 - владелец артифакта
   * @param ownerTypeForArtifact  - кто может владеть артефактом
   * @param idPrefix              - Префикс кода. Может быть пустой или null строкой
   * @param title                 - название артефакта
   * @param description           - описание
   * @param abilities             - способности артефакта
   */
  public AbstractArtifactImpl(T owner, OwnerTypeEnum ownerTypeForArtifact, String idPrefix, String title, String description, Ability... abilities) {
    super(owner, OwnerTypeEnum.ARTIFACT, StringUtils.isEmpty(idPrefix) ? "art" : idPrefix, title, description);
    // Кто может
    this.ownerTypeForArtifact = ownerTypeForArtifact;

    // проверим какие у нас тут способности
    Arrays.stream(abilities).forEach(ability -> {
      if (!ability.getOwnerTypeForAbility().equals(OwnerTypeEnum.ARTIFACT)) {
        // эта способность не для артефактов
        throw ARTIFACT_WRONG_ABILITY.getError(getContext().getGameName(), title, ability.getTitle(), ability.getOwnerTypeForAbility().getTitle());
      }
      // добавим способность в список
      this.abilities.put(ability.getTitle(), ability);
    });
  }
  //===================================================================================================

  @Override
  public OwnerTypeEnum getOwnerTypeForArtifact() {
    return ownerTypeForArtifact;
  }
  //===================================================================================================

  @Override
  public List<Ability> getAbilities() {
    return new ArrayList<>(abilities.values());
  }
  //===================================================================================================

  @Override
  public Artifact revival() {
    abilities.values().stream()
            .forEach(ability -> ability.revival());
    return this;
  }
  //===================================================================================================

  @Override
  public Artifact attachToOwner(Owner owner) {
    this.owner = (T)owner;
    return this;
  }
  //===================================================================================================

  public Artifact<T> applyToOwner(){
    switch (ownerTypeForArtifact){
      case WARRIOR:
        abilities.values().stream()
                // только те берем, что применимы к владельцу
                .filter(ability -> ability.getTargetType().equals(TargetTypeEnum.THIS_WARRIOR))
                // строим влияния по способности
                .forEach(ability -> ability.buildForTarget((Warrior) owner).stream()
                        // Применяем влияния
                        .forEach(influencer -> influencer.applyToWarrior(InfluenceResultImpl.forPositive((Warrior)owner))));
        break;
    }
    return this;
  }
  //===================================================================================================

}
