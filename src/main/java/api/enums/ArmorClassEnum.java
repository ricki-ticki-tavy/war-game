package api.enums;

import java.util.Arrays;

public enum ArmorClassEnum {
  ARMOR_0("Нет", 0, 6),
  ARMOR_1("легкая", 1, 8),
  ARMOR_2("Средняя", 2, 12),
  ARMOR_3("Тяжелая", 3, 15),
  ARMOR_4("Великая", 4, 20),
  ARMOR_5("Легендарная", 5, 30)
  ;

  private int armorClass;
  private int moveCost;
  private String title;

  public int getArmorClass() {
    return armorClass;
  }

  public int getMoveCost() {
    return moveCost;
  }

  public String getTitle(){
    return title;
  }

  /**
   * Класс брони, измененный на заданное значение брони
   * @return
   */
  public ArmorClassEnum changedBy(int delta){
    int armor = armorClass + delta;
    return Arrays.stream(ArmorClassEnum.values()).filter(armorClassEnum -> armorClassEnum.armorClass == armor).findFirst().get();
  }

  private ArmorClassEnum(String title, int armorClass, int moveCost){
    this.title = title;
    this.armorClass = armorClass;
    this.moveCost = moveCost;
  }


}
