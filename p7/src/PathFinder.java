import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class PathFinder {

	private static Map<Integer, Queue<Path>> pathCandidates = new HashMap<>();
	
  /**
   * Lays out a path connecting each wire on the chip, and then 
   * returns a map that associates a wire id numbers to the paths
   * corresponding to the connected wires on the grid. If it is 
   * not possible to connect the endpoints of a wire, then there
   * should be no association for the wire id# in the result.
   * 
   * The first attempt will be made to find a path without replacing
   * any existing wires. If no connection is found so far, each wire
   * that has been connected before the current one will be removed,
   * the current wire will try to connect again, and the removed wire
   * will be replaced. If no connection is found after all attempts, an
   * appropriate message is printed.A similar message is also printed if
   * the current wire is able to connect, but a replacement cannot do so
   * afterwards.
   * 
   * Status: All chips up to big_02 are solved quickly.
   * 
   * @param chip The chip to connect all wires as possible.
   * @return A map of wire id numbers and the associated paths, if applicable.
   */
  public static Map<Integer, Path> connectAllWires(Chip chip) {
	Map<Integer, Path> paths = new HashMap<>();
	Comparator<Path> pathComp = (p1, p2) -> p1.size() - p2.size();
	
	for(Wire w : chip.wires) {
		int curId = w.wireId;
		pathCandidates.put(curId, new PriorityQueue<>(pathComp));
		findPaths(chip, w);
		if(!pathCandidates.get(curId).isEmpty()) {
			Path front = pathCandidates.get(curId).peek();
			paths.put(curId, front);
			for(Coord coord : front) {
				chip.mark(coord, curId);
			}
		}
		else {
			Set<Integer> pathsToReplace = new HashSet<>();
			pathsToReplace.addAll(paths.keySet());
			
			for(Wire w2 : chip.wires) {
				int w2Id = w2.wireId;
				if(w2Id != curId && paths.containsKey(w2Id)) {
					for(Coord coord : paths.get(w2Id)) {
						if(!coord.equals(w2.from) && !coord.equals(w2.to))
							chip.mark(coord, Constants.FREE);
					}
					paths.remove(w2Id);
					pathCandidates.put(w2Id, new PriorityQueue<>(pathComp));
				}
			}
			findPaths(chip, w);
			if(!pathCandidates.get(curId).isEmpty()) {
				Path front2 = pathCandidates.get(curId).peek();
				paths.put(curId, front2);
				for(Coord coord : front2) {
					chip.mark(coord, curId);
				}
			}
			for(Wire w2 : chip.wires) {
				int w2Id = w2.wireId;
				if(w2Id != curId && pathsToReplace.contains(w2Id)) {
					findPaths(chip, w2);
					if(!pathCandidates.get(w2Id).isEmpty()) {
						Path front3 = pathCandidates.get(w2Id).peek();
						paths.put(w2Id, front3);
						for(Coord coord : front3) {
							chip.mark(coord, w2Id);
						}
					}
					else System.out.println("Could not replace " + w2.toString());
				}
			}
			if(pathCandidates.get(curId).isEmpty())
				System.out.println("Could not add " + w.toString());
		}
	}
	
    return paths;
  }
  
  
  /**
   * Makes the first call to findPathsHelper of this wire, 
   * starting with an empty path at the beginning of the wire.
   * 
   * @param chip The chip to find the wire's paths in.
   * @param w The wire to connect.
   */
  private static void findPaths(Chip chip, Wire w) {
	  findPathsHelper(chip, w, new Path(w), w.from);
  }

  /**
   * Helper function to find optimal paths.
   * curId is the wire's current ID.
   * front is the the current shortest path connecting this wire, if it exists
   * 
   * Unless from does not equal the start of the wire, it will be
   * added to the current path.
   * 
   * If from equals the end of the wire, the path will be added to
   * that wire's path candidates.
   * 
   * If from does not exist, or the current path's length does not
   * exceed that of front, each available coordinate will be sorted
   * by the distance between from and the end of the wire, and then
   * recur if front does not exist or if there is the possibility
   * of a shorter path being found.
   * 
   * @param chip The chip to find the wire's paths in.
   * @param w The wire to connect.
   * @param path The current path which will connect the wire.
   * @param from The current coord to use.
   */
  private static void findPathsHelper(Chip chip, Wire w, Path path, Coord from) {
	  int curId = w.wireId;
	  Path front = pathCandidates.get(curId).peek();

	  if(!from.equals(w.from))
		  path.add(from);
	  
	  if(from.equals(w.to))
		  pathCandidates.get(curId).add(path);
	  else if(front == null || path.size() < front.size()) {
		  Comparator<Coord> coordComp = (c1, c2) -> score(chip, curId, c1, w.to) - score(chip, curId, c2, w.to);
		  Queue<Coord> sortedCoords = new PriorityQueue<>(coordComp);
		  
		  for(Coord coord : from.neighbors(chip.dim)) {
			  if(chip.isAvailable(coord, curId) && !path.contains(coord)) {
				  sortedCoords.add(coord);
			  }
		  }
		  while(!sortedCoords.isEmpty()) {
			  Coord curCoord = sortedCoords.poll();
			  Path tempPath = new Path(w);
			  tempPath.addAll(path);
			  tempPath.remove(0);
			  if(front == null || (front != null && path.size() + separation(from, w.to) < front.size())) {
				  findPathsHelper(chip, w, tempPath, curCoord);
			  }
		  }
	  }
  }

  /**
   * Returns the distance between two coords.
   * 
   * @param c1 The first coordinate.
   * @param c2 The second coordinate.
   * @return The distance between two coordinates.
   */
  protected static int separation(Coord c1, Coord c2) {
	int rise = (Math.max(c1.y, c2.y) - Math.min(c1.y, c2.y));
	int run = (Math.max(c1.x, c2.x) - Math.min(c1.x, c2.x));

	return rise+run;
  }

  /**
   * Returns 0 if the coord is on an edge (e.g. an obstacle, the grid's border), 1 otherwise.
   * 
   * @param chip The chip that the coord is on.
   * @param wireId The wire that may have this coord available.
   * @param coord The coord to check if it is available.
   * @return 0 if the coord is on an edge, 1 otherwise.
   */
  protected static int onEdge(Chip chip, int wireId, Coord coord) {
	  if(!chip.isAvailable(coord, wireId))
		  return 0;
	  
	  return 1;
  }
  
  /**
   * Returns the combined score of the separation of two coordinates and
   * whether the first coordinate is on an edge.
   * 
   * @param chip The chip to use in onEdge().
   * @param wireId The wire to use in onEdge().
   * @param c1 The first coord, used in both separation() and onEdge().
   * @param c2 The second coord, used in separation().
   * @return the combined score of the separation of two coordinates and
   * whether the first coordinate is on an edge.
   */
  protected static int score(Chip chip, int wireId, Coord c1, Coord c2) {
	  return separation(c1, c2) + onEdge(chip, wireId, c1);
  }
  
  /**
   * Returns the sum of the lengths of all non-null paths in the given layout.
   * 
   * @param layout The layout to add the lengths from.
   * @return The sum of all lengths in the given layout.
   */
  public static int totalWireUsage(Map<Integer, Path> layout) {
	int sum = 0;
	Set<Integer> ids = layout.keySet();
	
	for(int i : ids)
		sum += layout.get(i).length();
	
    return sum;
  }
  
  /**
   * Returns true iff all wires on the chip are connected with a path.
   * 
   * @param chip The chip to use.
   * @param layout The paths of the chip's wires that were connected.
   * @return true iff all wires on the chip are connected with a path.
   */
  public static boolean allConnected(Chip chip, Map<Integer, Path> layout) {
		for(Wire w : chip.wires) {
			if(layout.get(w.wireId) == null)
				return false;
		}
		
		return true;
  }
  
}
