import java.util.function.BiPredicate;

/**
 * This class implements a generic height-balanced binary search tree, 
 * using the AVL algorithm. Beyond the constructor, only the insert()
 * method is changed.
 */
public class AVLTree<K> extends BinarySearchTree<K> {

  /**
   * Creates an empty AVL tree as a BST organized according to the
   * lessThan predicate.
   */
  public AVLTree(BiPredicate<K, K> lessThan) {
    super(lessThan);
  }

  /**
   * Inserts the given key into this AVL tree such that the ordering 
   * property for a BST and the balancing property for an AVL tree are
   * maintained.
   * 
   * @param key The key to be inserted.
   * @return The Node that the key was inserted into.
   */
  public Node insert(K key) {
    Node q = super.insert(key);
    Node saveQ = q;
    Node p = q.parent;
    Node r = null;
    
    while(p != null) {
        int leftHeight = 0;
        int rightHeight = 0;
        if(p.left != null)
        	leftHeight = p.left.height;
        if(p.right != null)
        	rightHeight = p.right.height;
    	int maxHeight = Math.max(leftHeight, rightHeight);
    	int minHeight = Math.min(leftHeight, rightHeight);	
        if(maxHeight - minHeight == 0) {
        	//System.out.println("Finished");
        	return saveQ;
        }
        else if(maxHeight - minHeight == 1) {
        	//System.out.println("Advancing up by 1");
        	r = q;
        	q = p;
        	p = q.parent;
        }
        else {
        	if(q == p.left) {
        		// Single rotation (LL)
        		if(r == q.left) {
        			//System.out.println("Entering LL rotation");
        			Node tempRight = q.right;
        		    q.parent = p.parent;
        			if(q.parent != null) {		
        				if(q.parent.left != null && q.parent.left == p) {
        					q.parent.left = q;
        				}
        				else if(q.parent.right != null && q.parent.right == p) {
        					q.parent.right = q;
        				}
        			}
        			p.parent = q;
        			q.right = p;
        			p.left = tempRight;
        			if(p.left != null) {
        				p.left.parent = p;
        			}
        			p.fixHeight();
        			q.fixHeight();
        			p = q.parent;
        			if(p != null)
            			p.fixHeight();
        		}
        		// Double rotation (LR)
        		else {
        			//System.out.println("Entering LR rotation");
        			Node tempRLeft = r.left;
        			Node tempRRight = r.right;
        			r.parent = p.parent;
        			if(r.parent != null) {		
        				if(r.parent.left != null && r.parent.left == p) {
        					r.parent.left = r;
        				}
        				else if(r.parent.right != null && r.parent.right == p) {
        					r.parent.right = r;
        				}
        			}
        			r.left = q;
        			r.right = p;
        			q.parent = r;
        			p.parent = r;
        			q.right = tempRLeft;
        			if(q.right != null) {
        				q.right.parent = q;
        			}
        			p.left = tempRRight;
        			if(p.left != null) {
        				p.left.parent = p;
        			}
        			p.fixHeight();
        			q.fixHeight();
        			r.fixHeight();
        			q = r;
        			p = r.parent;
        			if(p != null)
            			p.fixHeight();
        		}
        	}
        	else if(q == p.right) {
        		// Single rotation (RR)
        		if(r == q.right) {
        			//System.out.println("Entering RR rotation");
        			Node tempLeft = q.left;
        			q.parent = p.parent;
        			if(q.parent != null) {		
        				if(q.parent.left != null && q.parent.left == p) {
        					q.parent.left = q;
        				}
        				else if(q.parent.right != null && q.parent.right == p) {
        					q.parent.right = q;
        				}
        			}
        			q.left = p;
        			p.parent = q;
        			p.right = tempLeft;
        			if(p.right != null) {
        				p.right.parent = p;
        			}
        			p.fixHeight();
        			q.fixHeight();
        			p = q.parent;
        			if(p != null)
            			p.fixHeight();
        		}
        		// Double rotation (RL)
        		else {
        			//System.out.println("Entering RL rotation");
        			Node tempRLeft = r.left;
        			Node tempRRight = r.right;
        			r.parent = p.parent;
        			if(r.parent != null) {		
        				if(r.parent.left != null && r.parent.left == p) {
        					r.parent.left = r;
        				}
        				else if(r.parent.right != null && r.parent.right == p) {
        					r.parent.right = r;
        				}
        			}
        			r.left = p;
        			r.right = q;
        			p.parent = r;
        			q.parent = r;
        			p.right = tempRLeft;
        			if(p.right != null) {
        				p.right.parent = p;
        			}
        			q.left = tempRRight;
        			if(q.left != null) {
        				q.left.parent = q;
        			}
        			p.fixHeight();
        			q.fixHeight();
        			r.fixHeight();
        			q = r;
        			p = r.parent;
        			if(p != null)
            			p.fixHeight();
        		}
        	}
        }
    }

    root = q;
    root.fixHeight();
	//System.out.println("Finished at root");
    
    return saveQ;
  }

}
