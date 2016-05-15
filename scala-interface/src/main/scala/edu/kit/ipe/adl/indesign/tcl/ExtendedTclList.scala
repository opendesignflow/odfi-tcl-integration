package edu.kit.ipe.adl.indesign.tcl

import edu.kit.ipe.adl.indesign.tcl.integration.TclList
import edu.kit.ipe.adl.indesign.tcl.nx.NXObject



/**
 * Created by zm4632 on 13.02.15.
 */
class ExtendedTclList( interpreter: TclInterpreter,  list: TclList) extends TclObjectValue[TclList](interpreter,list) {

    

    override def toString = this.list.asString().getCString

    def getLength = this.list.getLength
    
    def apply(index: Int): TclValue = {

        //-- get 
        var obj = this.list.at(index)

        //-- Check Type
        this.objectToValue(obj)
    }
    
    def toList : List[TclValue] = {
      (0 until (this.getLength)).map {
        i => this(i)
      }.toList
    }
    
    def toNXList : List[NXObject] = {
      toList.map{o => NXObject.fromTclValue(o)}
    }

}
