package api.game;

import org.apache.logging.log4j.util.Strings;

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

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Coords && this.x == ((Coords)obj).x && this.y == ((Coords)obj).y;
  }

  @Override
  public String toString() {
    return x + ":" + y;
  }
}
