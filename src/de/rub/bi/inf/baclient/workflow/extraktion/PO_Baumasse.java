package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifc.IfcElementQuantity;
import com.apstex.ifctoolbox.ifc.IfcPhysicalQuantity;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcQuantityVolume;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.vecmath.Point3d;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_AnzahlVollgeschosse.Geschossinfo;

public class PO_Baumasse extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private PO_AnzahlVollgeschosse po_AnzahlVollgeschosse;
	private double baumasse = 0;

	public PO_Baumasse(PO_AnzahlVollgeschosse po_AnzahlVollgeschosse) {
		this.name = "Baumasse";
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
			
			 baumasse = 0;
			
			 if(po_AnzahlVollgeschosse!=null) {
			    	
			    	IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
					if (project == null) return;

					if (project instanceof IfcProject.Ifc4) {
						po_AnzahlVollgeschosse.getBimObjekte().forEach(obj ->{
							if (obj instanceof IfcSpace.Ifc4) {
								IfcSpace.Ifc4 space = (IfcSpace.Ifc4) obj;
							
								IfcQuantityVolume.Ifc4 volumeQuantity = 
					        			(IfcQuantityVolume.Ifc4) Util.Ifc4.getElementQuantity(space, IfcQuantityVolume.Ifc4.Instance.class, "GrossVolume");
								
								if(volumeQuantity!=null)
									baumasse+=volumeQuantity.getVolumeValue().getValue();
							}
						});
					}else if(project instanceof IfcProject.Ifc2x3) {
						po_AnzahlVollgeschosse.getBimObjekte().forEach(obj ->{
							if (obj instanceof IfcSpace.Ifc4) {
								IfcSpace.Ifc4 space = (IfcSpace.Ifc4) obj;
								IfcQuantityArea.Ifc4 areaQuantity = 
					        			(IfcQuantityArea.Ifc4) Util.Ifc4.getElementQuantity(space, IfcQuantityArea.Ifc4.Instance.class, "GrossVolume");
								
								baumasse+=areaQuantity.getAreaValue().getValue();
							}
						});
					}
			    	
			    	
					DecimalFormat format = new DecimalFormat("###.##");
					
			    	this.value = format.format(baumasse)+" cbm";
			    	this.comment = "Berechnete Baumasse";
			    	
			    }
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	
	}
	
	public double getBaumasse() {
		return baumasse;
	}

}
