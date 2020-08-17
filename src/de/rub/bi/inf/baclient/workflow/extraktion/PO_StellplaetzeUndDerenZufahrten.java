package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBoolean;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifc.IfcElementQuantity;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcRelDefines.Ifc2x3;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcSimpleValue;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifc.IfcValue;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.vecmath.Point3d;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;

public class PO_StellplaetzeUndDerenZufahrten extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	double area = 0;
	

	public PO_StellplaetzeUndDerenZufahrten() {
		this.name = "StellplaetzeUndDerenZufahrten";
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
		if(ifcModel != null) {
			
			this.modelNode = ifcModel;

			IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();

			if (project instanceof IfcProject.Ifc4) {
				
				gruppierteBimObjekte.put("PKW", Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class,
						"BauantragStellplätze", "Art", "PKW").stream().collect(Collectors.toSet()));
				
				gruppierteBimObjekte.put("FAHRRAD", Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class,
						"BauantragStellplätze", "Art", "FAHRRAD").stream().collect(Collectors.toSet()));
				
				gruppierteBimObjekte.put("Zufahrten", Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class,
						"BauantragZufahrten", "IstEinbahnstraße", null).stream().collect(Collectors.toSet()));
				
				gruppierteBimObjekte.entrySet().forEach(entry -> bimObjekte.addAll(entry.getValue()));

				bimObjekte.forEach(classInterface -> {
					IfcObject.Ifc4 obj = (IfcObject.Ifc4) classInterface;
					IfcQuantityArea.Ifc4 areaQuantity = (IfcQuantityArea.Ifc4) Util.Ifc4.getElementQuantity(obj,
							IfcQuantityArea.Ifc4.Instance.class, "GrossFloorArea");

					if (areaQuantity != null) {
						area += areaQuantity.getAreaValue().getValue();
					}
				});
				
				
				
			}

			DecimalFormat format = new DecimalFormat("###.##");
			
			value = format.format(area)+" qm";

		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}
	
	public double getArea() {
		return area;
	}

}
