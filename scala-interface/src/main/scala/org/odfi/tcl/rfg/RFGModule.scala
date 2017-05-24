package org.odfi.tcl.indesign.module.odfi.rfg

import org.odfi.indesign.core.module.IndesignModule

object RFGModule extends IndesignModule {
  
  def load = {
   // TCLFileHarvester.addChildHarvester(new RFGScriptFileHarvester)
  }
  
  
  this.onInit {
    //WWWViewHarvester.deliverDirect(new RFGMainView)
  }
  
}