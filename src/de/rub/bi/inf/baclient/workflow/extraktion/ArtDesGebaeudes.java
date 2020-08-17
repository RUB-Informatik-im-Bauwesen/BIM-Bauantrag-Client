package de.rub.bi.inf.baclient.workflow.extraktion;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcInteger;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcPositiveInteger;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcPropertyEnumeratedValue;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcValue;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc2x3;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;

public class ArtDesGebaeudes extends ExtraktionsVorgang{
	
	public ArtDesGebaeudes() {
		this.name="Art des Gebäudes";
	}
	
	private int artDesGebaeudes;

	@Override
	public void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel) {
		
		if (ifcModel != null) {
			
			IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
			if (project == null) return;
			
			if (project instanceof IfcProject.Ifc4) {
				IfcSimpleProperty.Ifc4 simpleProperty = Util.Ifc4.getElementProperty((IfcObjectDefinition.Ifc4) project, "BauantragAllgemein", "Art des Gebäudes");

				if (simpleProperty==null || !(simpleProperty instanceof IfcPropertyEnumeratedValue)) return;
				
				IfcPropertyEnumeratedValue.Ifc4 enumeratedValue = (IfcPropertyEnumeratedValue.Ifc4) simpleProperty;
		         
				
				if (enumeratedValue.getEnumerationValues()!=null && enumeratedValue.getEnumerationValues().size() > 0) {
					IfcValue value = enumeratedValue.getEnumerationValues().get(0);
					
					if(value instanceof IfcPositiveInteger) {
						artDesGebaeudes = ((IfcPositiveInteger.Ifc4)value).getValue();
					}else if(value instanceof IfcInteger) {
						artDesGebaeudes = ((IfcInteger.Ifc4)value).getValue();
					}else if (value instanceof IfcLabel) {
						artDesGebaeudes = Integer.valueOf(((IfcLabel.Ifc4)value).getDecodedValue());
					}
					
					this.value = Integer.toString(artDesGebaeudes);
							
				}
				
				
				
						
				
			}
			
			if (project instanceof IfcProject.Ifc2x3) {
				IfcSimpleProperty.Ifc2x3 simpleProperty = Util.Ifc2x3.getElementProperty((IfcObject.Ifc2x3) project, "BauantragAllgemein", "Art des Gebäudes");

				if (simpleProperty==null || !(simpleProperty instanceof IfcPropertySingleValue)) return;
				
				IfcPropertySingleValue.Ifc2x3 singleValue = (IfcPropertySingleValue.Ifc2x3) simpleProperty;
		         
				IfcValue value = singleValue.getNominalValue();
				
				if (value instanceof IfcLabel) {
					this.value = ((IfcLabel.Ifc2x3)value).getDecodedValue();
				}
					
			}
			
		}
		
	}
	
	public int getArtDesGebaeudes() {
		return artDesGebaeudes;
	}

}
