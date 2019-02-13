package api.enums;

/**
 * Тип действия
 */
public enum ManifestationOfInfluenceEnum {
  POSITIVE("Положительное")
  , NEGATIVE("Отрицательное");

  private String title;

  public String getTitle() {
    return title;
  }

  private ManifestationOfInfluenceEnum(String title){
    this.title = title;
  }
}
