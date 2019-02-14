package core.entity.artifact.base;

import api.core.Owner;
import api.entity.stuff.Artifact;
import api.enums.OwnerTypeEnum;
import api.game.ability.Ability;
import core.entity.abstracts.AbstractOwnerImpl;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static core.system.error.GameErrors.ARTIFACT_WRONG_ABILITY;

/**
 * базовый класс артифакта
 */
public class BaseArtifactImpl extends AbstractOwnerImpl implements Artifact{
  protected final Map<String, Ability> abilities = new ConcurrentHashMap<>(10);
  protected OwnerTypeEnum artifactOwnerType;

  /**
   *
    * @param owner                  - владелец артифакта
   * @param artifactOwnerType       - кто может владеть артефактом
   * @param idPrefix                - Префикс кода. Может быть пустой или null строкой
   * @param title                   - название артефакта
   * @param description             - описание
   * @param abilities               - способности артефакта
   */
  public BaseArtifactImpl(Owner owner, OwnerTypeEnum artifactOwnerType, String idPrefix, String title, String description, Ability... abilities){
    super(owner, OwnerTypeEnum.ARTIFACT, idPrefix, title, description);
    // Кто может
    this.artifactOwnerType = artifactOwnerType;

    // проверим какие у нас тут способности
    Arrays.stream(abilities).forEach(ability -> {
      if (!ability.getOwnerTypeForAbility().equals(OwnerTypeEnum.ARTIFACT)){
        // эта способность не для артефактов
        throw ARTIFACT_WRONG_ABILITY.getError(getContext().getGameName(), title, ability.getTitle(), ability.getOwnerTypeForAbility().getTitle());
      }
      // добавим способность в список
      this.abilities.put(ability.getTitle(), ability);
    });
  }
  //===================================================================================================

  @Override
  public OwnerTypeEnum getArtifactOwnerType() {
    return artifactOwnerType;
  }
  //===================================================================================================

}
