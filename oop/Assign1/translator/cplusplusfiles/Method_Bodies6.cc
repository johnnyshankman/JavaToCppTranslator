#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
namespace java { 
    namespace lang { 


        __A::__A() : __vptr(&__vtable) { 
            
            fld = __rt::literal("A");
        }
        
        
        __A_VT __A:: __vtable; 
        
        
        void __A::setFld( A __this, String f) { 
            
            
            __this->fld = f; 
            
            
        }
        
        void __A::almostSetFld(A __this , String f) { 
        
            String fld; 
            fld = f; 
            
        }
        
        String __A::getFld(A __this) { 


            return __this->fld; 
            
            
        }
        
        
        
        
        __Test6::__Test6() : __vptr(&__vtable) { 
            
            
        }
        
        __Test6_VT __Test6::__vtable; 
        
            
    }
}
