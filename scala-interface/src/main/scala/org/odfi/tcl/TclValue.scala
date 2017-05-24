package org.odfi.tcl

import org.bridj.Pointer

import org.odfi.tcl.integration.TclList
import org.odfi.tcl.integration.TclObject



/**
 * Created by zm4632 on 12.02.15.
 */
trait TclValue {

    val interpreter: TclInterpreter

    /**
     * This Factory creates a TclValue with a possible correct type from a
     * TclObject wrapper
     */
    def objectToValue(obj: Pointer[TclObject]): TclValue = {

        obj.get.isSimpleType() match {
       
            case false if(obj.get.isList()) =>  
                var lst =  new ExtendedTclList(this.interpreter,obj.as(classOf[TclList]).get)
                //lst.init(this.interpreter.interpreter, obj)
                lst
            case _ => new TclObjectValue(interpreter, obj.get())
        }

    }
    
    def isEmpty = toString match {
      case "" => true
      case _ => false
    }
    def asList : ExtendedTclList = this.asInstanceOf[ExtendedTclList]
    def isObjectValue = this match {
      case ob : TclObjectValue[_] => true
      case _ => false
    }
    def asObjectValue = this.asInstanceOf[TclObjectValue[_]]
    
    /**
     * Returns true if 1 or true, false otherwise
     */
    def toBoolean = toString match {
      case "true" =>  true
      case "1" => true
      case _ => false
    }
  
}

object TclValue {

}