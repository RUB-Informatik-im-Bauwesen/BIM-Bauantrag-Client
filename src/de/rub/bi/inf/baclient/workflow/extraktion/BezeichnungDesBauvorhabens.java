package de.rub.bi.inf.baclient.workflow.extraktion;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcLabel.Ifc4;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;

import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;

public class BezeichnungDesBauvorhabens extends ExtraktionsVorgang {
	
	public BezeichnungDesBauvorhabens() {
		this.name = "Vom Bauherrn vergebene Bezeichnung des Bauvorhabens";
	}

	@Override
	public void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel) {
		
		if (ifcModel != null) {
			IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
			if (project == null) return;
			
			if (project instanceof IfcProject.Ifc4) {
				IfcSimpleProperty.Ifc4 simpleProperty = 
						Util.Ifc4.getElementProperty((IfcObjectDefinition.Ifc4) project, 
								"BauantragAllgemein", "Bezeichnung des Bauvorhabens");

				if (simpleProperty==null || !(simpleProperty instanceof IfcPropertySingleValue)) return;
				
				IfcPropertySingleValue.Ifc4 singleValue = (IfcPropertySingleValue.Ifc4) simpleProperty;
		         
				IfcLabel.Ifc4 label = (Ifc4) singleValue.getNominalValue();
				
				value = label.getDecodedValue();
			}
		}

	}

}
