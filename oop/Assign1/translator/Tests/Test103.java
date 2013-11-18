//Has a relation 
 class A{
	
	
}
 class B{
	B(){
		System.out.println("In B");
	}
}
 
class C extends A{
	 B b = new B();
	
		
	} 

public class Test103 {
	public static void main(String args[]){
		C myC = new C();
		
	}

}
