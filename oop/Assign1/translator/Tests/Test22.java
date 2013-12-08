class B {
  public String toString() {
    return "This is Class B";
  }
}

public class Test22 { 
  public static void main(String[] args) {
    B sampleb1 = new B();
    B sampleb2 = new B()
    System.out.println(sampleb1.toString());
    System.out.println(sampleb2.toString());
  }
}