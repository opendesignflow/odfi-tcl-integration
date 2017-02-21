package org.odfi.tcl

import org.odfi.tcl.integration.TclintLibrary
import java.io.File
import org.bridj.BridJ
import org.odfi.tcl.TclInterpreter;

import com.idyria.osi.tea.file.TeaFileUtils

import org.odfi.tcl.module.TCLModule

object TEstLoadLIbrary extends App {

  //var odfiHarvester = new ODFIHarvester(new File("""E:\Common\Projects\git\odfi-manager"""))

  //odfiHarvester.harvest

  // TCLModule.load
  /* BridJ.addNativeLibraryDependencies("tclint", "tcl86")
 
  var l = BridJ.getNativeLibrary("tcl86")
  println("l: "+BridJ.getNativeLibraryFile("tcl86"))
  BridJ.addLibraryPath(BridJ.getNativeLibraryFile("tcl86").getParentFile.getAbsolutePath)*/

  // TeaFileUtils.copy(BridJ.getNativeLibraryFile("tcl86"), new File("tcl86.dll"))

  //sys.exit
  var property = System.getProperty("java.library.path")
  property.split(";").foreach {
    p =>
      println("Path:" + p)
  }

  System.out.println("sun.arch.data.model = " + System.getProperty("sun.arch.data.model"));
  //System.load(new File("target/generated-resources/lib/win64/tclint.dll").getAbsolutePath)
  TclintLibrary.enableDebug()
  println("Starting");
  var interface = new TclInterpreter
  interface.open
  interface.eval("""puts Hello""")

  println("Closing");
  interface.close

}