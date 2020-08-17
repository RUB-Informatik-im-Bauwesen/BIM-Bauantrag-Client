package de.rub.bi.inf.baclient.core.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.apstex.gm3d.core.TriFace;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.IndexedTriangleArray;
import com.apstex.javax.media.j3d.LineAttributes;
import com.apstex.javax.media.j3d.Material;
import com.apstex.javax.media.j3d.PolygonAttributes;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.javax.vecmath.Vector3f;

import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class GeometryUtil {

	public static GeometryArray createPolygon(Point3f[] polyline, boolean finalize) {
		
		GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		
		gi.setCoordinates(polyline);

		ArrayList<Integer> stripCountList = new ArrayList<>();
		stripCountList.add(polyline.length);
//		System.out.println(
//				Arrays.toString(
//				stripCountList.stream().mapToInt(Integer::intValue).toArray())
//						);
		gi.setStripCounts(stripCountList.stream().mapToInt(Integer::intValue).toArray());
		gi.setContourCounts(new int[] {1});
		
		GeometryArray geoArray = null;
		
		if(finalize) {			
			gi.convertToIndexedTriangles();
			gi.indexify();
			geoArray = gi.getIndexedGeometryArray();
		}else {
			geoArray = gi.getGeometryArray();
		}
		 
		geoArray.setCapability(GeometryArray.ALLOW_COLOR_READ);
		geoArray.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
		geoArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		geoArray.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
		geoArray.setCapability(GeometryArray.ALLOW_NORMAL_READ);		

		
		return geoArray;
		
	}
	
	
	public static Polygon create2DPolygonFX(Point3f... polyline) {
		double[] polygonPoints = new double[polyline.length*2];
		for(int i=0; i<polyline.length; i++) {
			polygonPoints[2*i] = polyline[i].x;
			polygonPoints[2*i+1] = polyline[i].y;
		}
		
		return new Polygon(polygonPoints);
	}
	
	public static Shape union_2D(Collection<? extends Shape> shapes) {
		
		ArrayList<? extends Shape> tmpList = new ArrayList<>(shapes);
		Shape first = tmpList.remove(0);
		for(Shape poly : tmpList) {
			 first = Shape.union(first, poly);
		}
		
		return first;
		
	}
	
	public static ArrayList<ArrayList<Point3f>> extract3DPolylineBoundary(Shape shape) {
		
		ArrayList<ArrayList<Point3f>> polyLineList = new ArrayList<>();
		
//		System.out.println(shape.getClass().getName());
		
		if(shape instanceof Path) {
			Path path = (Path) shape;
			
//			path.getElements().forEach(e -> System.out.println(e));
			
			ArrayList<Point3f> currentPolyline = null;
			
			for(int i=0; i<path.getElements().size(); i++) {
				
				if (path.getElements().get(i) instanceof MoveTo) {
					currentPolyline = new ArrayList<>();
					MoveTo moveTo = (MoveTo) path.getElements().get(i);
					currentPolyline.add(new Point3f(
							moveTo.xProperty().floatValue(),
							moveTo.yProperty().floatValue(),
							0.f));
				}else if(path.getElements().get(i) instanceof LineTo) {
					LineTo moveTo = (LineTo) path.getElements().get(i);
					currentPolyline.add(new Point3f(
							moveTo.xProperty().floatValue(),
							moveTo.yProperty().floatValue(),
							0.f));
				}else if(path.getElements().get(i) instanceof ClosePath) {
					polyLineList.add(currentPolyline);
				}else {
					try {
						throw new Exception("Non expected Element in Path: "+path.getElements().get(i));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
					
				
			}

		}else if (shape instanceof Polygon) {
			Polygon resultArea = (Polygon) shape;
			
			ArrayList<Point3f> currentPolyline = new ArrayList<>();
			
			for(int i=0; i<resultArea.getPoints().size()/2; i++) {
				Point3f p = new Point3f(
						resultArea.getPoints().get(2*i).floatValue(),
						resultArea.getPoints().get(2*i+1).floatValue(),
						0.f
						);
				currentPolyline.add(p);
			}
			
			polyLineList.add(currentPolyline);
		}
		
		
		return polyLineList;
		
	}
	
	public static double computeArea(IndexedTriangleArray triangleArray) {
		
		int[] indices = new int[triangleArray.getValidIndexCount()];
		triangleArray.getCoordinateIndices(0, indices);
		
		double area = 0;
		
		for(int i=0; i<indices.length/3; i++) {
			int id1 = indices[3*i];
			int id2 = indices[3*i+1];
			int id3 = indices[3*i+2];
	
			Point3f p1 = new Point3f();
			triangleArray.getCoordinate(id1, p1);
			Point3f p2 = new Point3f();
			triangleArray.getCoordinate(id2, p2);
			Point3f p3 = new Point3f();
			triangleArray.getCoordinate(id3, p3);
			
//			area+=(p1.x*p2.y + p2.x*p3.y + p3.x*p1.y 
//					- p1.x*p3.y - p2.x*p1.y - p3.x*p2.y)/2;
			
			Vector3f v1 = new Vector3f(p2.x-p1.x, p2.y-p1.y, 0);
			Vector3f v2 = new Vector3f(p3.x-p1.x, p3.y-p1.y, 0);
			
			Vector3f crossProduct = new Vector3f();
			crossProduct.cross(v1, v2);
			
			area+= 0.5*crossProduct.length();

		}
		
		
		
		return area;
		
	}
	
	
	public static Appearance createAppearance(Color color) {
    	Appearance appearance = new Appearance();
    	appearance.setMaterial(createMatrial(color));
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
    	coloringAttributes.setColor(new Color3f(color));
    	appearance.setColoringAttributes(coloringAttributes);
    	    	
    	return appearance;
	}
 

	public static Material createMatrial(Color color) {
	    	Material material = new Material();
	    	material.setDiffuseColor(new Color3f(color));
	    	material.setAmbientColor(new Color3f(color));
	    	return material;
	 }

}
