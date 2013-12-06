#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
namespace java {
namespace lang {

A __A::init_Construct(A __this ) {

    return __this; 
 } 

 Class __A::__class() { 
    static Class k  = 
    new __Class(__rt::literal("java.lang.A") , __Object::__class());
    return k; 
 }

void  __A::printOther$A ( A  __this , A other  )  {
   __rt::checkNotNull(other);
    std::cout << other->__vptr->toString(other)->data << std::endl;
}


__A::__A() : __vptr(&__vtable) {
}

__A_VT __A:: __vtable;

B __B::init_Construct(B __this ) {

    return __this; 
 } 

 Class __B::__class() { 
   static Class k  = 
   new __Class(__rt::literal("java.lang.B") , __A::__class());
   return k; 
}

void  __B::printOther$A ( B  __this , A other  )  {
   __rt::checkNotNull(other);
    std::cout << other->__vptr->toString(other)->data << std::endl;
}

String  __B::toString(B __this ) { 
 return __this-> __vptr->toString(__this)->data;
}


__B::__B() : __vptr(&__vtable) {
}

__B_VT __B:: __vtable;

Test16 __Test16::init_Construct(Test16 __this ) {

    return __this; 
 } 

 Class __Test16::__class() { 
    static Class k  = 
    new __Class(__rt::literal("java.lang.Test16") , __Object::__class());
    return k; 
 }



__Test16::__Test16() : __vptr(&__vtable) {
}

__Test16_VT __Test16:: __vtable;

}
}
