
//Taylor Clingenpeel
//after 3-4 days of nonstop coding I have created this 
//which does a thing
public class RedBlackTree {
	public final int DEFAULT_VALUE = 0;

	public enum color {// create the fake type Color
		red, black// with Red or Black as a option
	};

	class Node {// each of my nodes contains a LC,RC,Parent,Sibling,Uncle,and
				// Grandpa
		int data;// this makes it easier to moves the objects around in the tree
					// as well as to check
		Node leftChild;// an objects surroundings in the tree
		Node rightChild;
		Node parent;
		Node sibling;
		Node uncle;
		Node grandpa;
		color color;

		public Node getLeftChild() {// Case i need some getters and Setters
			return leftChild;
		}

		public void setLeftChild(Node leftChild) {
			this.leftChild = leftChild;
		}

		public Node getRightChild() {
			return rightChild;
		}

		public void setRightChild(Node rightChild) {
			this.rightChild = rightChild;
		}

		public Node(int adata) {// initialize i made mine black with the intention of
			this.data = adata;// making all null nodes black but i don't think i ended up using it
			this.leftChild = null;
			this.rightChild = null;
			this.grandpa = null;
			this.sibling = null;
			this.parent = null;
			this.uncle = null;
			this.color = RedBlackTree.color.black;
		}
	}

	public void TreeArrInsert(int[] a) {// Simple method to insert an Array of Objects instead of one at a time
		for (int i = 0; i < a.length; i++) {
			Treeinsert(a[i]);
		}

	}

	public static Node root = null;// Make me a root

	public void Treeinsert(int adata) {// Tree insert is simple

		if (root == null) {// i pulled the root in the beginning so it didn't mess with any recursion
			Node newNode = new Node(adata);// or family's later on in the code
			root = newNode;
			root.color = RedBlackTree.color.black;
			root.parent = null;
			root.uncle = null;
			root.grandpa = null;
			root.sibling = null;
		} else {// now into the meat of the code
			Treeinsert(root, adata);
			SetRootDown();

		}
		return;
	}

	public void Treeinsert(Node aNode, int adata) {// this is the recusivly
													// called Treeinsert
		Node newNode = new Node(adata);// I created a new node
		newNode.color = RedBlackTree.color.red;// and Made it red for this
												// reason
		// If i made all newNodes black i would have to worry about the black
		// path rule following my insert
		// this was if i break a rule its a red node rule which i can then fix
		if (aNode.data < adata)// if node is less than data go right
		{//You will see a common theme of (Node.data!=0) ill explain this later
			if (aNode.rightChild == null || aNode.rightChild.data == 0) {
				aNode.rightChild = newNode;
				familySetter(aNode, newNode);
				RedBlackSorter(newNode);
			} else {
				Treeinsert(aNode.rightChild, adata);
			} // You can see this method isn't anything special just a simple
				// Binary Tree Insert
		}
		if (aNode.data > adata) {// once it has found the correct node location
									// set-set
			if (aNode.leftChild == null || aNode.leftChild.data == 0) {
				aNode.leftChild = newNode;
				familySetter(aNode, newNode);// GO-TO method for description
				RedBlackSorter(newNode);// FINALLY THE RED AND BLACK PART
			} else {
				Treeinsert(aNode.leftChild, adata);
			}
		}
		return;
	}

	public void familySetter(Node aNode, Node newNode) {// this is my family Method every time i need a nodes family
		if (newNode == root) {// Members to be set, or set again i run this code which split its into 2 cases
			return;
		}
		if (aNode == root) {// CASE 1, Parent == root
			FamSetterInnerFamily(aNode, newNode);// this case you only do inner family cause there isn't must else
			return;
		} else {// CASE 2 do both
			FamSetterInnerFamily(aNode, newNode);
			FamSetterExtended(aNode, newNode);
		}
		if (aNode.parent != null) {// this recursively calls the method again, moving up the tree by one node
			if (aNode.parent == root || newNode == root)
				return;
			familySetter(aNode.parent, newNode.parent);
		}
		return;
	}

	
	 public boolean searchTree(int x) {//This is a new search method ii havent
	 if (x > 0) {// finished yet though i think i will use it to help with removals
	 if (root == null) {
	 System.out.println("Tree is empty");
	 }
	 else
	 {
	 if(searchTree(root, x) == true)
	 {return true;}
	 }
	 }
	 else {
	 System.out.println("invalid search value");
	
	 }
	 return false;
	 }
	
