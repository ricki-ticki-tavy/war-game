package api.old.rule.attribute;

import api.old.structure.NamedObject;

/**
 * тип создания, на которое оказывается воздействие
 */
public enum CreatureTargeter implements NamedObject{
  PLAYER("Этот игрок"),
  ENEMY_PLAYER("Игрок-противник"),
  TARGETED("Выбранный"),
  ENEMY("Вражеский"),
  OWNED("Собственный"),
  THIS("Этот"),
  ALL("Все");
  private String caption;

  CreatureTargeter(String caption){
    this.caption = caption;
  }

  @Override
  public String getTitle() {
    return caption;
  }
}
