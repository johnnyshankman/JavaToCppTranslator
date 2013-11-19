#pragma once

#include <stdint.h>
#include <string>

namespace java {
namespace lang {
// Foward Declarations 

struct __C;

struct __C_VT;

typedef __C* C;

struct __C { 

    // The data layout for java.lang.plainClassName
      __C_VT* __vptr;

     // The Constructor

      __C(); 

    // The instance methods of java.lang.plainClassName
    static void c( C);

    static void cc( C);

    static void ccc( C,String,String,String);

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 

    static __C_VT __vtable;

 };

struct __C_VT{
    Class __isa;
    int32_t (*hashCode)(C);
    bool (*equals)(C,Object);
    Class (*getClass)(C);
    String (*toString) (C);

    void (*c) (C);

    void (*cc) (C);

    void (*ccc) (C,String,String,String);

    __C_VT()
    : __isa(__C::__class()),
      hashCode((int32_t(*)(C))&__Object::hashCode),
      equals((bool(*)(C,Object)) &__Object::equals), 
      getClass((Class(*)(C)) &__Object::getClass), 
      toString((String(*)(C)) &__Object::toString), 
      c((void(*)(C))&__C::c),

      cc((void(*)(C))&__C::cc),


      ccc(&__C::ccc){
    }
};



struct __B;

struct __B_VT;

typedef __B* B;

struct __B { 

    // The data layout for java.lang.plainClassName
      __B_VT* __vptr;

     // The Constructor

      __B(); 

    // The instance methods of java.lang.plainClassName
    static void c( B);

    static void cc( B);

    static void ccc( B,String,String,String);
    static void b( B);


    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 

    static __B_VT __vtable;

 };

struct __B_VT{
    Class __isa;
    int32_t (*hashCode)(B);
    bool (*equals)(B,Object);
    Class (*getClass)(B);
    String (*toString) (B);

    void (*c) (B);

    void (*cc) (B);

    void (*ccc) (B,String,String,String);
    void (*b) (B);


    __B_VT()
    : __isa(__B::__class()),
      hashCode((int32_t(*)(B))&__Object::hashCode),
      equals((bool(*)(B,Object)) &__Object::equals), 
      getClass((Class(*)(B)) &__Object::getClass), 
      toString((String(*)(B)) &__Object::toString), 
      c((void(*)(B))&__C::c),

      cc((void(*)(B))&__C::cc),

      ccc((void(*)(B,String,String,String))&__C::ccc),

      b(&__B::b){
    }
};



struct __Test;

struct __Test_VT;

typedef __Test* Test;

struct __Test { 

    // The data layout for java.lang.plainClassName
      __Test_VT* __vptr;

     // The Constructor

      __Test(); 

    // The instance methods of java.lang.plainClassName
    static void c( Test);

    static void cc( Test);

    static void ccc( Test,String,String,String);
    static void b( Test);

    static void g( Test,String);

    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 

    static __Test_VT __vtable;

 };

struct __Test_VT{
    Class __isa;
    int32_t (*hashCode)(Test);
    bool (*equals)(Test,Object);
    Class (*getClass)(Test);
    String (*toString) (Test);

    void (*c) (Test);

    void (*cc) (Test);

    void (*ccc) (Test,String,String,String);
    void (*b) (Test);

    void (*g) (Test,String);

    __Test_VT()
    : __isa(__Test::__class()),
      hashCode((int32_t(*)(Test))&__Object::hashCode),
      equals((bool(*)(Test,Object)) &__Object::equals), 
      getClass((Class(*)(Test)) &__Object::getClass), 
      toString((String(*)(Test)) &__Object::toString), 
      c((void(*)(Test))&__C::c),

      cc((void(*)(Test))&__C::cc),

      ccc((void(*)(Test,String,String,String))&__C::ccc),
      b((void(*)(Test))&__B::b),


      g(&__Test::g){
    }
};



struct __HelloWorld;

struct __HelloWorld_VT;

typedef __HelloWorld* HelloWorld;

struct __HelloWorld { 

    // The data layout for java.lang.plainClassName
      __HelloWorld_VT* __vptr;

     // The Constructor

      __HelloWorld(); 

    // The instance methods of java.lang.plainClassName
    static void c( HelloWorld);

    static void cc( HelloWorld);

    static void ccc( HelloWorld,String,String,String);
    static void b( HelloWorld);

    static void g( HelloWorld,String);
    static bool goel( HelloWorld);

    static int goel2( HelloWorld);


    // The Function returning the class Object representing java.lang.plainClassName 
    static Class __class(); 

    static __HelloWorld_VT __vtable;

 };

struct __HelloWorld_VT{
    Class __isa;
    int32_t (*hashCode)(HelloWorld);
    bool (*equals)(HelloWorld,Object);
    Class (*getClass)(HelloWorld);
    String (*toString) (HelloWorld);

    void (*c) (HelloWorld);

    void (*cc) (HelloWorld);

    void (*ccc) (HelloWorld,String,String,String);
    void (*b) (HelloWorld);

    void (*g) (HelloWorld,String);
    bool (*goel) (HelloWorld);

    int (*goel2) (HelloWorld);


    __HelloWorld_VT()
    : __isa(__HelloWorld::__class()),
      hashCode((int32_t(*)(HelloWorld))&__Object::hashCode),
      equals((bool(*)(HelloWorld,Object)) &__Object::equals), 
      getClass((Class(*)(HelloWorld)) &__Object::getClass), 
      toString((String(*)(HelloWorld)) &__Object::toString), 
      c((void(*)(HelloWorld))&__C::c),

      cc((void(*)(HelloWorld))&__C::cc),

      ccc((void(*)(HelloWorld,String,String,String))&__C::ccc),
      b((void(*)(HelloWorld))&__B::b),

      g((void(*)(HelloWorld,String))&__Test::g),
      goel((bool(*)(HelloWorld))&__HelloWorld::goel),


      goel2(&__HelloWorld::goel2){
    }
};



}
}
