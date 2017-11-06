package Uct;


public class SuperTree {
	public static void main(String[] args){
		Node root = new Node(3);
		Node child4 = new Node(4);
		Node child5 = new Node(5);
		Node child6 = new Node(6);
		root.addChild(child4);
		root.addChild(child5);
		root.addChild(child6);
		
		child4.addChild(child5);
		child4.addChild(child6);
		
		child5.addChild(child6);
		
		preOrder(root);
	}
		private Node root;
		private int amountOfNodes;
		private String postOrder, preOrder;
		
		public static void inOrder(Node v){
			boolean flagOpen = false, flagClosed = false, flagOr = false;		
			for(int i = 0; i < v.getChildren().length; i++){
				if(v.getChildren()[i] != null){
					inOrder(v.getChildren()[i]);
				}
			}
			visit(v);
		}
		
		public static Node visit(Node v) {
			System.out.print(" " + v.getCardValue() + " ");
			return v;
		}
		
		public SuperTree(Node root){
			postOrder = " ";
			preOrder = " ";
			this.root = root;
			amountOfNodes = 1;
		}
		
		public static void preOrder(Node v){
			visitPre(v);
			for(int i = 0; i < v.getChildren().length; i++){
				if(v.getChildren()[i] != null){
					preOrder(v.getChildren()[i]);
				}
			}
			//if(v.leftChild != null) preOrder(v.leftChild);
			//if(v.rightChild != null) preOrder(v.rightChild);
		}

		public static void visitPre(Node v){
			System.out.print(v.getCardValue() + " ");
		}
		
		public void postOrder(Node v){
		//	if(v.leftChild != null){
		//		postOrder(v.leftChild);
		//	}
		//	if(v.rightChild != null){
		//		postOrder(v.rightChild);
		//	}
		//	visitPost(v);
		}
		
		public void visitPost(Node v){
		//	postOrder += (v.getElem() + " ");
		}
		
/*		
		public E visit(Node v) {
			System.out.print(" " + v.getElem() + " ");
			return v.getElem();
		}
		
		public void print(){
			System.out.println("PostOrder: " + postOrder);
			System.out.println("PreOrder:  " + preOrder);
		}
		
		public void inOrderCompute(Node v, boolean x, boolean y){
			if(v.leftChild != null){
				inOrder(v.leftChild);
			}
			compute(v);
			if(v.rightChild != null){
				inOrder(v.rightChild);
			}
		}
		
		public boolean compute(Node v){
			if(v.getElem() == "->"){
				if(!compute(v.getLeftChild()))return true;
				else if(compute(v.getRightChild()))return true;
				else return false;
			}
			if(v.getElem() == "or"){
				if(compute(v.getLeftChild()) || compute(v.getRightChild()))return true;
				else return false;
			}
			if(v.getElem() == "xor"){
				if(compute(v.getLeftChild()) == compute(v.getRightChild()))return false;
				else return true;
			}	
			if(v.getElem() == "and"){
				if(compute(v.getLeftChild()) && compute(v.getRightChild()))return true;
				else return false;
			}
			if(v.getElem() == "X")return false; 
			if(v.getElem() == "Y")return true;
			return false;
		}	
		*/
}
