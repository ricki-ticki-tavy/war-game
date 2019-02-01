package api.enums;

import api.entity.base.BaseEntityHeader;

/**
 * Тип фазы игрока
 */
public enum PlayerPhaseType implements BaseEntityHeader{
  PLAYER_PHASE_TYPE_DEFENSE("Режим защиты")
  , PLAYER_PHASE_TYPE_ATACK("Режим хода (атаки)");
  private String title;


  @Override
  public String getId() {
    return getTitle();
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return getTitle();
  }

  private PlayerPhaseType(String title){
    this.title = title;
  }
}