	 public boolean searchTree(Node node, int x) {
	 if (node.data == x) {
	 return true;
	 } else {
	 if (x < node.data)
	 searchTree(node.leftChild, x);
	 else if (x > node.data)
	 searchTree(node.rightChild, x);
	 if ((node.leftChild == null || node.leftChild.data == 0)
	 && (node.rightChild == null || node.rightChild.data == 0)) {
	
	 }
	
	 }
	 return false;
	 }


	public void FamSetterInnerFamily(Node aNode, Node newNode) {
		newNode.parent = aNode;// Set Parent easy
		if (aNode.leftChild == null || aNode.rightChild == null) {//if the cparent has a null child
			return;												//NO SIBLINGS
		} else {
			if (aNode.leftChild == newNode) {
				newNode.sibling = aNode.rightChild;
				aNode.rightChild.sibling = newNode;
				newNode.sibling.uncle = newNode.uncle;
			} else if (aNode.rightChild == newNode) {
				newNode.sibling = aNode.leftChild;
				aNode.leftChild.sibling = newNode;
				newNode.sibling.uncle = newNode.uncle;// also do some uncle work
														// here for fun
			}
			newNode.sibling.uncle = newNode.uncle;
		}

	}

	public void FamSetterExtended(Node aNode, Node newNode) {// Set the extended GRANDPA and UNCLE
		newNode.grandpa = aNode.parent;
		if (newNode.grandpa.leftChild == null || newNode.grandpa.rightChild == null) {//If Grandpas have 
			return;											//a null child then No Uncle either
		} else {
			if (aNode.data < aNode.parent.data) {
				newNode.uncle = newNode.grandpa.rightChild;
				if (newNode.uncle.leftChild != null) {// Check uncles children,
														// set if they are alive
					newNode.uncle.leftChild.uncle = newNode.parent;
				}

			} else if (aNode.data > aNode.parent.data) {
				newNode.uncle = newNode.grandpa.leftChild;
				if (newNode.uncle.rightChild != null) {
					newNode.uncle.rightChild.uncle = newNode.parent;
				}

			}
		}
		return;
	}

	public void SetRootDown() {// This method is a hybrid of the top one
		if (root == null) {// during my testing i kept coming across nodes with
							// false uncles or false siblings
			return; // so after a long time of thinking and trying I created a
					// method which starts at the root
		} // and goes down both subtrees FamilySetting every node along the way
		SetRootDown(root, root.leftChild);// and i do this at the end of every
											// insert just to make sure
		SetRootDown(root, root.rightChild);// Every node has his family up to
											// date
	}
	public void SetRootDown(Node aNode, Node Child) {
		if (Child == null) {
			return;
		}
		familySetter(aNode, Child);
		if (Child.leftChild == null || Child.leftChild.data == 0) {
			return;
		} else {
			SetRootDown(Child, Child.leftChild);
		}
		if (Child.rightChild == null || Child.rightChild.data == 0) {
			return;
		} else {
			SetRootDown(Child, Child.rightChild);
		}
	}
	public void RedBlackSorter(Node newNode) {// this is my first Case once the
												// node has been inserted
		if (newNode.parent.color == RedBlackTree.color.black) {// if the node has a black parent
			return;// everything is good to red rules broken and nothing to
					// recolor or switch
		} else if (newNode.parent.color == RedBlackTree.color.red) {// However if there is a red Parent
			Case(newNode);// move into diagnosing methods
		} else {
			System.out.println("hitting else in RedBlack Sorter");// fail safe
		}
		familySetter(newNode.parent, newNode);// family setter for fun
		root.color = RedBlackTree.color.black;// Make sure the root is black at all times
	}

