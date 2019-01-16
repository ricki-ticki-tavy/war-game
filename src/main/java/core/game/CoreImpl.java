package core.game;

import api.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Игровой движок. Все операции выполняютсяв рамках динамического игрового контекста
 */
@Component
public class CoreImpl implements Core{

  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

  @Autowired
  BeanFactory beanFactory;

  @PostConstruct
  public void init(){
    GameContext context = beanFactory.getBean(GameContext.class);
    context.loadMap(this.getClass().getClassLoader().getResourceAsStream("level1.xml"));
  }

  @Override
  //TODO
  public int getRandom(int min, int max) {
    return 0;
  }

}
