package org.odfi.tcl.module

import org.odfi.indesign.core.harvest.Harvest
import org.odfi.indesign.core.harvest.fs.FileSystemHarvester
import org.odfi.indesign.core.module.IndesignModule
import org.odfi.tcl.TclInterpreter

import java.util.concurrent.locks.ReentrantLock
import org.odfi.tcl.integration.TclintLibrary
import com.idyria.osi.tea.os.OSDetector
import org.bridj.BridJ
import org.odfi.indesign.core.module.ui.www.WWWViewHarvester
import org.odfi.tcl.module.www.TCLControlView
import org.odfi.tcl.module.interpreter.TCLInstallationHarvester

object TCLModule extends IndesignModule {

  // TclintLibrary.r
  /*TclintLibrary.enableDebug()
  TclintLibrary.disableDebug()*/

  /*OSDetector.getOS match {
    case os if (os == OSDetector.OS.LINUX) =>
    case _ => BridJ.addNativeLibraryDependencies("tclint", "tcl86")
  }*/

  //--------------
  // Interpreter Management
  //---------------------
  val interpreterLock = new ReentrantLock
  var interpreters = Map[String, TclInterpreter]()

  this.onLoad {

    //-- Test access to library
    //TclintLibrary.enableDebug()
    //TclintLibrary.disableDebug()
    Harvest.addHarvester(TCLInstallationHarvester)

  }

  this.onInit {
    println(s"LOADING")
    println(s"CL: " + Thread.currentThread().getContextClassLoader);

    //WWWViewHarvester.deliverDirect(new TCLControlView)

    //TclintLibrary.enableDebug()
    /*TclintLibrary.enableDebug()
    TclintLibrary.disableDebug()*/
  }

  this.onStart {
    //TclintLibrary.enableDebug
    //TclintLibrary.disableDebug()
  }

  def getInterpreter(name: String) = {

    interpreterLock.lock
    try {

      this.interpreters.get(name) match {
        case None =>
          println("New Inteprreseter from: " + Thread.currentThread().getId)

          var i = new TclInterpreter
          i.open
          this.interpreters = this.interpreters + (name -> i)
          reloadInterpreterPackages(name)
          i
        case Some(i) =>
          println("Got Inteprreseter from: " + Thread.currentThread().getId)
          i
      }
    } finally {
      interpreterLock.unlock
    }
  }

  def onInterpreter[T](name: String)(cl: TclInterpreter => T) = {
    this.synchronized {
      var i = this.getInterpreter(name)
      cl(i)
    }
  }

  /**
   * Forgets all the packages, and uses Harvest to reload all the indexes
   */
  def reloadInterpreterPackages(name: String): Unit = {
    println("reloading Inteprreseter from: " + Thread.currentThread().getId)
    //-- Forget Packages
    var i = this.getInterpreter(name)
    //i.forgetPackages

    //-- Load from Harvester
    Harvest.onHarvesters[TCLPackageHarvester] {
      case h =>
        h.onResources[TCLPackageFolder] {
          case pf =>
            println(s"Sourcing: " + pf.getIndexFile)
            //interpreter.eval(pf.getIndexFile)
            i.loadPackageIndexFile(pf.getIndexFile)
          // interpreter.e
        }
    }
  }

  def deleteInterpreter(int:TclInterpreter) : Unit = {
    this.interpreters.find { case (k,i) => i==int} match {
      case None => 
      case Some((name,int)) => 
        deleteInterpreter(name)
    }
  }
  def deleteInterpreter(name: String) : Unit = {

    //-- Close and clean
    this.interpreters.get(name) match {
      case None =>
      case Some(i) =>
        interpreterLock.lock
        try {
          i.close
          this.interpreters = this.interpreters - name
        } finally {
          this.interpreterLock.unlock()
        }
    }

  }
  /*
  def load = {

    //FileSystemHarvester.addChildHarvester(new TCLFileHarvester)
    //Harvest.registerAutoHarvesterClass(classOf[ODFIHarvester], classOf[TCLPackageHarvester])
  }*/

}