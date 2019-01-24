package api.game;

/**
 * Прямоугольник
 */
public class Rectangle {
  private Coords topLeftConner;
  private Coords bottomRightConner;

  public Rectangle(Coords topLeftConner, Coords bottomRightConner){
    this.topLeftConner = topLeftConner;
    this.bottomRightConner = bottomRightConner;
  }

  public Coords getTopLeftConner() {
    return topLeftConner;
  }

  public Coords getBottomRightConner() {
    return bottomRightConner;
  }
}
