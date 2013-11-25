#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
 namespace java { 
 namespace lang { 
     
     
     // The Constructor will pass in the vtable 
     __A::__A() : __vptr(&__vtable) { 
     
         
     }
     
     // Basically the One Static vtable 
     __A_VT __A:: __vtable; 
     
     
     
     // Instance Method 
     String __A::toString( A __this) { 
     
         std::ostringstream sout;
         sout << "A";
         return new __String(sout.str());
         
     
     
     }
     
     // The Object destructor.
   
     
     
     __Test1::__Test1() : __vptr(&__vtable) { 
     
     
     }
     
     // The static vtable 
     __Test1_VT __Test1:: __vtable; 
     
     
    
     
     
}
}

