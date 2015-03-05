#!/bin/bash

loc="$(dirname "$(readlink -f ${BASH_SOURCE[0]})")"
g++ $loc/TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -L$loc/../../../target/generated-resources/lib/linux_x64/ -ltclint -o test_openclose test_openclose.cpp 


./test_openclose