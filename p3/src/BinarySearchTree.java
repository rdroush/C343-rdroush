import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * This class implements a generic unbalanced binary search tree (BST).
 */
public class BinarySearchTree<K> implements Tree<K> {
  
  /**
   * A Node is a Location, which means that it can be the return value
   * of a search on the tree.
   */
  class Node implements Location<K> { 
    protected K data;
    protected Node left, right;
    protected Node parent;     // the parent of this node
    protected int height;      // the height of the subtree rooted at this node
    protected boolean dirty;   // true iff the key in this node has been removed

    /**
     * Constructs a leaf node with the given key.
     * 
     * @param data The data to be inserted.
     */
    public Node(K key) {
      this(key, null, null);
    }
    
    /**
     * Constructs a new node with the given values for fields.
     * 
     * @param data The data to be inserted.
     * @param left The left branch of this Node.
     * @param right The right branch of this Node.
     */
    public Node(K data, Node left, Node right) {
    	this.data = data;
    	this.left = left;
    	this.right = right;
    	this.height = 1;
    	this.dirty = false;
    }

    /**
     * Return true iff this node is a leaf in the tree.
     * 
     * @return true iff this node is a leaf in the tree.
     */
    protected boolean isLeaf() {
      return left == null && right == null;
    }
    
    /**
     * Performs a local update on the height of this node. Assumes that the 
     * heights in the child nodes are correct. This function *must* run in 
     * O(1) time.
     */
    protected void fixHeight() {
    	if(!isLeaf()) {
        	int leftHeight = 0;
        	int rightHeight = 0;
        	int maxHeight;
        	
        	if(left != null)
        		leftHeight = left.height;
           	if(right != null)
        		rightHeight = right.height;
           	
           	maxHeight = Math.max(leftHeight, rightHeight);

           	height = maxHeight+1;
    	}
    	else {
    		height = 1;
    	}
    }
    
    /**
     * Returns the data in this node.
     * 
     * @return the data in this node.
     */
    public K get() {
      return data;
    }

    /**
     * Returns the location of the node containing the inorder predecessor
     * of this node.
     * 
     * @return the location of the node containing the inorder predecessor
     * of this node, null otherwise.
     */
    public Node getBefore() {
    	if(parent != null && left == null) {
    		Node p = this;
    		while(p.parent != null) {
    			if(p.parent.right != null && p == p.parent.right) {
    				if(!p.parent.dirty)
    					return p.parent;
    				else
    					return p.parent.getBefore();
    			}
    			else if(p.parent == root && p.parent.left != null && p.parent.left == p) {
    				return null;
    			}
    			else
    				p = p.parent;
    		}
    		if(p == root && p.dirty)
    			return p.getBefore();
    	}
    	else if(left != null) {
    		Node p = left;
    		while(p.right != null)
    			p = p.right;
    		if(!p.dirty)
    			return p;
    		else
    			return p.getBefore();
    	}
    	
      return null;
     }

    /**
     * Returns the location of the node containing the inorder successor
     * of this node.
     * 
     * @return the location of the node containing the inorder successor
     * of this node if it exists, null otherwise.
     */
    public Node getAfter() {
    	if(parent != null && right == null) {
    		Node p = this;
    		while(p.parent != null) {
    			if(p.parent.left != null && p == p.parent.left) {
    				if(!p.parent.dirty)
    					return p.parent;
    				else
    					return p.parent.getAfter();
    			}
    			else if(p.parent == root && p.parent.right != null && p.parent.right == p) {
    				return null;
    			}
    			else
    				p = p.parent;
    		}
    		if(p == root && p.dirty)
    			return p.getAfter();
    	}
    	else if(right != null) {
    		Node p = right;
    		while(p.left != null)
    			p = p.left;
    		if(!p.dirty)
    			return p;
    		else
    			return p.getAfter();
    	}
    	
      return null;
    }
  }
  
  protected Node root;
  protected int n;
  protected BiPredicate<K, K> lessThan;
  
  /**
   * Constructs an empty BST, where the data is to be organized according to
   * the lessThan relation.
   */
  public BinarySearchTree(BiPredicate<K, K> lessThan) {
    this.lessThan = lessThan;
    n = 0;
  }
  
  /**
   * Looks up the key in this tree and, if found, returns the (possibly dirty)
   * location containing the key.
   * 
   * @param key The key to be searched.
   * @return the Node containing the key if it exists.
   */
  public Node search(K key) {
	  Node p = root;
	  
	  while(p != null) {
		  if(p.data == key)
			  return p;
		  else if(lessThan.test(key, p.data))
			  p = p.left;
		  else
			  p = p.right;
	  }
	  
    return null;
  } 

