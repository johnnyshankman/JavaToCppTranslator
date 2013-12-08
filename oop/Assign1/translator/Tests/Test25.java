class A {
  private double fld;

  public A(double fld) {
    this.fld = fld;
  }

  public double getFld() {
    return fld;
  }
}

public class Test25 { 
  public static void main(String[] args) {
    A a = new A(2.5);
    System.out.println(a.getFld());
  }
}