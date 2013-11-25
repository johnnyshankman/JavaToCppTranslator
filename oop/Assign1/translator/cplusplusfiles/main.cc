/*
 * Object-Oriented Programming
 * Copyright (C) 2012 Robert Grimm
 * Modifications copyright (C) 2013 Thomas Wies
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */

#include <iostream>

#include "java_lang.h"
#include "Header.h"
using namespace java::lang;

int main(void) {
  // Let's get started.
  std::cout << "--------------------------------------------------------------"
            << "----------------"
            << std::endl;

  std::cout.flush();

  // Object o = new Object();
  Object o = new __Object();

    A b = new __A();   
    
    std::cout << b->__vptr->toString(b)->data <<std::endl; 
    
  std::cout << "o.toString() : "
            << o->__vptr->toString(o) // o.toString()
            << std::endl;

  // Class oClass = o.getClass();
  Class oClass = o->__vptr->getClass(o);

  std::cout << "oClass.getName()  : "
            << oClass->__vptr->getName(oClass) // oClass.getName()
            << std::endl
            << "oClass.toString() : "
            << oClass->__vptr->toString(oClass) // oClass.toString()
            << std::endl;

  // int[] a = new int[5];
  __rt::Ptr<__rt::Array<int32_t> > a = new __rt::Array<int32_t>(5);

  // a[2]
  __rt::checkNotNull(a);
  std::cout << "a[2]  : " << (*a)[2] << std::endl;

  // a[2] = 5;
  __rt::checkNotNull(a);
  (*a)[2] = 5;

  // a[2]
  __rt::checkNotNull(a);
  std::cout << "a[2]  : " << a->__data[2] << std::endl;

  // String[] ss = new String[5];
  __rt::Ptr<__rt::Array<String> > ss = new __rt::Array<String>(5);

  // String s = "Hello";
  String s = __rt::literal("Hello");

  // ss[2] = "Hello";
  __rt::checkNotNull(ss);
  __rt::checkStore(ss, s);
  (*ss)[3] = s;

  std::cout << "ss[3] : " << (*ss)[3] << std::endl;


  // Object o2 = s;
  Object o2 = s;

  // s = (String) o2;
  s = __rt::java_cast<String>(o2);

  // Done.
  std::cout << "--------------------------------------------------------------"
            << "----------------"
            << std::endl;
  return 0;
}
