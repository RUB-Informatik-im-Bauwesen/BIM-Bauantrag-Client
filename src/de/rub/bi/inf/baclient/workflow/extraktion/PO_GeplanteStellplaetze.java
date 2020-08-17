package de.rub.bi.inf.baclient.workflow.extraktion;

import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcCountMeasure;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc2x3;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;

public class PO_GeplanteStellplaetze extends ExtraktionsVorgang {
	
	private PO_StellplaetzeUndDerenZufahrten po_StellplaetzeUndDerenZufahrten;
	private int pkwStellplaetze;
	private int fahrradStellplaetze;
	
	public PO_GeplanteStellplaetze(PO_StellplaetzeUndDerenZufahrten po_StellplaetzeUndDerenZufahrten) {
		this.po_StellplaetzeUndDerenZufahrten = po_StellplaetzeUndDerenZufahrten;
		this.name = "Geplante Stellplätze";
	}

	@Override
	public void perform(ApplicationModelNode applicationModelNode, XPlanungModel xPlanModel) {
		
		if(applicationModelNode != null) {
			this.modelNode = applicationModelNode;
			
			IfcProject project = applicationModelNode.getStepModel().getCollection(IfcProject.class).iterator().next();
			
			if (project instanceof IfcProject.Ifc4) {
				
				Collection<IfcObject> pkwSpaces = po_StellplaetzeUndDerenZufahrten.getGruppierteBimObjekte().get("PKW");
				
				if(pkwSpaces != null) {
					pkwSpaces.forEach(classInterface ->{
			    		IfcObject.Ifc4 obj4 = (IfcObject.Ifc4) classInterface;
			    		IfcPropertySingleValue.Ifc4 parkingProperty = 
			    				(IfcPropertySingleValue.Ifc4) Util.Ifc4.getElementProperty(obj4, "BauantragStellplätze", "Anzahl");
			    		if(parkingProperty!=null) {
			    			
			    			if(parkingProperty.getNominalValue() instanceof IfcCountMeasure.Ifc4){
								
								IfcCountMeasure.Ifc4 countMeasure = 
										(IfcCountMeasure.Ifc4) parkingProperty.getNominalValue();
					
								this.pkwStellplaetze += countMeasure.getValue();
			    			}
			    		}
			    	});
				}
				
				Collection<IfcObject> fahrradSpaces = po_StellplaetzeUndDerenZufahrten.getGruppierteBimObjekte().get("FAHRRAD");
				
				if(fahrradSpaces != null) {
					fahrradSpaces.forEach(classInterface ->{
			    		IfcObject.Ifc4 obj4 = (IfcObject.Ifc4) classInterface;
			    		IfcPropertySingleValue.Ifc4 parkingProperty = 
			    				(IfcPropertySingleValue.Ifc4) Util.Ifc4.getElementProperty(obj4, "BauantragStellplätze", "Anzahl");
			    		if(parkingProperty!=null) {
			    			
			    			if(parkingProperty.getNominalValue() instanceof IfcCountMeasure.Ifc4){
								
								IfcCountMeasure.Ifc4 countMeasure = 
										(IfcCountMeasure.Ifc4) parkingProperty.getNominalValue();
					
								this.fahrradStellplaetze += countMeasure.getValue();
			    			}
			    		}
			    	});
				}
				
		    
		    }
	
			
			this.value = this.pkwStellplaetze+" / " + this.fahrradStellplaetze;
			this.comment = "PKW / FAHRRAD";
			
		} //endif applicationModelNode

	}

	public int getPkwStellplaetze() {
		return pkwStellplaetze;
	}

	public int getFahrradStellplaetze() {
		return fahrradStellplaetze;
	}

}
