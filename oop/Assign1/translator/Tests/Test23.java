class B {
  public String toString() {
    return "B";
  }
}

public class Test23 {
  public static void main(String[] args) {
    B b1 = new B();
    B b2 = b1;
    Object o = b2;
    System.out.println(b2.toString());
  }
}