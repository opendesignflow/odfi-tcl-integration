#include "TclInt.h"

/*
 // Common Saved Stuff
 //------------------
 FILE * commonSTDOUT = NULL;

 // I/O Redirect channel definition
 //----------------
 long stdoutBufferSize = 0;
 char * stdoutBuffer = NULL;
 int redirectOutputProc (ClientData instanceData,
 const char *buf,
 int toWrite,
 int *errorCodePtr) {

 //fprintf("inside tcl write\n");

 //fwrite(buf,sizeof(char),toWrite,stdout);

 // Mmecopy
 memcpy(((void*)stdoutBuffer)+stdoutBufferSize,(void*)buf,sizeof(char)*toWrite);

 // Increase size
 stdoutBufferSize+=toWrite;

 return  toWrite;

 }

 int closeOutputProc(
 ClientData instanceData,
 Tcl_Interp *interp) {
 return 0;
 }


 Tcl_ChannelType stdoutRedirect = {
 "file",
 TCL_CHANNEL_VERSION_2,
 closeOutputProc,
 NULL,
 redirectOutputProc,
 NULL, // Tcl_DriverSeekProc *seekProc;
 NULL, //        Tcl_DriverSetOptionProc *setOptionProc;
 NULL, //       Tcl_DriverGetOptionProc *getOptionProc;
 NULL, //       Tcl_DriverWatchProc *watchProc;
 NULL, //       Tcl_DriverGetHandleProc *getHandleProc;
 NULL, //        Tcl_DriverClose2Proc *close2Proc;
 NULL, //        Tcl_DriverBlockModeProc *blockModeProc;
 NULL, //        Tcl_DriverFlushProc *flushProc;
 NULL, //        Tcl_DriverHandlerProc *handlerProc;
 NULL, //        Tcl_DriverWideSeekProc *wideSeekProc;
 NULL, //        Tcl_DriverThreadActionProc *threadActionProc;
 NULL //        Tcl_DriverTruncateProc *truncateProc;
 };


 Tcl_Channel  tclChan;
 Tcl_Channel  tclErrChan;


 // Open Command
 //-----------------------
 int redirectOpen (
 ClientData clientData,
 Tcl_Interp *interp,
 int argc,
 const char *argv[]) {

 fprintf(commonSTDOUT,"Opening a stream: %s\n",argv[1]);

 return 1;
 }




 int evalClean( const char * str) {

 // Prepare Interpreter
 //-----------------
 commonSTDOUT = stdout;

 //-- Create TCl Stdout
 Tcl_Interp * interpreter = Tcl_CreateInterp();
 Tcl_Init(interpreter);


 //-- Redirect stdout to output buffer
 //-----------
 stdoutBuffer = calloc(1024 * 1024 * 10, sizeof(char));

 Tcl_Channel tclChan = Tcl_CreateChannel(&stdoutRedirect, "stdoutl", 0, TCL_WRITABLE);
 Tcl_Channel tclErrChan = Tcl_CreateChannel(&stdoutRedirect, "stderrl", 0, TCL_WRITABLE);
 Tcl_RegisterChannel(interpreter, tclChan);
 Tcl_RegisterChannel(interpreter, tclErrChan);

 //-- Register
 Tcl_SetStdChannel(tclChan, TCL_STDOUT);
 Tcl_SetStdChannel(tclErrChan, TCL_STDERR);


 // Redirect Channel Open
 //---------------------------------

 Tcl_Command openCmd = Tcl_CreateCommand(interpreter, "::open", redirectOpen, 0, NULL);


 // Evaluate
 //-------------------------------

 //printf("Going to eval: %s\n", str);

 int res = Tcl_EvalEx(interpreter, str, -1, TCL_EVAL_DIRECT);

 if (res == TCL_ERROR) {

 const char * errRes = Tcl_GetStringResult(interpreter);
 //printf("An error occured: %s\n", errRes);

 Tcl_WriteChars(tclErrChan,errRes,strlen(errRes));

 } else {
 //Tcl_DoOneEvent(TCL_ALL_EVENTS);

 //Tcl_Eval(interpret, "flush stdout");

 //printf("Result of eval: %d\n", res);
 }

 // Clean Buffer
 //-----------------
 Tcl_Flush(tclChan);
 Tcl_Flush(tclErrChan);

 fflush(stdout);
 fflush(stderr);
 //setbuf(stdout, NULL);

 // Clean Interpreter
 //-------------------
 Tcl_DeleteInterp(interpreter);

 return TCL_OK;

 }

 Tcl_Interp * createInterpreter() {


 //-- Create TCl Stdout
 Tcl_Interp * interpreter = Tcl_CreateInterp();
 tclChan = Tcl_CreateChannel(&stdoutRedirect, "stdoutg", 0, TCL_WRITABLE);
 tclErrChan = Tcl_CreateChannel(&stdoutRedirect, "stderrg", 0, TCL_WRITABLE);
 //*tclChan = Tcl_CreateChannel(&stdoutRedirect, "stdout", 0, TCL_WRITABLE);
 Tcl_RegisterChannel(interpreter, tclChan);
 Tcl_RegisterChannel(interpreter, tclErrChan);

 //-- Register
 Tcl_SetStdChannel(tclChan, TCL_STDOUT);
 Tcl_SetStdChannel(tclErrChan, TCL_STDERR);


 return interpreter;

 }

 void init(Tcl_Interp *interpret) {
 Tcl_Init(interpret);
 }

 int eval(Tcl_Interp * interpret, const char * str) {

 //-- Redirect stdout to output buffer
 //-----------
 stdoutBuffer = calloc(1024 * 1024 * 10, sizeof(char));
 //setbuf(stdout, stdoutBuffer);

 Tcl_SetStdChannel(tclChan, TCL_STDOUT);
 Tcl_SetStdChannel(tclErrChan, TCL_STDERR);

 //printf("Going to eval: %s\n", str);

 int res = Tcl_EvalEx(interpret, str, -1, TCL_EVAL_DIRECT);

 if (res == TCL_ERROR) {

 const char * errRes = Tcl_GetStringResult(interpret);
 //printf("An error occured: %s\n", errRes);

 Tcl_WriteChars(tclErrChan,errRes,strlen(errRes));

 } else {
 //Tcl_DoOneEvent(TCL_ALL_EVENTS);

 //Tcl_Eval(interpret, "flush stdout");

 //printf("Result of eval: %d\n", res);
 }

 // Clean Buffer
 //-----------------
 Tcl_Flush(tclChan);
 Tcl_Flush(tclErrChan);
 fflush(stdout);
 fflush(stderr);
 //setbuf(stdout, NULL);

 return TCL_OK;

 }

 char * retrieveStdout(int * len) {

 char * b = stdoutBuffer;
 *len = stdoutBufferSize;

 // Reinit
 stdoutBuffer = NULL;
 stdoutBufferSize = 0;

 return b;
 }

 */

