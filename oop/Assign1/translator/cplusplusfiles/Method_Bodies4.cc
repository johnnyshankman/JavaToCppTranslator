#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
namespace java { 
    namespace lang { 
        
        __A::__A(String fld) : __vptr(&__vtable), fld(fld) {
            
            
        }
        
        __A_VT __A::__vtable;
        
        
        
        String __A::getFld( A __this ) { 
            
            
            
            
            return __this->fld;
            
        }
        
        
        __Test4::__Test4() : __vptr(&__vtable) {
            
        }
        
        __Test4_VT __Test4::__vtable; 
        
    }
}
