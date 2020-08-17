package de.rub.bi.inf.baclient.workflow.extraktion;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcGeometricCurveSet;
import com.apstex.ifctoolbox.ifc.IfcLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcPolyline;
import com.apstex.ifctoolbox.ifc.IfcProductRepresentation;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.IndexedTriangleArray;
import com.apstex.javax.media.j3d.LineAttributes;
import com.apstex.javax.media.j3d.Material;
import com.apstex.javax.media.j3d.PolygonAttributes;
import com.apstex.javax.media.j3d.Shape3D;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.step.core.ClassInterface;
import com.apstex.step.core.LIST;

import de.rub.bi.inf.baclient.core.geometry.CustomCadObjectJ3D;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.GeometryUtil;
import de.rub.bi.inf.baclient.core.utils.IfcSpaceUtil;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;
import de.rub.bi.inf.baclient.core.views.XViewer;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class PO_BebauteGrundstuecksflaeche extends ExtraktionsVorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private CadObjectJ3D bebauteFlaeche;
	private double area;
	private Transform3D transform3d = new Transform3D();
	

	public PO_BebauteGrundstuecksflaeche() {
		this.name = "BebauteGrundstuecksflaeche";
		this.value = "";
		this.comment = "";
		transform3d.setIdentity();
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
	
	public CadObjectJ3D getBebauteFlaeche() {
		return bebauteFlaeche;
	}
	
	public void perform(ApplicationModelNode ifcModel, XPlanungModel xPlanModel) {
		if(ifcModel != null) {
			
				IfcProject project = ifcModel.getStepModel().getCollection(IfcProject.class).iterator().next();
				if (project == null) return;
				
				this.modelNode = ifcModel;
				
				if (project instanceof IfcProject.Ifc4) {
					Collection<IfcObject.Ifc4> ifcSpaces = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragGeschoss", "IstGF", true);
					
					//Unterbaute Fläche detektieren
					Collection<IfcObject.Ifc4> vollgeschosse = Util.Ifc4.getObjectsByProperty(ifcModel, IfcSpace.Ifc4.class, "BauantragGeschoss", "IstVollgeschoss", true);
					double lowestZ = Double.MAX_VALUE;
					for (IfcObject.Ifc4 obj : vollgeschosse) {
						CadObjectJ3D cObjA = (CadObjectJ3D)ifcModel.getCadObjectModel().getCadObject((ClassInterface) obj);
						Point3d lower = new Point3d();
						cObjA.getBoundingBox().getLower(lower);
						lowestZ = lower.getZ();						
					}
					
					Collection<IfcObject.Ifc4> belowSpaces = new HashSet<>();
					for (IfcObject.Ifc4 obj : ifcSpaces) {
						CadObjectJ3D cObjA = (CadObjectJ3D)ifcModel.getCadObjectModel().getCadObject((ClassInterface) obj);
						Point3d lower = new Point3d();
						cObjA.getBoundingBox().getLower(lower);
						if(lower.getZ() < lowestZ) {
							belowSpaces.add(obj);
//							System.out.println(obj.getStepLine());
						}
					}
					
					Collection<IfcObject.Ifc4> aboveSpaces = new HashSet<>();
					aboveSpaces.addAll(ifcSpaces);
					aboveSpaces.removeAll(belowSpaces);
					
					
					
					this.bimObjekte.addAll(ifcSpaces);
			        
				
					
			        bebauteFlaeche = create2DprojectedArea(ifcSpaces);				
					ArrayList<CadObjectJ3D> result = new ArrayList<>();
					result.add(bebauteFlaeche);
					this.erstellteGeometrien.put("Bebaute Fläche", new ArrayList<>(result));
					bebauteFlaeche.getSolidShapes().forEach(shape -> {
						if(shape.getGeometry() instanceof IndexedTriangleArray) {
							area+= GeometryUtil.computeArea((IndexedTriangleArray) shape.getGeometry());
						}
					});
					
					bebauteFlaeche.setVisible(false);
					XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().addObject(bebauteFlaeche);
		        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
					
					CadObjectJ3D ueberbauteFlaeche = create2DprojectedArea(aboveSpaces);
					result = new ArrayList<>();
					result.add(ueberbauteFlaeche);
					this.erstellteGeometrien.put("Überbaute Fläche", new ArrayList<>(result));
					
					ueberbauteFlaeche.setVisible(false);
					XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().addObject(ueberbauteFlaeche);
		        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
		        	
		        	
		        	CadObjectJ3D unterbauteFlaeche = create2DprojectedArea(belowSpaces);
					result = new ArrayList<>();
					result.add(unterbauteFlaeche);
					this.erstellteGeometrien.put("Unterbaute Fläche", new ArrayList<>(result));
					
					unterbauteFlaeche.setVisible(false);
					XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().addObject(unterbauteFlaeche);
		        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
					
					
				
		        	
		        	
		        	DecimalFormat format = new DecimalFormat("###.##");
		        	
					this.value = format.format(area)+" qm";
					
				}
			
		}else {
			
			this.value = "";
			this.comment = "nicht kalkuliert";
		
		}	

	}
	
	private CadObjectJ3D create2DprojectedArea(Collection<IfcObject.Ifc4> ifcSpaces) {
		
		ArrayList<Polygon> polygons = new ArrayList<>();
		
		ifcSpaces.forEach(space -> {
			Point3f[] coordinates = IfcSpaceUtil.getContourPoints(this.modelNode, (IfcSpace) space, true);
			polygons.add(GeometryUtil.create2DPolygonFX(coordinates));
		});
		
		
		Shape union = GeometryUtil.union_2D(polygons);
		
		ArrayList<ArrayList<Point3f>> polylines = GeometryUtil.extract3DPolylineBoundary(union);				
		CadObjectJ3D ergebnisflaeche = new CustomCadObjectJ3D();
		
		if(this.modelNode.getCadObjectModel().getRootBranchGroup().getTransform()!=null) {
			transform3d = this.modelNode.getCadObjectModel().getRootBranchGroup().getTransform();
		}
		
		
		polylines.forEach(pl -> {
			Point3f[] polyline = new Point3f[pl.size()];
			pl.toArray(polyline);
			
			for(Point3f p: polyline) {
				transform3d.transform(p);
			}
			
			IndexedTriangleArray geoArray = (IndexedTriangleArray) GeometryUtil.createPolygon(polyline, true);			
			 
			
			Shape3D shape = new Shape3D(geoArray, GeometryUtil.createAppearance(
					Color.getHSBColor(
							(float)Math.random(), 
							0.5f, 
							0.5f)
					));
			
			ergebnisflaeche.addShape3D(shape);	
		});
		
		
		return ergebnisflaeche;
		
	}
	
	
	private void areaByProductRepresentation(IfcProductRepresentation.Ifc4 representation) {
		representation.getRepresentations().forEach(rep -> {
		    
			if(rep.getRepresentationIdentifier().getDecodedValue().equals("FootPrint")) {
				
				rep.getItems().forEach(item ->{
					if(item instanceof IfcGeometricCurveSet.Ifc4) {
						IfcGeometricCurveSet geometricCurveSet = (IfcGeometricCurveSet) item;
						geometricCurveSet.getElements().forEach(elem -> {
							if(elem instanceof IfcPolyline) {
								IfcPolyline polyline = (IfcPolyline) elem;
							
								
								ArrayList<Point3f> coordinateList = new ArrayList<>();
								ArrayList<Integer> stripCountList = new ArrayList<>();
								
								polyline.getPoints().forEach(point ->{
									System.out.println(point.toString());
									
									LIST<? extends IfcLengthMeasure> ifcCoordinates = point.getCoordinates();
									coordinateList.add(new Point3f(
											(float)ifcCoordinates.get(0).getValue(), 
											(float)ifcCoordinates.get(1).getValue(), 
											0.f));
									
									
								});

								GeometryInfo geometryInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
											
								Point3f[] coordinates = new Point3f[coordinateList.size()];
								coordinateList.toArray(coordinates);
								geometryInfo.setCoordinates(coordinates);
			
								
								stripCountList.add(coordinateList.size());
								System.out.println(
										Arrays.toString(
										stripCountList.stream().mapToInt(Integer::intValue).toArray())
												);
								geometryInfo.setStripCounts(stripCountList.stream().mapToInt(Integer::intValue).toArray());
//								geometryInfo.setStripCounts(new int[] {3});
								geometryInfo.setContourCounts(new int[] {1});
								geometryInfo.indexify();
								geometryInfo.convertToIndexedTriangles();
								
								
//								GeometryInfo geometryInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
//						    	geometryInfo.setCoordinates(coordinates);
//						    	geometryInfo.setCoordinateIndices(triangle1);
//						    	geometryInfo.setStripCounts(strips);
//						    	geometryInfo.indexify();
//						    	geometryInfo.convertToIndexedTriangles();
								
								GeometryArray geoArray = geometryInfo.getGeometryArray();
								geoArray.setCapability(GeometryArray.ALLOW_COLOR_READ);
								geoArray.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
								geoArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
								geoArray.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
								geoArray.setCapability(GeometryArray.ALLOW_NORMAL_READ);
								//sadjustCapabilities(geoArray);
								
								PolygonAttributes p = new PolygonAttributes();
								p.setCullFace(PolygonAttributes.CULL_NONE); // Renders both sites of the face
								
								Appearance appearance = new Appearance();
								appearance.setPolygonAttributes(p);
								appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
								appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
								ColoringAttributes planeCA = new ColoringAttributes(new Color3f(0.5f, 0.2f, 0.2f), 1);
								appearance.setColoringAttributes(planeCA);
								
								
								Shape3D shape = new Shape3D(geoArray, GeometryUtil.createAppearance(Color.RED));
								CadObjectJ3D bebauteFlaeche = new CustomCadObjectJ3D();
								bebauteFlaeche.addShape3D(shape);	
								
								//this.regelObjekte.add(abstandsfleache);

					        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().addObject(bebauteFlaeche);
					        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
								
							}else{
								System.out.println("No Polyline: "+elem.toString());
							}
						});
					}
				});
				
				
			}
			
			
		});
	}
	
	public double getArea() {
		return area;
	}
	 



	 
   
		
	


}
