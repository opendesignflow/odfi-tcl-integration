package org.odfi.tcl.threads

import org.odfi.tcl.TclInterpreter
import com.idyria.osi.tea.thread.ThreadLanguage

object TCLMultipleStdoutMultipleInterpreters extends App with ThreadLanguage {

  //-- Create Interpreter
  var interpreter = new TclInterpreter
  interpreter.open

  var interpreter2 = new TclInterpreter
  interpreter2.open

  //-- Print
  interpreter.evalString("""  puts "I1 Hello main thread" """)
  interpreter2.evalString(""" puts "I2 Hello main thread" """)

  var count = 50

  //-- Print in thread
  var th = createThread {

    var ct = 0
    while (ct < count) {
      ct += 1
      interpreter.evalString(""" puts "I1 Hello second thread" """)
      Thread.sleep(5 + (500 * Math.random()).toInt)
    }

  }
  th.start()

  var th2 = createThread {

    var ct = 0
    while (ct < count) {
      ct += 1
      interpreter2.evalString(""" puts "I2 Hello third thread" """)
      Thread.sleep(13 + (500 * Math.random()).toInt)
    }

  }
  th2.start()

  th.join()
  th2.join()

  println("Showing outputs:")
  println("** I1:")
  println(interpreter.streams.find{ case (id,s) => s.getName=="stdout"}.get._2.getContent)
  println("** I2:")
  println(interpreter2.streams.find{ case (id,s) => s.getName=="stdout"}.get._2.getContent) 
  
  interpreter.close
  interpreter2.close
}