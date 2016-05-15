#!/bin/bash

loc="$(dirname "$(readlink -f ${BASH_SOURCE[0]})")"

mkdir -p $loc/../../../target/generated-resources/lib/win64/

#g++ $loc/TclInt.cpp -shared  -static-libgcc -static-libstdc++ -Wl,--subsystem,windows -fPIC  -lstdc++ -ltcl86 -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/win64/tclint.dll

## MINGW Valid
#g++ $loc/TclInt.cpp -shared -static-libgcc -static-libstdc++  -Wl,-S,--subsystem,windows,--enable-runtime-pseudo-reloc,-no-undefined -static -lstdc++  -static -ltcl86.dll -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/win64/tclint.dll
g++ $loc/TclInt.cpp -shared -static-libgcc -static-libstdc++ -static -lstdc++ -Wl,-S,--subsystem,windows,--enable-runtime-pseudo-reloc,-no-undefined -static  -ltcl86.dll -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/win64/tclint.dll



#-Wl,--enable-runtime-pseudo-reloc,-no-undefined
#g++ $loc/TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -I/usr/include/tcl8.6/

#mingw32-g++  TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -I/usr/include/tcl8.6/ -I/usr/local/include/ -o test.dll
