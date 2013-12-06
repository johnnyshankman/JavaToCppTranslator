class A {
  



  public String toString() {
     

    return "A";
  }


  


  public String Something(String s)  { 

   return s; 
  }

  public String Something(String s, String d)  { 

//std::cout << " print Something Else"  << std::endl; 
  }

  
  

}

public class Test1 { 
  public static void main(String[] args) {
    A a = new A();
      //A [] a = new A[5];

      // Handle the case with a call to AN OVERLOADED METHOD 
    System.out.println(a.Something("ankit")); 
    System.out.println(a.toString());
      System.out.println(a.toString());
  }
}