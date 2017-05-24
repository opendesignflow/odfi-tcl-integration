package org.odfi.tcl

import org.odfi.tcl.module.TCLModule

object QuickTCLTest  extends App {
  
  println("TCL quick test")
  
  TCLModule.moveToInit
  
  var i = TCLModule.getInterpreter("main")
  
  println("Here we are")
  i.evalString("""puts "Hello From TCL" """)
  
  TCLModule.moveToShutdown
}