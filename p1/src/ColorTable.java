import java.awt.Color;
import java.util.Random;

/**
 * @author Reagan Roush
 * 
 * A ColorTable represents a dictionary of frequency counts, keyed on Color.
 * It is a simplification of Map<Color, Integer>. The size of the key space
 * can be reduced by limiting each Color to a certain number of bits per channel.
 */

public class ColorTable {
  /**
   * Counts the number of collisions during an operation.
   */
  private static int numCollisions = 0;
  /**
   * Returns the number of collisions that occurred during the most recent get or
   * put operation.
   */
  public static int getNumCollisions() {
    return numCollisions;
  }

  /**
   * Constructs a color table with a starting capacity of initialCapacity. Keys in
   * the color key space are truncated to bitsPerChannel bits. The collision resolution
   * strategy is specified by passing either Constants.LINEAR or Constants.QUADRATIC for
   * the collisionStrategy parameter. The rehashThrehold specifies the maximum tolerable load 
   * factor before triggering a rehash.
   * 
   * @throws RuntimeException if initialCapacity is not in the range [1..Constants.MAX_CAPACITY]
   * @throws RuntimeException if bitsPerChannel is not in the range [1..8]
   * @throws RuntimeException if collisionStrategy is not one of Constants.LINEAR or Constants.QUADRATIC
   * @throws RuntimeException if rehashThreshold is not in the range (0.0..1.0] for a
   *                             linear strategy or (0.0..0.5) for a quadratic strategy
   */
  
  private int capacity; // current capacity of the table starting with initialCapacity
  private int bpc;
  private int strat;
  private double threshold;

  private Integer[] table; // table of colors
  private Long[] freqTable; // table of aligned color frequencies
  
  public ColorTable(int initialCapacity, int bitsPerChannel, int collisionStrategy, double rehashThreshold) { 
	  if((initialCapacity < 1) || (initialCapacity > Constants.MAX_CAPACITY))
		  throw new RuntimeException("Initial capacity not in range 1...MAX_CAPACITY");
	  if((bitsPerChannel < 1) || (bitsPerChannel > 8))
		  throw new RuntimeException("Bits per channel not in range 1...8");
	  if((collisionStrategy != Constants.LINEAR) && (collisionStrategy != Constants.QUADRATIC))
		  throw new RuntimeException("Collision strategy is not linear or quadratic");
	  if(collisionStrategy == Constants.LINEAR && (rehashThreshold < 0.0 || rehashThreshold > 1.0))
		  throw new RuntimeException("Rehash threshold for linear strategy not in range 0.0...1.0");
	  if(collisionStrategy == Constants.QUADRATIC && (rehashThreshold < 0.0 || rehashThreshold > 0.5))
		  throw new RuntimeException("Rehash threshold for quadratic strategy not in range 0.0...0.5");
	  
	  this.capacity = initialCapacity;
	  this.bpc = bitsPerChannel;
	  this.strat = collisionStrategy;
	  this.threshold = rehashThreshold;

	  this.table = new Integer[capacity];
	  this.freqTable = new Long[capacity];
	  
	  for(int i = 0; i < freqTable.length; i++) {
		  freqTable[i] = (long)0;
	  }
  }

  /**
   * Returns the number of bits per channel used by the colors in this table.
   */
  public int getBitsPerChannel() {
	  return bpc;
  }

  /**
   * Returns the frequency count associated with color. Note that colors not
   * explicitly represented in the table are assumed to be present with a
   * count of zero. Uses Util.pack() as the hash function.
   */
  public long get(Color color) {
	  long freq = 0;
	  int hash = Util.pack(color, bpc);

	  for(int i = 0; i < capacity; i++) {
		  if(table[i] != null && table[i] == hash)
			  freq = freqTable[i];
	  }
	  
    return freq;
  }

  /**
   * Associates the count with the color in this table. Do nothing if count is less than
   * or equal to zero. Uses Util.pack() as the hash function.
   */
  public void put(Color color, long count) {
	  int start = 0; // where to start our collision resolution
	  int step = 1; // step of our collision resolution
	  int hash = Util.pack(color, bpc);
	  int curHash = hash; // hash to be modified during collision resolution
	  
	  if(count > 0) {
		  
		  for(int i = hash % capacity; i < capacity; i++) {
			  if(table[i] != null && hash == table[i]) {
				 //System.out.println("Replacing key " + hash + " at " + i);
				 freqTable[i] = count;
				 i = capacity;
			  }
			  
			  if((i != capacity && table[i] == null) && (curHash % capacity == i)) {
				 //System.out.println("Placing new key " + hash + " at " + i);
				 table[i] = hash;
				 freqTable[i] = count;
				 i = capacity;
			  }
			  
			  if(strat == Constants.LINEAR && i != capacity && table[i] != null && (curHash % capacity == i)) {
				  //System.out.println("Collision detected at i=" + i);
				  numCollisions++;
				  if(step == 1)
					  start = i;
				  i = ((start + step) % capacity)-1;
				  curHash++;
				  step++;
			  }
			  
			  if(strat == Constants.QUADRATIC && i != capacity && table[i] != null && (curHash % capacity == i)) {
				  //System.out.println("Collision detected at i=" + i);
				  numCollisions++;
				  if(step == 1)
					  start = i;
				  i = ((start + square(step)) % capacity)-1;
				  curHash = hash + square(step);
				  step++;
			  }
		  }
	  }
	  if(getLoadFactor() >= threshold) {
		  // System.out.println("Rehashing table");
		  rehash();
	  }
  }

