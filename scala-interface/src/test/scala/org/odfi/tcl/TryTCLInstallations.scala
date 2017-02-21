package org.odfi.tcl

import org.odfi.tcl.module.interpreter.TCLInstallationHarvester
import org.odfi.tcl.module.interpreter.TCLInstallation

object TryTCLInstallations extends App {
  sys.props.put("bridj.quiet", "1")
  sys.props.put("bridj.quiet", "1")
  
  TCLInstallationHarvester.harvest
  
  TCLInstallationHarvester.getValidInstallation  match {
    case Some(inst) => 
      println("Found valid installation at: "+inst.libraryPath)
    case None => 
      println("No Valid Installation to activate")
  }
  //TCLInstallationHarvester.getResourcesOfType[TCLInstallation].head.activate
  
  //-- Create Interpreter and show version
 /* var interpreter = new TclInterpreter
  interpreter.open
  
  interpreter.evalString(""" puts "V: [info tclversion]; [info nameofexecutable]; $tcl_library"    """)
  interpreter.evalString(""" package require uri   """)
  
  interpreter.close*/
  
}