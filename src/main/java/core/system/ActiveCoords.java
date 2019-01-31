package core.system;

import api.core.Result;
import api.entity.warrior.HasCoordinates;
import api.game.Coords;
import api.game.Rectangle;
import core.system.error.GameError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Класс для обсчета доступнсти перемещений, являющийся, по сути, сам координатой
 */
public class ActiveCoords extends Coords {

  public ActiveCoords(Coords coords) {
    super(coords);
  }
  //===================================================================================================

  public ActiveCoords(int x, int y) {
    super(x, y);
  }
  //===================================================================================================
// TODO реализовать

  /**
   * Возвращает допустимые координаты, исходя из заградительного периметра и вектора направленя
   * от текущей координаты к новой
   *
   * @param newCoords
   * @param perimeter
   * @return
   */
  public Result<ActiveCoords> tryMoveToWithPerimeter(Coords newCoords, Rectangle perimeter) {
    BigDecimal functionYX = newCoords.getX() == getX()
            ? BigDecimal.ZERO
            : new BigDecimal(newCoords.getY() - getY()).divide(
            new BigDecimal(newCoords.getX() - getX()), 5, RoundingMode.HALF_UP);
    BigDecimal reverseFunctionXY = newCoords.getY() == getY()
            ? BigDecimal.ZERO
            : new BigDecimal(newCoords.getX() - getX()).divide(
            new BigDecimal(newCoords.getY() - getY()), 5, RoundingMode.HALF_UP);
    int newX = newCoords.getX();
    int newY = newCoords.getY();

    if (perimeter.getTopLeftConner().getX() > newX) {
      // точка левее левого горизонта
      newY += functionYX.multiply(new BigDecimal(perimeter.getTopLeftConner().getX() - newX)).intValue();
      newX = perimeter.getTopLeftConner().getX();
    } else if (perimeter.getBottomRightConner().getX() < newX) {
      // точка правее правого горионта
      newY += functionYX.multiply(new BigDecimal(perimeter.getBottomRightConner().getX() - newX)).intValue();
      newX = perimeter.getBottomRightConner().getX();
    }

    if (perimeter.getTopLeftConner().getY() > newY) {
      // точка выше верхнего горизонта. Опустим ее до верхнего гоизонта и скорректируем ось Y
      newX += reverseFunctionXY.multiply(new BigDecimal(perimeter.getTopLeftConner().getY() - newY)).intValue();
      newY = perimeter.getTopLeftConner().getY();
    } else if (perimeter.getBottomRightConner().getY() < newY) {
      // точка ниже нижнего горионта
      newX += reverseFunctionXY.multiply(new BigDecimal(perimeter.getBottomRightConner().getY() - newY)).intValue();
      newY = perimeter.getBottomRightConner().getY();
    }

    return ResultImpl.success(new ActiveCoords(newX, newY));
  }
  //===================================================================================================

  private int calcQuadOfWayLength(Coords pointFrom, Coords pointTo) {
    return (pointFrom.getX() - pointTo.getX()) * (pointFrom.getX() - pointTo.getX())
            + (pointFrom.getY() - pointTo.getY()) * (pointFrom.getY() - pointTo.getY());
  }
  //===================================================================================================

  /**
   * Попытка перемещения юнита на новую координату
   *
   * @param newCoords
   * @param otherObjects
   * @param objectSize
   * @param maxWayLengthInPixels - максимальная дальность перемещения в "пикселах"
   * @param perimeter
   * @return
   */
  public Result<Coords> tryToMove(Coords newCoords, List<HasCoordinates> otherObjects, int objectSize, int maxWayLengthInPixels, Rectangle perimeter) {
    if (perimeter != null) {
      newCoords = (Coords) tryMoveToWithPerimeter(newCoords, perimeter);
    }

    final int quadObjectSize = objectSize * objectSize;
    final Coords testCoords = new Coords(newCoords);
    return otherObjects.stream()
            .filter(hasCoordinates -> quadObjectSize < calcQuadOfWayLength(hasCoordinates.getCoords(), testCoords))
            .findFirst()
            .map(hasCoordinates -> ResultImpl.fail(new GameError("", "")))
            .orElse(ResultImpl.success(testCoords));
  }
  //===================================================================================================
}
