package org.odfi.tcl.threads

import org.odfi.tcl.TclInterpreter
import com.idyria.osi.tea.thread.ThreadLanguage

object TCLMultipleStdoutThreads extends App with ThreadLanguage  {
  
  //-- Create Interpreter
  var interpreter = new TclInterpreter
  interpreter.open

  //-- Print
  interpreter.evalString(""" puts "Hello main thread" """)

  //-- Print in thread
  var th = createThread {

    while (true) {
      interpreter.evalString(""" puts "Hello second thread" """)
      Thread.sleep(250+(500*Math.random()).toInt)
    }

  }
  th.start()

  var th2 = createThread {

    while (true) {
      interpreter.evalString(""" puts "Hello third thread" """)
      Thread.sleep(500+(500*Math.random()).toInt)
    }

  }
  th2.start()

  th.join()
  th2.join()

  interpreter.close
}