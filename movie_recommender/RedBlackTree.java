// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.NoSuchElementException;


/**
 * Class that inserts the new node into the tree and keeps the black and red tree properties
 *
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

	// imported def from starter sheet
        protected static class RBTNode<T> extends Node<T> {
                public int blackHeight = 0;
                public RBTNode(T data) { super(data); }
                public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
                public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
                public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1];}
        }

	/**
	 * function that inserts the node into the tree
	 * @param T data and the val that the node holds
	 * @return True if node is sucessfully added
	 */
        public boolean insert(T val) {
                if (val == null){
                        throw new IllegalArgumentException("Error inserting Node");
                }
                RBTNode<T> newNode = new RBTNode<>(val);
                boolean insert = insertHelper(newNode);
                if (insert) {
                        enforceRBTreePropertiesAfterInsert(newNode);
                        if (root instanceof RBTNode){
                                ((RBTNode<T>) root).blackHeight = 1;
                        }
                }
                return insert;
        }

	/**
	 * keeps the properties of the black and red nodes intact
	 * @param node that we start looking at to maintain the balance
	 */
        protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> node) {
                if (node == root){
			// root needs to be black
                        node.blackHeight = 1;
                        return;
                }

		// set parent and granparent nodes for reference
                RBTNode<T> par = node.getUp();
                RBTNode<T> gP = par.getUp();
		
		// cant have two red nodes next to eachother
                while (par != null && par.blackHeight == 0){
                        if (gP == null) {
                                throw new IllegalStateException("Wrong State: Root is red.");
                        }

			// checks to see what child the current node it
                        boolean isParentLeftChild = gP.getDownLeft() == par;
			RBTNode<T> uncle;
			if (isParentLeftChild) {
   				 uncle = gP.getDownRight();
			} else {
   				 uncle = gP.getDownLeft();
			}
			// if uncle is not black
                        if (uncle != null && uncle.blackHeight == 0){
                                par.blackHeight = 1;
                                uncle.blackHeight = 1;
                                gP.blackHeight = 0;
                                node = gP;
				}else{
				// if left child senario
                                if (isParentLeftChild){
                                        if (par.getDownRight() == node){
                                                rotate(node, par);
                                                node = par;
                                                par = node.getUp();
                                        }

                                        rotate(par, gP);
                                        par.blackHeight = 1;
                                        gP.blackHeight = 0;
				// if right child senario
                                }else{
                                        if (par.getDownLeft() == node){
                                                rotate(node, par);
                                                node = par;
                                                par = node.getUp();
                                        }
					// roatate nodes so color order is maintained
                                        rotate(par, gP);
                                        par.blackHeight = 1;
                                        gP.blackHeight = 0;
                                }
                        }
                        par = node.getUp();
                        if (par != null) {
                                gP = par.getUp();
                        }
                }
		// check if root
                if (root instanceof RBTNode){
                        ((RBTNode<T>) root).blackHeight = 1;
                }
        }



}
