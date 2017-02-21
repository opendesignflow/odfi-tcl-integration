package org.odfi.tcl

import org.odfi.tcl.TclInterpreter;

/**
 * Created by zm4632 on 13.02.15.
 */
object TestListInterface extends App  {



  println(s"In List  test")

  var interface = new TclInterpreter

  interface.open

  var res = interface.eval(
    """
      |return [list a b c [list d e]]
    """.stripMargin).asInstanceOf[ExtendedTclList]

  println(s"Return list size: ${res.getLength}")

  println(s"First elt: "+res(0).toString())

  // Create
  //var lst = new SuperTCLList

  // Test
  //lst.getLength

}
