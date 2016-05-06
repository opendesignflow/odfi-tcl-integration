package tcl

import org.bridj.Pointer
import tcl.integration.TclObject
import tcl.integration.TclList

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

}

object TclValue {

}