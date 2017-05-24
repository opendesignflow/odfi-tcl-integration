
package org.odfi.tcl




/**
 * Created by zm4632 on 13.02.15.
 */
class StringValue(val interpreter: TclInterpreter,val str:String) extends TclValue {

  override def toString = str

}
