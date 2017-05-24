#include <tcl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef _WIN32

  /* You should define ADD_EXPORTS *only* when building the DLL. */

    #define ADDAPI __declspec(dllexport)


  /* Define calling convention in one place, for convenience. */
  #define ADDCALL __cdecl

#else /* _WIN32 not defined. */

  /* Define with no value on non-Windows OSes. */
  #define ADDAPI
  #define ADDCALL

#endif



/*void init(Tcl_Interp *);

int eval(Tcl_Interp *, const char * str);
int evalClean(const char * str);

char * retrieveStdout(int *);*/




// Stream redirection
//-----------------------------

// Write Callback
/// @return The number of writen bytes
typedef int  (*StreamWriteCallBack)(const char* buf,int toWrite);

// Stream Close Callback
typedef void (*StreamCloseCallBack)(void);

typedef struct redirected_stream {

	char  * name;
	int nameSize;
  char  * id;
	StreamWriteCallBack streamWrite;
  StreamCloseCallBack streamClose;
	long limit;
	long position;

	/// Reserved for C++ implementation
	Tcl_Channel tclChan;

} redirected_stream;



// Stream Create Callback
typedef redirected_stream* (*StreamCreateCallBack)(const char * name,int nameSize, const char * id, int idSize);


// Normal C
//----------------

typedef struct interpreter {
	Tcl_Interp * interpreter;
	StreamCreateCallBack  createCallBack;
	redirected_stream * stdoutChannel;
} interpreter;

// Object Interface
//----------------------

class TclObject {

public:

    Tcl_Obj * _ptr;
    interpreter * _interpreter;

    TclObject();
    void ADDAPI init(interpreter *,Tcl_Obj *);

    //TclObject(interpreter *,Tcl_Obj *);
    virtual ~TclObject();

    // Utils
    //----------------

    /**
     * @return type name or empty string if not defined
     */
    const char * ADDAPI getTypeName();

    bool ADDAPI isNULL();

    // String
    //--------------
    const char * ADDAPI asString();
    bool ADDAPI isSimpleType();

    // List
    //-----------------

    bool ADDAPI isList();

    /**
     * @warning Check with #isList for validity!
     */
    //TclList * toList();




};

// List Interface for returns
//-------------------------
class TclList : public TclObject {

   public:

   //interpreter * _interpreter;

   //TclObject * _listObj;


   TclList();

   /**
    * The TclObject * is used as source and recreated to make sure the List has its own incremented count reference.
    * The used TclObject can then freely be deleted
    */
   //TclList(interpreter *,Tcl_Obj * o);

   /**
   Must: Decrement reference count to list
   */
   virtual ~TclList();

    /**
    Must: Increment reference count to list
    */
   /*void init(interpreter *  , Tcl_Obj * );

   void init(interpreter *,TclObject *);

   static TclList * fromTclObject(TclObject*o) {
	   return new TclList(o->_interpreter,o);
   }*/

  /* void init(TclObject * obj) {
	   TclObject::init(obj->_interpreter,obj->_ptr);
   }*/

   // Interface
   //------------------

   int ADDAPI getLength();

   TclObject * ADDAPI at(int index);


};



#ifdef __cplusplus
extern "C"
{
#endif


// Global things
//------------------------------
bool ADDAPI testLibIsValid();
void ADDAPI enableDebug();
void ADDAPI disableDebug();


// Functions interface
//--------------------------

interpreter * ADDAPI createInterpreter(StreamCreateCallBack  createCallBack);
void ADDAPI closeInterpreter(interpreter *);


redirected_stream * ADDAPI createStream(const char * name,interpreter *,Tcl_Interp *targetInterpreter);

//int evalString(interpreter * interpreter,const char * text,char ** stringResult);

/**
 * file : true then the second argument is a file path
 */
int ADDAPI eval(interpreter * ,const char * ,bool file, TclObject ** );



#ifdef __cplusplus
} // __cplusplus defined.
#endif
