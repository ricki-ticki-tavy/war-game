package api.enums;

/**
 * Тип активатора действия, способности
 */
public enum OwnerTypeEnum {
  PLAYER("Игрок")
  , WARRIOR("Воин")
  , WEAPON("Оружие")
  , ARTIFACT("Артифакт")
  , SYSTEM("внутреннее")
  ;
  private String title;

  public String getTitle() {
    return title;
  }

  OwnerTypeEnum(String title){
    this.title = title;
  }
}
