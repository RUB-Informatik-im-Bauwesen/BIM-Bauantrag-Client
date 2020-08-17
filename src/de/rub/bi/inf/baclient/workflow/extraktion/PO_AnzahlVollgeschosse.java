package de.rub.bi.inf.baclient.workflow.extraktion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBuilding;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey.Ifc2x3;
import com.apstex.ifctoolbox.ifc.IfcElementQuantity;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcQuantityLength;
import com.apstex.ifctoolbox.ifc.IfcRelDecomposes;
import com.apstex.ifctoolbox.ifc.IfcRelDefines;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;

public class PO_AnzahlVollgeschosse extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private ArrayList<IfcBuildingStorey.Ifc2x3> sortedStoreys= new ArrayList<>();
	Map<String, Geschossinfo> geschossinfos = new HashMap<>();
	
	private int anz_Vollgeschosse=0;

	public PO_AnzahlVollgeschosse() {
		this.name = "AnzahlVollgeschosse";
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
	

	private class SortByHeight implements Comparator<Object> 
	{ 
		private ApplicationModelNode ifcModel;
		
		public SortByHeight(ApplicationModelNode ifcModel){
			this.ifcModel = ifcModel;
		}
		
	    // Used for sorting in ascending order of 
	    // roll number 
	    public int compare(Object a, Object b) 
	    {
	    	CadObjectJ3D cObjA = (CadObjectJ3D)ifcModel.getCadObjectModel().getCadObject((ClassInterface) a);
	    	CadObjectJ3D cObjB = (CadObjectJ3D)ifcModel.getCadObjectModel().getCadObject((ClassInterface) b);
	    	
	    	if(cObjA != null && cObjB != null) {
				BoundingBox boxA = cObjA.getBoundingBox();
				BoundingBox boxB = cObjB.getBoundingBox();
				
				if(boxA != null && boxB != null) {
					Point3d lowerA = new Point3d();
					boxA.getLower(lowerA);
					
					Point3d lowerB = new Point3d();
					boxB.getLower(lowerB);
					
					double comp = lowerA.z - lowerB.z;
					if(comp < 0) {
						return -1;
					}
					
					if(comp > 0) {
						return 1;
					}
				}
			}

			return 0;
	    } 
	}
	
	class Geschossinfo{
		
		IfcSpace.Ifc2x3 GF_space = null;
		IfcElementQuantity.Ifc2x3 EF_elementQuantity = null;
		Double gf = null;
		
		IfcSpace.Ifc2x3 IGF_space = null;
		IfcElementQuantity.Ifc2x3 IGF_elementQuantity = null;
		Double igf = null;
		Double lichteHoehe = null;
		
		boolean istOberirdisch = false;
		boolean istVollgeschoss = false;
		boolean istKeinGeschoss = false;
	}

	public void perform(ApplicationModelNode applicationModelNode, XPlanungModel xPlanModel) {
		
		if(applicationModelNode != null) {
			
            this.modelNode = applicationModelNode;
            
            
        	IfcProject project = applicationModelNode.getStepModel().getCollection(IfcProject.class).iterator().next();
			if (project == null) return;
			
			
			if (project instanceof IfcProject.Ifc4) {
				Collection<IfcObject.Ifc4> spaces = Util.Ifc4.getObjectsByProperty(
						applicationModelNode, IfcSpace.Ifc4.class, "BauantragGeschoss", "IstVollgeschoss", true);
				
				bimObjekte.addAll(spaces);
				anz_Vollgeschosse = spaces.size();
			}
			
			if(project instanceof IfcProject.Ifc2x3) {
				Collection<IfcObject.Ifc2x3> spaces = Util.Ifc2x3.getObjectsByProperty(
						applicationModelNode, IfcSpace.Ifc2x3.class, "BauantragGeschoss", "IstVollgeschoss", true);
				
				bimObjekte.addAll(spaces);
				anz_Vollgeschosse = spaces.size();
			}
	
			this.value = Integer.toString(anz_Vollgeschosse);
			

		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}
		
	}
	
	
    
    public ArrayList<IfcBuildingStorey.Ifc2x3> getSortedStoreys() {
		return sortedStoreys;
	}
    
    public Map<String, Geschossinfo> getGeschossinfos() {
		return geschossinfos;
	}
    
    
    

	public static double getAreaSizeIfc2x3(ApplicationModelNode model, IfcBuildingStorey.Ifc2x3 storey, double height, String spaceType) {
		
		double tempSizeUpper = 0.0;
		double tempSizeLower = 0.0;
		double differ = 1.0;
	    if(storey.getIsDecomposedBy_Inverse() != null) {
	    	for(IfcRelDecomposes.Ifc2x3 rel : storey.getIsDecomposedBy_Inverse()) {
		    	for(Object ele : rel.getRelatedObjects()) {
		    		if(ele instanceof IfcSpace.Ifc2x3) {
		    			if(((IfcSpace.Ifc2x3)ele).getName().getDecodedValue().equals(spaceType)) {
		    				CadObjectJ3D objectJ3D = (CadObjectJ3D)model.getCadObjectModel().getCadObject((ClassInterface) ele);

		    				for(MultiAppearanceShape3D shape : objectJ3D.getSolidShapes()) {
		    					Point3d lower = new Point3d();
		    					Point3d upper = new Point3d();
		    					
		    					objectJ3D.getBoundingBox().getLower(lower);
		    					objectJ3D.getBoundingBox().getUpper(upper);
		    					
		    					differ = upper.z - lower.z;
		    					if(differ < 0) {
		    						differ = -1.0 * differ;
		    					}
		    					
		    					GeometryInfo info = new GeometryInfo(((GeometryArray)shape.getGeometry()));
		    					info.indexify(true);
		    					info.compact();	
		    					
		    					Point3f[] points = info.getCoordinates();
		    					int[] indice = info.getCoordinateIndices();
		    					
		    					for(int j = 0; j < indice.length; j += 3 ) {
		    						Point3f pA = points[indice[j]];
		    						Point3f pB = points[indice[j + 1]];
		    						Point3f pC = points[indice[j + 2]];
		    						
		    						float d1 = pA.distance(pB);
		    						float d2 = pA.distance(pC);
		    						
		    						if((pA.z == pB.z) && (pB.z == pC.z) && (pC.z == new Double(upper.z).floatValue())) {
		    							tempSizeUpper += (d1 * d2) / 2.0f;
		    						}
		    						
		    						if((pA.z == pB.z) && (pB.z == pC.z) && (pC.z == new Double(lower.z).floatValue())) {
		    							tempSizeLower += (d1 * d2) / 2.0f;
		    						}
		    						
		    					}	    					
		    				}
		    			}
		    		}
		    	}
		    }
	    }
	    
	    return ((height / differ) * tempSizeLower + ((differ - height) / differ) * tempSizeUpper);
	}
	
	
	public int getAnz_Vollgeschosse() {
		return anz_Vollgeschosse;
	}
}
