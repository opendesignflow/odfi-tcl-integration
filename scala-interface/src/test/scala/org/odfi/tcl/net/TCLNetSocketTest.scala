package org.odfi.tcl.net

import org.odfi.tcl.TclInterpreter
import org.odfi.tcl.module.interpreter.TCLInstallationHarvester
import com.idyria.osi.tea.thread.ThreadLanguage

object TCLNetSocketTest extends App with ThreadLanguage {

  TCLInstallationHarvester.harvest
  

  println("Socket Test: ")
  var th = createThread {

    var interpreter = new TclInterpreter
  interpreter.open
  
    interpreter.evalString(""" 
      puts "testing socket"
      flush stdout
      
      set r [socket -server [list Server [clock seconds]] 8692]
      puts "OPenend socket: $r"
      flush stdout
      try {
          
          vwait forever
      } finally {
          close $r
      }  """.trim)

  }
  th.start()
  th.join

  println("Socket Test: ")

  //interpreter.close

}