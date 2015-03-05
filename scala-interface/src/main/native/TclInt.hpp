#include <tcl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>





/*void init(Tcl_Interp *);

int eval(Tcl_Interp *, const char * str);
int evalClean(const char * str);

char * retrieveStdout(int *);*/




// Global things
//------------------------------

void enableDebug();
void disableDebug();



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

// Object Interface
//----------------------

class TclObject {

public:

    Tcl_Obj * _ptr;
    interpreter * _interpreter;

    TclObject(interpreter *,Tcl_Obj * o);
    virtual ~TclObject();

    // Utils
    //----------------


    bool isNULL();

    // String
    //--------------
    const char * asString();
    bool isSimpleType();

    // List
    //-----------------

    bool isList();

    /**
     * @warning Check with #isList for validity!
     */
    //TclList * toList();




};

// List Interface for returns
//-------------------------
class TclList {

   public:

   interpreter * _interpreter;

   TclObject * _listObj;


   TclList();

   /**
    * The TclObject * is used as source and recreated to make sure the List has its own incremented count reference.
    * The used TclObject can then freely be deleted
    */
   TclList(interpreter *,TclObject *);

   /**
   Must: Decrement reference count to list
   */
   virtual ~TclList();

    /**
    Must: Increment reference count to list
    */
   void init(interpreter *  , Tcl_Obj * );

   void init(interpreter *,TclObject *);

   static TclList * fromTclObject(TclObject*o) {
	   return new TclList(o->_interpreter,o);
   }

   // Interface
   //------------------

   int getLength();

   TclObject * at(int index);


};


// Functions interface
//--------------------------

interpreter * createInterpreter(StreamCreateCallBack  createCallBack);
void closeInterpreter(interpreter *);


redirected_stream * createStream(const char * name,interpreter *);

int evalString(interpreter * interpreter,const char * text,char ** stringResult);


int evalStringList(interpreter * interpreter,const char * text,TclList * listResult);



