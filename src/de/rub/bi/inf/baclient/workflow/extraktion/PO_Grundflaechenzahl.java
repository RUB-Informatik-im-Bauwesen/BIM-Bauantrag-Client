package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcPhysicalQuantity;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.vecmath.Point3d;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;

public class PO_Grundflaechenzahl extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private PO_BebauteGrundstuecksflaeche po_BebauteGrundstuecksflaeche;
	private double ratio;

	public PO_Grundflaechenzahl(PO_BebauteGrundstuecksflaeche po_BebauteGrundstuecksflaeche) {
		this.name = "Grundflaechenzahl";
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
							
							ratio = po_BebauteGrundstuecksflaeche.getArea()/quantityArea.getAreaValue().getValue();
							
							DecimalFormat format = new DecimalFormat("#.##");
				  
							value = format.format(ratio);
						}
					
					}
				}
			}
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}

	public double getRatio() {
		return ratio;
	}

}
