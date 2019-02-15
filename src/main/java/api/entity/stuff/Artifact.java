package api.entity.stuff;

import api.core.Owner;
import api.enums.OwnerTypeEnum;
import api.enums.PlayerPhaseType;
import api.game.ability.Ability;

import java.util.List;

/**
 * артефакт. Предмет, которым владеет игрок или юнит.
 */
public interface Artifact<T extends Owner> extends Owner<T> {
  /**
   * Возвращает тип владельца, который может владеть данным артифактом
   *
   * @return
   */
  OwnerTypeEnum getOwnerTypeForArtifact();

  /**
   * Возвращает список способностей артефакта
   *
   * @return
   */
  List<Ability> getAbilities();

  /**
   * Восстановить артефакт, если есть ограничения на кол-во действий на ход
   *
   * @return
   */
  Artifact revival();

  /**
   * Задать владельца артефакта. Артефакт может переходить от владельца к владельцу
   *
   * @param owner
   * @return
   */
  Artifact attachToOwner(T owner);

  /**
   * применить действие артефакта на владельца
   *
   * @return
   */
  Artifact<T> applyToOwner(PlayerPhaseType phase);
}
