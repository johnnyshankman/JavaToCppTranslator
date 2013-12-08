class A {
  private int x;
  private int y;

  public A(int a, int b) {
    x = a;
    y = b;
  }

  public String getX() {
    return x;
  }

  public String getY() {
    return y;
  }
}

public class Test24 { 
  public static void main(String[] args) {
    A a = new A(10, 20);
    System.out.println(a.getX());
    System.out.println(a.getY());
  }
}