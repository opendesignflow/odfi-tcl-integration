package edu.kit.ipe.adl.indesign.tcl

import edu.kit.ipe.adl.indesign.tcl.integration.TclObject
import edu.kit.ipe.adl.indesign.tcl.nx.NXObject

class TclObjectValue[T <: TclObject](val interpreter: TclInterpreter, val obj: T) extends TclValue {

  override def toString = obj.asString().getCString

  def asNXObject = new NXObject(interpreter, obj)
}