package org.odfi.tcl

import org.odfi.tcl.integration.TclObject
import org.odfi.tcl.nx.NXObject

class TclObjectValue[T <: TclObject](val interpreter: TclInterpreter, val obj: T) extends TclValue {

  override def toString = obj.asString().getCString

  def isNXObject = toString match {
    case v if (v.startsWith("::nsf::")) => true
    case _ => false
  }
  def asNXObject = new NXObject(interpreter, obj)
  
}