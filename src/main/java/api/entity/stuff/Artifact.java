package api.entity.stuff;

import api.core.Owner;
import api.enums.OwnerTypeEnum;

/**
 * артефакт. Предмет, которым владеет игрок или юнит.
 */
public interface Artifact extends Owner{
  /**
   * Возвращает тип владельца, который может владеть данным артифактом
   * @return
   */
 OwnerTypeEnum getArtifactOwnerType();

}
