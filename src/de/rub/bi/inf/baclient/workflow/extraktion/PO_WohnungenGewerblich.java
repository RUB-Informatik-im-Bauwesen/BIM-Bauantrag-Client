package de.rub.bi.inf.baclient.workflow.extraktion;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcIdentifier;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcSpace;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;

public class PO_WohnungenGewerblich extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private TreeMap<String, Set<IfcSpace>> nutzungseinheiten = new TreeMap<>();

	public PO_WohnungenGewerblich() {
		this.name = "WohnungenGewerblich";
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
		    	
		    	Collection<IfcObject.Ifc4> nutzungseinheiten = 
		    			Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragNutzungseinheiten", "Nutzung",
		    					"WOHNEINHEIT_GEWERBLICH");
		        
		    	bimObjekte.addAll(nutzungseinheiten);
		    	
		    	nutzungseinheiten.forEach(ne->{
		    		IfcSimpleProperty simpleProperty = Util.Ifc4.getElementProperty(ne, "BauantragNutzungseinheiten", "Einheit");
		    		
		    		if(simpleProperty==null) { //if not has val for Einheit
		    			Set<IfcSpace> set = this.nutzungseinheiten.get("Not assigned");
	    				if(set == null) {
	    					set = new HashSet<>();
	    					this.nutzungseinheiten.put("Not assigned", set);
	    				}
		    			set.add((IfcSpace) ne);
		    		}
		    		
		    		if(simpleProperty instanceof IfcPropertySingleValue.Ifc4) {
		    			IfcPropertySingleValue.Ifc4 singleValue = (IfcPropertySingleValue.Ifc4) simpleProperty;
		    			
	//	    			System.out.println("Einheit: "+singleValue.getNominalValue().toString());
		    			
		    			if (singleValue.getNominalValue() instanceof IfcIdentifier.Ifc4) {
		    				IfcIdentifier.Ifc4 identifierValue = (IfcIdentifier.Ifc4) singleValue.getNominalValue();
		    				
		    				Set<IfcSpace> set = this.nutzungseinheiten.get(identifierValue.getDecodedValue());
		    				if(set == null) {
		    					set = new HashSet<>();
		    					this.nutzungseinheiten.put(identifierValue.getDecodedValue(), set);
		    				}
		    				set.add((IfcSpace) ne);
		    			}
		    		}
		    	});
		    
		    }
		    else if (project instanceof IfcProject.Ifc2x3) {
		    	
		    	//Not implemented
		    
		    }
		    
		    this.nutzungseinheiten.entrySet().forEach(entry -> {
	    		
	    		String key="";
	    		if(entry.getKey().equals("Not assigned")) {
	    			key = "Keiner Einheit zugeordnet("+entry.getValue().size()+")";
	    		}else{
	    			key = entry.getKey().toString()+"("+entry.getValue().size()+")";
	    		}
		
	    		gruppierteBimObjekte.put(key, 
	    				entry.getValue().stream().map(e -> (IfcObject)e).collect(Collectors.toSet()));
	    	});
	    	
	    		
	    	this.comment="NE / Räume";
	    	this.value=this.nutzungseinheiten.keySet().size()+" / "+
	    			nutzungseinheiten.values().stream().mapToInt(i -> i.size()).sum();
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}

	}
	
	public TreeMap<String, Set<IfcSpace>> getNutzungseinheiten() {
		return nutzungseinheiten;
	}
}
