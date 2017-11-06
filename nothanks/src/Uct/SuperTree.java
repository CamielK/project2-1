package Uct;

public class SuperTree {
	public static void main(String[] args){
		Vertex[] v = new Vertex[33];
		/*for(int i = 0; i<=32; i++) v[i] = new Vertex(i+3);
		for(int i = 0; i<=32; i++){
			for(int j = 0; j<=32; j++)if(j!=i)v[i].addSuperChild(v[j]);
		}*/
		v[5] = new Vertex(5+3);
		v[6] = new Vertex(6+3);
		v[7] = new Vertex(7+3);
		
		v[5].addSuperChild(v[6]);
		
		v[6].addSuperChild(v[7]);
		System.out.println(v[5].getChild().getChild().getCardValue());
		System.out.println("Test");
	}
}
