package org.odfi.tcl.flist

import org.odfi.tcl.nx.NXObject

object MutableList extends App {
  
  def toNXList(obj: NXObject) = obj("toTCLList").asList.toNXList 
  
}