////////////////////////
// TCL Stream Interfacing to structures
//   - The TCL function use the client data to get a reference to the underlying stream character
////////////////////////////////






int outputWriteProc(ClientData instanceData, const char *buf, int toWrite,
		int *errorCodePtr) {



	//-- Get stream
	redirected_stream * stream = (redirected_stream*) instanceData;

	fprintf(stdout,"[TCLWRITE] Writing %d to stream: %s, address=%x\n",toWrite,stream->name,stream->stream);

	//-- Copy
	memcpy( ((void*)(stream->stream)) + (stream->position), (void*) buf,
			sizeof(char) * toWrite);

	//-- Update position
	stream->position += toWrite;

	/*//fprintf("inside tcl write\n");

	 //fwrite(buf,sizeof(char),toWrite,stdout);

	 // Mmecopy
	 memcpy(((void*)stdoutBuffer)+stdoutBufferSize,(void*)buf,sizeof(char)*toWrite);

	 // Increase size
	 stdoutBufferSize+=toWrite;*/

	fflush(stdout);
	return toWrite;

}

int closeOutputProc(ClientData instanceData, Tcl_Interp *interp) {
	return 0;
}

Tcl_ChannelType streamRedirectChannelType = { "file", TCL_CHANNEL_VERSION_2,
		closeOutputProc, NULL, outputWriteProc, NULL, // Tcl_DriverSeekProc *seekProc;
		NULL, //        Tcl_DriverSetOptionProc *setOptionProc;
		NULL, //       Tcl_DriverGetOptionProc *getOptionProc;
		NULL, //       Tcl_DriverWatchProc *watchProc;
		NULL, //       Tcl_DriverGetHandleProc *getHandleProc;
		NULL, //        Tcl_DriverClose2Proc *close2Proc;
		NULL, //        Tcl_DriverBlockModeProc *blockModeProc;
		NULL, //        Tcl_DriverFlushProc *flushProc;
		NULL, //        Tcl_DriverHandlerProc *handlerProc;
		NULL, //        Tcl_DriverWideSeekProc *wideSeekProc;
		NULL, //        Tcl_DriverThreadActionProc *threadActionProc;
		NULL //        Tcl_DriverTruncateProc *truncateProc;
};

