#!/bin/bash

loc="$(dirname "$(readlink -f ${BASH_SOURCE[0]})")"
g++ $loc/TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -I/usr/include/tcl8.6/ -o $loc/../../../target/generated-resources/lib/linux_x64/libtclint.so
