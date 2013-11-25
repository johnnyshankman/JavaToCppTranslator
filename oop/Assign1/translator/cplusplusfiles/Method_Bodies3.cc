#include "java_lang.h"    
#include "Header.h"
#include <sstream>

namespace java { 
    
    namespace lang { 
    // The Constructor will pass in the vtable 
     __A::__A(String f) : __vptr(&__vtable), 
                         fld(f) { 
     
         
     }
     
     // Basically the One Static vtable 
     __A_VT __A:: __vtable; 
     
     
     
     // Instance Method 
     String __A::getFld( A __this) { 
     
         return __this->fld; 
         
     
     
     }
     
     
     __Test3::__Test3() : __vptr(&__vtable) { 
     
     
     }
     
     // The static vtable 
     __Test3_VT __Test3:: __vtable; 


}

}
