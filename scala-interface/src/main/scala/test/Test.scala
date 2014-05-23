package test

import tcl.integration.TclintLibrary

object Test extends App {
 
  /*println(s"Allocation test")
  
  for (i <- 0 until 20) {
    println(s"opening $i")
     var interface = new TclInterpreterInterface2
     interface.open
     interface=null
     sys.runtime.gc()
  }
  
  sys.exit*/
  
  
  //----- Stream Test
  var interface = new TclInterpreterInterface2
  
 
  
  println("Hello")
  interface.open
  println("Hello")
  
  
  
 /*interface.eval("""
     package require nx
     package require odfi::dev::hw::package
 puts "Hello Word"
 set fd [open test.out w]
 puts "Hello Word 2"
     
 puts "Our Stream: $fd"
 puts $fd "Hello inside stream"
 flush $fd
 close $fd
 flush stdout
  
  
  """)*/
  
  
  
  interface.eval ("""
      
package require odfi::dev::hw::package
package require odfi::dev::hw::rtl


odfi::dev::hw::package::part example {


   ## Pins
   pin {A @A1} {
        attr::output
   }

   pin {B @A2} {
        attr::output
   }

   pin {C @B1} {
        attr::input
   }
   pin {D @B2} {
        attr::input
   }


}

odfi::dev::hw::package::SVGOutput svgOut  ::example


svgOut defineParameters {

    .*A.*%2Ccolor  blue

    .*B%2Ccolor    red
    .*C%2Ccolor   firebrick
    .*D.*%2Ccolor   darkred

}

svgOut produceToFile part-to-svg.svg

  catch {
    wm forget .
    exit 0
}     
  
 """)
  
  
  //interface.eval("""puts Hello""");
  
  
  //interface.close
  
  //-- Show streams
  println(s"--> Streams: "+interface.streams.size)
  interface.streams.foreach {
    s => 
      println(s"--> Available stream: "+s.getName)
      println(s"--> Size: "+ s.buffer.position())
      
      s.buffer.position() match {
        case 0 => 
        case p => 
          var ct = new Array[Byte](p.toInt)
          s.buffer.flip()
          s.buffer.get(ct)
          println(s"--->Content:"+new String(ct));
          //println(s"--> Content: "+s.buffer.get(ct))
      }
  }
  
  interface.close
  
 // TclintLibrary.evalClean(org.bridj.Pointer.pointerToCString("""open test.out rw"""))
  System.out.flush()
  System.err.flush()
  sys.exit
  //-----------------------------------------------------------------------//
  
 /* 
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
 */
  
  //Console.readLine
}