	public Node createFakeUncle(Node newNode) {
		Node fakeUncle = new Node(0);
		newNode.parent.sibling = fakeUncle;
		fakeUncle.sibling = newNode.parent;
		newNode.uncle = fakeUncle;
		if (newNode.parent == newNode.grandpa.leftChild) {
			newNode.grandpa.rightChild = fakeUncle;
		} else {
			newNode.grandpa.leftChild = fakeUncle;
		}
		fakeUncle.parent = newNode.grandpa;
		if (newNode.grandpa != root)
			fakeUncle.grandpa = newNode.grandpa.parent;
		fakeUncle.color = RedBlackTree.color.black;
		Node UL = new Node(0);
		Node UR = new Node(0);
		fakeUncle.leftChild = UL;
		fakeUncle.rightChild = UR;
		UL.parent = fakeUncle;
		UR.parent = fakeUncle;
		UL.uncle = fakeUncle.sibling;
		UR.uncle = fakeUncle.sibling;
		UL.grandpa = fakeUncle.parent;
		UR.grandpa = fakeUncle.parent;
		return fakeUncle;
	}

	public Node createFakeGrandpa(Node newNode) {
		Node fakeGrandpa = new Node(0);
		newNode.grandpa.parent = fakeGrandpa;
		newNode.uncle.grandpa = fakeGrandpa;
		newNode.parent.grandpa = fakeGrandpa;
		newNode.parent.uncle = null;
		newNode.uncle.uncle = null;
		fakeGrandpa.leftChild = newNode.grandpa;
		fakeGrandpa.color = RedBlackTree.color.black;
		return fakeGrandpa;

	}

	public void Case(Node newNode) {// here we start with if the root is apart
									// of our Nodes immediate family
		if (newNode == root || newNode.parent == root) {// if it is there a goodchance it's fine so return;
			return;
		}
		if (newNode.grandpa == root) {// next if the Grandpa is the root
			if (newNode.uncle == null// now there is 2 cases if grandpas the
										// root
					|| (newNode.uncle.data == 0 && newNode.uncle.color.equals(RedBlackTree.color.black))) {
				Node fakeUncle = createFakeUncle(newNode);// CASE 1 there is no
															// Uncle
				Node fakeGGrandpa = createFakeGrandpa(newNode);// i wrote this code after my rotation an recolorations and found
				CasePart2(newNode); // that if my node didnt have an Uncle none of my fixes would work so i came up with the idea of creating
				fakeUncle.getLeftChild().equals(null);// a fake Uncle and in this case a fake GreatGrandfather to help with the fixes
				fakeUncle.getRightChild().equals(null);
				fakeUncle = null;// After the fix both of these are set to null and they are wiped away *except their data is 0*
				fakeGGrandpa = null;
				return;
			} else {
				Node fakeGGrandpa = createFakeGrandpa(newNode);// this is if it did have an Uncle
				CasePart2(newNode);
				fakeGGrandpa = null;
				return;
			}
		} else if (newNode.uncle == null// If grandpa isnt the root then we dont have to worry about him so we create
				|| (newNode.uncle.data == 0 && newNode.uncle.color.equals(RedBlackTree.color.black))) {// FakeUncle
			Node fakeUncle = createFakeUncle(newNode);
			CasePart2(newNode);
			fakeUncle.getLeftChild().equals(null);// then set to null afterwards
			fakeUncle.getRightChild().equals(null);
			fakeUncle = null;
			return;
		}
		if (newNode.grandpa != null && newNode.uncle != null) {// also if neither are null or root 
			CasePart2(newNode);//simple Case 2
			return;
		}
		return;

	}

	public void CasePart2(Node newNode) {// this is the next major Case in our diagnosis
		if (newNode == root) {// again root for fail safe
			return;
		}
		if (newNode.uncle.color == RedBlackTree.color.red) {// CASE 2A if the uncle (which could be fake) is red
			Case1(newNode);// this needs nothing but a recoloring
			if (newNode == root || newNode.parent == root || newNode.grandpa == root) {
				// This recursivly calls the whole diagnosis in case you have a
				// huge tree that needs to "fix up" in a sense
			} else {
				RedBlackSorter(newNode.grandpa);
			}// if Uncle is black *or fake* then its more complicated
		} else if (newNode.uncle.color == RedBlackTree.color.black) {
			Case2(newNode);// Case2 or more like Case4 is a lot more interesting
			if (newNode == root || newNode.parent == root || newNode.grandpa == root) {

			} else {
				RedBlackSorter(newNode.grandpa);
			}
		} else {
			System.out.println("Hitting else in Case method Uncle is neither black nor red");
		}
		return;
	}

