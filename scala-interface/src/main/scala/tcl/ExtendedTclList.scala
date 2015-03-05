package tcl

import tcl.integration.TclList
import tcl.integration.TclObject
import org.bridj.Pointer

/**
 * Created by zm4632 on 13.02.15.
 */
class ExtendedTclList(val interpreter: TclInterpreter, val list: TclList) extends TclValue {

    

    override def toString = this.list._listObj().get.asString().getCString

    def getLength = this.list.getLength
    
    def apply(index: Int): TclValue = {

        //-- get 
        var obj = this.list.at(index)

        //-- Check Type
        this.objectToValue(obj)
    }

}
