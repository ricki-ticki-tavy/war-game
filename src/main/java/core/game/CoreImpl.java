package core.game;

import api.core.Context;
import api.core.Core;
import api.enums.MapTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Игровой движок. Все операции выполняютсяв рамках динамического игрового контекста
 */
@Component
public class CoreImpl implements Core{

  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

  private Map<String, GameContext> contextMap = new ConcurrentHashMap<>(10);

  @Autowired
  BeanFactory beanFactory;

  @Override
  public Context createGame(MapTypeEnum mapType) {
    GameContext context = beanFactory.getBean(GameContext.class);
    context.loadMap(this.getClass().getClassLoader().getResourceAsStream(mapType.getMapName()));
    contextMap.put(context.getContextId(), context);
    return context;
  }

  @PostConstruct
  public void init(){
  }

  @Override
  //TODO
  public int getRandom(int min, int max) {
    return 0;
  }

}
