package api.enums;

public enum AttributeEnum{
  HEALTH("Очки жизни", Integer.class),
  MANNA("Очки магии", Integer.class),
  ARMOR("Уровень брони", Integer.class),
  DISTANCE_ATTACK_COST("Стоимость дистанционной атаки в очках действия", Integer.class),
  DISTANCE_ATTACK_MIN_ATTACK_RANGE("Минимальная дальность дистанционной атаки", Integer.class),
  DISTANCE_ATTACK_MAX_ATTACK_RANGE("Максимальная дальность дистанционной атаки", Integer.class),
  DISTANCE_ATTACK_MIN_DAMAGE("Минимальный урон дистанционной атаки", Integer.class),
  DISTANCE_ATTACK_MAX_DAMAGE("Максимальный урон дистанционной атаки", Integer.class),
  DISTANCE_ATTACK_FADE_RANGE_START("Дальность начала спадания поражения", Integer.class),
  DISTANCE_ATTACK_FADE_PERCENT_DAMAGE_PER_LENGTH("процент спадания урона на единицу длины", Integer.class),
  MELEE_ATTACK_MIN_DAMAGE("Минимальный урон рукопашной атаки", Integer.class),
  MELEE_ATTACK_MAX_DAMAGE("Максимальный урон рукопашной атаки", Integer.class),

  MOVE_COST_COEF("Коэффициент цены за передвижение", Double.class),
  ACTION_POINT("Кол-во очков действия", Integer.class),
  MAGIC_POINT("Кол-во очков магии", Integer.class),
  X_COORD("координата X", Integer.class),
  Y_COORD("координата Y", Integer.class),
  RANGED_ATTACK_LUCK("Удача в стрельбе", String.class),
  MELEE_ATTACK_LUCK("Удача в рукопашной атаке", String.class),
  ATTACK_LUCK("Удача в атаке", String.class),
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

  private AttributeEnum(String caption, Class type){
    this.caption = caption;
    this.type = type;
  }

  public String getCaption() {
    return caption;
  }

  public Class getType() {
    return type;
  }
}
