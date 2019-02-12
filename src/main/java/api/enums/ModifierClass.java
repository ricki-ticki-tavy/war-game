package api.enums;

/**
 * Класс влияния, оказываемого модификатором. Нужно для введения сопротивляемости влияниям.
 */
public enum ModifierClass {
  PHYSICAL("Физическое")
  , FIRE("Огонь")
  , POISON("Яд")
  , COLD("Холод")
  , MAGIC("Магия")
  ;
  private String title;

  public String getTitle() {
    return title;
  }

  private ModifierClass(String title){
    this.title = title;
  }

}
