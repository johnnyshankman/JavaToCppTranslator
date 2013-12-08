class A {
  public A self;
  
  public A() {
    self = this;
  }
}

class B extends A{
	public B(){}
}

public class Test9 {
  public static void main(String[] args) {
    A a = new A();
    B b = a;
    System.out.println(b.self.toString());
  }
}