#include <iostream>

#include "ptr.h"

using namespace __rt;

struct A {
  Ptr<int, object_policy> x;
  
  A(Ptr<int, object_policy> _x) : x(_x) {  }

 ~A() { }
};

int main() {
  

  int* x = new int(5);

  Ptr<A, object_policy> p = new A(x);


  //Ptr<A, object_policy> q = p;

  //std::cout << *(q->x) << std::endl;

  return 0;
}