	private void Case1(Node newNode) {// this checks our variables and recolors
										// the nodes
		if ((newNode.uncle == null || newNode.uncle.data == 0) || (newNode.grandpa == null || newNode.grandpa.data == 0)
				|| newNode.parent == null) {
			System.out.println("null family was given to Case 1");
			return;
		} // at the end I set the root equal to black again just in case one of
			// our family members was the root;
		newNode.parent.color = RedBlackTree.color.black;
		newNode.uncle.color = RedBlackTree.color.black;
		newNode.grandpa.color = RedBlackTree.color.red;
		root.color = RedBlackTree.color.black;
		return;
	}

	private void Case2(Node newNode) {
								// here I came across a similar problem		
		Node T1 = new Node(0);// with uncle and grandpa if the nodes children were null then they weren't going to move correctly So 
		Node T2 = new Node(0);//created all the fakeChildren i would need and i set up if statements to
		Node T3 = new Node(0);// actually use these variables if i needed them in the Case
		Node T4 = new Node(0);// With Case2 we have 4 possible Cases
		Node T5 = new Node(0);// A left Rotate, small left Rotate, Right rotate, small right Rotate.
		if (newNode == root || newNode.leftChild == root || newNode.rightChild == root) {
			return;
		}
		if (newNode.parent.data < newNode.grandpa.data)// So if Parent is less the Grandma (LEFT)
			{
			if (newNode.data < newNode.parent.data)// and if Node is less than (LEFT)
			{ // that is a LEFT LEFT case and we simple need do a RIGHT ROTATE
				if (newNode.parent.rightChild == null) {// if it needs fake children give it.
					T3.parent = newNode.parent;
					T3.grandpa = newNode.grandpa;
					T3.sibling = newNode;
					T3.uncle = newNode.uncle;
					newNode.parent.rightChild = T3;
					newNode.sibling = T3;
					RightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
					T3 = null;
				} else
					RightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
			} else if (newNode.data > newNode.parent.data)
			// If the node is
			// greater than
			// Parent then (Right)
			// this is a Left Right case and to do that is easy
			// do a small left Rotate
			// and Finish off with a full RIGHT ROTATE
			{// and if it needs DEM CHILDREN give it
				if ((newNode.parent.leftChild == null || (newNode.parent.leftChild.data == 0))
						|| (newNode.leftChild == null || (newNode.leftChild.data == 0))
						|| (newNode.rightChild == null || (newNode.parent.leftChild.data == 0))) {
					if ((newNode.parent.leftChild == null || newNode.parent.leftChild.data == 0)
							&& (newNode.leftChild == null || (newNode.leftChild.data == 0))
							&& (newNode.rightChild == null || (newNode.rightChild.data == 0))) {
						T1.parent = newNode.parent;
						T2.parent = newNode;
						T3.parent = newNode;
						newNode.parent.leftChild = T1;
						newNode.leftChild = T2;
						newNode.rightChild = T3;
						T1.uncle = newNode.uncle;
						T2.uncle = T1;
						T3.uncle = T1;
						T2.sibling = T3;
						T3.sibling = T2;
						T1.sibling = newNode;
						newNode.sibling = T1;
						Node tempNode = newNode;
						// here i realized after the
						// small left Rotate we needed
						// to change the Node
						newNode = smallLeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						RightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						T3 = null;
						T4 = null;// Null children
						T5 = null;
						newNode = tempNode;// set node back
					} else {// Same story with less fake children if needed
						if (newNode.parent.leftChild == null) {
							T1.parent = newNode.parent;
							newNode.parent.leftChild = T1;
							T1.uncle = newNode.uncle;
							T1.sibling = newNode;
							newNode.sibling = T1;

						}
						if (newNode.leftChild == null) {
							T2.parent = newNode;
							newNode.leftChild = T2;
							T2.uncle = T1;
							T2.sibling = T3;
						}
						if (newNode.rightChild == null) {
							T3.parent = newNode;
							newNode.rightChild = T3;
							T3.uncle = T1;
							T3.sibling = T3;

						}

						Node tempNode = newNode;
						newNode = smallLeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						RightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						T1 = null;
						T2 = null;
						T3 = null;
						newNode = tempNode;
						return;
					}
				} else {
					Node tempNode = newNode;
					newNode = smallLeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
					RightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
					newNode = tempNode;
					return;
				}
			}

		} else if (newNode.parent.data > newNode.grandpa.data)// Next Case is if Grandpa is less than  parent
		{ // RIGHT

			if (newNode.parent.data < newNode.data)
				// if parent is also less
				// than Node thats another
				// ANOTHER RIGHT
			{// RIGHT RIGHT means we do the opposite of a Left Left meaning
				// (LEFT ROTATE)
				if (newNode.parent.leftChild == null || newNode.parent.leftChild.data == 0) {// give it the chldren it needs 
					T3.parent = newNode.parent;
					T3.grandpa = newNode.grandpa;
					T3.sibling = newNode;
					T3.uncle = newNode.uncle;
					newNode.parent.leftChild = T3;
					newNode.sibling = T3;
					LeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
					T3 = null;// alway null children
				} else
					LeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
			} else if (newNode.parent.data > newNode.data) {// Right Left

				// If it is Right Left then we simply do a smallRightRotate and
				// full LeftRotate
				if (newNode.leftChild == null || newNode.rightChild == null || newNode.parent.rightChild == null) {
					if (newNode.leftChild == null && newNode.rightChild == null && newNode.parent.rightChild == null) {
						newNode.leftChild = T3;
						newNode.rightChild = T4;// give children if needed
						newNode.parent.rightChild = T5;
						T3.parent = newNode;
						T3.uncle = T5;
						T4.uncle = T5;
						T3.sibling = T4;
						T4.parent = newNode;
						T4.sibling = T3;
						T5.parent = newNode.parent;
						T5.sibling = newNode;
						T5.uncle = newNode.uncle;
						Node tempNode = newNode;
						newNode = smallRightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						LeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						T3 = null;// make fake children null
						T4 = null;// again set newNode back to its
						T5 = null;// original node
						newNode = tempNode;
											
					} else {
						if (newNode.parent.rightChild == null) {
							newNode.parent.rightChild = T5;
							T5.parent = newNode.parent;
							T5.sibling = newNode;
							T5.uncle = newNode.uncle;

						}
						if (newNode.rightChild == null) {
							newNode.rightChild = T4;
							T4.uncle = T5;
							T4.parent = newNode;
							T4.sibling = T3;

						}
						if (newNode.leftChild == null) {
							newNode.leftChild = T3;
							T3.parent = newNode;
							T3.uncle = T5;
							T3.sibling = T5;
						}

						Node tempNode = newNode;
						newNode = smallRightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						LeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
						newNode = tempNode;
						T3 = null;
						T4 = null;
						T5 = null;
					}
				} else {
					Node tempNode = newNode;
					newNode = smallRightRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
					LeftRotate(newNode, newNode.parent, newNode.grandpa, newNode.uncle);
					newNode = tempNode;
				}
			}
		}
		return;
	}
	// This is my Right Rotate if would explain it but its kinda boringly simple
	// just set everything to its new everything
	// ill attempt to organize this at a later date if i can think of a way
	private void RightRotate(Node Node, Node Parent, Node Grandpa, Node Uncle) {
		Grandpa.leftChild = Parent.rightChild;
		Parent.rightChild = Grandpa;
		Parent.parent = Grandpa.parent;
		Grandpa.parent.leftChild = Parent;
		Grandpa.parent = Parent;
		Node.grandpa = Node.parent.parent;
		Grandpa.leftChild.parent = Grandpa;
		Grandpa.leftChild.grandpa = Parent;
		Node.sibling = Grandpa;
		Parent.sibling = Grandpa.sibling;
		Grandpa.sibling = Node;
		Uncle.sibling = Grandpa.leftChild;
		Grandpa.leftChild.sibling = Uncle;
		Node.uncle = Parent.sibling;
		Grandpa.uncle = Parent.sibling;
		Grandpa.color = RedBlackTree.color.red;
		Parent.color = RedBlackTree.color.black;
		Parent.grandpa = Parent.parent.parent;
		Parent.uncle = Parent.parent.sibling;
		Grandpa.grandpa = Parent.parent;
		Uncle.grandpa = Parent;
		Uncle.uncle = Node;
		if (Node.leftChild != null)
			Node.leftChild.uncle = Grandpa;
		if (Node.rightChild != null)
			Node.rightChild.uncle = Grandpa;
		Grandpa.leftChild.uncle = Node;
		Uncle.leftChild.uncle = Grandpa.leftChild;
		Uncle.rightChild.uncle = Grandpa.leftChild;
		if (Parent.parent.leftChild == Grandpa)
			Parent.parent.leftChild = Parent;
		else if (Parent.parent.rightChild == Grandpa)
			Parent.parent.rightChild = Parent;
		if (Grandpa == root)// if Grandpa was root set parent to root
			root = Parent;
		return;
	}

