package de.rub.bi.inf.baclient.workflow.pruefung;

import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcSpace;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import net.opengis.gml._3.AbstractFeatureType;

public class ErsterUndZweiterRettungsweg extends Pruefvorgang {
	
	public ErsterUndZweiterRettungsweg() {
		this.name="Erster und zweiter Rettungsweg";
	}

	@Override
	public void pruefe(ApplicationModelNode ifcModel, AbstractFeatureType bpBereich) {
		
		 if (ifcModel==null)
			 return;
		
		 this.modelNode = ifcModel;
		
		
		 IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
		
		 if (project instanceof IfcProject.Ifc2x3) {
		
		        
		        
		 }
		 else if (project instanceof IfcProject.Ifc4) {
			Collection<IfcObject.Ifc4> spaces = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "Pset_SpaceFireSafetyRequirements", "FireExit", true);
	
			
		 }

	}

}
