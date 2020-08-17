package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcSpace;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;

public class Nettogrundflachen extends ExtraktionsVorgang {
	
	private double sum;
	
	public Nettogrundflachen() {
		this.name = "Netto-Grundfläche";
		this.value = "";
		this.comment = "";
	}

	@Override
	public void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel) {
		if(ifcModel != null) {
			
			this.modelNode = ifcModel;
			
			
		    IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
		     
		    if (project instanceof IfcProject.Ifc4) {
		    	Collection<IfcObject.Ifc4> spaces = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragNettoflächen", "Art", null);
		        this.bimObjekte.addAll(spaces);
		               
		        
		        spaces.forEach(space->{
		        	IfcQuantityArea.Ifc4 areaQuantity = 
		        			(IfcQuantityArea.Ifc4) Util.Ifc4.getElementQuantity(space, IfcQuantityArea.Ifc4.Instance.class, "GrossFloorArea");
		        	
		        	if(areaQuantity !=null) {
		        		System.out.println(areaQuantity.getName()+": "+areaQuantity.getAreaValue().getValue());	
		        		sum+=areaQuantity.getAreaValue().getValue();
		        	}
		        });
		        
		        
		        ArrayList<String> nufs = new ArrayList<>();
		        nufs.add("NUF1");
		        nufs.add("NUF2");
		        nufs.add("NUF3");
		        nufs.add("NUF4");
		        nufs.add("NUF5");
		        nufs.add("NUF6");
		        nufs.add("NUF7");
		        nufs.add("VF");
		        nufs.add("TF");
		        
		        nufs.forEach(nuf -> {
		        	Collection<IfcObject.Ifc4> nufSpaces = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class,
							"BauantragNettoflächen", "Art", nuf);
					if (nufSpaces.size() > 0)
						gruppierteBimObjekte.put(nuf+" (" + nufSpaces.size() + ")", nufSpaces.stream().collect(Collectors.toSet()));
		        });
		        
				
				
				

		    }
		    
		    DecimalFormat format = new DecimalFormat("###.##");
		    
		    value = format.format(sum)+" qm";
				
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}

	}

}
