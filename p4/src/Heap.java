import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 * The keys in the heap must be stored in an array.
 * 
 * There may be duplicate keys in the heap.
 * 
 * The constructor takes an argument that specifies how objects in the 
 * heap are to be compared. This argument is a java.util.Comparator, 
 * which has a compare() method that has the same signature and behavior 
 * as the compareTo() method found in the Comparable interface. 
 * 
 * Here are some examples of a Comparator<String>:
 *    (s, t) -> s.compareTo(t);
 *    (s, t) -> t.length() - s.length();
 *    (s, t) -> t.toLowerCase().compareTo(s.toLowerCase());
 *    (s, t) -> s.length() <= 3 ? -1 : 1;  
 */

public class Heap<E> implements PriorityQueue<E> {
  protected List<E> keys;
  private Comparator<E> comparator;
  
  /**
   * Creates a heap whose elements are prioritized by the comparator.
   * 
   * @param comparator The comparator to be used.
   */
  public Heap(Comparator<E> comparator) {
	  this.keys = new LinkedList<E>();
	  this.comparator = comparator;
  }

  /**
   * Returns the comparator on which the keys in this heap are prioritized.
   * 
   * @return The heap's comparator.
   */
  public Comparator<E> comparator() {
    return comparator;
  }

  /**
   * Returns the top of this heap. This will be the highest priority key.
   * 
   * @return the top of this heap. 
   * @throws NoSuchElementException if the heap is empty.
   */
  public E peek() {
	  if(keys.isEmpty())
		  throw new NoSuchElementException("Heap is empty!");
	  return keys.get(0);
  }

  /**
   * Inserts the given key into this heap. Uses siftUp().
   * 
   * @param key The key to be inserted.
   */
  public void insert(E key) {
	  keys.add(key);
	  siftUp(keys.size()-1);
  }

  /**
   * Removes and returns the highest priority key in this heap.
   * 
   * @return The top key before the delete.
   * @throws NoSuchElementException if the heap is empty.
   */
  public E delete() {
	  if(keys.isEmpty())
		  throw new NoSuchElementException("Heap is empty!");
	  E lastKey = keys.get(size()-1);
	  E topKey = keys.remove(0);
	  keys.add(0, lastKey);
	  keys.remove(size()-1);
	  siftDown(0);
	  
	  return topKey;
  }

  /**
   * Restores the heap property by sifting the key at position p down
   * into the heap.
   * 
   * @param p The position of the key to sift down.
   */
  public void siftDown(int p) {
    int leftChild = getLeft(p);
    int rightChild = getRight(p);
    if (leftChild < size()) {
      if(comparator.compare(keys.get(leftChild), keys.get(p)) < 0 && 
        (rightChild >= size() || comparator.compare(keys.get(rightChild), keys.get(p)) >= 0)) {
    	  swap(leftChild, p);
    	  siftDown(leftChild);
      }
      else if(comparator.compare(keys.get(leftChild), keys.get(p)) >= 0 && rightChild < size() && comparator.compare(keys.get(rightChild), keys.get(p)) < 0) {
    	  swap(rightChild, p);
    	  siftDown(rightChild);
      }
      else if(rightChild < size()) {
    	  int maxChild = comparator.compare(keys.get(leftChild), keys.get(rightChild));
    	  if(maxChild <= 0 && comparator.compare(keys.get(leftChild), keys.get(p)) < 0) {
        	  swap(leftChild, p);
        	  siftDown(leftChild);
    	  }
    	  else if(maxChild > 0 && comparator.compare(keys.get(rightChild), keys.get(p)) < 0) {
        	  swap(rightChild, p);
        	  siftDown(rightChild);
    	  }
      }
    }
  }
  
  /**
   * Restores the heap property by sifting the key at position q up
   * into the heap. (Used by insert()).
   * 
   * @param q The position of the key to be sifted up.
   */
  public void siftUp(int q) {
	  int parent = getParent(q);
	  
	  if(parent >= 0 && comparator.compare(keys.get(q), keys.get(parent)) <= 0)
		  swap(q, parent);
	  
	  if(parent > 0)
		  siftUp(parent);
  }

  /**
   * Exchanges the elements in the heap at the given indices in keys.
   * 
   * @param i The first key to be swapped.
   * @param j The second key to be swapped.
   */
  public void swap(int i, int j) {
    E saveI = keys.get(i);
    E saveJ = keys.get(j);
    keys.remove(i);
    keys.add(i, saveJ);
    keys.remove(j);
    keys.add(j, saveI);
  }
  
  /**
   * Returns the number of keys in this heap.
   * 
   * @return the number of keys in this heap.
   */
  public int size() {
    return keys.size();
  }

  /**
   * Returns a textual representation of this heap.
   * 
   * @return a textual representation of this heap.
   */
  public String toString() {
    return keys.toString();
  }
  
  /**
   * Returns the index of the left child of p.
   * 
   * @return the index of the left child of p.
   */
  public static int getLeft(int p) {
    return (p*2)+1;
  }

  /**
   * Returns the index of the right child of p.
   * 
   * @return the index of the right child of p.
   */
  public static int getRight(int p) {
    return (p*2)+2;
  }

  /**
   * Returns the index of the parent of p.
   * 
   * @return the index of the parent of p.
   */
  public static int getParent(int p) {
    return (p-1)/2;
  }
  
}