  /**
   * Returns the square of an integer for quadratic probing.
   */
  private static int square(int step) {
	  return (step * step);
  }

  /**
   * Increments the frequency count associated with color. Note that colors not
   * explicitly represented in the table are assumed to be present with a
   * count of zero.
   */
  public void increment(Color color) {
	  boolean exists = get(color) != 0;
	  
	  if(exists == true)
		  put(color, get(color)+1);
	  else
		  put(color, 1);
  }

  /**
   * Returns the load factor for this table.
   */
  public double getLoadFactor() {
	  return ((double)getSize()/(double)getCapacity());
  }

  /**
   * Returns the size of the internal array representing this table.
   */
  public int getCapacity() {
    return table.length;
  }

  /**
   * Returns the number of key/value associations in this table.
   */
  public int getSize() {
	  int keys = 0;
	  for(int i = 0; i < capacity; i++) {
		  if(table[i] != null)
			  keys++;
	  }
    return keys;
  }

  public double getThreshold() {
	  return threshold;
  }
  
  /**
   * Returns true iff this table is empty.
   */
  public boolean isEmpty() {
    return (getSize() == 0);
  }

  /**
   * Increases the size of the array to the smallest prime greater than double the 
   * current size that is of the form 4j + 3, and then moves all the key/value 
   * associations into the new array. 
   * 
   * @throws RuntimeException if the table is already at maximum capacity.
   */
  private void rehash() { 
	  if(capacity == Constants.MAX_CAPACITY)
		  throw new RuntimeException("Table already at maximum capacity");
	  
	  int doubleCapacity = capacity*2;
	  Integer[] oldTable = table;
	  Long[] oldFreqTable = freqTable;
	  
	  if(doubleCapacity > Constants.MAX_CAPACITY || doubleCapacity < 0)
		  capacity = Constants.MAX_CAPACITY;
	  else {
		  for(int i = doubleCapacity; i <= Constants.MAX_CAPACITY; i++) {
			  if(Util.isPrime(i) && (i-3) % 4 == 0) {
				  capacity = i;
				  i = Constants.MAX_CAPACITY + 1;
			  }
		  }
	  }
	  
	  table = new Integer[capacity];
	  freqTable = new Long[capacity];
	  
	  
	  for(int j = 0; j < freqTable.length; j++) {
		  freqTable[j] = (long)0;
	  }
	  
	  for(int k = 0; k < oldTable.length; k++) {
		  if(oldTable[k] != null) {
			  //System.out.println("Rehashing key " + oldTable[k]);
			  put(Util.unpack(oldTable[k], bpc), oldFreqTable[k]);
		  }
	  }
  }

  /**
   * Returns an Iterator that marches through each color in the key color space and
   * returns the sequence of frequency counts.
   */
  public Iterator iterator() {
	  class ColorIterator implements Iterator {
		  int curr = 0;
		  int end;
		  
		  ColorIterator() {
			  this.curr = 0;
			  this.end = getCapacity()-1;
		  }
		  
		  public boolean hasNext() {
			  return curr < end;
		  }
		  
		  public long next() {
			  return freqTable[curr++];
		  }
		  
	  }
    return new ColorIterator();
  }

  /**
   * Returns a String representation of this table.
   */
  public String toString() {
	  String pairs = "";
	  
	  for(int i = 0; i < table.length; i++) {
		  if(table[i] != null)
			 pairs += (i+":"+table[i]+","+freqTable[i]+" ");
	  }
    return "[" + pairs + "]";
  }

  /**
   * Returns the count in the table at index i in the array representing the table.
   * The sole purpose of this function is to aid in writing the unit tests.
   */
  public long getCountAt(int i) { 
    return freqTable[i];
  }

  /**
   * Simple testing.
   */
  public static void main(String[] args) {
    ColorTable table = new ColorTable(3, 6, Constants.QUADRATIC, .49);
    int[] data = new int[] { 32960, 4293315, 99011, 296390 };
    for (int i = 0; i < data.length; i++) {
      table.increment(new Color(data[i]));
    }
    System.out.println("capacity: " + table.getCapacity()); // Expected: 7
    System.out.println("size: " + table.getSize());         // Expected: 3
    
    /* The following automatically calls table.toString().
       Notice that we only include non-zero counts in the String representation.
       
       Expected: [3:2096,2, 5:67632,1, 6:6257,1]
       
       This shows that there are 3 keys in the table. They are at positions 3, 5, and 6.
       Their color codes are 2096, 67632, and 6257. The code 2096 was incremented twice.
       You do not have to mimic this format exactly, but strive for something compact
       and readable.
       */
    System.out.println(table);
  }
}
