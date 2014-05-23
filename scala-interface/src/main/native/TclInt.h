#include <tcl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>




/*void init(Tcl_Interp *);

int eval(Tcl_Interp *, const char * str);
int evalClean(const char * str);

char * retrieveStdout(int *);*/



// Stream redirection
//-----------------------------

// Write Callback
/// @return The number of writen bytes
typedef int (*StreamWriteCallBack)(const char* buf,int toWrite);

typedef struct redirected_stream {

	char  * name;
	int nameSize;
	StreamWriteCallBack streamWrite;
	long limit;
	long position;

	/// Reserved for C++ implementation
	Tcl_Channel tclChan;

} redirected_stream;

typedef redirected_stream* (*StreamCreateCallBack)();


// Normal C
//----------------

typedef struct interpreter {
	Tcl_Interp * interpreter;
	StreamCreateCallBack  createCallBack;
} interpreter;

interpreter * createInterpreter(StreamCreateCallBack  createCallBack);

redirected_stream * createStream(const char * name,interpreter *);

int evalString(interpreter * interpreter,const char * text);

void closeInterpreter(Tcl_Interp *);


// C++
//-----------------
class ATCL {



	// Fields
	//----------------

private:

	redirected_stream * stdout_stream;
	redirected_stream * stderr_stream;

public:

	Tcl_Interp * interpreter;

	/**
	 * All created streams, registered by openStream
	 */
	//list<redirected_stream*> streams;


	StreamCreateCallBack  createCallBack;



	// Interpreter Life
	//--------------

	/**
	 * Create an interpreter
	 */
	ATCL();
	virtual ~ATCL();



	/**
	 * Close
	 */
	void close();

protected:
	void open(StreamCreateCallBack cb);

public:

	// Stream Interface
	//----------------

	/**
	 * Open a new stream and register it with the interpreter
	 * Use the #createStream method to delegatethe actual character stream creation to Scala
	 */
	redirected_stream * openStream( const char * name);


	/**
	 *
	 * Eval
	 */
	int evalString( const char * text);




};

