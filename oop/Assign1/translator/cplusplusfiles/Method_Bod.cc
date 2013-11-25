#include "java_lang.h" 
#include "Header.h" 
#include <sstream> 
namespace java {
namespace lang {
__A::__A() : __vptr(&__vtable) {}
__A__VT __A:: __vtable;

String__A::toString(A__this) { 
   std::ostringstream sout;
   sout <<"B";
   return new __String(sout.str());
}
__Test1::__Test1() : __vptr(&__vtable) {}
__Test1__VT __Test1:: __vtable;

}
}
