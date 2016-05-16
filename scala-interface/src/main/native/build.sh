#!/bin/bash

loc="$(dirname "$(readlink -f ${BASH_SOURCE[0]})")"

## Building MingW
################

if [[ -n $(which x86_64-w64-mingw32-g++) ]]
then
	echo "Building WIN64 Interface: $(pwd)" 

	## An Interpreter is required for this build
	##########
	baseTCLFolder=$loc/../../../target/native/tcl86-win64
	if [[ ! -e $baseTCLFolder/tcl8.6.5-src.tar.gz ]]
	then 
		mkdir -p $baseTCLFolder
		cd  $baseTCLFolder
		wget http://prdownloads.sourceforge.net/tcl/tcl8.6.5-src.tar.gz
		tar xvaf tcl8.6.5-src.tar.gz
		cd $loc
	fi 

	if [[ ! -e $baseTCLFolder/tcl8.6.5/win/tcl86.dll ]]
	then
		echo "Building interpreter for mingw target"
		cd $baseTCLFolder/tcl8.6.5/win
		./configure --prefix=$(pwd)/out --enable-64bit
		make clean 
		make
		cd $loc 
	fi

	## ENV 
	##########
	#MINGWLDFLAGS="-L$loc/../../../target/native/tcl86/tcl8.6.5/win/out/bin -L$loc/../../../target/native/tcl86/tcl8.6.5/win/out/lib"
	MINGWLDFLAGS="-L$loc/../../../target/native/tcl86-win64/tcl8.6.5/win/ -Ltarget/native/tcl86-win64/tcl8.6.5/win/out/lib"

	echo "LFDLAGS: $MINGWLDFLAGS"

	mkdir -p $loc/../../../target/generated-resources/lib/win64/
	x86_64-w64-mingw32-g++ -I$loc/../../../target/native/tcl86-win64/tcl8.6.5/generic -I./tcl86/ $MINGWLDFLAGS  $loc/TclInt.cpp $MINGWLDFLAGS -shared -static-libgcc -static-libstdc++  -static -ltcl86 -static -lstdc++   -Wl,-S,--subsystem,windows,--enable-runtime-pseudo-reloc,-no-undefined  -o $loc/../../../target/generated-resources/lib/win64/tclint.dll

fi

if [[ -n $(which i686-w64-mingw32-g++) ]]
then
	echo "Building WIN32 Interface: $(pwd)" 

	## An Interpreter is required for this build
	##########
	baseTCLFolder=$loc/../../../target/native/tcl86-win32
	if [[ ! -e $baseTCLFolder/tcl8.6.5-src.tar.gz ]]
	then 
		mkdir -p $baseTCLFolder
		cd  $baseTCLFolder
		wget http://prdownloads.sourceforge.net/tcl/tcl8.6.5-src.tar.gz
		tar xvaf tcl8.6.5-src.tar.gz
		cd $loc
	fi 

	if [[ ! -e $baseTCLFolder/tcl8.6.5/win/tcl86.dll ]]
	then
		echo "Building interpreter for mingw target"
		cd $baseTCLFolder/tcl8.6.5/win
		./configure --prefix=$(pwd)/out
		make clean 
		make
		cd $loc 
	fi

	## ENV 
	##########
	#MINGWLDFLAGS="-L$loc/../../../target/native/tcl86/tcl8.6.5/win/out/bin -L$loc/../../../target/native/tcl86/tcl8.6.5/win/out/lib"
	MINGWLDFLAGS="-L$loc/../../../target/native/tcl86-win32/tcl8.6.5/win/ -Ltarget/native/tcl86-win32/tcl8.6.5/win/out/lib"



	mkdir -p $loc/../../../target/generated-resources/lib/win32/
	i686-w64-mingw32-g++ -I$loc/../../../target/native/tcl86-win32/tcl8.6.5/generic -I./tcl86/ $MINGWLDFLAGS  $loc/TclInt.cpp -shared -static-libgcc -static-libstdc++ -static -ltcl86 -static -lstdc++   -Wl,-S,--subsystem,windows,--enable-runtime-pseudo-reloc,-no-undefined  -o $loc/../../../target/generated-resources/lib/win32/tclint.dll

fi

## Building local linux
#############
if [[ -n $(which x86_64-pc-linux-gnu-g++) ||-n $(which x86_64-linux-gnu-g++) ]]
then
	echo "Building Linux64 Interface" 
	mkdir -p $loc/../../../target/generated-resources/lib/linux_x64/
	x86_64-linux-gnu-g++ $loc/TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/linux_x64/libtclint.so

fi


exit 0

x86_64-w64-mingw32-gcc



#g++ $loc/TclInt.cpp -shared  -static-libgcc -static-libstdc++ -Wl,--subsystem,windows -fPIC  -lstdc++ -ltcl86 -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/win64/tclint.dll

## MINGW Valid
#g++ $loc/TclInt.cpp -shared -static-libgcc -static-libstdc++  -Wl,-S,--subsystem,windows,--enable-runtime-pseudo-reloc,-no-undefined -static -lstdc++  -static -ltcl86.dll -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/win64/tclint.dll
g++ $loc/TclInt.cpp -shared -static-libgcc -static-libstdc++ -static -lstdc++ -Wl,-S,--subsystem,windows,--enable-runtime-pseudo-reloc,-no-undefined -static  -ltcl86.dll -I/usr/include/tcl8.6/ -Itcl86/ -o $loc/../../../target/generated-resources/lib/win64/tclint.dll



#-Wl,--enable-runtime-pseudo-reloc,-no-undefined
#g++ $loc/TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -I/usr/include/tcl8.6/

#mingw32-g++  TclInt.cpp -shared -fPIC -lstdc++ -ltcl8.6 -I/usr/include/tcl8.6/ -I/usr/local/include/ -o test.dll
