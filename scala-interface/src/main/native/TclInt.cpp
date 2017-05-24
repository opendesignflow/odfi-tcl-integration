#include "TclInt.hpp"

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

////////////////////////////////////
/// Global stuff
////////////////////////////////////////
bool __debug__ = false;

void enableDebug() {
	__debug__ = true;
}
void disableDebug() {
	__debug__ = false;
}

bool  testLibIsValid() {
	return true;
}

////////////////////////
// TCL Stream Interfacing to structures
//   - The TCL function use the client data to get a reference to the underlying stream character
////////////////////////////////

int outputWriteProc(ClientData instanceData, const char *buf, int toWrite,
		int *errorCodePtr) {

	//-- Get stream
	redirected_stream * stream = (redirected_stream*) instanceData;

	if (__debug__) {
		fprintf(stdout, "[TCLWRITE] Writing %d to stream: %s, \n", toWrite,
				stream->name);
	}

	//-- Copy
	/*memcpy( ((void*)(stream->stream)) + (stream->position), (void*) buf,
	 sizeof(char) * toWrite);

	 //-- Update position
	 stream->position += toWrite;*/

	int written = stream->streamWrite(buf, toWrite);

	if (__debug__) {
		fprintf(stdout, "[TCLWRITE] write went fine %d\n", written);
		fflush (stdout);
	}

	return written;

	/*//fprintf("inside tcl write\n");

	 //fwrite(buf,sizeof(char),toWrite,stdout);

	 // Mmecopy
	 memcpy(((void*)stdoutBuffer)+stdoutBufferSize,(void*)buf,sizeof(char)*toWrite);

	 // Increase size
	 stdoutBufferSize+=toWrite;*/

	fflush (stdout);
	return toWrite;

}

int closeOutputProc(ClientData instanceData, Tcl_Interp *interp) {

	// GEt Stream structure
	redirected_stream * stream = (redirected_stream *) instanceData;

	// Call close
	stream->streamClose();

	// Unregister Channel
	Tcl_UnregisterChannel(interp,stream->tclChan);

	return 0;
}

void watchProc(ClientData instanceData, int mask) {
	if (__debug__) {
		printf("Watch proc called\n");
		fflush (stdout);
	}
}

Tcl_ChannelType streamRedirectChannelType = { "stream", TCL_CHANNEL_VERSION_2,
		closeOutputProc, NULL, outputWriteProc, NULL, // Tcl_DriverSeekProc *seekProc;
		NULL, //        Tcl_DriverSetOptionProc *setOptionProc;
		NULL, //       Tcl_DriverGetOptionProc *getOptionProc;
		watchProc, //       Tcl_DriverWatchProc *watchProc;
		NULL, //       Tcl_DriverGetHandleProc *getHandleProc;
		NULL, //        Tcl_DriverClose2Proc *close2Proc;
		NULL, //        Tcl_DriverBlockModeProc *blockModeProc;
		NULL, //        Tcl_DriverFlushProc *flushProc;
		NULL, //        Tcl_DriverHandlerProc *handlerProc;
		NULL, //        Tcl_DriverWideSeekProc *wideSeekProc;
		NULL, //        Tcl_DriverThreadActionProc *threadActionProc;
		NULL //        Tcl_DriverTruncateProc *truncateProc;
		};

int redirectOpen(ClientData clientData, Tcl_Interp *interpreter, int argc,
		const char *argv[]) {

	/*fprintf(stdout,"[TCLOPEN] Opening a stream: %s\n",argv[1]);

	 //-- Open Stream (Also created in TCL and registered there)
	 redirected_stream * stream = ((ATCL*)clientData)->openStream(argv[1]);
	 fprintf(stdout,"[TCLOPEN]--> Created stream: %s\n",stream->name);

	 //-- Return Name
	 Tcl_SetResult(interpreter,stream->name,TCL_STATIC);

	 fflush(stdout);*/
	return TCL_OK;

}

/**
 * Forbid Exiting :-)
 */
int redirectExit(ClientData clientData, Tcl_Interp *intp, int argc,
		const char *argv[]) {

	return TCL_OK;

}

/**
 * Redirect Written outputs to internal streams, and proceed read only to normal TCL
 */
