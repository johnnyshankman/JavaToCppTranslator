class A {
  String fld;
  
  public A() {
    fld = "A";
  }

  public getFld(){
    return fld;
  }

}

class B extends A {
  String fld;

  public B() {
    fld = "B";
  }

  public getFld(String str){
    return fld + " " + str;
  }
}


public class Test28 {
  public static void main(String[] args) {
    B b = new B();
    System.out.println(b.getFld());
    System.out.println(b.getFld("hello"));
  }
}