package api.enums;

public enum TargetTypeEnum{
  THIS_WARRIOR("Этот воин"),
  WARRIOR("Дружественный воин"),
  ENEMY_WARRIOR("Вражеский Воин"),
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
