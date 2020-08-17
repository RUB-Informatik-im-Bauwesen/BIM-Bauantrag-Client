package de.rub.bi.inf.baclient.workflow.extraktion;

import java.text.DecimalFormat;
import java.util.Collection;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.vecmath.Point3d;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;

public class PO_HoeheDerBaulichenAnlage extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private double hoeheDerBaulichenAnlage = 0.0;
	private double tiefeDerBaulichenAnlage = 0.0;

	public PO_HoeheDerBaulichenAnlage() {
		this.name = "HoeheDerBaulichenAnlage";
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
	
			
			this.comment = "Wird aus der Gesamthöhe aller IFC-Elemente bestimmt, da für die Höhe baulicher Anlagen (HA) auch Türme, Schornsteine, Antennen usw. mit in die Höhe einbezogen werden. " +
					"Der Bezugspunkt ist wichtig: z.B.  bezogen  auf  Normalnull  (NN)  oder  auf  die  festgesetzte Geländeoberfläche (§18 Absatz1 BauNVO). Es wird derzeit nur Normalnull angenommen.";
			
			if(ifcModel.getStepModel().getFile_SchemaString().equals(IfcSchema.IFC4.toString())) {
				Collection<IfcElement.Ifc4> arr = ifcModel.getStepModel().getCollection(IfcElement.Ifc4.class);
				for(IfcElement.Ifc4 ele : arr) {
					CadObjectJ3D cObj = (CadObjectJ3D)ifcModel.getCadObjectModel().getCadObject(ele);
				
					if(cObj != null) {
						BoundingBox box = cObj.getBoundingBox();
						
						if(box != null) {
							Point3d upper = new Point3d();
							box.getUpper(upper);
							
							if(upper.z > hoeheDerBaulichenAnlage) {					
								hoeheDerBaulichenAnlage = upper.z;
							}
							
							Point3d lower = new Point3d();
							box.getLower(lower);
							
							if(lower.z < tiefeDerBaulichenAnlage) {					
								tiefeDerBaulichenAnlage = lower.z;
							}
						}
					}
				}
			}
			
			if(ifcModel.getStepModel().getFile_SchemaString().equals(IfcSchema.IFC2X3.toString())) {
				Collection<IfcElement.Ifc2x3> arr = ifcModel.getStepModel().getCollection(IfcElement.Ifc2x3.class);
				for(IfcElement.Ifc2x3 ele : arr) {
					CadObjectJ3D cObj = (CadObjectJ3D)ifcModel.getCadObjectModel().getCadObject(ele);
				
					if(cObj != null) {
						BoundingBox box = cObj.getBoundingBox();
						
						if(box != null) {
							Point3d upper = new Point3d();
							box.getUpper(upper);
							
							if(upper.z > hoeheDerBaulichenAnlage) {					
								hoeheDerBaulichenAnlage = upper.z;
							}
							
							Point3d lower = new Point3d();
							box.getLower(lower);
							
							if(lower.z < tiefeDerBaulichenAnlage) {					
								tiefeDerBaulichenAnlage = lower.z;
							}
						}
					}
				}
			}
			
			
			DecimalFormat format = new DecimalFormat("###.##");
			
			this.value = format.format(hoeheDerBaulichenAnlage)+" m";

		}else {
		
			this.value = "";
			this.comment = "nicht kalkuliert";
	
		}
	
	}
	
	public double getHoeheDerBaulichenAnlage() {
		return hoeheDerBaulichenAnlage;
	}
	
	public double getTiefeDerBaulichenAnlage() {
		return tiefeDerBaulichenAnlage;
	}

}