int redirectOpenC(ClientData clientData, Tcl_Interp *intp, int argc,
		const char *argv[]) {

	fprintf(stdout,"[TCLOPEN2] Opening a stream: %s for interpreter %x, args=%d\n",argv[1],intp,argc);
	fflush(stdout);

	//-- Check the rights, if reading don't redirect
	if (argc > 2 && (strstr(argv[2], "w") || strstr(argv[2], "w+"))) {

		
		//fprintf(stdout,"[TCLOPEN]--> Created stream: %s\n",stream->name);
		interpreter* is = (interpreter*) clientData;


		//-- Open Stream (Also created in TCL and registered there)
		redirected_stream * stream = createStream(argv[1], is,intp);

		//-- Return Name
		Tcl_SetResult(intp, (char*) Tcl_GetChannelName(stream->tclChan), TCL_STATIC);

		
		return TCL_OK;

	} else {

		// Open for reading
		//---------------

		/* fprintf(stdout,"[TCLOPEN]--> Opening stream for reading %s , mode=%s\n",argv[1],argv[2]);
		 fflush(stdout);

		 fprintf(stdout,"[TCLOPEN]--> Do it\n");
		 fflush(stdout);*/

		Tcl_Channel chan = Tcl_OpenFileChannel(intp, argv[1], "r", 0644);
		if (chan == NULL) {

			/*fprintf(stdout,"[TCLOPEN]--> Chan is  null\n");
			 fflush(stdout);*/

			//Tcl_SetResult(intp,"",TCL_STATIC);
			return TCL_ERROR;
		} else {

			/*fprintf(stdout,"[TCLOPEN]--> Chan is not null\n");
			 fflush(stdout);*/

			//-- Register
			Tcl_RegisterChannel(intp, chan);

			//-- Return result
			Tcl_SetResult(intp, (char*) Tcl_GetChannelName(chan), TCL_STATIC);

			return TCL_OK;
		}

	}

}


int slaveInterpreterCount = 0;

int redirectInterpC(ClientData clientData, Tcl_Interp *intp, int argc,
		const char *argv[]) {

	if (argc>1 && strcmp("create",argv[1])==0) {
		fprintf(stdout,"[TCLOPEN2] Create interpreter %x, args=%d\n",intp,argc);
		fflush(stdout);

		char slaveId [256] ; 
		sprintf(slaveId,"odfislaveintp%d",slaveInterpreterCount++);

		//-- Get originating interpreter extended structure
		interpreter* is = (interpreter*) clientData;

		//-- Create Slave Interpreter
		Tcl_Interp * slave = Tcl_CreateSlave(intp, slaveId, 0);
		Tcl_Init(slave);

		//-- Redirect open function
		Tcl_Command openCmd = Tcl_CreateCommand(slave, "::open",
				redirectOpenC, is, NULL);
		//Tcl_Command closeCmd = Tcl_CreateCommand(slave, "::close",
		//	redirectCloseC, is, NULL);

		//-- Set Result
		Tcl_SetResult(intp, slaveId, TCL_STATIC);

		return TCL_OK;
	} else {

		//-- Get command
		/*Tcl_CmdInfo interpCommand ;
		Tcl_GetCommandInfo(intp, "::orignalinterp", &interpCommand);

		//-- Call Command
		interpCommand.proc(interpCommand.clientData,intp,argc,argv);*/
		Tcl_Obj **objv = (Tcl_Obj **)malloc(argc*sizeof(Tcl_Obj *));
		objv[0] = Tcl_NewStringObj("::orignalinterp",strlen("::orignalinterp") );
		for (int i = 1 ; i< argc; i++) {
			//fprintf(stdout,"[TCLOPEN2] Converting argument %d %s\n",i,argv[i]);
			//fflush(stdout);
			
			objv[i] = Tcl_NewStringObj(argv[i],strlen(argv[i]) );
		}
		

		return Tcl_EvalObjv(intp,argc,objv,TCL_EVAL_DIRECT);

	}

	
	
	return TCL_OK;

}

int redirectedStreamCount = 0;

