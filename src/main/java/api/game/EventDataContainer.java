package api.game;

import api.entity.base.BaseEntityHeader;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * источник события. Содержит объекты, необходимые для переачи в сообщение.
 * Извлекаются по типу класса
 */
public class EventDataContainer {
  private Set<Object> data = new HashSet<>(3);

  public EventDataContainer(Object... objects) {
    if (objects != null)
      Arrays.stream(objects).forEach(object -> data.add(object));
  }

  public <T extends BaseEntityHeader> T get(Class<T> clazz) {
    return (T) data.stream().filter(object -> ClassUtils.isAssignable(clazz, object.getClass())).findFirst().get();
  }
}
