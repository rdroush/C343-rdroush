/**
 * This is a simple structure to encapsulate a numbered wire.
 * 
 * TODO: Implement separation().
 */

public class Wire {
  protected Coord from, to;
  protected int wireId;

  /**
   * Creates a new wire between grid cells at the given coordinates.
   */
  public Wire(int wireId, Coord from, Coord to) {
    this.wireId = wireId;
    this.from = from;
    this.to = to;
  }

  /**
   * Creates a new wire between grid cells at the given coordinates.
   */
  public Wire(int wireNumber, int x1, int y1, int x2, int y2) {
    this(wireNumber, new Coord(x1, y1), new Coord(x2, y2));
  }
  
  /**
   * Returns the minimal distance separating the two endpoints of this wire.
   * The result corresponds to the length of the shortest connecting wire.
   * This is also known of as the Manhattan Distance between two points on
   * a grid. 
   */
  public int separation() {
	int rise = (Math.max(from.y, to.y) - Math.min(from.y, to.y));
	int run = (Math.max(from.x, to.x) - Math.min(from.x, to.x));

    return rise+run;
  }
  
  /**
   * Returns a textual representation of this wire.
   */
  public String toString() {
    return String.format("Wire[%d:%s,%s]", wireId, from, to);
  }
}
