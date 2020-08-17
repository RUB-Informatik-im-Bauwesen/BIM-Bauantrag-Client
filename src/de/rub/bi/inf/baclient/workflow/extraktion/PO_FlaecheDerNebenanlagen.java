package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcElementQuantity;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifc.IfcRelDefines.Ifc2x3;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;

public class PO_FlaecheDerNebenanlagen extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private double area;
	
	public PO_FlaecheDerNebenanlagen() {
		this.name = "FlaecheDerNebenanlagen";
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
		    	Collection<IfcObject.Ifc4> spaces = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragGrundstücksflächen", "IstNebenanlage", true);
//		        System.out.println(spaces.size()+ " spaces found for Nebenanlagen");
		        this.bimObjekte.addAll(spaces);
		        spaces.forEach(space->{
		        	IfcQuantityArea.Ifc4 areaQuantity = 
		        			(IfcQuantityArea.Ifc4) Util.Ifc4.getElementQuantity(space, IfcQuantityArea.Ifc4.Instance.class, "GrossFloorArea");
		        	
		        	if(areaQuantity !=null) {
		        		System.out.println(areaQuantity.getName()+": "+areaQuantity.getAreaValue().getValue());	
		        		area+=areaQuantity.getAreaValue().getValue();
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
