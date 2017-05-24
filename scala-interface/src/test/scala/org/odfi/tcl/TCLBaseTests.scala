package org.odfi.tcl

import org.odfi.tcl.TclInterpreter;
import org.scalatest.BeforeAndAfter
import org.scalatest.Finders
import org.scalatest.FunSuite
import org.scalatest.GivenWhenThen





class TCLBaseTest extends FunSuite with BeforeAndAfter with GivenWhenThen {
   
     
      
    test("Open and Close") {
        
        var interface = new TclInterpreter
        interface.open
        assert(true)
        
        interface.close
    }
    
    
    
    test("Interpreter Error") {
        
        intercept[TclEvaluationError] {
            var interface = new TclInterpreter
        interface.open
            interface.eval("""|
                          |puts "Hello $world"
                          |
                          |""".stripMargin)
        }
        
        
    }
    
    test("List Result Test") {
        
        var interface = new TclInterpreter
        interface.open
        
        Given("A TCL constructed list")
        var res = interface.eval(
    """
      |return [list a b c [list d e]]
    """.stripMargin)
        
        Then("The TclValue should be an extended TCL List")
        assertResult(classOf[ExtendedTclList])(res.getClass)
        var list = res.asInstanceOf[ExtendedTclList]
        
        Then("Verify the content")
        
        assertResult(4,"The lenght of the list")(list.getLength)
        
        assertResult("a","The first element")(list(0).toString())
        
        assertResult("b","The second element")(list(1).toString())
        
        assertResult(classOf[ExtendedTclList],"The Fourth element should be a list")(list(3).getClass)
       
        Then("Verify the second List")
        var secondList = list(3).asInstanceOf[ExtendedTclList]
        assertResult(2,"The lenght of the second list")(secondList.getLength)
        assertResult("d","The first element")(secondList(0).toString())
        assertResult("e","The second element")(secondList(1).toString())
        
    }
    
    
}