package api.game;

import api.core.Context;
import api.entity.base.BaseEntityHeader;
import api.enums.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class GameEvent {
  @Autowired
  protected Context context;

  protected EventType eventType;
  protected GameEvent causeEvent;
  protected BaseEntityHeader source;
  protected BaseEntityHeader tool;
  protected Object error;

  /**
   * Получить контекст
   *
   * @return
   */
  public Context getContext() {
    return context;
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
   * Посредством чего происходит влияние. Оружие, способность,заклинание
   *
   * @return
   */
  public BaseEntityHeader getTool() {
    return tool;
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

  public GameEvent() {
  }

  /**
   * инициализация переменных собятия
   * @param eventType
   * @param causeEvent
   * @param source
   * @param tool
   * @return
   */
  public GameEvent init(EventType eventType,
                        GameEvent causeEvent,
                        BaseEntityHeader source,
                        BaseEntityHeader tool) {
    this.eventType = eventType;
    this.source = source;
    this.causeEvent = causeEvent;
    this.tool = tool;
    return this;
  }

  /**
   * инициировать обработку события
   * @return
   */
  GameEvent fire(){
    context.fireGameEvent(this);
    return this;
  }

  /**
   * Новая инстанция события
   * @return
   */
  public static GameEvent newInstance(){
    GameEvent event = new GameEvent();
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(event);
    return event;
  }

  public static GameEvent newInstance(EventType eventType,
                                      GameEvent causeEvent,
                                      BaseEntityHeader source,
                                      BaseEntityHeader tool){
    return newInstance().init(eventType, causeEvent, source, tool);
  }

}
