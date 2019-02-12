package api.core;

import api.enums.EventType;

import java.util.Map;

public interface Event {
  /**
   * Получить контекст
   *
   * @return
   */
  Context getSourceContext();

  /**
   * Тип события
   *
   * @return
   */
  EventType getEventType();

  /**
   * Событие, приведшее к этому событию
   *
   * @return
   */
  Event getCauseEvent();

  /**
   * Инициатор события. Игрок, воин, система
   *
   * @return
   */
  <T extends Object> T  getSource(Class<T> clazz);

  /**
   * На кого направлено событие. Игрок, воин, система
   *
   * @return
   */
  <T extends Object> T  getTarget(Class<T> clazz);

  /**
   * Ошибка, если была
   *
   * @return
   */
  Object getError();

  /**
   * Сохранить ошибку
   *
   * @param errorObject
   */
  void fail(Object errorObject);

  /**
   * Получить набор параметров события.
   * @return
   */
  Map<String, Object> getParams();

  /**
   * инициировать обработку события
   *
   * @return
   */
  Event fire();
}
