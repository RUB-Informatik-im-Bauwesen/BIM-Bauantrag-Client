package de.rub.bi.inf.baclient.workflow.extraktion;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.IndexedTriangleArray;
import com.apstex.javax.media.j3d.Shape3D;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Point3f;

import de.rub.bi.inf.baclient.core.geometry.CustomCadObjectJ3D;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.GeometryUtil;
import de.rub.bi.inf.baclient.core.utils.IfcSpaceUtil;
import de.rub.bi.inf.baclient.core.views.XViewer;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class PO_GrundflaechenDerBaulichenAnlage extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private PO_BebauteGrundstuecksflaeche po_BebauteGrundstuecksflaeche = null;
	private PO_FlaecheDerNebenanlagen po_FlaecheDerNebenanlagen = null;
	private PO_StellplaetzeUndDerenZufahrten po_StellplaetzeUndDerenZufahrten = null;
	
	private Transform3D transform3d = new Transform3D();
	
	private double area;

	public PO_GrundflaechenDerBaulichenAnlage(
			PO_BebauteGrundstuecksflaeche po_BebauteGrundstuecksflaeche,
			PO_FlaecheDerNebenanlagen po_FlaecheDerNebenanlagen,
			PO_StellplaetzeUndDerenZufahrten po_StellplaetzeUndDerenZufahrten) {
		
		this.name = "Grundfläche der baulichen Anlage";
		this.value = "";
		this.comment = "";
		
		this.po_BebauteGrundstuecksflaeche = po_BebauteGrundstuecksflaeche;
		this.po_FlaecheDerNebenanlagen = po_FlaecheDerNebenanlagen;
		this.po_StellplaetzeUndDerenZufahrten = po_StellplaetzeUndDerenZufahrten;
		
		transform3d.setIdentity(); //default value
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
			
			IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
			if (project == null) return;
			
			this.modelNode = ifcModel;
			
			
			
			if (project instanceof IfcProject.Ifc4) {
				
				Collection<IfcObject.Ifc4> ifcSpaces = new ArrayList<>();
				
				ifcSpaces.addAll(po_BebauteGrundstuecksflaeche.getBimObjekte().stream().map(e -> (IfcSpace.Ifc4) e)
						.collect(Collectors.toList()));
				
				ifcSpaces.addAll(po_FlaecheDerNebenanlagen.getBimObjekte().stream().map(e -> (IfcSpace.Ifc4) e)
						.collect(Collectors.toList()));
				
				ifcSpaces.addAll(po_StellplaetzeUndDerenZufahrten.getBimObjekte().stream().map(e -> (IfcSpace.Ifc4) e)
						.collect(Collectors.toList()));
				
				
				
				bimObjekte.addAll(ifcSpaces);	
		        
		        ArrayList<Polygon> polygons = new ArrayList<>();
				
				ifcSpaces.forEach(space -> {
	    			Point3f[] coordinates = IfcSpaceUtil.getContourPoints(ifcModel, (IfcSpace) space, true);
	    			polygons.add(GeometryUtil.create2DPolygonFX(coordinates));
				});
				
				
				Shape union = GeometryUtil.union_2D(polygons);

				ArrayList<ArrayList<Point3f>> polylines = GeometryUtil.extract3DPolylineBoundary(union);
				
				CadObjectJ3D gfdba = new CustomCadObjectJ3D();
				Appearance appearance = GeometryUtil.createAppearance(
						Color.getHSBColor(
								(float)Math.random(), 
								0.5f, 
								0.5f)
						);
				
				if(ifcModel.getCadObjectModel().getRootBranchGroup().getTransform()!=null) {
					transform3d = ifcModel.getCadObjectModel().getRootBranchGroup().getTransform();
				}
				
				
				for (ArrayList<Point3f> pl : polylines) {
					
					Point3f[] polyline = new Point3f[pl.size()];
					pl.toArray(polyline);
					
					for(Point3f p: polyline) {
						transform3d.transform(p);
					}
					
					GeometryArray geoArray = GeometryUtil.createPolygon(polyline, true);			
					 
					
					Shape3D shape = new Shape3D(geoArray, appearance);
					
					gfdba.addShape3D(shape);	
					
					area+= GeometryUtil.computeArea((IndexedTriangleArray) geoArray);
				}
				

				ArrayList<CadObjectJ3D> result = new ArrayList<>();
				result.add(gfdba);
				this.erstellteGeometrien.put("Grundfläche der Baulichen Anlage", new ArrayList<>(result));
			
				gfdba.setVisible(false);
				XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().addObject(gfdba);
	        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();	
	        	
	        	DecimalFormat format = new DecimalFormat("###.##");
	        	
				this.value = format.format(area)+" qm";
				
			}
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}
	
	public double getArea() {
		return area;
	}

}
