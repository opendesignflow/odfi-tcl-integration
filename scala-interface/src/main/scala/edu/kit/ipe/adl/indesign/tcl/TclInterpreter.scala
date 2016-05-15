/**
 *
 */
package edu.kit.ipe.adl.indesign.tcl

import java.io.File
import java.nio.ByteBuffer

import org.bridj.Pointer
import edu.kit.ipe.adl.indesign.tcl.integration.redirected_stream
import edu.kit.ipe.adl.indesign.tcl.integration.TclObject
import edu.kit.ipe.adl.indesign.tcl.integration.TclintLibrary
import edu.kit.ipe.adl.indesign.tcl.integration.TclintLibrary.StreamWriteCallBack
import edu.kit.ipe.adl.indesign.tcl.integration.TclintLibrary.StreamCreateCallBack
import edu.kit.ipe.adl.indesign.tcl.integration.interpreter
import edu.kit.ipe.adl.indesign.tcl.integration.TclList
import org.bridj.BridJ

/**
 * @author rleys
 *
 */
class TclInterpreter {


  BridJ.addNativeLibraryDependencies("tclint", "tcl86")
  
  val bufferLimit = 1024 * 1024

  //------------------------------------
  // Packages
  //------------------------------------
  def addPackageSource(location: File) = {

    new File(location.getAbsoluteFile, "pkgIndex.tcl") match {
      case index if (index.exists()) => 
        eval(s"source $index") 
      case _ => throw new RuntimeException(s"Cannot add package source $location, no pkgIndex.tcl file located")
    }

  }

  //------------------------------------
  // Stream Redirection
  //------------------------------------
  class internal_stream extends redirected_stream {

    var buffer: ByteBuffer = null

    val cb = new StreamWriteCallBack {

      override def apply(bytes: Pointer[java.lang.Byte], toWrite: Int): Int = {

        getName match {
          case "stdout" =>
            //println(s"IN write callback for ${getName}, write $toWrite, remaining: ${buffer.remaining()}")
            print(new String(bytes.getBytes(toWrite), "UTF-8"))
            toWrite
          case _ =>
           // println(s"IN write callback for ${getName}, write $toWrite, remaining: ${buffer.remaining()}")

            //-- Write
            buffer.put(bytes.getBytes(toWrite))

            toWrite
        }

      }
    }

    def getName: String = new String(name().getBytes(nameSize()), "UTF-8")

    /**
     * Returns the content as a string, and reset the buffer
     */
    def getContent: String = {
      var ct = new Array[Byte](buffer.position())
      buffer.flip()
      buffer.get(ct)
      buffer.rewind()

      new String(ct, "UTF-8")

    }

  }

  // List of streams
  //----------------
  var streams = List[internal_stream]()

  // Create Stream
  //-----------------
  val callBack = new StreamCreateCallBack {
    override def apply: Pointer[redirected_stream] = {

      //println(s"Creating Stream: ");

      // Create Stream
      //-----------------
      var stream = new internal_stream
      TclInterpreter.this.streams = TclInterpreter.this.streams :+ stream

      //stream.name(Pointer.pointerTo(name.getBytes("UTF-8")))

      // Create Buffer
      //------------------
      var bytesBuffer = ByteBuffer.allocateDirect(bufferLimit)
      stream.buffer = bytesBuffer
      //stream.stream(Pointer.pointerToBytes(bytesBuffer))
      stream.streamWrite(Pointer.pointerTo(stream.cb))

      // Set with limits
      //---------------------
      stream.limit(bufferLimit)
      stream.position(0)

      Pointer.pointerTo(stream.asInstanceOf[redirected_stream])
    }
  }
  val callBackPointer = Pointer.pointerTo(callBack)
  val callBackPointerPointer = Pointer.allocatePointer(classOf[StreamCreateCallBack])
  callBackPointerPointer.set(callBackPointer)

  var interpreter: Pointer[interpreter] = null

  // Open
  //--------------------
  def open: Unit = { 

    //-- Create interpreter
   interpreter = TclintLibrary.createInterpreter(callBackPointer) 

    //-- Check validity
    interpreter.get()

    sys.addShutdownHook {
      try {
        //this.close
      } finally {

      }
    }
    //this.open(Pointer.pointerTo(callBack))

  }

  // Reset
  //----------------

  /**
   * Sets back the Standard streams and removes the other ones
   */
  def reset = {

    // Only keep stdout and stderr
    streams = streams.collect {
      case s if (s.getName == "stdout" || s.getName == "stderr") => Some(s)
      case s => None
    }.filter (_ == Some).map(_.get)

  }

