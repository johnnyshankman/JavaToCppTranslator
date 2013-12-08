class B {
  private String fld = "B";

  public B() { }

  public B(String newfld){
    fld = newfld;
  }

  public void setFld(String f) {
    fld = f;
  }

  public void almostSetFld(String f) {
    String fld;
    fld = f;
  }

  public String getFld() {
    return fld;
  }
}

public class Test27 { 
  public static void main(String[] args) {
    B b1 = new B();
    B b2 = new B("Hello");
    b1 = b2;
    b1.almostSetFld("Fake new feild");
    System.out.println(b1.getFld());
    a.setFld("Real new feild");
    System.out.println(b1.getFld());
  }
}