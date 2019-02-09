package api.enums;

public enum TargetTypeEnum{
  ANY("Любой воин"),
  ENEMY_WARRIOR("Вражеский Воин"),
  ALLIED_WARRIOR("Дружественный воин"),
  THIS_WARRIOR("Этот воин"),
  PLAYER("Игрок"),
  ENEMY_PLAYER("Противник");

  private String caption;

  private TargetTypeEnum(String caption){
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
