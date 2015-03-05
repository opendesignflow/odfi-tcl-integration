package tcl

import tcl.integration.TclObject

class TclObjectValue(val interpreter : TclInterpreter,val obj : TclObject ) extends TclValue {

  override def toString = obj.asString().getCString
  
}