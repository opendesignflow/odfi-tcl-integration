package org.odfi.tcl.module

import org.odfi.indesign.core.harvest.Harvester
import org.odfi.indesign.core.harvest.fs.HarvestedFile
import java.nio.file.Path

class TCLFileHarvester extends Harvester {

 

  /**
   * Reacts on pom.xml file
   */
  this.onDeliverFor[HarvestedFile] {

    case r =>

    //println(s"Testign TCL file: "+r.path)
    
    //if (r.
    r.path.toUri().toString().endsWith(".tcl") match {
      case true =>
        println(s"Delivered TCL FILE: " + r.path.toUri())
        this.gather(new TCLFile(r.path))
        true
      case false =>
        false
    }

  }
}

object TCLFileHarvester {

  implicit def pathToResource(p: Path) = new HarvestedFile(p)
}