redirected_stream * createStream(const char * name, interpreter * interpreter,Tcl_Interp *targetInterpreter) {


	// Create Stream
	//-------------

	//fprintf(stdout,"[TCLOPEN2] Create Stream: %s, %x", name,interpreter);
	//fflush(stdout);
	char streamId [256] ; 
	sprintf(streamId,"rstream%d",redirectedStreamCount++);

	
	redirected_stream * stream = (interpreter->createCallBack)(name,strlen(name),streamId,strlen(streamId));

	stream->name = (char*) calloc(strlen(name), sizeof(char));
	stream->nameSize = strlen(name);
	strcpy(stream->name, name);

	stream->id = (char*) calloc(strlen(streamId), sizeof(char));
	strcpy(stream->id, streamId);

	if (__debug__) {
		printf("[createStream] Creating stream: %s in is=??\n", stream->name);
		fflush (stdout);
	}

	// Create TCL stream
	//----------------------
	stream->tclChan = Tcl_CreateChannel(&streamRedirectChannelType, streamId,
			stream, TCL_WRITABLE);

	

	// Register
	//----------------
	//int moderes;
	//Tcl_Channel c = Tcl_GetChannel(interpreter->interpreter, stream->name, &moderes);
	//if (c != NULL) {
		//Tcl_UnregisterChannel(interpreter->interpreter, stream->tclChan);
	//}
	if (strcmp(streamId,"stdout")!=0) {
		Tcl_RegisterChannel(targetInterpreter, stream->tclChan);
	}
	

	return stream;

}

///////////////////////////////////////////////////
// C
///////////////////////////////////////////////////

interpreter * createInterpreter(StreamCreateCallBack createCallBack) {

	//-- Create TCl Stdout
	//---------------------------
	interpreter * is = (interpreter*) calloc(1, sizeof(interpreter));

	Tcl_Interp * interpreter = Tcl_CreateInterp();
	is->interpreter = interpreter;
	is->createCallBack = createCallBack;

	//-- Create stdout and stderr streams
	//--------------------
	//return is;

	//-- Unregister stdout and stderr
	int moderes = 0;
	Tcl_Channel c = NULL;

	//Tcl_GetChannel(interpreter, "stderr", &moderes);
	//Tcl_GetChannel(interpreter, "stdout", &moderes);

	Tcl_SetStdChannel(NULL, TCL_STDOUT); // Important, otherwise often fails
	Tcl_SetStdChannel(NULL, TCL_STDERR);


	/*c = Tcl_GetChannel(interpreter, "stderr", &moderes);
	if (c != NULL) {
		Tcl_UnregisterChannel(interpreter, c);
	}

	c = Tcl_GetChannel(interpreter, "stdout", &moderes);
	if (c != NULL) {
		Tcl_UnregisterChannel(interpreter, c);
	}*/

	//printf("New version\n");
	//fflush (stdout);

	redirected_stream * interpreter_stdout = createStream("stdout", is,interpreter);
	is->stdoutChannel = interpreter_stdout;

	Tcl_SetStdChannel(is->stdoutChannel->tclChan, TCL_STDOUT);
	Tcl_RegisterChannel(NULL, is->stdoutChannel->tclChan); // Workaround fix


	// Don4t replace stderr
	//redirected_stream * interpreter_stderr = createStream("stderr", is);
	//Tcl_SetStdChannel(interpreter_stdout->tclChan, TCL_STDOUT);

	Tcl_Init(interpreter);

	// Redirect open function
	//-------------------------
	Tcl_Command openCmd = Tcl_CreateCommand(is->interpreter, "::open",
			redirectOpenC, is, NULL);
	//Tcl_Command closeCmd = Tcl_CreateCommand(is->interpreter, "::close",
	//		redirectCloseC, is, NULL);

	// Redirect Interpreter creation
	//--------------------------
	//-- Get command
	Tcl_CmdInfo interpCommand ;
	Tcl_GetCommandInfo(is->interpreter, "interp", &interpCommand);
	Tcl_Command  interpCmd = Tcl_CreateObjCommand(is->interpreter, "::orignalinterp",
			interpCommand.objProc, interpCommand.objClientData, interpCommand.deleteProc);
	Tcl_Command  newinterpCmd = Tcl_CreateCommand(is->interpreter, "::interp",
			redirectInterpC, is, NULL);
	/*Tcl_Command openCmd = Tcl_CreateCommand(is->interpreter, "::open",
			redirectOpenC, is, NULL);
	Tcl_Command exitCmd = Tcl_CreateCommand(is->interpreter, "::exit",
			redirectExit, is, NULL);*/

	

	return is;

}

