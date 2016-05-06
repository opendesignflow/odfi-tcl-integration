package nxaccess

import nx.NXObject
import tcl.TclInterpreter
import com.idyria.osi.tea.timing.TimingSupport
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import tcl.integration.TclObject

class TestNXAccess extends FunSuite with BeforeAndAfter with GivenWhenThen  {
    
    var interface = new TclInterpreter
    before {
        interface.open
    }
    after {
        //interface.close
    }
    
    test("Load NX") {
        
        interface.eval(
        """
          |package require nx 2.0.0
          |
          | 
          |puts "In Here"
        """.stripMargin)
        
        
    }
    
    def simpleClassDefintiion = new {
      interface.eval("""|
                        |package require nx 2.0.0
                        |
                        |nx::Class create A {
                        |    
                        |    :property -accessor public {test val}
                        |    
                        |}
                        |
                        |""".stripMargin)
    }
    
    test("Create an Object") {
        
        Given("A Class Definition")
        val c = simpleClassDefintiion
        
        Then("Create an Object")
        var obj = interface.eval("A create first")
        assertResult("::first")(obj.toString())
        
        println(s"Created First object: ${obj.toString()} -> ${obj.obj.asInstanceOf[TclObject].getTypeName().getCString} ")
        
        Then("Try to access a field")
        var nxObject : NXObject = obj
        assertResult("val","Access a test field")(nxObject.test.toString())
        println("Test field: "+nxObject.test)
        
        Then("Try to update a field")
        nxObject.test = "Hello"
        assertResult("Hello","Updated value of a test field")(nxObject.test.toString())
        
        Then("Make a performance test")
        (0 until 20000).foreach {
            i => 
                nxObject.test = s"Hello $i"
        }
        
    }
    
    
    
}
/*
/**
 * Created by zm4632 on 12.02.15.
 */
object TestNXAccess extends App with TimingSupport {

  println(s"Started")
  // Create Interpreter
  //----------------------


  var interface = new TclInterpreter

  interface.open


  // Load package
  //------------------

  println(s"Just saying Hello")
  interface.eval(
    """
      |package require nx 2.0.0
      |
      | 
      |puts "In Here"
    """.stripMargin)




  // Test create class object, and return it
  //-----------------------
  var res = interface.eval (
    """
      |nx::Class create Test {
      |
      |   :variable -accessor public name "John"
      |}
      |
      |set res [Test new]
      |puts "New object is: $res"
      |
      |puts "Obj name is [::nsf::__#0 name get]"
      |$res
      |
      |
    """.stripMargin)

 
    
  var nxo : NXObject = res
  
   println(s"Object name: "+nxo.name.toString)
   println(s"Object name: "+nxo.name.toString)
  
  var rl = List[String]()
  var t = time {
    
    (0 to 10000).foreach {
      i => rl = rl :+ nxo.name.toString
    }
    
    
  }
  
  println(s"Time needed: "+t)

  /*println(s"Returned value: "+res.string)

  interface.eval (
    s"""
      |puts "Obj name is [${res.string} name get]"
    """.stripMargin)

  println(s"Object name: "+nxo.name.string)*/


  //Try returning a list of stuff
  //----------------------------
  /*res = interface.eval (
    """
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

  println(s"Result of list return: ${res.string}")*/
 // interface.close

}
*/