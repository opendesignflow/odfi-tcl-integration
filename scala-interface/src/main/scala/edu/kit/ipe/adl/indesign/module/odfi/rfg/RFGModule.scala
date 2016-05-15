package edu.kit.ipe.adl.indesign.module.odfi.rfg

import edu.kit.ipe.adl.indesign.core.module.IndesignModule

object RFGModule extends IndesignModule {
  
  def load = {
   // TCLFileHarvester.addChildHarvester(new RFGScriptFileHarvester)
  }
  
  
  this.onInit {
    //WWWViewHarvester.deliverDirect(new RFGMainView)
  }
  
}