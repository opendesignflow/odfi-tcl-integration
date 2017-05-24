#include <tcl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "nsf.h"

unsigned int MyThreadImplFunc2(ClientData clientData) {

	Tcl_Interp * interpreter = Tcl_CreateInterp();
	Tcl_EvalEx(interpreter, "puts \"Hello from thread\"; flush stdout;", -1,TCL_EVAL_DIRECT);

	Tcl_CmdInfo * info = (Tcl_CmdInfo*)(clientData);

	//Tcl_CreateObjCommand(interpreter, "::nsf::__#0", info->objProc, info->objClientData, info->deleteProc);

	Tcl_EvalEx(interpreter, "puts [::nsf::__#0 name get]", -1,TCL_EVAL_DIRECT);

	printf("Native command: %d\n",info->isNativeObjectProc);

	for (int i=0 ; i<10 ; i++) {
		fprintf(stderr,"InTH\n");
	}

	printf("Finishing Thread... \n");
	Tcl_DeleteInterp(interpreter);
	Tcl_FinalizeThread();
	TCL_THREAD_CREATE_RETURN;
}
/*

  Tcl_ThreadCreateProc  MyThreadImplFunc;
 Tcl_ThreadCreateProc  MyThreadImplFunc (ClientData clientData) {
    int i, limit = (int) clientData;
    for (i=0 ; i<limit ; i++) {

    }
    TCL_THREAD_CREATE_RETURN;
}*/


int main() {

	// Create Interpreter
	//-------------------
	Tcl_Interp * interpreter = Tcl_CreateInterp();

	// Load
	//---------------
	int res = Tcl_EvalEx(interpreter, "puts \"Hello\"; flush stdout;", -1,TCL_EVAL_DIRECT);

	// Create NX
	//--------------
	Tcl_EvalEx(interpreter, "source nsf/pkgIndex.tcl", -1,TCL_EVAL_DIRECT);
	Tcl_EvalEx(interpreter, "package require nx 2.0.0", -1,TCL_EVAL_DIRECT);

	// Source script
	//----------------
	Tcl_EvalEx(interpreter, "source createclasses.tcl", -1,TCL_EVAL_DIRECT);

	// Try to get the value of instance: ::nsf::__#0
	//------------------------
	Tcl_CmdInfo info;
	res = Tcl_GetCommandInfo(interpreter, "::nsf::__#0", &info);

	printf("Got info %d \n",res);
	Tcl_EvalEx(interpreter, "puts [::nsf::__#0 name get]", -1,TCL_EVAL_DIRECT);

	// Create Thread
	//----------------
	int limit = 1000000000;
	ClientData limitData = (void*)((Tcl_CmdInfo*) &info);
	Tcl_ThreadId id;    /* holds identity of thread created */
	int result;

	if (Tcl_CreateThread(&id, &MyThreadImplFunc2, limitData,
	        TCL_THREAD_STACK_DEFAULT,
	        TCL_THREAD_JOINABLE) != TCL_OK) {
	    /* Thread did not create correctly */
		printf("Thread failed \n");
	    return -1;
	}
	/* Do something else for a while here */
	if (Tcl_JoinThread(id, &result) != TCL_OK) {
	    /* Thread did not finish properly */
		printf("Thread finished failed \n");
	    return -1;
	}

	// Close
	//----------
	printf("Finishing... \n");
	Tcl_DeleteInterp(interpreter);

	return 0;
}
