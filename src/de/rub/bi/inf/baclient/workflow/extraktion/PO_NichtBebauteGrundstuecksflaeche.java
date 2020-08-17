package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcPhysicalQuantity;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcSpace;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;

public class PO_NichtBebauteGrundstuecksflaeche extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private PO_BebauteGrundstuecksflaeche po_BebauteGrundstuecksflaeche;
	private double area;

	public PO_NichtBebauteGrundstuecksflaeche(PO_BebauteGrundstuecksflaeche po_BebauteGrundstuecksflaeche) {
		this.name = "NichtBebauteGrundstuecksflaeche";
		this.value = "";
		this.comment = "";
		
		this.po_BebauteGrundstuecksflaeche = po_BebauteGrundstuecksflaeche;
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
			
			IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
			if (project == null) return;
			
			this.modelNode = ifcModel;
			
			if (project instanceof IfcProject.Ifc4) {
				Collection<IfcObject.Ifc4> grundstueckColl = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragGrundstück", "IstGrundstücksfläche", true);
				if(grundstueckColl.size()==1) {
					IfcSpace.Ifc4 grundstueck = (IfcSpace.Ifc4) grundstueckColl.iterator().next();
					IfcPhysicalQuantity.Ifc4 quantity = Util.Ifc4.getElementQuantity(grundstueck, "Qto_SpaceBaseQuantities", "GrossFloorArea");
					
					//Test if Archicad Alias
					if (quantity==null) {						
						quantity = Util.Ifc4.getElementQuantity(grundstueck, "BaseQuantities", "GrossFloorArea");
					}
					
					if (quantity!=null) {
						
						if(quantity instanceof IfcQuantityArea.Ifc4) {
							IfcQuantityArea.Ifc4 quantityArea = (IfcQuantityArea.Ifc4) quantity;
							
							area = quantityArea.getAreaValue().getValue()-po_BebauteGrundstuecksflaeche.getArea();
							
							DecimalFormat format = new DecimalFormat("###.##");
							
							value = format.format(area)+" qm";
						}
					
					}
				}
			}
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}
	
	public double getArea() {
		return area;
	}

}
