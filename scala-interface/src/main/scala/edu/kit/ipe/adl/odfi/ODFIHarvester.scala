package edu.kit.ipe.adl.odfi

import edu.kit.ipe.adl.indesign.core.harvest.Harvester
import java.io.File
import edu.kit.ipe.adl.indesign.core.harvest.fs.HarvestedFile
import java.nio.file.Path
import edu.kit.ipe.adl.indesign.core.harvest.fs.FileSystemHarvester
import org.bridj.BridJ
import java.util.Arrays
import edu.kit.ipe.adl.indesign.tcl.module.TCLPackageHarvester
import edu.kit.ipe.adl.indesign.module.maven.MavenProjectHarvester

class ODFIHarvester(var managerPath: File) extends FileSystemHarvester {

  this.addChildHarvester(new TCLPackageHarvester)
  override def doHarvest = {

    var installPath = new File(managerPath, "install")
    installPath.listFiles().filter(_.isDirectory()).foreach {
      mf =>
        var resource = gather(new ODFIModule(mf.toPath()))
       // println(s"Odfi gathering: "+resource)
      /*resource.onAdded {
          case h if(h==this) =>
            this.addPath(resource.path)
        }*/
    }
    this.finishGather
    super.doHarvest

  }

}

class ODFIModule(p: Path) extends HarvestedFile(p) {
  /*this.root
  this.local = true*/
  // Init 
  //-----------

  //-- Lib 
  var libFolder = new File(p.toFile(), "lib")
  libFolder.exists() match {
    case true =>
      //println("Found lib oflder: " + libFolder)
      System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + libFolder)
      BridJ.addLibraryPath(libFolder.getAbsolutePath)
      //set sys_paths to null so that java.library.path will be reevalueted next time it is needed
      val sysPathsField = classOf[ClassLoader].getDeclaredField("sys_paths");
      sysPathsField.setAccessible(true);
      sysPathsField.set(null, null);
    //System.getProperty("java.library.path")
    case false =>
  }
  //-- Bin
  var binFolder = new File(p.toFile(), "bin")
  binFolder.exists() match {
    case true =>
      //println("Found bin oflder: " + binFolder)
      System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + binFolder)
      BridJ.addLibraryPath(binFolder.getAbsolutePath)
      //set sys_paths to null so that java.library.path will be reevalueted next time it is needed
      val sysPathsField = classOf[ClassLoader].getDeclaredField("sys_paths");
      sysPathsField.setAccessible(true);
      sysPathsField.set(null, null);
      //System.getProperty("java.library.path")

     

    case false =>
  }

}