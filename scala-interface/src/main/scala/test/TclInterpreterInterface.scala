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
import tcl.integration.TclintLibrary.StreamWriteCallBack

/**
 * @author rleys
 *
 */
class TclInterpreterInterface extends ATCL {

  val bufferLimit = 1024 * 1024

  class internal_stream extends redirected_stream {

    var buffer: ByteBuffer = null

    val cb = new StreamWriteCallBack {

      override def apply(bytes: Pointer[java.lang.Byte], toWrite: Int): Int = {

        println(s"IN write callback")
        //-- Write
        buffer.put(bytes.getBytes(toWrite))

        toWrite

      }
    }

    def getName: String = new String(name().getBytes(), "UTF-8")

    /**
     * Returns the content as a string, and reset the buffer
     */
    def getContent: String = {
      var ct = new Array[Byte](buffer.position())
      buffer.flip()
      buffer.get(ct)
      buffer.reset()
      
      new String(ct,"UTF-8")

    }

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
      stream.buffer = bytesBuffer
      //stream.stream(Pointer.pointerToBytes(bytesBuffer))
      stream.streamWrite(Pointer.pointerTo(stream.cb))

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
  def open: Unit = {

    this.open(Pointer.pointerTo(callBack))

  }

  // Reset
  //----------------

  /**
   * Sets back the Standard streams and removes the other ones
   */
  def reset = {
		  
    // Only keep stdout and stderr
    streams = streams.collect {
      case s if (s.getName=="stdout" || s.getName == "stderr") => s
    }
    
  }

  // Eval
  //-----------------
  def eval(str: String): Int = {
    //  this.
    //return super.jeval(str)
    //super.eval(str);
    //println(s"Going to eval");
    super.evalString(org.bridj.Pointer.pointerToCString(str))

    //-- Flush Standard I/O
    super.evalString(org.bridj.Pointer.pointerToCString("flush stdout; flush stderr;"))

  }

}