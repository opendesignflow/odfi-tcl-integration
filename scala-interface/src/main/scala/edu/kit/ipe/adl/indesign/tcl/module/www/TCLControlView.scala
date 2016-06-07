package edu.kit.ipe.adl.indesign.tcl.module.www

import edu.kit.ipe.adl.indesign.core.module.ui.www.IndesignUIView
import edu.kit.ipe.adl.indesign.tcl.module.TCLModule

class TCLControlView extends IndesignUIView {

  this.viewContent {

    div {

      h1("TCL Control") {

      }

      TCLModule.hasErrors match {
        case true =>
          "ui info error message" :: div {
            textContent(s"""
              The TCL Module is in error state:
              ${TCLModule.errors.head} : ${TCLModule.errors.head.getLocalizedMessage}
              """)
           "ui bulleted list" ::  div  {
              TCLModule.errors.head match {
                case e: ExceptionInInitializerError =>

                  //-- Causes
                  e.getCause match {
                    case null =>
                    case cause: RuntimeException if (cause.getLocalizedMessage.contains("Failed to register class edu.kit.ipe.adl.indesign.tcl.integration.TclintLibrary")) =>
                      "item" :: div {
                        textContent("The TCL Library loading failed, maybe the interpreter is missing")
                        "list" :: div {
                          cause.getCause match {
                            case cause: ClassNotFoundException =>
                              "item" :: div {
                                textContent(s"""
                                    A class could not be found, maybe the module was run with a wrong class loader: 
                                    ${cause.getLocalizedMessage}
                                """)

                              }
                            case null =>
                          }
                        }

                      }
                  }
                  
                case e : UnsatisfiedLinkError => 
                  li {
                    textContent("The TCL Library linking failed, probably the interpreter is missing")
                  }
                case e: Throwable =>
              }
            }
          }
        case false =>
          "ui info success message" :: div {
            textContent("The TCL Module seems to be loaded fine")
          }
      }
      

    }
  }
}