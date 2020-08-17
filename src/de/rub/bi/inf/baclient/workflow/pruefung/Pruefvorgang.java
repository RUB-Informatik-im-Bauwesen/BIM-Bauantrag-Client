package de.rub.bi.inf.baclient.workflow.pruefung;

import java.util.ArrayList;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import net.opengis.gml._3.AbstractFeatureType;

public abstract class Pruefvorgang {
	
	protected String name;
	protected String value;
	protected String comment;
	
	protected ApplicationModelNode modelNode;
	public ApplicationModelNode getModelNode() {
		return modelNode;
	}
	
	protected XPlanungModel xPlanungModel;
	public void setxPlanungModel(XPlanungModel xPlanungModel) {
		this.xPlanungModel = xPlanungModel;
	}
	public XPlanungModel getxPlanungModel() {
		return xPlanungModel;
	}


	protected ArrayList<CadObjectJ3D> regelObjekte;
	
	public ArrayList<CadObjectJ3D> getRegelObjekte() {
		return regelObjekte;
	}
	
	public Pruefvorgang() {
		this.name = "GenerischesPrüfungsObject";
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
	
	public abstract void pruefe(ApplicationModelNode ifcModel, AbstractFeatureType bpBereich);

}
