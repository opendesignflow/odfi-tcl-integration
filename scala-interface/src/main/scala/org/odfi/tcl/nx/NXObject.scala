package org.odfi.tcl.nx

import scala.collection.mutable.Stack
import scala.language.dynamics

import org.odfi.tcl.TclInterpreter
import org.odfi.tcl.TclObjectValue
import org.odfi.tcl.TclValue
import org.odfi.tcl.integration.TclObject
import java.io.File

/**
 * Created by zm4632 on 13.02.15.
 */
class NXObject(interpreter: TclInterpreter, obj: TclObject) extends TclObjectValue[TclObject](interpreter, obj) with Dynamic {

  def this(o: NXObject) = this(o.interpreter, o.obj)

  var commandStack = Stack[String]()

  def getNXClass = this.apply("info class").toString
  def isNXClass(cl: String) = this(s"isClass $cl").toBoolean
  def isMutableList = this(s"isClass ::odfi::flist::MutableList").toBoolean

  def selectDynamic(fieldName: String): TclValue = {

    //println(s"Going to run /${objectName} $fieldName get/")
    interpreter.synchronized {
      interpreter.eval(s"${this.toString} $fieldName get")
    }
  }

  def updateDynamic(fieldName: String)(value: Any): Unit = {
    //println(s"Running "+s"""${this.toString} $fieldName set "${value.toString()}" rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr r""")

    interpreter.synchronized {
      interpreter.eval(s"""${this.toString} $fieldName set "${value.toString()}" """)
    }
  }

  def apply(command: String) = {
    interpreter.synchronized {
      interpreter.eval(s"${this.toString} $command")
    }

  }

  def callApply(command: String) = {
    interpreter.synchronized {
      interpreter.eval(s"${this.toString} apply { $command ; }")
    }

  }

  def applyFile(f: File) = {
    interpreter.synchronized {
      interpreter.eval(s"${this.toString} apply { source ${f.getCanonicalPath.replace('\\', '/')} ; }")
    }

  }

}

object NXObject {

  def apply(interpreter: TclInterpreter, str: String): NXObject = {
    interpreter.evalString(s"return $str").asNXObject
  }

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

