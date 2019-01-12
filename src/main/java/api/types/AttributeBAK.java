package api.types;

import api.structure.AnAttribute;

public enum AttributeBAK implements AnAttribute {
  HEALTH("Очки жизни", Integer.class),
  MANNA("Очки магии", Integer.class),
  ARMOR("Уровень брони", Integer.class),
  ATTACK_RANGE("Дальность атаки", Integer.class),
  HAS_DISTANCE_ATTACK("Дистанционная атака", Boolean.class),
  ACTION_POINT_QTY("Кол-во очков действия", Integer.class),
  MAGIC_POINT_QTY("Кол-во очков магии", Integer.class),
  X_COORD("координата X", Integer.class),
  Y_COORD("координата Y", Integer.class),
  CAPTION("Название", String.class)
//  ,
//  ("", Integer.class),
//  ("", Integer.class),
//  ("", Integer.class),
//  ("", Integer.class),
//  ("", Integer.class),
//  ("", Integer.class),
//  ("", Integer.class),
//  ("", Integer.class),
  ;

  private String caption;
  private Class type;

  private AttributeBAK(String caption, Class type){
    this.caption = caption;
    this.type = type;
  }



  @Override
  public String getCaption() {
    return caption;
  }

  @Override
  public Class getType() {
    return type;
  }
}
