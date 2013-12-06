#include <iostream>
#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
using namespace java::lang;

int main(void) { 

    A  a = __A::init_Construct(  new __A());
    B  other = __B::init_Construct(  new __B());
     other->some = __rt::java_cast<B>(a);
     a->__vptr->printOther$A( a , other  );
}
