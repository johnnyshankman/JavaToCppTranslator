#include <iostream>

#include "java_lang.h"
#include <sstream> 
 
using namespace java::lang; 
using namespace std; 

int main (void) { 

  
    // Remove for Test1 ( g++ Main2.cpp Method_Bodies1.cc java_lang.cc -o Test2) 
            cout.flush();
         
          Object a = new __Object();  // Equivalent to A a = new A()  
     
        
          cout << a->__vptr->toString(a)->data << endl;   // Equivalent to System.out.println(a.toString()) 
        
     
    // Remove for Test2 ( g++ Main2.cpp Method_Bodies2.cc java_lang.cc -o Test2)
         
         /*
         A a = new __A();
         Object o = (Object)a; 
         cout << o->__vptr->toString(o)->data << endl; 
         **/
    
    // Remove for Test3 (g++ Main2.cpp Method_Bodies3.cc java_lang.cc -o Linker)
            
            /*
            std::ostringstream sout;
            sout << "Avalue"; 
            A a = new __A(new __String(sout.str())); 
            cout << a->__vptr->getFld(a)->data << endl;
            **/
    
    
    
    // Remove for Test4 (g++ Main2.cpp Method_Bodies4.cc java_lang.cc -o Linker)
        /*
        A a = new __A(new __String("A"));
    
        cout << a->__vptr->getFld(a)->data <<endl;
        **/
    
    
    
     //Remove for Test 5 compile with ( g++ Main2.cpp Method_Bodies5.cc java_lang.cc -o Linker)
        /*
        B b = new __B();
    
    
        A a1 = new __A();
    
    
    
        A a2 = (A)b; 
    
        cout << a1->__vptr->toString(a1)->data << endl;
    
        cout << a2->__vptr->toString(a2)->data << endl; 
    
         */
    
    
    
    // Remove for Test6 
    /*
    A a = new __A();
    
    a->__vptr->almostSetFld(a, __rt::literal("B")); 
                            
    cout << a->__vptr->getFld(a)->data <<endl; 
    
    
    a->__vptr->setFld(a, __rt::literal("B")); 
                            
                            
    cout << a->__vptr->getFld(a)->data <<endl;                         
    **/                
    
    
    // Remove for Test 7 
    
    /*
    B b = new __B();
    
    
    cout << b->a->data <<endl; 
    
    cout << b->b->data <<endl; 
    **/
    
    
    
    return 0;
                            
     
}