void closeInterpreter(interpreter * interpreter) {

	// Close Channel
	//-------------------

	Tcl_Channel __stdout = Tcl_GetChannel(interpreter->interpreter, "stdout",
			NULL);
	Tcl_Channel __stderr = Tcl_GetChannel(interpreter->interpreter, "stderr",
			NULL);

	if (__stdout != NULL) {
		//Tcl_UnregisterChannel(interpreter->interpreter, __stdout);
		//Tcl_Close(interpreter->interpreter,__stdout);
	}

	if (__stderr != NULL) {
		//Tcl_UnregisterChannel(interpreter->interpreter, __stderr);
		//Tcl_Close(interpreter->interpreter,__stderr);
	}

	/*Tcl_UnregisterChannel(interpreter->interpreter, stdout);
	 Tcl_Close(interpreter->interpreter,stdout);

	 Tcl_UnregisterChannel(interpreter->interpreter, stderr);
	 Tcl_Close(interpreter->interpreter,stderr);*/

	Tcl_DeleteInterp(interpreter->interpreter);

}



/*int evalString(interpreter * interpreter,const char * text,char ** stringResult) {

 if (__debug__) {
 printf("Evaluating\n");
 fflush(stdout);
 }


 // Convert to UTF8
 //----------------
 Tcl_DString dstring;
 Tcl_DStringInit(&dstring);
 char * utfString = Tcl_ExternalToUtfDString(Tcl_GetEncoding(interpreter->interpreter,"UTF-8"), text, strlen(text), &dstring);

 int res = Tcl_EvalEx(interpreter->interpreter, utfString, -1, TCL_EVAL_DIRECT);

 if (res == TCL_ERROR) {

 const char * errRes = Tcl_GetStringResult(interpreter->interpreter);
 *stringResult = (char *)  malloc(strlen(errRes)*sizeof(char));
 strcpy(*stringResult,errRes);

 printf("An error occured: %s\n", errRes);

 Tcl_WriteChars(Tcl_GetChannel(interpreter->interpreter,"stdout",NULL),errRes,strlen(errRes));

 } else {
 //Tcl_DoOneEvent(TCL_ALL_EVENTS);

 // Get Object
 //------------------
 Tcl_Obj * objResult =  Tcl_GetObjResult(interpreter->interpreter);
 if (objResult==NULL) {
 printf("No result object\n");
 } else if (objResult->typePtr!=NULL && strcmp(objResult->typePtr->name,"list")==0) {

 // printf("Result object is of type: %s\n",objResult->typePtr->name);
 Tcl_Obj * resPtr;
 if(Tcl_ListObjIndex(interpreter->interpreter, objResult, 0, &resPtr)==TCL_OK) {

 printf("First element in list is: %s \n",resPtr->typePtr->name);

 /*int size = -1;
 Tcl_ListObjLength(interp, listPtr, intPtr)
 printf("Res size: %d\n",);*/

/*    }

 }

 //Tcl_Eval(interpret, "flush stdout");
 const char * resStr = Tcl_GetStringResult(interpreter->interpreter);

 //printf("Result of eval2: %d -> %s \n", res,resStr);
 //fflush(stdout);



 // Populate result
 //-----------------
 *stringResult = (char *) malloc(strlen(resStr)*sizeof(char));
 strcpy(*stringResult,resStr);
 //*stringResult = Tcl_GetStringResult(interpreter->interpreter);




 }

 // Free Result
 //---------------
 Tcl_FreeResult(interpreter->interpreter);

 // Flush local stdout and Channel Stdout if necessary
 //---------------------
 int moderes = 0 ;
 Tcl_Channel interpreter_stdout = Tcl_GetChannel(interpreter->interpreter,"stdout",&moderes);
 Tcl_Channel interpreter_stderr = Tcl_GetChannel(interpreter->interpreter,"stderr",&moderes);
 //printf("Stdout buffered: %d\n",Tcl_ChannelBuffered(interpreter_stdout));
 Tcl_Flush(interpreter_stdout);
 Tcl_Flush(interpreter_stderr);
 if (Tcl_ChannelBuffered(interpreter_stdout)>0) {
 Tcl_Flush(interpreter_stdout);
 }
 if (Tcl_ChannelBuffered(interpreter_stderr)>0) {
 Tcl_Flush(interpreter_stderr);
 }


 fflush(stdout);
 fflush(stderr);

 // Free converted string
 //--------------
 Tcl_DStringFree(&dstring);

 return res;


 }
 */

