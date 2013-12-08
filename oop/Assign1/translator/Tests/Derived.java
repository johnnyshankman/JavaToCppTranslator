public class Base {
    public        void m(Object o) { System.out.println("Base.m(Object)"); }
    public static void m(String s) { System.out.println("Base.m(String)"); }
    public        void m(Class c)  { System.out.println("Base.m(Class)");  }
}

public class Derived extends Base {
    public        void m(Object o) { System.out.println("Derived.m(Object)"); }
    public static void m(String s) { System.out.println("Derived.m(String)"); }
    
    public static void main(String[] args) {
        Base b = new Derived();
        b.m(new Object());
        b.m(new Integer(5));
        b.m("Hello");
        b.m(b.getClass());
    }
}