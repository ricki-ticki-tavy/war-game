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
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {StartGameWith2PlayersAndMovesTest.class})
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class EventRoutinesTest extends AbstractMapTest{

//  @Autowired
  GameWrapper gameWrapper;

  public EventRoutinesTest setGameWrapper(GameWrapper gameWrapper){
    this.gameWrapper = gameWrapper;
    return this;
  }

  public void innerDoTest(){
    initMap(gameWrapper);

    // Проверка отработки события готовности к игре
    AtomicBoolean eventDone = new AtomicBoolean(false);
    String subId = gameWrapper.getCore().findGameContextByUID(gameContext).getResult().subscribeEvent(event -> eventDone.set(true), EventType.PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS);
    // игрок 1 готов
    Result<Player> playerResult = gameWrapper.playerReady(player1, true);
    assertSuccess(playerResult);
    Assert.isTrue(eventDone.get(), "события не было");

    // отпишемся от события
    gameWrapper.getCore().findGameContextByUID(gameContext).getResult().unsubscribeEvent(subId, EventType.PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS);
    eventDone.set(false);
    // игрок 1 НЕ готов
    playerResult = gameWrapper.playerReady(player1, false);
    assertSuccess(playerResult);
    Assert.isTrue(!eventDone.get(), "события было");

    // опять подпишемся на это событие
    subId = gameWrapper.getCore().findGameContextByUID(gameContext).getResult().subscribeEvent(event -> eventDone.set(true), EventType.PLAYER_CHANGED_ITS_READY_TO_PLAY_STATUS);
    // игрок 1 готов
    playerResult = gameWrapper.playerReady(player1, true);
    assertSuccess(playerResult);
    Assert.isTrue(eventDone.get(), "события не было");

    // теперь отпишемся без указания типа события
    gameWrapper.getCore().findGameContextByUID(gameContext).getResult().unsubscribeEvent(subId);
    eventDone.set(false);
    // игрок 1 НЕ готов
    playerResult = gameWrapper.playerReady(player1, false);
    assertSuccess(playerResult);
    Assert.isTrue(!eventDone.get(), "события было");

    Result r = gameWrapper.getCore().removeGameContext(gameContext);
    assertSuccess(r);

  }

//  @Test
//  public void doTest(){
//    innerDoTest();
//  }
}