int eval(interpreter * interpreter, const char * text, bool file,
		TclObject ** result) {

	// File or String
	//--------------------
	int res = 0;
	if (file) {

		if (__debug__) {
			printf("Evaluating File\n");
			fflush (stdout);
		}
		res = Tcl_EvalFile(interpreter->interpreter, text);
	} else {

		if (__debug__) {
			printf("Evaluating String\n");
			fflush (stdout);
		}

		//-- Make sure Std Channels are correct
		//---------------
		int moderes = 0;
		//Tcl_GetChannel(interpreter->interpreter, "stderr", &moderes);
		//Tcl_GetChannel(interpreter->interpreter, "stdout", &moderes);

		Tcl_SetStdChannel(interpreter->stdoutChannel->tclChan, TCL_STDOUT);
		Tcl_RegisterChannel(NULL, interpreter->stdoutChannel->tclChan); // Workaround fix

		//-- Convert to UTF8
		//----------------
		Tcl_DString dstring;
		Tcl_DStringInit(&dstring);
		char * utfString = Tcl_ExternalToUtfDString(
				Tcl_GetEncoding(interpreter->interpreter, "UTF-8"), text,
				strlen(text), &dstring);

		res = Tcl_EvalEx(interpreter->interpreter, utfString, -1,
		TCL_EVAL_DIRECT);

		// Free converted string
		//--------------
		Tcl_DStringFree(&dstring);
		//free(utfString);
	}

	if (res == TCL_ERROR) {

		Tcl_Obj *options = Tcl_GetReturnOptions(interpreter->interpreter, res);
		Tcl_Obj *key = Tcl_NewStringObj("-errorinfo", -1);
		Tcl_Obj *stackTrace;
		Tcl_IncrRefCount(key);
		Tcl_DictObjGet(NULL, options, key, &stackTrace);
		Tcl_DecrRefCount(key);
		/* Do something with stackTrace */
		Tcl_DecrRefCount(options);

		*result = new TclObject();
		(*result)->init(interpreter, stackTrace);

		/*
		 Tcl_Obj * objResult = Tcl_GetObjResult(interpreter->interpreter);
		 if (objResult != NULL) {

		 //printf("An error occured with an object result\n");

		 //-- Create a single object
		 *result = new TclObject();
		 (*result)->init(interpreter, objResult);

		 } else {
		 const char * errRes = Tcl_GetStringResult(interpreter->interpreter);
		 printf("An error occured with no object result: %s\n", errRes);
		 *result = new TclObject();
		 (*result)->init(interpreter, NULL);
		 }*/

	} else {
		//Tcl_DoOneEvent(TCL_ALL_EVENTS);

		// Get Object
		//------------------
		Tcl_Obj * objResult = Tcl_GetObjResult(interpreter->interpreter);
		if (objResult == NULL) {
			//printf("No result object\n");

			//-- Create an Empty object

			*result = new TclObject();
			(*result)->init(interpreter, NULL);
		}
		//-- List Result
		else if (objResult->typePtr != NULL
				&& strcmp(objResult->typePtr->name, "list") == 0) {

			//-- Create A list object to set to list result, and store the one result in there
			TclList * l = new TclList();
			(*result) = (TclObject*) l;
			l->init(interpreter, objResult);

			//printf("List result of length %d\n",  l->getLength());

		}
		// Single Result
		//----------------
		else {

			//-- Create a single object
			TclList * l = new TclList();
			(*result) = (TclObject*) l;
			(*result)->init(interpreter, objResult);
		}

	}

	// Free Result
	//---------------
	Tcl_FreeResult(interpreter->interpreter);

	// Flush local stdout and Channel Stdout if necessary
	//---------------------
	int moderes = 0;
	Tcl_Channel interpreter_stdout = Tcl_GetChannel(interpreter->interpreter,
			"stdout", &moderes);
	Tcl_Channel interpreter_stderr = Tcl_GetChannel(interpreter->interpreter,
			"stderr", &moderes);
	//printf("Stdout buffered: %d\n",Tcl_ChannelBuffered(interpreter_stdout));

	Tcl_EvalEx(interpreter->interpreter, "flush stdout", -1,
	TCL_EVAL_DIRECT);


	if (interpreter_stdout != NULL) {


		Tcl_Flush(interpreter_stdout);
		if (Tcl_ChannelBuffered(interpreter_stdout) > 0) {
			Tcl_Flush(interpreter_stdout);
		}
	}

	if (interpreter_stderr != NULL) {
		Tcl_Flush(interpreter_stderr);
		if (Tcl_ChannelBuffered(interpreter_stderr) > 0) {
			Tcl_Flush(interpreter_stderr);
		}
	}

	fflush (stdout);
	fflush (stderr);

	return res;

}

