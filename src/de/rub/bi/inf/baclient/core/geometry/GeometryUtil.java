package de.rub.bi.inf.baclient.core.geometry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.ifctoolbox.ifc.IfcRelAggregates;
import com.apstex.ifctoolbox.ifc.IfcRelDecomposes;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.AxisAngle4f;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.javax.vecmath.Vector3f;
import com.apstex.step.core.ClassInterface;

import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.DirectPositionListType;
import net.opengis.gml._3.GeometryPropertyType;

public class GeometryUtil {
	
//	private static Double geoDistance = Double.NaN;
	private static Vector3f translation = new Vector3f(0.0f, 0.0f, 0.0f);
	private static AxisAngle4f rotation = new AxisAngle4f();
	private static double scale = 1.0;
	private static boolean separatedTransform = true;
	
	private static int geometryLineThickness = 3;
	private static int geometryPointThickness = 10;
	
	private static double defaultExtrusionValue = 12.5;
	
	public static ArrayList<Point3d> directPositionToPointList(DirectPositionListType directPosListType) {
		
		List<Double> posList = directPosListType.getValue();

		BigInteger srsDim = directPosListType.getSrsDimension(); // e.g. 2 (x and y -> 2 Dimensions)
		BigInteger count = directPosListType.getCount();
		
		if(srsDim == null) {
			srsDim = new BigInteger("2");
		}

		if(count == null) {
			count = new BigInteger(new Integer(posList.size() / srsDim.intValue()).toString());
		}
		
		// read all points in context
		ArrayList<Point3d> pointList = new ArrayList<>();
		for (int i = 0; i < (count.intValue() * srsDim.intValue()); i += srsDim.intValue()) {
			double[] pos = new double[srsDim.intValue()];
			for (int j = 0; j < srsDim.intValue(); j++) {
				pos[j] = posList.get(i + j);
			}

			Point3d point = createPosition(pos);
			pointList.add(point);
		}
		
		return pointList;
		
	}

	public static Point3d createPosition(double pos[]) {
		
		Point3d point = new Point3d();
		for(int i = 0; i < pos.length; i++) {
			switch(i) {
				case 0: point.setX(pos[i]); break;
				case 1: point.setY(pos[i]); break;
				case 2: point.setZ(pos[i]); break;
				default: break;
			}
		}
		
//		autoGeoLocalization(point);
		
		return point;
	}

	
//	private static void autoGeoLocalization(Point3f point) {
//		//Distance measure to origin
//		double distance = point.distanceSquared(new Point3f(0.0f, 0.0f, 0.0f));
//		
//		if(geoDistance.isNaN() || (distance < geoDistance.doubleValue())) {
//			Vector3f translation = new Vector3f(
//					point.getX() * -1.0f, 
//					point.getY() * -1.0f, 
//					point.getZ() * -1.0f
//			);
//			
//			GeometryUtil.translation = translation;
//		}
//
//	}
	
	public static Transform3D createAutoGeoLocalization() {
		
		Transform3D transform = new Transform3D();
		if(separatedTransform) {
			transform.setScale(GeometryUtil.scale);
			
			Transform3D rotateTF = new Transform3D();
			rotateTF.setRotation(GeometryUtil.rotation);
			
			Transform3D translateTF = new Transform3D();
			translateTF.setTranslation(GeometryUtil.translation);
			
			transform.mul(rotateTF);
			transform.mul(translateTF);
			
		}else {
			transform.setRotation(GeometryUtil.rotation);
			transform.setScale(GeometryUtil.scale);
			transform.setTranslation(GeometryUtil.translation);			
		}
		
		return transform;
	}
	
	public static Transform3D createAutoGeoLocalization(Vector3f translation, AxisAngle4f rotation, double scale) {
		setTranslation(translation);
		setRotation(rotation);
		setScale(scale);
		Transform3D transformation = createAutoGeoLocalization();
		return transformation;
	}
	
	public static AbstractGeometryType hasGeometry(AbstractFeatureType featureType) {
		
		try {
			Method method = featureType.getClass().getMethod("getPosition");
			GeometryPropertyType geometryPropertyType = (GeometryPropertyType) method.invoke(featureType);
			return geometryPropertyType.getAbstractGeometry().getValue();
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static Vector3f calculatePlaneNormal(Point3f point0, Point3f point1, Point3f point2) {
		
		Point3f p0 = new Point3f(point0);
		Point3f p1 = new Point3f(point1);
		Point3f p2 = new Point3f(point2);
		
		p1.sub(p0);
		p2.sub(p0);
		Vector3f v1 = new Vector3f(p1);
		Vector3f v2 = new Vector3f(p2);
		Vector3f normal = new Vector3f();
		normal.cross(v1,v2);
		
		double degree = Math.toDegrees(Math.atan2(normal.z, normal.y) - Math.atan2(1, 0));						
		if(degree > 90.0 || degree < -90.0 ) {
			normal.negate();
		}
		normal.normalize();
		
		return normal;
	}
	
	
	public static void setTranslation(Vector3f translation) {
		GeometryUtil.translation = translation;
	}
	
	public static Vector3f getTranslation() {
		return translation;
	}
	
	public static void setScale(double scale) {
		GeometryUtil.scale = scale;
	}
	
	public static double getScale() {
		return scale;
	}
	
	public static AxisAngle4f getRotation() {
		return rotation;
	}
	
	public static void setRotation(AxisAngle4f rotation) {
		GeometryUtil.rotation = rotation;
	}
	
	public static void setSeparatedTransform(boolean separatedTransform) {
		GeometryUtil.separatedTransform = separatedTransform;
	}
	
	public static boolean isSeparatedTransform() {
		return separatedTransform;
	}
	
	public static void setGeometryLineThickness(int geometryLineThickness) {
		GeometryUtil.geometryLineThickness = geometryLineThickness;
	}
	
	public static int getGeometryLineThickness() {
		return geometryLineThickness;
	}
	
	public static void setGeometryPointThickness(int geometryPointThickness) {
		GeometryUtil.geometryPointThickness = geometryPointThickness;
	}
	
	public static int getGeometryPointThickness() {
		return geometryPointThickness;
	}
	
	public static double getDefaultExtrusionValue() {
		return defaultExtrusionValue;
	}

	public static void setDefaultExtrusionValue(double defaultExtrusionValue) {
		GeometryUtil.defaultExtrusionValue = defaultExtrusionValue;
	}

	
	
	public static double getAreaSizeIfc4(ApplicationModelNode model, IfcBuildingStorey.Ifc4 storey, double height, String spaceType) {
		
		double tempSizeUpper = 0.0;
		double tempSizeLower = 0.0;
		double differ = 1.0;
	    if(storey.getIsDecomposedBy_Inverse() != null) {
	    	for(IfcRelDecomposes.Ifc4 rel : storey.getIsDecomposedBy_Inverse()) {
	    		if(rel instanceof IfcRelAggregates.Ifc4) {
	    			for(Object ele : ((IfcRelAggregates.Ifc4)rel).getRelatedObjects()) {
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
	    }
	    
	    return ((height / differ) * tempSizeLower + ((differ - height) / differ) * tempSizeUpper);
	}
}
