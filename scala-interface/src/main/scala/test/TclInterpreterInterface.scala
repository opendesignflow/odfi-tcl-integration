/**
 *
 */
package test

import tcl.integration.redirected_stream
import org.bridj.Pointer
import java.nio.CharBuffer
import java.nio.ByteBuffer
import org.bridj.NativeObject
import tcl.integration.TclintLibrary.StreamCreateCallBack
import tcl.integration.ATCL

/**
 * @author rleys
 *
 */
class TclInterpreterInterface extends ATCL {

  val bufferLimit = 1024 * 1024

  class internal_stream extends redirected_stream {

    var buffer: ByteBuffer = null

  }

  // List of streams
  //----------------
  var streams = List[internal_stream]()


  
  // Create Stream
  //-----------------
  val callBack = new StreamCreateCallBack {
    override def apply: Pointer[redirected_stream] = {

      println(s"Creating Stream: ");
      
      // Create Stream
      //-----------------
      var stream = new internal_stream
      TclInterpreterInterface.this.streams = TclInterpreterInterface.this.streams :+ stream

      //stream.name(Pointer.pointerTo(name.getBytes("UTF-8")))

      // Create Buffer
      //------------------
      var bytesBuffer = ByteBuffer.allocateDirect(bufferLimit)
      stream.buffer =bytesBuffer
      stream.stream(Pointer.pointerToBytes(bytesBuffer))
      
      // Set with limits
      //---------------------
      stream.limit(bufferLimit)
      stream.position(0)

      return Pointer.pointerTo(stream.asInstanceOf[tcl.integration.redirected_stream])
    }
  }
  val callBackPointer = Pointer.pointerTo(callBack)
  val callBackPointerPointer = Pointer.allocatePointer(classOf[StreamCreateCallBack])
  callBackPointerPointer.set(callBackPointer)



  // Open
  //--------------------
  def open : Unit = {
    
    this.open(Pointer.pointerTo(callBack))
    
  }
  
  // Eval
  //-----------------
  def eval(str: String): Int = {
    //  this.
    //return super.jeval(str)
    //super.eval(str);
    //println(s"Going to eval");
    super.evalString(org.bridj.Pointer.pointerToCString(str))
  }


}