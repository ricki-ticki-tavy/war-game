package core.system;

import api.core.Result;
import api.game.Coords;
import api.game.Rectangle;

import static core.system.error.GameErrors.SYSTEM_NOT_REALIZED;

/**
 * Класс для обсчета доступнсти перемещений, являющийся, по сути, сам координатой
 */
public class ActiveCoords extends Coords{

  public ActiveCoords(Coords coords){
    super(coords);
  }
  //===================================================================================================
// TODO реализовать
  /**
   * Возвращает допустимые координаты, исходя из заградительного периметра и вектора направленя
   * от текущей координаты к новой
   * @param newCoords
   * @param perimeter
   * @return
   */
  public Result<ActiveCoords> tryMoveToWithPerimeter(Coords newCoords, Rectangle perimeter){
    return ResultImpl.fail(SYSTEM_NOT_REALIZED.getError("ActiveCoords.tryMoveToWithPerimeter"));
  }
  //===================================================================================================
}