int redirectOpen (
 ClientData clientData,
 Tcl_Interp *interpreter,
 int argc,
 const char *argv[]) {

 fprintf(stdout,"[TCLOPEN] Opening a stream: %s\n",argv[1]);

 //-- Open Stream (Also created in TCL and registered there)
 redirected_stream * stream = ((ATCL*)clientData)->openStream(argv[1]);
 fprintf(stdout,"[TCLOPEN]--> Created stream: %s\n",stream->name);

 //-- Return Name
 Tcl_SetResult(interpreter,stream->name,TCL_STATIC);

 fflush(stdout);
 return TCL_OK;

}

/////////////////////////
// Correct interfacing
////////////////////////////

ATCL::ATCL() {


	//this->createCallBack = NULL;

	// Register Stdout/Stderr
	//-------------------
	//Tcl_SetStdChannel(tclChan, TCL_STDOUT);
	//Tcl_SetStdChannel(tclErrChan, TCL_STDERR);
	//this->interpreter

}

ATCL::~ATCL() {

}

void ATCL::open(StreamCreateCallBack cb) {

	printf("Open in C\n");

	//-- Setup callback
	//----------
	this->createCallBack = cb;

	//-- Create TCl Interperter
	this->interpreter = Tcl_CreateInterp();

	//Tcl_Close(this->interpreter,Tcl_GetChannel(this->interpreter,"stdout",&moderes));

	//-- Create stdout and stderr streams
	//--------------------

	//-- Unregister stdout and stderr
	int moderes = 0 ;
	Tcl_UnregisterChannel(this->interpreter,Tcl_GetChannel(this->interpreter,"stdout",&moderes));
	Tcl_UnregisterChannel(this->interpreter,Tcl_GetChannel(this->interpreter,"stderr",&moderes));

	//-- Now create them again to our redirection targets
	stdout_stream = openStream("stdout");
	stderr_stream = openStream("stderr");

	// Redirect open function
	//-------------------------
	Tcl_Command openCmd = Tcl_CreateCommand(interpreter, "::open", redirectOpen, this, NULL);

	Tcl_Init(this->interpreter);
	fflush(stdout);
}
void ATCL::close() {

	// Flush stdout and stderr streams
	//----------------
	evalString("flush stdout");
	evalString("flush stderr");

	Tcl_Flush(stdout_stream->tclChan);
	Tcl_Flush(stderr_stream->tclChan);

	//for (std::list<redirected_streams*>::iterator it=this->streams.begin(); it != this->streams.end(); ++it) {
	//	Tcl_Flush((*it)->tclChan);
	//}

	//Tcl_Flush(tclErrChan);

	fflush(stdout);
	fflush(stderr);
	//setbuf(stdout, NULL);

	// Clean Interpreter
	//-------------------
	Tcl_DeleteInterp(interpreter);

}

int ATCL::evalString(const char * text) {

	printf("Evaluating\n");

	int res = Tcl_EvalEx(this->interpreter, text, -1, TCL_EVAL_DIRECT);

	if (res == TCL_ERROR) {

		const char * errRes = Tcl_GetStringResult(interpreter);
		printf("An error occured: %s\n", errRes);

		//Tcl_WriteChars(tclErrChan,errRes,strlen(errRes));

	} else {
		//Tcl_DoOneEvent(TCL_ALL_EVENTS);

		//Tcl_Eval(interpret, "flush stdout");

		//printf("Result of eval: %d\n", res);
	}

	fflush(stdout);
	fflush(stderr);

	return res;
}

redirected_stream * ATCL::openStream(const char * name) {

	// Create Stream
	//-------------
	printf("Creating stream: %s\n",name);
	redirected_stream * stream = (createCallBack)();
	//stream->name = (char*)calloc(strlen(name),sizeof(char));
	strcpy(stream->name,name);


	// Create TCL stream
	//----------------------
	stream->tclChan = Tcl_CreateChannel(&streamRedirectChannelType, name, stream, TCL_WRITABLE);

	// Register
	//----------------
	Tcl_RegisterChannel(interpreter, stream->tclChan);

	fflush(stdout);

	return stream;
}


