package test

import tcl.integration.TclintLibrary

object Test extends App {

  
  //----- Stream Test
  var interface = new TclInterpreterInterface
  interface.open
 interface.eval("""
 puts "Hello Word"
 set fd [open test.out w]
 puts "Hello Word 2"
     
 puts "Our Stream: $fd"
 puts $fd "Hello inside stream"
 flush $fd
 close $fd
 flush stdout
  
  
  """)
  
  //interface.eval("""puts Hello""");
  
  
  interface.close
  
  //-- Show streams
  println(s"--> Streams: "+interface.streams.size)
  interface.streams.foreach {
    s => 
      println(s"--> Available stream: "+new String(s.name().getBytes(),"UTF-8"))
      println(s"--> Size: "+ s.position())
      
      s.position() match {
        case 0 => 
        case p => 
          var ct = new Array[Byte](p.toInt)
          s.buffer.get(ct)
          println(s"--->Content:"+new String(ct));
          //println(s"--> Content: "+s.buffer.get(ct))
      }
  }
  
 // TclintLibrary.evalClean(org.bridj.Pointer.pointerToCString("""open test.out rw"""))
  System.out.flush()
  System.err.flush()
  sys.exit
  //-----------------------------------------------------------------------//
  
  
  var interpreterPointer = TclintLibrary.createInterpreter()
    interpreterPointer.get()
  TclintLibrary.init(interpreterPointer)
 
  
 // TclintLibrary.eval(interpreterPointer,org.bridj.Pointer.pointerToCString("""puts "Hello from TCL version: [info tclversion""""))
  TclintLibrary.evalClean(org.bridj.Pointer.pointerToCString("""package require odfi::closures"""))
  
  
  var len = org.bridj.Pointer.allocate[Integer](classOf[Integer])
  var outBytes = TclintLibrary.retrieveStdout(len)
  
  var resBytes = new Array[Byte](len.get())
  outBytes.getByteBuffer(len.get().toLong).get(resBytes)
  
  println(s"Available bytes: "+new String(resBytes))
  
  /*var str = outBytes.toArray().takeWhile {
     b => b.toChar match {
       case '\0' => false
       case _ => true
     } 
  }.mkString
  println(s"Available bytes: "+str)*/
  
  System.out.flush()
  System.err.flush()
  sys.exit
 
  
  //Console.readLine
}