	// both small Rotates return a node value as the temp value of our new Node
	// value so when it fully rotates it is correct
	private Node smallLeftRotate(Node Node, Node Parent, Node Grandpa, Node Uncle) {
		Node.parent = Grandpa;
		Node x = Node.leftChild;
		Parent.rightChild = x;
		Node.leftChild = Parent;
		Node.rightChild.grandpa = Grandpa;
		Node.rightChild.sibling = Parent;
		Node.rightChild.uncle = Uncle;
		Parent.parent = Node;
		Parent.grandpa = Grandpa;
		Node.grandpa = Grandpa.parent;
		Grandpa.leftChild = Node;
		Node.sibling = Uncle;
		Node.uncle = Grandpa.sibling;
		if (Uncle != null)
			Uncle.leftChild.uncle = Node;
		if (Uncle != null)
			Uncle.rightChild.uncle = Node;
		Parent.rightChild.parent = Parent;
		Parent.rightChild.parent = Parent;
		Parent.rightChild.uncle = Node.rightChild;
		Parent.leftChild.uncle = Node.rightChild;
		Parent.leftChild.sibling = Parent.rightChild;
		Parent.rightChild.sibling = Parent.leftChild;
		Uncle.sibling = Node;
		Parent.rightChild.parent = Parent;
		Parent.rightChild.grandpa = Node;
		Parent.leftChild.grandpa = Node;
		Parent.uncle = Uncle;
		Parent.sibling = Node.rightChild;
		Node.rightChild.grandpa = Grandpa;
		return Parent;
	}