  def close = {

   
    
   TclintLibrary.closeInterpreter(interpreter)  

  }
  
  //----------------
  // Package Loading
  //-------------------
  
  def loadPackageIndexFile(indexFile:File) {
    evalString(s"""set dir ${indexFile.getParentFile.getAbsolutePath.replace('\\', '/')}""")
    eval(indexFile)
  }
  
  
  def forgetPackages(matchS:String) = {
      this.evalString(s"""
        foreach p [package names] {
          if {[string match "$matchS" $$p]} {
            catch {package forget $$p}
          }
          
        } 
        """)
  }

  // Eval
  //-----------------
  /*def evalAsString(str: String): TclValue = {
        //  this.
        //return super.jeval(str)
        //super.eval(str);
        //println(s"Going to eval");

        //-- Prepare return pointer
        val resultString = Pointer.allocatePointer(classOf[java.lang.Byte])

        //-- Eval
        TclintLibrary.evalString(interpreter, org.bridj.Pointer.pointerToCString(str), resultString) match {
            case 0 =>

                /* var chars = new Array[Char](0)
         var next = resultString.get()
         while ( next.get().toChar!='\0') {
           chars = chars :+ next.get.toChar
           next = next.next()
         }*/

                //TclintLibrary.evalString(interpreter,org.bridj.Pointer.pointerToCString("flush stdout; flush stderr;"),resultString)

                return new StringValue(this, resultString.get.getCString)
            case 1 =>

                /*var chars = new Array[Char](0)
         var next = resultString.get().next()
         while ( next.get().toChar!='\0') {
           chars = chars :+ next.get.toChar
           next = next.next()
         }*/

                throw new RuntimeException(s"An error occured while running evaluation: ${resultString.get.getCString}")

            case res =>
                throw new RuntimeException(s"Return code $res unknown after eval String")
            //new TclResult("")
        }

        //-- Flush Standard I/O
        //TclintLibrary.evalString(interpreter,org.bridj.Pointer.pointerToCString("flush stdout; flush stderr;"))

    }*/

  def eval(str: String,file:Boolean = false): TclValue = {
    //  this.
    //return super.jeval(str)
    //super.eval(str);
    //println(s"Going to eval");

    //-- Prepare return pointer
    val result = Pointer.allocatePointer(classOf[TclObject])

    //-- Eval 
    
    TclintLibrary.eval(interpreter, org.bridj.Pointer.pointerToCString(str),file, result) match {

      case 0 if(result.get.get.isNULL()) => 
        new StringValue(this,"")
      // If the list has only one element, just return as object value  if(resultList.getLength==1)
      case 0 if (!result.get.get.isList()) =>

        //var first = resultList.at(0).get
        //println(s"Result is simple ${first.isSimpleType} with str: "+first.asString().getCString)
        /*first.isSimpleType match {
          case true => 
            new StringValue(this,first.asString().getCString)
          case false => */
        new TclObjectValue(this, result.get.get)
      //}

      //throw new RuntimeException(s"Dummy stop ")
      //return new TclObjectValue(resultList.at(0).get,this)

      // Return normal list
      case 0 if (result.get.get.isList()) =>

        ///println(s"Returning normal list of length: " + result.get.as(classOf[TclList]).get.getLength)
        //println(s"")
        //var l = new TclList
        //l.init(interpreter,result.get)

        //l.init(result.get) 

        // println(s"Returning Copy " +l.getLength)
        //l.init(this.interpreter,result.get.get._ptr)

        var res = new ExtendedTclList(this, result.get.as(classOf[TclList]).get)

        //res.init(this.interpreter, resultList._listObj())
        //res._listObj( resultList._listObj())

        // println(s"Resulting EList is: " + res.list.getLength)
        res

      case 1 =>

        // throw new RuntimeException(s"An error occured while running evaluation: "+resultList.at(0).get.asString().getCString)
        //throw new TclEvaluationError(s"An error occured while running evaluation: "+result.get.get.asString().getCString)
        throw new TclEvaluationError(result.get.get)

      case res =>
        throw new RuntimeException(s"Return code $res unknown after eval String")
      //new TclResult("")
    }

    //-- Flush Standard I/O
    //TclintLibrary.evalString(interpreter,org.bridj.Pointer.pointerToCString("flush stdout; flush stderr;"))

  }

  def eval(f: File): TclValue = {

    //this.eval(Source.fromFile(f, "UTF-8").mkString)
    //eval(s"source $f")
    eval(f.getAbsolutePath,true)

  }

  def evalString(str:String) = {
    this.eval(str.stripMargin)
  }
}

class TclEvaluationError(val errorObj: TclObject) extends RuntimeException(errorObj.asString().getCString)