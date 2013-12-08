class A {
  A self;
  
  public A() {
    self = this;
  }

  public A self() { return self; }
}

public class Test17 {
  public static void main(String[] args) {
    A a = new A();
    System.out.println(a.self().toString());
  }
}