	// leftRotate
	private void LeftRotate(Node Node, Node Parent, Node Grandpa, Node Uncle) {
		Grandpa.rightChild = Parent.leftChild;
		Grandpa.rightChild.parent = Grandpa;
		Parent.leftChild = Grandpa;
		Parent.parent = Grandpa.parent;
		Grandpa.parent = Parent;
		Node.grandpa = Node.parent.parent;
		Grandpa.rightChild.grandpa = Parent;
		Grandpa.rightChild.uncle = Node;
		if (Node.leftChild != null)
			Node.leftChild.uncle = Grandpa;
		if (Node.rightChild != null)
			Node.rightChild.uncle = Grandpa;
		Node.sibling = Grandpa;
		Parent.sibling = Grandpa.sibling;
		if (Grandpa.sibling != null)
			Grandpa.sibling.sibling = Parent;
		Grandpa.sibling = Node;
		Uncle.sibling = Grandpa.rightChild;
		Node.uncle = Parent.sibling;
		Grandpa.uncle = Parent.sibling;
		Parent.color = RedBlackTree.color.black;
		Grandpa.color = RedBlackTree.color.red;
		Grandpa.rightChild.sibling = Uncle;
		Uncle.grandpa = Parent;
		Uncle.uncle = Node;
		Grandpa.grandpa = Parent.parent;
		Parent.grandpa = Parent.parent.parent;
		Parent.uncle = Parent.parent.sibling;
		if (Parent.parent.leftChild == Grandpa)
			Parent.parent.leftChild = Parent;
		else if (Parent.parent.rightChild == Grandpa)
			Parent.parent.rightChild = Parent;
		if (Grandpa == root) {
			root = Parent;
		}
		return;
	}

