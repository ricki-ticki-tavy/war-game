package api.enums;

/**
 * Тип активатора действия, способности
 */
public enum ActorTypeEnum {
  PLAYER("Игрок")
  , WARRIOR("Воин")
  , WEAPON("Оружие")
  ;
  private String title;

  public String getTitle() {
    return title;
  }

  private ActorTypeEnum(String title){
    this.title = title;
  }
}