  /**
   * Returns the height of this tree. Runs in O(1) time!
   * 
   * @return the height of this tree.
   */
  public int height() {
	  if(root == null)
		  return 0;
	  
    return root.height;
  }
  
  /**
   * Clears all the keys from this tree. Runs in O(1) time!
   */
  public void clear() {
	  root = null;
	  n = 0;
  }

  /**
   * Returns the number of keys in this tree.
   * 
   * @return the number of keys in this tree.
   */
  public int size() {
    return n;
  }
  
  /**
   * Inserts the given key into this BST, as a leaf, where the path
   * to the leaf is determined by the predicate provided to the tree
   * at construction time. The parent pointer of the new node and
   * the heights in all node along the path to the root are adjusted
   * accordingly.
   * 
   * Note: we assume that all keys are unique. Thus, if the given
   * key is already present in the tree, nothing happens.
   * 
   * Returns the location where the insert occurred (i.e., the leaf
   * node containing the key).
   * 
   * @param key The key to be inserted.
   * @return the location where the insert occurred (i.e., the leaf
   * node containing the key).
   */
  public Node insert(K key) {
	  Node p = root;
	  boolean contains = contains(key);
	  if(root == null) {
		  root = new Node(key, null, null);
		  n++;
		  contains = true;
	  }
	  else if(root.data.equals(key) && root.dirty) {
		  root.dirty = false;
		  n++;
		  contains = true;
	  }
	  while(p != null && !contains) {
		  boolean curLessThan = lessThan.test(key, p.data);
		  if(curLessThan) {
			  if(p.left == null) {
				  p.left = new Node(key, null, null);
				  p.left.parent = p;
				  n++;
				  contains = true;
				  p = p.left;
			  }
			  else if (p.left.data.equals(key) && p.left.dirty == true) {
				  p.left.dirty = false;
				  n++;
				  contains = true;
				  p = p.left;
			  }
			  else {
				  p = p.left;
			  }
		  }
		  else{
			  if(p.right == null) {
				  p.right = new Node(key, null, null);
				  p.right.parent = p;
				  n++;
				  contains = true;
				  p = p.right;
			  }
			  else if (p.right.data.equals(key) && p.right.dirty == true) {
				  p.right.dirty = false;
				  n++;
				  contains = true;
				  p = p.right;
			  }
			  else {
				  p = p.right;
			  }  
		  }
	  }
	  p = search(key);
	  Node saveNode = p;
	  while(p != null) {
		  p.fixHeight();
		  p = p.parent;
	  }
	  
    return saveNode;
  }

  /**
   * Returns true iff the given key is in this BST.
   * 
   * @param key The key to be checked.
   * @return true iff the given key is in this BST.
   */
  public boolean contains(K key) {
    Node p = search(key);
    return (p != null && !p.dirty);
  }

  /**
   * Removes the key from this BST using lazy deletion. 
   * If the key is not in the tree or is marked as dirty, nothing happens.
   * 
   * @param key The key to be "removed".
   */
  public void remove(K key) {
	  Node removed = search(key);
	  if(removed != null && !removed.dirty) {
		  n--;
		  removed.dirty = true;
	  }
  }
  
  /**
   * Clears out all dirty nodes from this BST.
   * 
   * Uses the following algorithm:
   * (1) Let ks be the list of non-dirty keys in this tree. 
   * (2) The tree is cleared.
   * (3) Each key in ks is inserted back into the tree, minus the dirty ones.
   */
  public void rebuild() {
	  List<K> keys = keys();
	  clear();
	  keys.forEach((k)->insert(k));
  }
    
  /**
   * Returns a sorted list of all the keys in this tree.
   * 
   * @return a sorted list of all the keys in this tree.
   */
  public List<K> keys() {
	  List<K> list = new LinkedList<>();
	  Node p = root;
	  
	  if(root != null) {
		  while(p.left != null) {
			  p = p.left;
		  }
		  
		  if(!p.dirty)
			  list.add(p.data);
		  
		  while(p.getAfter() != null) {
			  Node tempNode = p.getAfter();
			  list.add(tempNode.data);
			  p = tempNode;
		  }
	  }
	  
    return list;
  }

  /**
   * TODO
   * 
   * Returns a textual representation of this BST.
   */
  public String toString() {
	  return "";
  }
}