	private Node smallRightRotate(Node Node, Node Parent, Node Grandpa, Node Uncle) {
		Grandpa.rightChild = Node;
		Node.parent = Grandpa;
		Node.grandpa = Grandpa.parent;
		Node.uncle = Grandpa.sibling;
		Node x = Node.rightChild;
		Node.rightChild = Parent;
		Node.sibling = Uncle;
		Parent.parent = Node;
		Parent.grandpa = Grandpa;
		Parent.uncle = Uncle;
		Parent.leftChild = x;
		Parent.sibling = Node.rightChild;
		Grandpa.rightChild = Node;
		Uncle.sibling = Node;
		if (Uncle.leftChild != null)
			Uncle.leftChild.uncle = Node;
		if (Uncle.rightChild != null)
			Uncle.rightChild.uncle = Node;
		Node.rightChild.grandpa = Grandpa;
		Node.rightChild.uncle = Uncle;
		Node.rightChild.sibling = Parent;
		Parent.leftChild.parent = Parent;
		Parent.leftChild.grandpa = Node;
		Parent.leftChild.uncle = Node.leftChild;
		Parent.leftChild.sibling = Parent.rightChild;
		Parent.rightChild.grandpa = Node;
		Parent.rightChild.uncle = Node.leftChild;
		Parent.rightChild.sibling = Parent.leftChild;
		return Parent;
	}

	public void printPreOrder() {// Simple PreOrder Traversal method
		System.out.println("Printing in Pre-Order");
		printPreOrder(root);
	}

	public void printPreOrder(Node node) {
		if (node == null || node.data == 0) {
			return;
		}
		System.out.print(node.data);
		if (node.color == RedBlackTree.color.black)
			System.out.print(" Black ");
		else
			System.out.print(" Red ");
		System.out.println(" ");

		if (node.leftChild != null) {
			printPreOrder(node.leftChild);
		}
		if (node.rightChild != null) {
			printPreOrder(node.rightChild);

		}
		return;
	}

	public void printInorder() {// simple In Order Traversal method
		System.out.println("Printing In-Order");
		printInOrder(root);// Start out recursive method
	}

	public void printInOrder(Node node) {

		if (node == null || node.data == 0) {
			return;
		}
		printInOrder(node.leftChild);// get the left most
		System.out.print(node.data);
		if (node.color == RedBlackTree.color.black)
			System.out.print(" Black ");
		else
			System.out.print(" Red ");// print the currNode
		System.out.println(" ");
		printInOrder(node.rightChild);// Go right one
	}

	public void printPostOrder() {// simple Post Order Traversal
		System.out.println("Printing in Post-Order");
		printPostOrder(root);
	}

	public void printPostOrder(Node node) {
		if (node == null || (node.data == 0 && node.color == RedBlackTree.color.black)) {
			return;
		}
		if (node.leftChild != null) {
			printPostOrder(node.leftChild);
		}
		if (node.rightChild != null) {
			printPostOrder(node.rightChild);
		}
		{
			System.out.print(node.data);
			if (node.color == RedBlackTree.color.black)
				System.out.print(" Black ");// all traversals also print color
			else
				System.out.print(" Red ");
			System.out.println(" ");
		}

		return;
	}

	public static int height;

	public int treeHeight() {// gets the tree height Often pretends null nodes are levels
		height = 0;// will try to debug later
		if (root == null)
			return 0;
		if (root.leftChild == null && root.rightChild == null)
			return 1;
		treeHeight(root);
		return height;
	}

	public void treeHeight(Node node) {
		if (node.data == 0 && node.color == RedBlackTree.color.black) {
			return;
		}
		if (node == root) {
			height = 1;
		}
		if (node.leftChild != null && node.leftChild.data != 0) {
			treeHeight(node.leftChild);
			height++;
		}
		if (node.rightChild != null && node.leftChild == null) {
			treeHeight(node.rightChild);
			height++;
		}
		if (node.leftChild != null && node.rightChild != null)
			treeHeight(node.rightChild);

		return;
	}

	public void printLevelOrder() {// level order Print method i set it up to
									// print level by level
		System.out.println("Printing in Level-Order");
		for (int d = 1; d <= treeHeight(); d++) {
			if (d > 1)
				System.out.println(" ");
			System.out.print(d + " ");
			printGivenLevel(d, root);
		}
	}

