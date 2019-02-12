import api.game.Coords;
import api.game.Rectangle;
import api.game.map.LevelMap;
import core.entity.map.LevelMapImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;
import tests.config.TestContextConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MoveCheckTest.class})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
public class MoveCheckTest {

  @Autowired
  LevelMap levelMap;

  @Test
  public void testRestrictionPerimeter() {
    LevelMapImpl map = (LevelMapImpl) levelMap;
    Rectangle perimeter = new Rectangle(new Coords(300, 300), new Coords(500, 500));
    Coords baseCoords = new Coords(400, 400);
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(100, 100), perimeter).getResult().equals(300, 300), "1-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(100, 200), perimeter).getResult().equals(300, 333), "2-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(200, 100), perimeter).getResult().equals(333, 300), "3-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(400, 100), perimeter).getResult().equals(400, 300), "4-st failed");

    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(700, 100), perimeter).getResult().equals(500, 300), "5-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(700, 200), perimeter).getResult().equals(500, 333), "6-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(600, 100), perimeter).getResult().equals(467, 300), "7-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(700, 400), perimeter).getResult().equals(500, 400), "8-st failed");

    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(700, 700), perimeter).getResult().equals(500, 500), "9-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(700, 600), perimeter).getResult().equals(500, 467), "10-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(600, 700), perimeter).getResult().equals(467, 500), "11-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(400, 700), perimeter).getResult().equals(400, 500), "12-st failed");

    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(100, 700), perimeter).getResult().equals(300, 500), "13-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(100, 600), perimeter).getResult().equals(300, 467), "14-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(200, 700), perimeter).getResult().equals(333, 500), "15-st failed");
    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(100, 400), perimeter).getResult().equals(300, 400), "16-st failed");

    Assert.isTrue(map.tryMoveToWithPerimeter(baseCoords, new Coords(450, 350), perimeter).getResult().equals(450, 350), "17-st failed");


  }
}
