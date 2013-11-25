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
     
     // The constructor will pass in the vtable
     __Test2::__Test2() : __vptr(&__vtable) { 
         
         
     }
     
     // The static vtable 
     __Test2_VT __Test2:: __vtable; 

 
 
 }
 }

