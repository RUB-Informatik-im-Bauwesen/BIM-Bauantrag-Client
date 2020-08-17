package de.rub.bi.inf.baclient.workflow.extraktion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.step.core.ClassInterface;
import com.apstex.step.model.StepModel;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;

public abstract class ExtraktionsVorgang {
	
	protected String name;
	protected String value;
	protected String comment;
	
	protected ApplicationModelNode modelNode;
	public ApplicationModelNode getModelNode() {
		return modelNode;
	}

	
	protected Set<ClassInterface> bimObjekte = new HashSet<ClassInterface>();
	public Set<ClassInterface> getBimObjekte() {
		return bimObjekte;
	}
	
	protected TreeMap<String, Set<IfcObject>> gruppierteBimObjekte = new TreeMap<>();
	public TreeMap<String, Set<IfcObject>> getGruppierteBimObjekte() {
		return gruppierteBimObjekte;
	}
	
	
	protected HashMap<String, ArrayList<CadObjectJ3D>> erstellteGeometrien = new HashMap<>();
	public HashMap<String, ArrayList<CadObjectJ3D>> getErstellteGeometrien() {
		return erstellteGeometrien;
	}
	
	
	public ExtraktionsVorgang() {
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
	
	public abstract void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel);

}
