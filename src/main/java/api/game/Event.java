package api.game;

import api.core.Context;
import api.entity.base.BaseEntityHeader;
import api.enums.EventType;

import java.util.Collections;
import java.util.Map;

public class Event {

  protected Context sourceContext;
  protected EventType eventType;
  protected Event causeEvent;
  protected EventDataContainer source;
  protected EventDataContainer target;
  protected Object error;
  protected Map<String, Object> params;
  /**
   * Получить контекст
   *
   * @return
   */
  public Context getSourceContext() {
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
  public Event getCauseEvent() {
    return causeEvent;
  }

  /**
   * Инициатор события. Игрок, воин, система
   *
   * @return
   */
  public <T extends Object> T  getSource(Class<T> clazz) {
    return source.get(clazz);
  }

  /**
   * На кого направлено событие. Игрок, воин, система
   *
   * @return
   */
  public <T extends Object> T  getTarget(Class<T> clazz) {
    return target.get(clazz);
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

  private void init(Context sourceContext
          , Event causeEvent
          , EventType eventType
          , EventDataContainer source
          , EventDataContainer target
          , Map<String, Object> params){
    this.sourceContext = sourceContext;
    this.eventType = eventType;
    this.source = source;
    this.causeEvent = causeEvent;
    this.causeEvent = causeEvent;
    this.params = params != null ? params : Collections.EMPTY_MAP;
    this.target = target != null ? target : new EventDataContainer();
  }

  /**
   *
   * @param sourceContext
   * @param causeEvent
   * @param eventType
   * @param source
   * @param params
   */
  public Event(
          Context sourceContext
          , Event causeEvent
          , EventType eventType
          , EventDataContainer source
          , Map<String, Object> params) {
    init(sourceContext, causeEvent, eventType, source, null, params);
  }

  /**
   *
   * @param sourceContext
   * @param causeEvent
   * @param eventType
   * @param source
   * @param params
   */
  public Event(
          Context sourceContext
          , Event causeEvent
          , EventType eventType
          , EventDataContainer source
          , EventDataContainer target
          , Map<String, Object> params) {
    init(sourceContext, causeEvent, eventType, source, target, params);
  }

  /**
   * инициировать обработку события
   *
   * @return
   */
  Event fire() {
    sourceContext.fireGameEvent(this);
    return this;
  }
}
