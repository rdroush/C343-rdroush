import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * A Board represents the current state of the game. Boards know their dimension, 
 * the collection of tiles that are inside the current flooded region, and those tiles 
 * that are on the outside.
 * 
 * @author Reagan Roush
 */

public class Board {
  private Map<Coord, Tile> inside, outside, dormant;
  private int size;
  private boolean first = true;
  
  /**
   * Constructs a square game board of the given size, initializes the list of 
   * inside tiles to include just the tile in the upper left corner, and puts 
   * all the other tiles in the outside list.
   * 
   * @param size The size of the board to be constructed.
   */
  public Board(int size) {
    // A tile is either inside or outside the current flooded region.
    inside = new HashMap<>();
    outside = new HashMap<>();
    dormant = new HashMap<>();
    this.size = size;
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++) {
        Coord coord = new Coord(x, y);
        outside.put(coord, new Tile(coord));
      }
    // Move the corner tile into the flooded region and run flood on its color.
    Tile corner = outside.remove(Coord.ORIGIN);
    inside.put(Coord.ORIGIN, corner);
    flood(corner.getColor());
  }
  
  /**
   * Constructs a square game board of the given size, with each tile having the same color,
   * initializes the list of inside tiles to include just the tile in the upper left corner, and puts 
   * all the other tiles in the outside list.
   * 
   * @param size The size of the board to be constructed.
   * @param color The color of all the tiles in the Board.
   */
  public Board(int size, WaterColor color) {
	    // A tile is either inside or outside the current flooded region.
	    inside = new HashMap<>();
	    outside = new HashMap<>();
	    dormant = new HashMap<>();
	    this.size = size;
	    for (int y = 0; y < size; y++)
	      for (int x = 0; x < size; x++) {
	        Coord coord = new Coord(x, y);
	        outside.put(coord, new Tile(coord));
	        get(coord).setColor(color);
	      }
	    // Move the corner tile into the flooded region and run flood on its color.
	    Tile corner = outside.remove(Coord.ORIGIN);
	    inside.put(Coord.ORIGIN, corner);
	    flood(corner.getColor());
  }
  
  /**
   * Returns the tile at the specified coordinate.
   * 
   * @param coord The coordinate where the tile is located.
   * @return The tile at the specified coordinate.
   */ 
  public Tile get(Coord coord) {
    if (outside.containsKey(coord))
      return outside.get(coord);
    else if(dormant.containsKey(coord))
    	return dormant.get(coord);
    return inside.get(coord);
  }
  
  /**
   * Returns the size of this board.
   * 
   * @return The size of this board.
   */
  public int getSize() {
    return size;
  }
  
  /**
   * Returns true iff outside map is empty.
   * 
   * @return true iff outside map is empty.
   */
  public boolean fullyFlooded() {
    return outside.isEmpty();
  }

  /**
   * Flood function that uses a while loop, continuing until
   * the point is reached where no necessary candidates are found.
   * 
   * @param color The color to flood the inside region.
   */
  public void flood(WaterColor color) {
	  Set<Coord> candidates = new HashSet<>();
	  Set<Coord> surrounded = new HashSet<>();
	  boolean noCandidates = false;
	  if(first) {
		  first = false;
	      flood(inside.get(Coord.ORIGIN).getColor());
	  }
	  while(!noCandidates) {
		  // check if our neighbors need to be added
		  inside.forEach((k, v)-> {
			  List<Coord> neighbors = k.neighbors(getSize());
			  neighbors.forEach((c)-> {
				  if(get(c).getColor() == color && outside.containsKey(c)) {
					  candidates.add(c);
				  }
			  });
		  }
		  );
		  if(candidates.isEmpty())
			  noCandidates = true;
		  else {
			  // add the candidates to inside and remove from outside
			  candidates.forEach((k)-> {  
				  inside.put(k, get(k));
				  outside.remove(k);
			  }
			  );
			  // check if each tile is surrounded and add to the surrounded set if it is
			  inside.forEach((k, v)-> {
				  if(isSurrounded(k)) {
					  surrounded.add(k);
				  }
			  }
			  );
			  // move each surrounded tile to the dormant map, removing them from the "active" inside map
			  surrounded.forEach((k)-> {
				  dormant.put(k, get(k));
				  inside.remove(k, get(k));
			  }
			  );
			  inside.forEach((k,v)->v.setColor(color)); 
			  dormant.forEach((k,v)->v.setColor(color));
			  surrounded.clear();
			  candidates.clear();
		  }
	  }
  }
  
  /**
   * Flood function similar to the chosen one above,
   * that recursively calls flood() until the point 
   * is reached that no necessary candidates are found.
   * This was abandoned because the flow is messier
   * compared to using a while loop. It also tries to add candidates
   * even in the case where no candidates are found.
   * 
   * @param color The color to flood the inside region.
   */
  public void flood1(WaterColor color) {
	  Set<Coord> candidates = new HashSet<>();
	  Set<Coord> surrounded = new HashSet<>();
	  
	  if(first) {
		  first = false;
	      flood1(inside.get(Coord.ORIGIN).getColor());
	  }
	  
	  // check if our neighbors need to be added
	  inside.forEach((k, v)-> {
		  List<Coord> neighbors = k.neighbors(getSize());
		  neighbors.forEach((c)-> {
			  if(get(c).getColor() == color && outside.containsKey(c)) {
				  candidates.add(c);
			  }
		  });
	  }
	  );
	  // add the candidates to inside and remove from outside
	  candidates.forEach((k)-> {  
		  inside.put(k, get(k));
		  outside.remove(k);
	  }
	  );
	  // check if each tile is surrounded and add to the surrounded set if it is
	  inside.forEach((k, v)-> {
		  if(isSurrounded(k)) {
			  surrounded.add(k);
		  }
	  }
	  );
	  // move each surrounded tile to the dormant map, removing them from the "active" inside map
	  surrounded.forEach((k)-> {
		  dormant.put(k, get(k));
		  inside.remove(k, get(k));
	  }
	  );
	  inside.forEach((k,v)->v.setColor(color)); 
	  dormant.forEach((k,v)->v.setColor(color));
	  // recursively flood unless no candidates were found
	  if(!candidates.isEmpty()) {
		  flood1(color);
	  }
  }
 
  /**
   * Checks if a tile is surrounded on all sides - by another inside tile, or by the board's boundary.
   * 
   * @param c The coordinate to be checked.
   * @return true if it is surrounded, false if not
   */
  private boolean isSurrounded(Coord c) {
	  return (inside.containsKey(c.up()) || dormant.containsKey(c.up()) || !c.up().onBoard(getSize())) &&
			 (inside.containsKey(c.down()) || dormant.containsKey(c.down()) || !c.down().onBoard(getSize())) &&
			 (inside.containsKey(c.left()) || dormant.containsKey(c.left()) || !c.left().onBoard(getSize())) &&
			 (inside.containsKey(c.right()) || dormant.containsKey(c.right()) || !c.right().onBoard(getSize()));
  }

  /**
   * Returns the "best" GameColor for the next move with a modified "dry" vesion of the 
   * flood() method for each color, using temporary copies of the inside/outside maps.
   * The color with the maximum inside+dormant size (or the first such one in the case of a tie)
   * will be returned.
   * 
   * @return the "best" GameColor for the next move.
   */
  public WaterColor suggest() {
	  WaterColor[] colors = WaterColor.values();
	  int maxInside = inside.size() + dormant.size();
	  WaterColor bestColor = colors[0];
	  
	  for(WaterColor color : colors) {
		  Map<Coord, Tile> tempInside = new HashMap<>();
		  Map<Coord, Tile> tempDormant = new HashMap<>();
		  Map<Coord, Tile> tempOutside = new HashMap<>();
		  Set<Coord> tempCandidates = new HashSet<>();
		  Set<Coord> tempSurrounded = new HashSet<>();
		  boolean noCandidates = false;
		  
		  tempInside.putAll(inside);
		  tempDormant.putAll(dormant);
		  tempOutside.putAll(outside);
		  
		  while(!noCandidates) {
			  tempInside.forEach((k, v)-> {
				  List<Coord> neighbors = k.neighbors(getSize());
				  neighbors.forEach((c)-> {
					  if(get(c).getColor() == color && tempOutside.containsKey(c)) {
						  tempCandidates.add(c);
					  }
				  });
			  }
			  );
			  if(tempCandidates.isEmpty()) {
				  noCandidates = true;
			  }
			  else {
				  tempCandidates.forEach((k)-> { 
					  tempInside.put(k, get(k));
					  tempOutside.remove(k);
				  }
				  );
				  tempInside.forEach((k, v)-> {
					  if(isSurrounded(k)) {
						  tempSurrounded.add(k);
					  }
				  }
				  );
				  tempSurrounded.forEach((k)-> {
					  tempDormant.put(k, get(k));
					  tempInside.remove(k, get(k));
				  }
				  );
				  tempCandidates.clear();
			  }
		  }
		  if(tempInside.size() + tempDormant.size() > maxInside) {
			  maxInside = tempInside.size() + tempDormant.size();
			  bestColor = color;
		  }
	  }
	  
	  return bestColor;
  }
  
  /**
   * Returns a string representation of this board. Tiles are given as their
   * color names, with those inside the flooded region written in uppercase.
   * 
   * @return A string representation of this board.
   */ 
  public String toString() {
    StringBuilder ans = new StringBuilder();
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        Coord curr = new Coord(x, y);
        WaterColor color = get(curr).getColor();
        ans.append((inside.containsKey(curr) || dormant.containsKey(curr)) ? color.toString().toUpperCase() : color);
        ans.append("\t");
      }
      ans.append("\n");
    }
    return ans.toString();
  }
  
  /**
   * Simple testing.
   */
  public static void main(String... args) {
    // Print out boards of size 1, 2, ..., 5
    int n = 5;
    for (int size = 1; size <= n; size++) {
      Board someBoard = new Board(size);
      System.out.println(someBoard);
    }
  }
}






