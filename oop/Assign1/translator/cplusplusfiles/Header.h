#pragma once

#include <stdint.h>
#include <string>

namespace java {
namespace lang {
// Foward Declarations 

struct __A;

struct __A_VT;

typedef __rt::Ptr<__A> A;

struct __A { 

    // The data layout for java.lang.plainClassName
      __A_VT* __vptr;


     // The Constructor

          __A(); 
    // The instance methods of java.lang.plainClassName
    static A init_Construct( A); 
    static void printOther$A( A , A);

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 
    static void init(  __A*  );

    static __A_VT __vtable;

 };

struct __A_VT{
    Class __isa;
    void (*__delete)(__A*);
    int32_t (*hashCode)(A);
    bool (*equals)(A , Object);
    Class (*getClass)(A);
    String (*toString) (A);

    void (*printOther$A) (A , A);

    __A_VT()
    : __isa(__Object::__class()),
    __delete(&__rt::__delete<__A>),
      hashCode((int32_t(*)(A))&__Object::hashCode),
      equals((bool(*)(A , Object)) &__Object::equals), 
      getClass((Class(*)(A)) &__Object::getClass), 
      toString((String(*)(A)) &__Object::toString), 

      printOther$A(&__A::printOther$A) {
    }
};



struct __B;

struct __B_VT;

typedef __rt::Ptr<__B> B;

struct __B { 

    // The data layout for java.lang.plainClassName
      __B_VT* __vptr;

     B some;


     // The Constructor

          __B(); 
    // The instance methods of java.lang.plainClassName
    static B init_Construct( B); 
    static void printOther$A( B , A);
    static String toString( B);

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 
    static void init(  __B*  );

    static __B_VT __vtable;

 };

struct __B_VT{
    Class __isa;
    void (*__delete)(__B*);
    int32_t (*hashCode)(B);
    bool (*equals)(B , Object);
    Class (*getClass)(B);
    String (*toString) (B);

    void (*printOther$A) (B , A);

    __B_VT()
    : __isa(__Object::__class()),
    __delete(&__rt::__delete<__B>),
      hashCode((int32_t(*)(B))&__Object::hashCode),
      equals((bool(*)(B , Object)) &__Object::equals), 
      getClass((Class(*)(B)) &__Object::getClass), 
      toString(&__B::toString),

      printOther$A(&__B::printOther$A) {
    }
};



struct __Test16;

struct __Test16_VT;

typedef __rt::Ptr<__Test16> Test16;

struct __Test16 { 

    // The data layout for java.lang.plainClassName
      __Test16_VT* __vptr;


     // The Constructor

          __Test16(); 
    // The instance methods of java.lang.plainClassName
    static Test16 init_Construct( Test16); 

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 
    static void init(  __Test16*  );

    static __Test16_VT __vtable;

 };

struct __Test16_VT{
    Class __isa;
    void (*__delete)(__Test16*);
    int32_t (*hashCode)(Test16);
    bool (*equals)(Test16 , Object);
    Class (*getClass)(Test16);
    String (*toString) (Test16);


    __Test16_VT()
    : __isa(__Object::__class()),
    __delete(&__rt::__delete<__Test16>),
      hashCode((int32_t(*)(Test16))&__Object::hashCode),
      equals((bool(*)(Test16 , Object)) &__Object::equals), 
      getClass((Class(*)(Test16)) &__Object::getClass), 
      toString((String(*)(Test16)) &__Object::toString) { 
    }
};



}
}