// Object Interface
//--------------------------

TclObject::TclObject() :
		_interpreter(NULL), _ptr(NULL) {

}

void TclObject::init(interpreter * i, Tcl_Obj* o) {

	this->_interpreter = i;
	this->_ptr = o;

	Tcl_IncrRefCount(this->_ptr);
}

TclObject::~TclObject() {

	Tcl_DecrRefCount(this->_ptr);
	//delete this->_ptr;
}

const char * TclObject::getTypeName() {

	if (this->_ptr != NULL && this->_ptr->typePtr != NULL) {
		return this->_ptr->typePtr->name;
	} else {
		return "";
	}

}

const char * TclObject::asString() {

	return Tcl_GetStringFromObj(this->_ptr, NULL);

}

bool TclObject::isSimpleType() {

	if (this->_ptr->typePtr == NULL) {
		return true;
	} else {
		return false;
	}

}

bool TclObject::isNULL() {

	if (this->_ptr == NULL) {
		return true;
	} else {
		return false;
	}
}

bool TclObject::isList() {

	if (!this->isSimpleType()
			&& strcmp(this->_ptr->typePtr->name, "list") == 0) {
		return true;
	}

	return false;
}

/*TclList * TclObject::toList() {
 TclList * lst = new TclList();
 lst->init(this->_interpreter,this->_ptr);
 return lst;
 }*/

// List Interface
//----------------------
/*TclList::TclList() {




 }*/
TclList::TclList() :
		TclObject() {

	/*this->_interpreter = interpreter;

	 this->_listObj = new TclObject(this->_interpreter,obj->_ptr);*/

}

TclList::~TclList() {

// Tcl_DecrRefCount(this->_listObj->_ptr);
	//delete this->_listObj;
}

/*void TclList::init(interpreter * interpreter , Tcl_Obj * listPtr) {

 this->_interpreter = interpreter;

 this->_listObj = new TclObject(this->_interpreter,listPtr);


 //Tcl_IncrRefCount(this->_listObj->_ptr);
 }

 void TclList::init(interpreter *i,TclObject *o) {

 this->_interpreter = i;

 this->_listObj =new TclObject(this->_interpreter,o->_ptr);
 }*/

/*TclList * TclList::fromTclObject(interpreter * i, TclObject*o) {

 return new TclList(i,o);

 }*/

int TclList::getLength() {

	/*fprintf(stderr,"getLength on listObject %x\n",this->_ptr);
	 fflush(stderr);*/
	if (this->isNULL()) {
		return 0;
	} else {
		int size = -1;
		Tcl_ListObjLength(this->_interpreter->interpreter, this->_ptr, &size);

		return size;
	}

}
TclObject * TclList::at(int index) {

	Tcl_Obj * resPtr;
	if (Tcl_ListObjIndex(this->_interpreter->interpreter, this->_ptr, index,
			&resPtr) == TCL_OK) {
		TclObject * res = new TclObject();
		res->init(this->_interpreter, resPtr);
		return res;
	} else {
		fprintf(stderr, "List at function failed\n");
		fflush (stderr);
		return NULL;
	}

}
