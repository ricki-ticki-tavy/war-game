package api.enums;

import static api.enums.EventType.*;

/**
 * тип событий в которых измеряются времена жизни влияний, действующих на юнит
 */
public enum LifeTimeUnit {
  FULL_ROUND("игровой круг", ROUND_FULL)
  , ROUND_START("в начале хода игрока", PLAYER_TAKE_TURN)
  , ROUND_END("в конце хода игрока", PLAYER_LOOSE_TURN)
  , USING_TIMES("Разы использования", ALWAYS);


  private String title;
  private EventType eventType;
  public String getTitle(){
    return title;
  }

  private LifeTimeUnit(String title, EventType eventType){
    this.title = title;
    this.eventType = eventType;
  }
}
