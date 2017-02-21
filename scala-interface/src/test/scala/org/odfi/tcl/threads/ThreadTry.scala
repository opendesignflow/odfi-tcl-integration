package org.odfi.tcl.threads

import com.idyria.osi.tea.thread.ThreadLanguage
import org.odfi.tcl.TclInterpreter

object ThreadTry extends App with ThreadLanguage {
  
   
  //-- Create Interpreter
  var interpreter = new TclInterpreter
  interpreter.open
  
  //-- Print
  interpreter.evalString(""" puts "Hello main thread" """)
  interpreter.evalString(""" puts "Hello main thread 2" """)
  
  //-- Print in thread
   var th = createThread {
    
    interpreter.evalString(""" puts "Hello second thread" """)
    
  }
  th.start()
  th.join()
  
  interpreter.close
  
}