	public void printGivenLevel(int level, Node node) {
		if (node == null)
			return;
		if (level == 1) {
			if (node == null || (node.data == 0 && node.color == RedBlackTree.color.black)) {
				return;
			}
			System.out.print("(" + node.data);
			if (node.color == RedBlackTree.color.black)
				System.out.print(" Black) ");
			else
				System.out.print(" Red) ");
			return;
		} else {
			printGivenLevel(level - 1, node.leftChild);
			printGivenLevel(level - 1, node.rightChild);
		}
		return;
	}

	public void FamilyPicture() {// Method of my own design
		// Prints each node in level-Order giving detailed descriptions on each
		// nodes family and variables
		for (int d = 1; d <= treeHeight(); d++) {
			FamilyPicture(d, root);
		} // for debugging purposes
	}

	public void FamilyPicture(int level, Node node) {
		if (node == null)
			return;
		if (level == 1) {
			if (node == null || (node.data == 0 && node.color == RedBlackTree.color.black)) {
				return;
			}
			printNodeInfo(node);
		} else {
			FamilyPicture(level - 1, node.leftChild);
			FamilyPicture(level - 1, node.rightChild);
		}
		return;
	}

	public void printNodeInfo(Node node) {// this method actually prints the
											// node info
		System.out.println(" ");
		System.out.println(" ");
		if (node == root) {
			System.out.print("Root");
			System.out.println(" ");
		}
		System.out.print("Data = " + node.data);
		System.out.print("| Color =");
		if (node.color == RedBlackTree.color.black)
			System.out.print(" Black ");
		else
			System.out.print(" Red ");
		System.out.print("| Level = " + GetDepth(node));
		System.out.println(" ");
		if (node.parent != null) {
			if (node.parent.data != 0) {
				System.out.println("Parent " + node.parent.data);
			} else
				System.out.println("No Parent");
		} else
			System.out.println("No Parent");
		if (node.grandpa != null) {
			if (node.grandpa.data != 0) {
				System.out.println("Grandpa " + node.grandpa.data);
			} else
				System.out.println("No Grandpa");
		} else
			System.out.println("No Grandpa");
		if (node.uncle != null) {
			if (node.uncle.data != 0) {
				System.out.println("Uncle " + node.uncle.data);
			} else
				System.out.println("No Uncle");
		} else
			System.out.println("No Uncle");
		if (node.sibling != null) {
			if (node.sibling.data != 0) {
				System.out.println("Sibling " + node.sibling.data);
			} else
				System.out.println("No Sibling");
		} else
			System.out.println("No Sibling");
		if (node.leftChild != null && node.rightChild != null)
			System.out.println("Left = " + node.leftChild.data + "| Right = " + node.rightChild.data);
		else if (node.leftChild == null && node.rightChild != null)
			System.out.println("Left = null | Right = " + node.rightChild.data);
		else if (node.rightChild == null && node.leftChild != null)
			System.out.println("Left = " + node.leftChild.data + "| Right = null");
		else if (node.rightChild == null && node.leftChild == null)
			System.out.println("No Children");
	}

	public static int depthheight;// this is a regular search that returns a
									// depth instead

	public int GetDepth(Node treasure) {
		depthheight = 0;
		if (root == null)
			return 0;
		if (root.leftChild == null && root.rightChild == null)
			return 1;
		GetDepth(treasure, root);
		// System.out.print(height+ " ");
		return depthheight;
	}

	public void GetDepth(Node treasure, Node node) {
		if (node.data == 0 && node.color == RedBlackTree.color.black) {
			return;
		}
		if (node == root)
			depthheight = 1;
		if (treasure == node) {
			return;
		}
		if (treasure.data < node.data) {
			depthheight++;
			GetDepth(treasure, node.leftChild);
		} else {
			depthheight++;
			GetDepth(treasure, node.rightChild);
		}
		return;
	}

	// If you need any help understanding how i did these please ask i kinda quickly commented it for you
	//though i think there a probabably easier ways than these that just didnt come to mind
	// after i finished.
	// (4/13/2016)
}
