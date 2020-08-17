package de.rub.bi.inf.baclient.workflow.pruefung;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.ifctoolbox.ifc.IfcBoolean;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySetDefinition;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcRelDefines;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifc.IfcSpace.Ifc2x3;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.Geometry;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.IndexedGeometryArray;
import com.apstex.javax.media.j3d.LineAttributes;
import com.apstex.javax.media.j3d.Material;
import com.apstex.javax.media.j3d.PolygonAttributes;
import com.apstex.javax.media.j3d.RenderingAttributes;
import com.apstex.javax.media.j3d.Shape3D;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.javax.vecmath.Vector3f;
import com.apstex.step.core.SET;

import de.rub.bi.inf.baclient.core.geometry.CustomCadObjectJ3D;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.views.XViewer;
import net.opengis.gml._3.AbstractFeatureType;

public class RO_Abstandsfleachen extends Pruefvorgang{
	
	protected String name;
	protected String value;
	protected String comment;
	
	private Transform3D transform3d = new Transform3D();
	
	private ArrayList<IfcSpace> sortedSpaces;
	
	public RO_Abstandsfleachen() {
		this.name = "Abstandsflächen";
		this.value = "";
		this.comment = "";
		
		this.sortedSpaces = new ArrayList<>();
		this.regelObjekte = new ArrayList<>();
		
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
	
	public void pruefe(ApplicationModelNode applicationModelNode, AbstractFeatureType bpBereich) {
		
		if(applicationModelNode != null) {
			
			if(applicationModelNode.getCadObjectModel().getRootBranchGroup().getTransform()!=null) {
				transform3d = applicationModelNode.getCadObjectModel().getRootBranchGroup().getTransform();
			}
			
            this.modelNode = applicationModelNode;
	
			this.comment = "";
			
			CadObjectJ3D abstandsfleache = new CustomCadObjectJ3D();
			abstandsfleache.setName("Abstandsfleache");
    		
			
			if(applicationModelNode.getStepModel().getFile_SchemaString().equals(IfcSchema.IFC4.toString())) {
				
				Collection<IfcObject.Ifc4> ifcSpaces = Util.Ifc4.getObjectsByProperty(applicationModelNode, IfcSpace.Ifc4.class, "BauantragGeschoss", "IstGF", true);
				sortedSpaces.addAll(ifcSpaces.stream().map(IfcSpace.Ifc4.class::cast).collect(Collectors.toList()));

				//Geschossparameter ermitteln
				
				for(int i = 0; i < sortedSpaces.size(); i++) {
					
					IfcSpace.Ifc4 space = (IfcSpace.Ifc4) sortedSpaces.get(i);
			
					CadObject cadObject = applicationModelNode.getCadObjectModel().getCadObject(space);
					if(cadObject instanceof CadObjectJ3D) {
						System.out.println("Erstelle Abstandsfleachen fuer Space: " + space.getName().getDecodedValue());
						CadObjectJ3D spaceJ3D = (CadObjectJ3D)cadObject;
						pruefeStart(space.getName().getDecodedValue(), spaceJ3D, abstandsfleache);
					}
					
				} //end for	
				
			}
			
			this.regelObjekte.add(abstandsfleache);
			abstandsfleache.setVisible(false);
        	XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().addObject(abstandsfleache);
			XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
			
			this.value = this.regelObjekte.size() + " objekte vorhanden";
			this.comment = "Erstellt";

		}else {
			
			this.value = "";
			this.comment = "nicht erstellt";
		
		}
		
	}
	
    public ArrayList<IfcSpace> getSortedSpaces() {
		return sortedSpaces;
	}
    
    
    private void pruefeStart(String name, CadObjectJ3D space, CadObjectJ3D abstandsfleache) {
    	
    	double distance = 0.4;
    	
    	for(MultiAppearanceShape3D mShape : space.getSolidShapes()) {

    		ArrayList<Point3f[]> polygon = new ArrayList<>();
			Float lowerBoundry = new Float(Float.MAX_VALUE);
			Float upperBoundry = new Float(Float.MIN_VALUE);
			Point3f[] points = null;
    		
    		if(mShape.getGeometry() instanceof GeometryArray) {
    			GeometryInfo geometryInfo = new GeometryInfo((GeometryArray)mShape.getGeometry());
    			IndexedGeometryArray indexedGeoArr = geometryInfo.getIndexedGeometryArray(true);
    			
    			points = geometryInfo.getCoordinates();
    			
    			//calculate lower bounds
    			for(Point3f point : points) {
    				if(point.getZ() < lowerBoundry) {
    					lowerBoundry = point.getZ();
    				}
    				if(point.getZ() > upperBoundry) {
    					upperBoundry = point.getZ();
    				}
    			}
    			//System.out.println("LowerBoundry is " + lowerBoundry.toString());
    			
    			int[] triangles = new int[indexedGeoArr.getValidIndexCount()];
    			indexedGeoArr.getCoordinateIndices(0, triangles);
    			
    			ArrayList<int[]> conturTriangles = getConturTriangles(points, triangles, lowerBoundry);
    			ArrayList<int[]> conturEdges = getEdges(conturTriangles);
    			
    			for (int[] edge : conturEdges) {
    				
    				Point3f[] poly = new Point3f[2];
    				poly[0] = new Point3f(
    			    		points[edge[0]].x,
    			    		points[edge[0]].y, 
    			    		0.0f
    			    );
    			    
    				poly[1] = new Point3f(
    			    		points[edge[1]].x,
    			    		points[edge[1]].y, 
    			    		0.0f
    			    ); 

    				polygon.add(poly);
    			}
    			
    		}
    		
    		//Check
    		if((upperBoundry + lowerBoundry) > 0.0) {
    			
    			float tiefe = (float)(distance * upperBoundry); // distance = 0.4, upperBoundry = H
    			if(tiefe < 3.0f) {
    				tiefe = 3.0f; // Tiefe der Abstandsflächen beträgt 0.4 H, mindestens 3 m
    			}
    			
    			for(Point3f[] pArray: polygon) {
    				for (Point3f p : pArray) {    					
    					transform3d.transform(p);
    				}
				}
   			
    			
    			erstelle_flaechen_new(abstandsfleache, polygon, tiefe);
    		}
		
    	}
    }
    
    private Material createMatrial(Color color) {
    	Material material = new Material();
    	material.setDiffuseColor(new Color3f(color));
    	material.setAmbientColor(new Color3f(color));
    	return material;
    }
    
    private Appearance createAppearance() {
    	Appearance appearance = new Appearance();
    	appearance.setMaterial(createMatrial(Color.RED));
    	appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
		
		LineAttributes lineAttributes = new LineAttributes();
		lineAttributes.setLinePattern(LineAttributes.PATTERN_SOLID);
		lineAttributes.setLineWidth(5.0f);
		lineAttributes.setLineAntialiasingEnable(true);
		appearance.setLineAttributes(lineAttributes);
		
		PolygonAttributes polygonAttributes = new PolygonAttributes();
		polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
		polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		appearance.setPolygonAttributes(polygonAttributes);
		
    	ColoringAttributes coloringAttributes = new ColoringAttributes();
    	coloringAttributes.setColor(new Color3f(Color.RED));
    	appearance.setColoringAttributes(coloringAttributes);
    	    	
    	return appearance;
    }
    
    private void erstelle_flaechen_new(CadObjectJ3D objectJ3D, ArrayList<Point3f[]> polygon, float height) {
    	
    	Area cuttingShape = createCuttingShape(polygon);
    	for(Point3f[] edge : polygon) {
    		Shape3D shape = new Shape3D(createArea(edge, height, cuttingShape), createAppearance());
    		objectJ3D.addShape3D(shape);
    	}
    	
    }
    
    private Area createCuttingShape(ArrayList<Point3f[]> polygon) {

    	//TODO
    	
    	Area area = new Area();
    	return area;
    }
    
    public Geometry createArea(Point3f[] edge, float distance, Area cuttingShape) {
    	
    	Point3f startP = edge[0];
    	Point3f endP = edge[1];
    	
    	Vector3f zAchsenVector = new Vector3f(0, 0, 1);
    	Vector3f direction = new Vector3f(endP.x, endP.y, endP.z);
    	direction.sub(startP);
    	
        //var normDir = pNormalize(direction);
    	Vector3f normale = new Vector3f();
    	normale.cross(direction, zAchsenVector);
    	normale.normalize();
    			
    	Vector3f scaledNormal = new Vector3f(normale);
    	scaledNormal.scale(-1.0f);
    	scaledNormal.scale(distance);
    	
    	float xE1 = startP.x + scaledNormal.x;
    	float yE1 = startP.y + scaledNormal.y;
    	float zE1 = 0.0f;

    	float xE2 = endP.x + scaledNormal.x;
    	float yE2 = endP.y + scaledNormal.y;
    	float zE2 = 0.0f;
    	
    	Point3f[] coordinates = new Point3f[4];
    	
    	coordinates[0] = new Point3f(startP.x, startP.y, 0.0f);
    	coordinates[1] = new Point3f(endP.x, endP.y, 0.0f);
    	coordinates[2] = new Point3f(xE1, yE1, 0.0f);
    	coordinates[3] = new Point3f(xE2, yE2, 0.0f);
    	
    	

    	//=============CUTTING GEOMETRY================
    	//=============================================
    	/*
    	ArrayList<Vertex> vertexs = new ArrayList<>();
    	vertexs.add(new Vertex(new Vector(startP.x, startP.y, 0.0f), new Vector(endP.x, endP.y, 0.0f)));
    	vertexs.add(new Vertex(new Vector(endP.x, endP.y, 0.0f), new Vector(xE1, yE1, 0.0f)));
    	vertexs.add(new Vertex(new Vector(xE1, yE1, 0.0f), new Vector(xE2, yE2, 0.0f)));
    	vertexs.add(new Vertex(new Vector(xE2, yE2, 0.0f), new Vector(startP.x, startP.y, 0.0f)));
    	
    	Polygon polygon = new Polygon(vertexs);
    	Area area = new Area((Shape)polygon);
    	
    	area.subtract(cuttingShape);
    	
    	PathIterator iterator = area.getPathIterator(new AffineTransform());
    	
    	ArrayList<Point3f[]> coordinateArr = new ArrayList<>();
    	Point3f[] coords = new Point3f[4];
    	while(!iterator.isDone()) {
    	
    		switch(iterator.getWindingRule()) {
    		case PathIterator.SEG_LINETO : 
    			float[] vals = new float[6];
    			iterator.currentSegment(vals);
    			coords[0] = vals[] break;
    		case PathIterator.SEG_MOVETO : break;
    		case PathIterator.SEG_QUADTO : break;
    		case PathIterator.SEG_CUBICTO : break;
    		case PathIterator.SEG_CLOSE : break;
    		default : break;
    		}
    		
    		iterator.next();
    	}
    	*/
    	//=============================================
    	
    	int[] triangle1 = new int[6];
    	triangle1[0] = 0;
    	triangle1[1] = 1;
    	triangle1[2] = 2;
    	triangle1[3] = 1;
    	triangle1[4] = 3;
    	triangle1[5] = 2;
    	
    	int[] strips = {3, 3};
    	
    	GeometryInfo geometryInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
    	geometryInfo.setCoordinates(coordinates);
    	geometryInfo.setCoordinateIndices(triangle1);
    	geometryInfo.setStripCounts(strips);
    	geometryInfo.indexify();
    	geometryInfo.convertToIndexedTriangles();
    	    	
    	GeometryArray geometryArray = geometryInfo.getGeometryArray();
    	
		geometryArray.setCapability(GeometryArray.ALLOW_COLOR_READ);
		geometryArray.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
		geometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		geometryArray.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
		geometryArray.setCapability(GeometryArray.ALLOW_NORMAL_READ);
    
    	return geometryArray;
    }
    
    private ArrayList<int[]> getConturTriangles(Point3f[] points, int[] triangles, float lowerBoundry){
    	ArrayList<int[]> conturTriangles = new ArrayList<>();
		for(int t = 0; t < triangles.length - 2; t += 3) {
			
			//System.out.println("Triangle: " + triangles[t] + ", " + triangles[t + 1] + ", " + triangles[t + 2]);
			//System.out.println("Triangle Points: " + points[triangles[t]].toString() + ", " + points[triangles[t + 1]].toString() + ", " + points[triangles[t + 2]].toString());
			
			Point3f pA = points[triangles[t]];
			Point3f pB = points[triangles[t + 1]];
			Point3f pC = points[triangles[t + 2]];
			
			if(pA.getZ() == lowerBoundry && pB.getZ() == lowerBoundry && pC.getZ() == lowerBoundry) {
				int[] conTri = new int[3];
				conTri[0] = triangles[t];
				conTri[1] = triangles[t + 1];
				conTri[2] = triangles[t + 2];
				
				conturTriangles.add(conTri);
			}
			
		}
		return conturTriangles;
    }

    private ArrayList<int[]> getEdges(ArrayList<int[]> conturTriangles) {
    	
    	HashMap<String, Integer> edgeMap = new HashMap<>();
    	for(int[] triangle : conturTriangles) {
    		String keyA = "E[" + triangle[0] + "," + triangle[1] + "]";
    		String keyAi = "E[" + triangle[1] + "," + triangle[0] + "]";
        	
    		String keyB = "E[" + triangle[1] + "," + triangle[2] + "]";
    		String keyBi = "E[" + triangle[2] + "," + triangle[1] + "]";
        	
    		String keyC = "E[" + triangle[2] + "," + triangle[0] + "]";
    		String keyCi = "E[" + triangle[0] + "," + triangle[2] + "]";
        	
    		if(!edgeMap.containsKey(keyA)) {
    			edgeMap.put(keyA, 0);
    		}
    		if(!edgeMap.containsKey(keyAi)) {
    			edgeMap.put(keyAi, 0);
    		}
    		if(!edgeMap.containsKey(keyB)) {
    			edgeMap.put(keyB, 0);
    		}
    		if(!edgeMap.containsKey(keyBi)) {
    			edgeMap.put(keyBi, 0);
    		}
    		if(!edgeMap.containsKey(keyC)) {
    			edgeMap.put(keyC, 0);
    		}
    		if(!edgeMap.containsKey(keyCi)) {
    			edgeMap.put(keyCi, 0);
    		}
    		
    		edgeMap.put(keyA, edgeMap.get(keyA) + 1);
    		edgeMap.put(keyAi, edgeMap.get(keyAi) + 1);
    		edgeMap.put(keyB, edgeMap.get(keyB) + 1);
    		edgeMap.put(keyBi, edgeMap.get(keyBi) + 1);
    		edgeMap.put(keyC, edgeMap.get(keyC) + 1);
    		edgeMap.put(keyCi, edgeMap.get(keyCi) + 1);
    		
    	}
    	
    	ArrayList<int[]> edgeList = new ArrayList<>();
    	for(int[] triangle : conturTriangles) {
    		String keyA = "E[" + triangle[0] + "," + triangle[1] + "]";
    		String keyB = "E[" + triangle[1] + "," + triangle[2] + "]";
    		String keyC = "E[" + triangle[2] + "," + triangle[0] + "]";
    	
    		if(edgeMap.get(keyA) == 1) {
    			int[] edge = new int[2];
    			edge[0] = triangle[0];
    			edge[1] = triangle[1];
    			
    			edgeList.add(edge);
    		}
    		
    		if(edgeMap.get(keyB) == 1) {
    			int[] edge = new int[2];
    			edge[0] = triangle[1];
    			edge[1] = triangle[2];
    			
    			edgeList.add(edge);
    		}
    		
    		if(edgeMap.get(keyC) == 1) {
    			int[] edge = new int[2];
    			edge[0] = triangle[2];
    			edge[1] = triangle[0];
    			
    			edgeList.add(edge);
    		}
    	}
    	
    	return edgeList;
    }
 
	
}
