package api.game;

import api.core.GameContext;
import api.entity.base.BaseEntityHeader;
import api.enums.EventType;

import java.util.Map;

public class GameEvent {

  protected GameContext sourceContext;
  protected EventType eventType;
  protected GameEvent causeEvent;
  protected BaseEntityHeader source;
  protected Object error;
  protected Map<String, Object> params;
  /**
   * Получить контекст
   *
   * @return
   */
  public GameContext getSourceContext() {
    return sourceContext;
  }

  /**
   * Тип события
   *
   * @return
   */
  public EventType getEventType() {
    return eventType;
  }

  /**
   * Событие, приведшее к этому событию
   *
   * @return
   */
  public GameEvent getCauseEvent() {
    return causeEvent;
  }

  /**
   * Инициатор события. Игрок, воин, система
   *
   * @return
   */
  public BaseEntityHeader getSource() {
    return source;
  }

  /**
   * Ошибка, если была
   *
   * @return
   */
  public Object getError() {
    return error;
  }

  /**
   * Сохранить ошибку
   *
   * @param errorObject
   */
  public void fail(Object errorObject) {
    error = errorObject;
  }

  /**
   * Получить набор параметров события.
   * @return
   */
  public Map<String, Object> getParams(){
    return params;
  }


  /**
   *
   * @param sourceContext
   * @param causeEvent
   * @param eventType
   * @param source
   * @param params
   */
  public GameEvent(
          GameContext sourceContext
          , GameEvent causeEvent
          , EventType eventType
          , BaseEntityHeader source
          , Map<String, Object> params) {
    this.sourceContext = sourceContext;
    this.eventType = eventType;
    this.source = source;
    this.causeEvent = causeEvent;
    this.causeEvent = causeEvent;
    this.params = params;
  }

  /**
   * инициировать обработку события
   *
   * @return
   */
  GameEvent fire() {
    sourceContext.fireGameEvent(this);
    return this;
  }
}
