package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcSpace;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc2x3;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_AnzahlVollgeschosse.Geschossinfo;

public class PO_Geschossflaeche extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	
	
	private PO_AnzahlVollgeschosse po_AnzahlVollgeschosse;
	private double geschossflaeche = 0;

	public PO_Geschossflaeche(PO_AnzahlVollgeschosse po_AnzahlVollgeschosse) {
		this.name = "Geschossflaeche";
		this.value = "";
		this.comment = "";
		this.po_AnzahlVollgeschosse = po_AnzahlVollgeschosse;
	}

	public String getValue() {
		return this.value;
	}
	
	public String getName() {
		return this.name;
	}

	public String getComment() {
		return this.comment;
	}
	
	public void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel) {
		if(ifcModel != null) {
			
			geschossflaeche = 0;
			
		    if(po_AnzahlVollgeschosse!=null) {
		    	
		    	IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
				if (project == null) return;

				if (project instanceof IfcProject.Ifc4) {
					po_AnzahlVollgeschosse.getBimObjekte().forEach(obj ->{
						if (obj instanceof IfcSpace.Ifc4) {
							IfcSpace.Ifc4 space = (IfcSpace.Ifc4) obj;
							IfcQuantityArea.Ifc4 areaQuantity = 
				        			(IfcQuantityArea.Ifc4) Util.Ifc4.getElementQuantity(space, IfcQuantityArea.Ifc4.Instance.class, "GrossFloorArea");
							
							geschossflaeche+=areaQuantity.getAreaValue().getValue();
					
						}
					});
				}else if(project instanceof IfcProject.Ifc2x3) {
					po_AnzahlVollgeschosse.getBimObjekte().forEach(obj ->{
						if (obj instanceof IfcSpace.Ifc2x3) {
							IfcSpace.Ifc2x3 space = (IfcSpace.Ifc2x3) obj;
							IfcQuantityArea.Ifc2x3 areaQuantity = 
				        			(IfcQuantityArea.Ifc2x3) Util.Ifc2x3.getElementQuantity(space, IfcQuantityArea.Ifc2x3.Instance.class, "GrossFloorArea");
							
							geschossflaeche+=areaQuantity.getAreaValue().getValue();
						}
					});
				}
		    	
		    	
				DecimalFormat format = new DecimalFormat("###.##");
				
		    	this.value = format.format(geschossflaeche)+ " qm";
		    	this.comment = "GF";
		    	
		    }
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}
	
	public double getGeschossflaeche() {
		return geschossflaeche;
	}

}
