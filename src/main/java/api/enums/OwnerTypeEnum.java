package api.enums;

/**
 * Тип активатора действия, способности
 */
public enum OwnerTypeEnum {
  PLAYER("Игрок")
  , WARRIOR("Воин")
  , WEAPON("Оружие")
  ;
  private String title;

  public String getTitle() {
    return title;
  }

  private OwnerTypeEnum(String title){
    this.title = title;
  }
}
