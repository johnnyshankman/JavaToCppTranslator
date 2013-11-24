#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
namespace java { 
    namespace lang { 
     
        __A::__A() : __vptr(&__vtable)  { 
               
            // You need a static function to do initilization 
            __A::init(this);
            
        }   
        
        // Static vtable 
        __A_VT __A::__vtable;
        
        
     // Basic Initilization
     void   __A::init(A __this) {
        // __A::a = new __String("A");
         
         __this->a = new __String("A"); 
        
     }
        // Constructor calls init 
        __B::__B() : __vptr(&__vtable) { 
            
            __B::init(this); 
            
        }
        // Basic Initilization 
     void   __B::init( B __this) {
            
            __this->b = new __String("B");
            // This will initiliaze anything that needs to be 
            __A::init((A)__this);
            
        }
        
        
        // Basically the one static vtable 
        __B_VT __B::__vtable; 


        __Test7::__Test7() : __vptr(&__vtable) { 
                            
        }
        
        __Test7_VT __Test7::__vtable; 
        
        
        
    }
}