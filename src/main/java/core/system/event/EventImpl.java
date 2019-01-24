package core.system.event;

import api.core.Context;
import api.enums.EventType;
import api.game.Event;
import api.game.EventDataContainer;

import java.util.Collections;
import java.util.Map;

public class EventImpl implements Event{

  protected Context sourceContext;
  protected EventType eventType;
  protected Event causeEvent;
  protected EventDataContainer source;
  protected EventDataContainer target;
  protected Object error;
  protected Map<String, Object> params;

  public Context getSourceContext() {
    return sourceContext;
  }

  public EventType getEventType() {
    return eventType;
  }

  public Event getCauseEvent() {
    return causeEvent;
  }

  public <T extends Object> T  getSource(Class<T> clazz) {
    return source.get(clazz);
  }

  public <T extends Object> T  getTarget(Class<T> clazz) {
    return target.get(clazz);
  }

  public Object getError() {
    return error;
  }

  public void fail(Object errorObject) {
    error = errorObject;
  }

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
  public EventImpl(
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
  public EventImpl(
          Context sourceContext
          , EventImpl causeEvent
          , EventType eventType
          , EventDataContainer source
          , EventDataContainer target
          , Map<String, Object> params) {
    init(sourceContext, causeEvent, eventType, source, target, params);
  }

  public Event fire() {
    sourceContext.fireGameEvent(this);
    return this;
  }
}
