#pragma once

#include <stdint.h>
#include <string>

namespace java {
namespace lang {
// Foward Declarations 

struct __A;

struct __A_VT;

typedef __A* A;

struct __A { 

    // The data layout for java.lang.plainClassName
      __A_VT* __vptr;

     // The Constructor

      __A(); 

A
    // The instance methods of java.lang.plainClassName
    static String toString( A);

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 

    static __A_VT __vtable;

 };

struct __A_VT{
    Class __isa;
    int32_t (*hashCode)(A);
    bool (*equals)(A,Object);
    Class (*getClass)(A);
    String (*toString) (A);


    __A_VT()
    : __isa(__A::__class()),
      hashCode((int32_t(*)(A))&__Object::hashCode),
      equals((bool(*)(A,Object)) &__Object::equals), 
      getClass((Class(*)(A)) &__Object::getClass), 
      toString(&__A::toString),
      toString((String(*)(A))&__A::toString),

    }
};



struct __Test2;

struct __Test2_VT;

typedef __Test2* Test2;

struct __Test2 { 

    // The data layout for java.lang.plainClassName
      __Test2_VT* __vptr;

     // The Constructor

      __Test2(); 

Test2
    // The instance methods of java.lang.plainClassName

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 

    static __Test2_VT __vtable;

 };

struct __Test2_VT{
    Class __isa;
    int32_t (*hashCode)(Test2);
    bool (*equals)(Test2,Object);
    Class (*getClass)(Test2);
    String (*toString) (Test2);


    __Test2_VT()
    : __isa(__Test2::__class()),
      hashCode((int32_t(*)(Test2))&__Object::hashCode),
      equals((bool(*)(Test2,Object)) &__Object::equals), 
      getClass((Class(*)(Test2)) &__Object::getClass), 
      toString((String(*)(Test2)) &__Object::toString), 
    }
};



}
}
