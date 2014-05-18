#include <tcl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


Tcl_Interp * createInterpreter();

void init(Tcl_Interp *);

int eval(Tcl_Interp *, const char * str);
int evalClean(const char * str);

char * retrieveStdout(int *);

// Stream redirection
//-----------------------------


typedef struct redirected_stream {

	char  name[100];
	char * stream;
	long limit;
	long position;

	/// Reserved for C++ implementation
	Tcl_Channel tclChan;

} redirected_stream;

typedef redirected_stream* (*StreamCreateCallBack)();

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

