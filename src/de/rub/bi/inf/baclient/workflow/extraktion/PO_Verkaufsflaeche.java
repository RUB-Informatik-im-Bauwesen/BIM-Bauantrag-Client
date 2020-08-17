package de.rub.bi.inf.baclient.workflow.extraktion;

import java.util.Collection;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.vecmath.Point3d;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;

public class PO_Verkaufsflaeche extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;

	public PO_Verkaufsflaeche() {
		this.name = "Verkaufsflaeche";
		this.value = "";
		this.comment = "";
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
		if(ifcModel != null && xPlanModel != null) {
			
			//TODO erstelle pruefung
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}

}
