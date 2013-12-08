/*
 * Object-Oriented Programming
 * Copyright (C) 2013 Thomas Wies
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

  Ptr<A, object_policy> q = p;

  std::cout << *(q->x) << std::endl;

  return 0;
}