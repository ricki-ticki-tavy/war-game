import api.core.Result;
import api.enums.EventType;
import api.game.map.Player;
import api.game.wraper.GameWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Проверка подписывания и отписывания от событий.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartGameWith2PlayersAndMovesTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class ComplexTest{

  @Autowired
  GameWrapper gameWrapper;

  protected GameWrapper getGameWrapper() {
    return gameWrapper;
  }

  private void innerDoTest(){
    new CreateWarriorsWeaponsUsersTest().setGameWrapper(gameWrapper).innerDoTest();
    new EventRoutinesTest().setGameWrapper(gameWrapper).innerDoTest();
    new StartGameWith2PlayersAndMovesTest().setGameWrapper(gameWrapper).innerDoTest();
  }

  @Test
  public void doTest(){
    innerDoTest();
  }
}
