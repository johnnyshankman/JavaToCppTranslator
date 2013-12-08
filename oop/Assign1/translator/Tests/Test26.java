class A {

  private double fld;

  public String toString() {
    return "A's value is" + fld;
  }

  public A(double fld) {
    this.fld = fld;
  }

}


class B extends A  {
  private double fld;

  public B(double fld) {
    this.fld = fld;
  }

  public double getFld() {
    return fld;
  }
}




public class Test26 {

  public static void main(String[] args) {
    B b = new B(1.5);
    A a1 = new A(2.5);
    A a2 = b;
    System.out.println(a1.toString());
    System.out.println(a2.toString());
  }
}