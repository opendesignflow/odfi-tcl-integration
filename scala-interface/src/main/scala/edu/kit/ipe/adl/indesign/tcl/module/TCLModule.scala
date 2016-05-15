package edu.kit.ipe.adl.indesign.tcl.module

import edu.kit.ipe.adl.indesign.core.harvest.Harvest
import edu.kit.ipe.adl.indesign.core.harvest.fs.FileSystemHarvester
import edu.kit.ipe.adl.indesign.core.module.IndesignModule
import edu.kit.ipe.adl.indesign.module.maven.POMFileHarvester
import edu.kit.ipe.adl.indesign.tcl.TclInterpreter
import edu.kit.ipe.adl.odfi.ODFIHarvester

object TCLModule extends IndesignModule {

  //--------------
  // Interpreter Management
  //---------------------
  var interpreters = Map[String, TclInterpreter]()

  def getInterpreter(name: String) = {
    this.interpreters.get(name) match {
      case None =>
        var i = new TclInterpreter
        i.open
        this.interpreters = this.interpreters + (name -> i)
        reloadInterpreterPackages(name)
        i
      case Some(i) =>
        i
    }
  }

  /**
   * Forgets all the packages, and uses Harvest to reload all the indexes
   */
  def reloadInterpreterPackages(name: String): Unit = {

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

  def load = {

    //FileSystemHarvester.addChildHarvester(new TCLFileHarvester)
    //Harvest.registerAutoHarvesterClass(classOf[ODFIHarvester], classOf[TCLPackageHarvester])
  }

}