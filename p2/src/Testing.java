import static org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnit tests for all TODO methods.
 */

public class Testing {
  
  // Test of Coord.onBoard()
  @Test
  public void testOnBoard() {
	Coord coord1 = new Coord(3, 4);
	Coord corner = new Coord(0, 0);
	
    assertFalse(coord1.onBoard(4));
    assertTrue(coord1.onBoard(5));
    assertTrue(corner.onBoard(1));
    assertFalse(corner.onBoard(0));
  }

  // Test of Coord.neighbors()
  @Test
  public void testCoordNeighbors() {
	  Coord coord1 = new Coord(2, 1);
	  Coord coord2 = new Coord(5, 5);
	  Coord corner = new Coord(0, 0);
	  
	  assertEquals("[(2, 0), (2, 2), (1, 1)]", coord1.neighbors(3).toString());
	  assertEquals("[(2, 0), (2, 2), (1, 1), (3, 1)]", coord1.neighbors(4).toString());
	  assertEquals("[]", coord2.neighbors(3).toString());
	  assertEquals("[(5, 4), (4, 5)]", coord2.neighbors(6).toString());
	  assertEquals("[(0, 1), (1, 0)]", corner.neighbors(3).toString());
	  assertEquals("[]", corner.neighbors(1).toString());
  }
  
  // test of Coord.hashCode()
  @Test
  public void testCoordHashCode() {
	  Coord coord1 = new Coord(2, 1);
	  Coord coord2 = new Coord(5, 5);
	  Coord corner = new Coord(0, 0);
	  
	  assertEquals(coord1.hashCode(), 63);
	  assertEquals(coord2.hashCode(), 160);
	  assertEquals(corner.hashCode(), 0);
  }
  
  // Various tests for a fully flooded board.
  @Test
  public void testFullyFloodedBoard() {
	  Board board = new Board(10, WaterColor.BLUE);
	  assertEquals(WaterColor.BLUE, board.suggest());
	  board.flood(WaterColor.CYAN); // Should not change any colors as all tiles are dormant!
	  assertEquals(WaterColor.BLUE, board.suggest());
	  assertTrue(board.fullyFlooded());
  }
  
  // Test flooding a board using flood() and suggest()
  @Test
  public void testRandomBoard1() {
	  Board board = new Board(10);
	  int steps = 0;
	  
	  while(!board.fullyFlooded()) {
		  assertFalse(board.fullyFlooded());
		  WaterColor bestColor = board.suggest();
		  board.flood(bestColor);
		  steps++;
	  }

	  assertTrue(board.fullyFlooded());

	  System.out.println("Finished testRandomBoard1() on size 10 board in " + steps + " steps.");
  }
  
  //Test flooding a board using flood1() and suggest()
  @Test
  public void testRandomBoard2() {
	  Board board = new Board(10);
	  int steps = 0;
	  
	  while(!board.fullyFlooded()) {
		  assertFalse(board.fullyFlooded());
		  WaterColor bestColor = board.suggest();
		  board.flood1(bestColor);
		  steps++;
	  }
	  
	  assertTrue(board.fullyFlooded());

	  System.out.println("Finished testRandomBoard2() on size 10 board in " + steps + " steps.");
  }
}