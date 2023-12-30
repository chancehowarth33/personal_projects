// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.Stack;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>> implements IterableMultiKeySortedCollectionInterface<T> {

	// initilization of startpoint and keyCount
	private Comparable<T> iterationStartPoint;
    	private int keyCount = 0;


/**
     * Inserts value into tree that can store multiple objects per key by keeping
     * lists of objects in each node of the tree.
     * @param key object to insert
     * @return true if a new node was inserted, false if the key was added into an existing node
     */
	public boolean insertSingleKey(T key){
    		KeyList<T> newKeyList = new KeyList<>(key);

    		// searches for the key in the given tree
    		Node<KeyListInterface<T>> existingNode = this.findNode(newKeyList);

    		if (existingNode != null) {
        		// if key already exists, add it to the key list
        		existingNode.data.addKey(key);
			keyCount++;
        		return false;
    		} else {
        		// if key doesnt exist, add it to the tree
        		this.insert(newKeyList);
        		keyCount++;
        		return true;
		}
	}

    /**
     * @return the number of values in the tree.
     */
	public int numKeys() {
        return keyCount;
    }

    	/**
	 * calls the clear method of BinarySraechTree to clear the tree.
	 *
	 */
    	@Override
    	public void clear() {
        	super.clear(); // calls the clear method of the superclass (BinarySearchTree)
        	keyCount = 0; 
    }

    	/**
     	* Sets the starting point for iterations. Future iterations will start at the
     	* starting point or the key closest to it in the tree. This setting is remembered
     	* until it is reset. Passing in null disables the starting point.
     	* @param startPoint the start point to set for iterations
     	*/
        public void setIterationStartPoint(Comparable<T> startPoint){
                this.iterationStartPoint = startPoint;
        }


	/**
	 * helper method for the Iterator<T> iterator(), 
	 *
	 */
	protected Stack<Node<KeyListInterface<T>>> getStartStack() {

                // creates and empty stack
                Stack<Node<KeyListInterface<T>>> stack = new Stack<>();

                // sets current node to the root of the tree
                Node<KeyListInterface<T>> currentnode = this.root;

                // runs untill current node is null, returns the nodes on the left-most path untill either the smallest node is found or the iterationStartPoint is found
                while (currentnode != null) {
                        // declaring the int to hold the value of the iterationStartPoint
                        int startvalue;
                        // if startvalue is null, push currentnode to stack and make left child new currentnode
                        if (iterationStartPoint == null) {
                                stack.push(currentnode);
                                currentnode = currentnode.down[0];
                        // set the startvalue to either a negative number, 0 ora a positive number to determine correlation to the iterationStartPoint value
                        } else {
                                startvalue = iterationStartPoint.compareTo(currentnode.data.iterator().next());
                                if (startvalue <= 0) {
                                        stack.push(currentnode);
                                        // if startvalue is exactly 0 we terminate the loop
                                        if (startvalue == 0) {
                                                currentnode = null;
                                        // if it is just less than we move to the left child of the current node
                                        } else {
                                                currentnode = currentnode.down[0];
                                        }
                                // if the startvalue is greater than zero we move to the right child of the current node as we need a larger value
                                } else {
                                        currentnode = currentnode.down[1];
                                }
                        }
                 }


                 return stack;
        }

    /**
     * Returns an iterator that does an in-order iteration over the tree.
     */
	public Iterator<T> iterator() {

		return new Iterator<T>() {
        		
			private Stack<Node<KeyListInterface<T>>> stack = getStartStack();
			
			private Iterator<T> keyListIterator;{
    				if (stack.isEmpty()) {
        				keyListIterator = null;
    				} else {
        				keyListIterator = stack.peek().data.iterator();
				}
			}
			
			/**
			 * checks is there is more nodes to be iterated over in inside the tree
			 *
			 */
        		@Override
        		public boolean hasNext() {
				
				// first checks if the current key list iterator has more keys, if true there's no need to explore further in the tree
            			if (keyListIterator != null && keyListIterator.hasNext()) {
                			return true;
            			}
				// uses stack to get next key if the the key list is empty or null
            			while (!stack.isEmpty()) {
                			Node<KeyListInterface<T>> node = stack.pop();
					// checks for right child, if there it becomes the next in line for traversal
                			if (node.down[1] != null) {
                    				stack.push(node.down[1]);
                    				node = node.down[1];
						// go to leftmost node from right child and push it to the stack	
                    				while (node.down[0] != null) {
                        				stack.push(node.down[0]);
                        				node = node.down[0];
					}
				}
				// checks top node on the stack and sets the iterator to it if nessary
                		if (!stack.isEmpty() && stack.peek().data.iterator().hasNext()) {
                    			keyListIterator = stack.peek().data.iterator();
                    			return true;
				}
			}

            		return false;
			}
			
			/**
			 * checks if the next entry in the tree exists ir is empty
			 * returns next node in tree
			 */
        		@Override
			public T next() {
				// if the current key list iterator has more keys return the next key
            			if (keyListIterator != null && keyListIterator.hasNext()) {
                			return keyListIterator.next();
				}
				// if key list is empty, call hasNext() to check if there is any other nodes in the tree
            			if (!hasNext()) {
                			throw new NoSuchElementException("iterator is empty");
				}
				// set keyListIterator to the keys of the next node
            			keyListIterator = stack.peek().data.iterator();
            			return keyListIterator.next();
			}
		};
	}

	
}
