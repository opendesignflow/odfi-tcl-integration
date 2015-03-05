package nx


import scala.language.dynamics
import tcl.integration.TclObject
import tcl.TclInterpreter
import tcl.TclValue
import tcl.TclObjectValue

/**
 * Created by zm4632 on 13.02.15.
 */
class NXObject(val objectPtr: TclObject, val interpreter : TclInterpreter) extends Dynamic with TclValue {

  def selectDynamic(fieldName:String) : TclValue = {

    //println(s"Going to run /${objectName} $fieldName get/")
    interpreter.eval(s"${objectPtr.asString().getCString} $fieldName get")

  }


}

object NXObject {

  implicit def fromTclValue(r: TclValue): NXObject = r match {
    case objectValue : TclObjectValue => new NXObject(objectValue.obj,r.interpreter)
    case v => throw new Exception(s"Cannot convert TclValue of type: ${v.getClass.getName} to NXObject. Must be a TclObjectValue")
  }
   

}
