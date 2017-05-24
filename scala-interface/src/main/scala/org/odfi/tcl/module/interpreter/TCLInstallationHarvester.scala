package org.odfi.tcl.module.interpreter

import org.odfi.indesign.core.harvest.Harvester
import java.io.File
import org.odfi.indesign.core.harvest.fs.HarvestedFile
import java.nio.file.Path
import com.idyria.osi.tea.os.OSDetector.OS
import com.idyria.osi.tea.os.OSDetector
import org.odfi.indesign.core.module.process.IDProcess
import org.odfi.indesign.core.module.process.IDCommand
import com.idyria.osi.tea.io.TeaIOUtils
import org.bridj.BridJ
import org.odfi.tcl.integration.TclintLibrary
import java.util.logging.Level

object TCLInstallationHarvester extends Harvester {

  val pathVariableName = OSDetector.isWindows() match {
    case true => "Path"
    case false => "PATH"
  }

  val getVersionScript = File.createTempFile("odfi-tcl/getversion", ".tcl")
  TeaIOUtils.writeToFile(getVersionScript, getClass.getClassLoader.getResource("tcllib/getversion.tcl").openStream())

  override def doHarvest = {

    //println(s"Sepqrqting: "+File.pathSeparatorChar)
    sys.env.get(pathVariableName).get.split(File.pathSeparatorChar).filter {
      basePath =>
        var pathPathFile = new File(basePath)
        //println(s"Testing: "+pathPathFile)
        pathPathFile.listFiles() match {
          case null => false
          case files =>
            files.find {
              f =>
                //println(s"Testing file: "+f)
                f.getName.matches("tclsh8.*")
            }.isDefined
        }
    } foreach {
      foundTCLPath =>

        new File(foundTCLPath).listFiles().filter {
          f => f.getName.matches("tclsh8.*")
        }.foreach {
          interpreter =>
            gather(new TCLInstallation(interpreter.getCanonicalFile.toPath()))
        }

    }

  }

  def activateFirstValidInstallation = TCLInstallationHarvester.getResourcesOfType[TCLInstallation].collectFirst {
    case inst if (inst.activate) =>
      inst
  }

  /**
   *
   */
  def getValidInstallation = this.getResourcesOfType[TCLInstallation].find { i => i.valid }

}

class TCLInstallation(var interpreterPath: Path) extends HarvestedFile(interpreterPath) {

  var version = "0"
  var libraryPath: Option[File] = None
  var valid = false

  def activate = {
    this.resetErrors
    valid = false

    var libName = OSDetector.isWindows() match {
      case true =>
        s"tcl${version.replace(".", "")}"
      case false =>
        s"tcl${version}"
    }

    var libFile = new File(interpreterPath.toFile().getParentFile, libName + ".dll")

    // First Check the TCL library can be loaded
    //----------
    try {
      System.load(libFile.getCanonicalPath)

      //-- Now try compatibilty with TclInt Library
      //println("Activating: " + libName)
      sys.props.put("bridj.quiet", "1")
      BridJ.setMinLogLevel(Level.OFF)
      BridJ.unregister(classOf[TclintLibrary])
      BridJ.setNativeLibraryFile(libName, libFile)
      BridJ.resetNativeLibraryDependencies("tclint")
      BridJ.addNativeLibraryDependencies("tclint", libName)

      var res = BridJ.register(classOf[TclintLibrary])
      this.keepErrorsOn(this) {
        TclintLibrary.testLibIsValid()
      }
      this.hasErrors match {
        case true =>
          BridJ.releaseLibrary("tclint")
          BridJ.releaseLibrary(libName)
        case false =>
          valid = true
      }
    } catch {
      case e: UnsatisfiedLinkError =>
        this.addError(e)
    }

    valid

  }
  def deactivate = {
    this.valid = false
  }

  def isValid = this.valid
  def isActive = this.valid

  this.onGatheredBy(TCLInstallationHarvester) {

    //println("Determining TCL version of: " + interpreterPath)
    var command = new IDCommand(interpreterPath.toFile())
    var process = command.createToolProcess(TCLInstallationHarvester.getVersionScript.getAbsolutePath)

    process.outputToBuffer
    process.startProcessAndWait

    var result = process.getOutputString.trim.split("-")
    this.version = result(0).trim
    this.libraryPath = result.size match {
      case 1 => None
      case other => Some(new File(result(1).trim).getCanonicalFile)
    }

    println("Version: " + version)

    //-- Try to activate if none has been activated before
    TCLInstallationHarvester.getValidInstallation.isDefined match {
      case true =>
      case false =>
        activate
        println("Activated: " + isActive)
    }

    /*
    Thread.sleep(100)
    process.process.get.getOutputStream.write("""puts [info tclversion]; flush stdout; exit;\n""".getBytes)
    process.process.get.getOutputStream.flush()
    process.process.get.getOutputStream.flush()
   
    Thread.sleep(200)
    //process.waitOnProcess
    
    var version = process.getOutputString.trim
    println("Version: "+version)*/

  }

}