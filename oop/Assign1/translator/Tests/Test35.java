class A {
  A some;

  public void printOther(A other) {
    System.out.println(other.toString());
  }
}

public class Test14 {
  public static void main(String[] args) {
    A a = new A();
    A other = new A();
    a.printOther(other); // throws NullPointerException
  }
}