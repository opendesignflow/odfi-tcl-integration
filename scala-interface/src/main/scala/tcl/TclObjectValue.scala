package tcl

import tcl.integration.TclObject

class TclObjectValue[T <: TclObject](val interpreter : TclInterpreter,val obj : T ) extends TclValue {

  override def toString = obj.asString().getCString
  
}