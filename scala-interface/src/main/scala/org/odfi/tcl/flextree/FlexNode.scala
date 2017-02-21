package org.odfi.tcl.flextree

import org.odfi.tcl.nx.NXObject
import org.odfi.tcl.flist.MutableList
import java.io.File

trait FlexNode extends NXObject {
  

  
  def getChildren(shade:String="")  = shade match {
    case "" => MutableList.toNXList(this(s"children").asObjectValue.asNXObject)
    case s => MutableList.toNXList(this(s"shade $s children").asObjectValue.asNXObject)
  }
  
}