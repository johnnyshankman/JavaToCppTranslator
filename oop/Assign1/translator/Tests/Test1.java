class A {
  public String toString() {
    return "A";
  }
}

public class Test1 { 
  public static void main(String[] args) {
    A a = new A();
    System.out.println(a.toString());
  }
}