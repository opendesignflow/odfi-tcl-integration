package org.odfi.tcl.nx

import org.odfi.tcl.flist.MutableList
import org.odfi.tcl.TclValue
import org.odfi.tcl.integration.interpreter
import org.odfi.tcl.TclInterpreter

trait NXObjectCacheFactory[T <: NXObject] {
  
  var objectsCache = Map[(TclInterpreter,String),T]()
  
  def apply(objs:NXObject,fact: (NXObject => T)) : List[T] = {
    
    objs.isMutableList match {
      case true =>  MutableList.toNXList(objs).map(build(_,fact)).toList
      case false =>  List(build(objs,fact))
    }
   
   
  }
  
  def build(o:NXObject,fact: (NXObject => T)) : T = {
    
    objectsCache.getOrElse((o.interpreter -> o.toString), {
      
      var res = fact(o)
      objectsCache = objectsCache + ((o.interpreter -> o.toString) -> res)
      res
      
    })
    
  }
  
  def mutableListToObjects(objs:NXObject,fact: (NXObject => T)) : List[T] = {
    
    MutableList.toNXList(objs).map(build(_,fact)).toList
   
  }
  

  
}