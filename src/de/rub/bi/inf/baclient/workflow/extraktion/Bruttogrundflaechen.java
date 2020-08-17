package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcPropertyEnumeratedValue;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifc.IfcValue;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;

public class Bruttogrundflaechen extends ExtraktionsVorgang {
	
	private double sum;
	
	public Bruttogrundflaechen() {
		this.name = "Brutto-Grundfläche";
		this.value = "";
		this.comment = "";
	}

	@Override
	public void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel) {
		if(ifcModel != null) {
			
			this.modelNode = ifcModel;
			
			
		    IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
		     
		    if (project instanceof IfcProject.Ifc4) {
		    	Collection<IfcObject.Ifc4> spaces = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragBruttoflächen", "IstBGF", true);
		        this.bimObjekte.addAll(spaces);
		        
		        Set<IfcObject> spacesR = new HashSet<IfcObject>();
		        Set<IfcObject> spacesS = new HashSet<IfcObject>();
		        
		        spaces.forEach(space -> {
		        	 IfcSimpleProperty raumumschließung = Util.Ifc4.getElementProperty(space, "BauantragBruttoflächen", "Raumumschließung");
		        	 
		        	 if (raumumschließung != null && raumumschließung instanceof IfcPropertyEnumeratedValue) {
		        		 IfcPropertyEnumeratedValue.Ifc4 raumumschließungEnum = (IfcPropertyEnumeratedValue.Ifc4) raumumschließung;
		        		 
		        		 for(IfcValue.Ifc4 value: raumumschließungEnum.getEnumerationValues()) {
		        			 
		        			 if(value instanceof IfcLabel.Ifc4) {
		        				 
		        				 IfcLabel.Ifc4 label = (IfcLabel.Ifc4) value;
		        				 
		        				 if (label.getDecodedValue().equals("REGELFALL")) {
		        					 spacesR.add(space);
		        					 
		        				 }else if (label.getDecodedValue().equals("SONDERFALL")) {
									spacesS.add(space);
								}
		        			 }
		        			 
		        		 }
		        	 }
		        	 
		        });
		        
		       
		        gruppierteBimObjekte.put("REGELFALL ("+spacesR.size()+")", spacesR);
		        gruppierteBimObjekte.put("SONDERFALL ("+spacesS.size()+")", spacesS);
		        
		        spaces.forEach(space->{
		        	IfcQuantityArea.Ifc4 areaQuantity = 
		        			(IfcQuantityArea.Ifc4) Util.Ifc4.getElementQuantity(space, IfcQuantityArea.Ifc4.Instance.class, "GrossFloorArea");
		        	
		        	if(areaQuantity !=null) {
		        		System.out.println(areaQuantity.getName()+": "+areaQuantity.getAreaValue().getValue());	
		        		sum+=areaQuantity.getAreaValue().getValue();
		        	}
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
