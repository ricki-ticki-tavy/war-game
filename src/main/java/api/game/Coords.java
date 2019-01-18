package api.game;

/**
 * Координаты
 */
public class Coords {
  private int x, y;

  public Coords(int x, int y){
    this.x = x;
    this.y = y;
  }

  public Coords(Coords source){
    this.x = source.getX();
    this.y = source.getY();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
