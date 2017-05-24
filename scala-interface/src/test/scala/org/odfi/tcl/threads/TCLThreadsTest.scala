package org.odfi.tcl.threads

import com.idyria.osi.tea.thread.ThreadLanguage
import org.odfi.tcl.integration.TclintLibrary
import org.odfi.tcl.TclInterpreter

object TCLThreadsTest extends App with ThreadLanguage {
  
   //TclintLibrary.enableDebug()
   
  //-- Create Interpreter
  var interpreter = new TclInterpreter
  interpreter.open
  
  //-- Print
  interpreter.evalString(""" puts "Hello main thread" """)
  
  //-- Print in thread
   var th = createThread {
    
    while(true) {
      interpreter.evalString(""" puts "Hello second thread" """)
      Thread.sleep(1200)
    }
    
    
  }
  th.start()
  
   var th2 = createThread {
    
    while(true) {
      interpreter.evalString(""" puts "Hello third thread" """)
      Thread.sleep(2400)
    }
    
    
  }
  th2.start()
  
  th.join()
  th2.join()
  
  interpreter.close
  
}