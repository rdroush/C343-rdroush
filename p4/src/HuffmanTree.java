import java.util.Comparator;

/**
 * A HuffmanTree represents a variable-length code such that the shorter the
 * bit pattern associated with a character, the more frequently that character
 * appears in the text to be encoded.
 */

public class HuffmanTree {
  
  class Node {
    protected char key;
    protected int priority;
    protected Node left, right;
    
    /**
     * Constructs a Node from a priority and a key.
     * 
     * @param priority The priority of the key.
     * @param key The key to construct the Node from.
     */
    public Node(int priority, char key) {
      this(priority, key, null, null);
    }
    
    /**
     * Constructs a Node from a priority and left/right branches.
     * This constructor's purpose is to add two smaller Nodes together.
     * The key '\0' indicates that there is no character in this Node.
     * 
     * @param priority The priority of the key.
     * @param left The left branch of the Node.
     * @param right The right branch of the Node.
     */
    public Node(int priority, Node left, Node right) {
      this(priority, '\0', left, right);
    }
    
    /**
     * Constructs a Node from a priority, a key, and left/right branches.
     * 
     * @param priority The priority of the key.
     * @param key The key to construct the Node from.
     * @param left The left branch of the Node.
     * @param right The right branch of the Node.
     */   
    public Node(int priority, char key, Node left, Node right) {
      this.key = key;
      this.priority = priority;
      this.left = left;
      this.right = right;
    }
    
    /**
     * Determines if a Node is a leaf.
     * 
     * @return true if the Node is a leaf.
     */
    public boolean isLeaf() {
      return left == null && right == null;
    }
  }
  
  protected Node root;
  
  /**
   * Creates a HuffmanTree from the given frequencies of letters in the
   * alphabet using the algorithm described in lecture.
   * 
   * @param charFreqs The FrequencyTable to use to construct the HuffmanTree.
   */
  public HuffmanTree(FrequencyTable charFreqs) {
    Comparator<Node> comparator = (x, y) -> {
    	if(y.priority < x.priority) {
    		// System.out.println("Not swapping " + y.key + " and " + x.key);
    		return 1;
    	}
    	else if(y.priority > x.priority || (y.priority == x.priority && y.key == '\0')) {
    		// System.out.println("Swapping " + y.key + " and " + x.key);
    		return -1;
    	}
    	
      return 0;
    };  
    
    PriorityQueue<Node> forest = new Heap<Node>(comparator);
    
    charFreqs.forEach((k, v)->{forest.insert(new Node (v, k));});
    
    while(forest.size() > 1) {
    	Node left = forest.delete();
    	//System.out.println(left.key);
    	Node right = forest.delete();
    	//System.out.println(right.key);
    	Node temp = new Node(left.priority+right.priority, left, right);
    	forest.insert(temp);
    }
    
    root = forest.peek();
  }
  
  /**
   * Returns the character associated with the prefix of bits.
   * 
   * @param bits The bits to decode.
   * @return The decoded character.
   * @throws DecodeException if bits does not match a character in the tree.
   */
  public char decodeChar(String bits) {
	char[] bitsArray = bits.toCharArray();
	int i = 0;
	Node p = root;
	
	while(!p.isLeaf() && i < bitsArray.length) {
		if(bitsArray[i] == '0')
			p = p.left;
		else if(bitsArray[i] == '1')
			p = p.right;
		i++;
	}
	
	if(p.key == '\0')
		throw new DecodeException(bits);
	
    return p.key;
  }

  /**
   * Returns the bit string associated with the given character. Must
   * search the tree for a leaf containing the character. Every left
   * turn corresponds to a 0 in the code. Every right turn corresponds
   * to a 1. This function is used by CodeBook to populate the map.
   * 
   * @param ch The character to look up.
   * @return The bit string for the character, if available.
   * @throws EncodeException if the character does not appear in the tree.
   */
  public String lookup(char ch) {
    return lookupHelper(ch, root, "");
  }
  
  /**
   * Helper function for lookup(), recursively walking down the tree searching
   * for a bit string matching the character, if available.
   * 
   * @param ch The character to look up.
   * @param p The current node being searched.
   * @param s The current bit string at this point in the search.
   * @return The bit string for the character, if available.
   * @throws EncodeException if the character does not appear in the tree.
   */
  public String lookupHelper(char ch, Node p, String s) {
	  String leftString = "";
	  String rightString = "";
	  String maxString = "";
	  if(p.isLeaf()) {
		  if(p.key == ch)
			  return s;
		  return "";
	  }
	  
	  if(p.left != null) {
		  leftString = lookupHelper(ch, p.left, s + "0");
	  }
	  else {
		  leftString = "";
	  }

	  if(p.right != null) {
		  rightString = lookupHelper(ch, p.right, s + "1");
	  }
	  else {
		  rightString = "";
	  }

	  maxString = maxString(leftString, rightString);
	  
	  if(p == root && maxString == "")
		  throw new EncodeException(ch);
	  
	  return maxString;
  }
  
  /**
   * Helper function for lookupHelper() that calculates the maximum
   * length string of two, for the purpose of finding the (single possible) candidate
   * string matching with the given character.
   * 
   * @param s1 The first string to compare.
   * @param s2 The second string to compare.
   * @return The maximum length string of the two.
   */
  public String maxString(String s1, String s2) {
	  boolean lessThan = s1.length() < s2.length();
	  if(lessThan)
		  return s2;
	  return s1;
  }

}
