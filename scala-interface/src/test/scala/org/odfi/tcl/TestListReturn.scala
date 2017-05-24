//package test.list
/*
import test.TclInterpreter

/**
 * Created by zm4632 on 13.02.15.
 */
object TestListReturn extends App {

  var interface = new TclInterpreter

  interface.open

  interface.eval ("return \"Hi\"");

  var res = interface.eval (
    """
      |package org.odfi.tcl nx
      |
      |nx::Class create Test {
      |
      |   :variable -accessor public name "John"
      |}
      |
      |set res [list [Test new]  [Test new]  [Test new]  [Test new] [list [Test new]  [Test new] ]]
      |puts "Returning list: $res"
      |
      |
      |return $res
      |
      |
    """.stripMargin)

  println(s"Result of list return: ${res.string}")

}*/
