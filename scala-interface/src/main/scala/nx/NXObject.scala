package nx

import scala.language.dynamics
import tcl.TclInterpreter
import tcl.TclObjectValue
import tcl.TclValue
import tcl.integration.TclObject
import scala.collection.mutable.Stack

/**
 * Created by zm4632 on 13.02.15.
 */
class NXObject(interpreter: TclInterpreter, obj: TclObject) extends TclObjectValue[TclObject](interpreter, obj) with Dynamic {

  var commandStack = Stack[String]()

  def selectDynamic(fieldName: String): TclValue = {

    //println(s"Going to run /${objectName} $fieldName get/")
    interpreter.eval(s"${this.toString} $fieldName get")

  }

  def updateDynamic(fieldName: String)(value: Any): Unit = {
    //println(s"Running "+s"""${this.toString} $fieldName set "${value.toString()}" rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr r""")
    interpreter.eval(s"""${this.toString} $fieldName set "${value.toString()}" """)
  }

  def apply(command: String) = {
    interpreter.eval(s"${this.toString} $command")
  }

}

object NXObject {

  implicit def fromTclValue(r: TclValue): NXObject = r match {
    case objectValue: TclObjectValue[_] => new NXObject(r.interpreter, objectValue.obj)
    case v => throw new Exception(s"Cannot convert TclValue of type: ${v.getClass.getName} to NXObject. Must be a TclObjectValue")
  }

  implicit class NXTCLObjectValueWrapper(val self: TclObjectValue[_]) extends AnyVal {

    def toNXObject: NXObject = NXObject.fromTclValue(self)

  }

}

class NXObjectValueWrapper(val underlying: TclValue) extends AnyVal {

  def toNXObject: NXObject = NXObject.fromTclValue(underlying)

}

