#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
 namespace java { 
 namespace lang { 

     __A::__A() : __vptr(&__vtable) {
         
         
     }
     
     
     // Here is the static vtable 
     __A_VT __A:: __vtable; 
     
     String __A::toString( A __this ) { 
         
         std::ostringstream sout;
         sout << "A";
         return new __String(sout.str());
         
         
     }
     
     __B::__B() : __vptr(&__vtable) { 
         
         
     }
     
     __B_VT __B:: __vtable; 
     
     
     
     String __B::toString( B __this) { 
         
         std::ostringstream sout;
         sout << "B";
         return new __String(sout.str());
         
     
     }
     
     __Test5::__Test5() : __vptr(&__vtable) { 
     
     
     }
     
     __Test5_VT __Test5:: __vtable; 
     
     
     
}
}
