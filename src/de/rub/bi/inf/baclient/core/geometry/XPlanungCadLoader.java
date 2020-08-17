package de.rub.bi.inf.baclient.core.geometry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBElement;

import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.BranchGroup;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.LineArray;
import com.apstex.javax.media.j3d.LineAttributes;
import com.apstex.javax.media.j3d.LineStripArray;
import com.apstex.javax.media.j3d.PointArray;
import com.apstex.javax.media.j3d.PointAttributes;
import com.apstex.javax.media.j3d.PolygonAttributes;
import com.apstex.javax.media.j3d.Shape3D;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Vector3d;

import de.rub.bi.inf.baclient.core.actions.XPlanungSelectionModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import net.opengis.gml._3.AbstractCurveSegmentType;
import net.opengis.gml._3.AbstractCurveType;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.AbstractRingPropertyType;
import net.opengis.gml._3.AbstractRingType;
import net.opengis.gml._3.AbstractSurfaceType;
import net.opengis.gml._3.ArcStringType;
import net.opengis.gml._3.BoundingShapeType;
import net.opengis.gml._3.CompositeCurveType;
import net.opengis.gml._3.CurvePropertyType;
import net.opengis.gml._3.CurveType;
import net.opengis.gml._3.DirectPositionType;
import net.opengis.gml._3.FeaturePropertyType;
import net.opengis.gml._3.GeometryPropertyType;
import net.opengis.gml._3.LineStringSegmentType;
import net.opengis.gml._3.LineStringType;
import net.opengis.gml._3.LinearRingType;
import net.opengis.gml._3.MultiSurfaceType;
import net.opengis.gml._3.PointType;
import net.opengis.gml._3.PolygonType;
import net.opengis.gml._3.RingType;
import net.opengis.gml._3.SurfacePropertyType;

/**
 * Add and load CadObjects into a specific viewer.
 * 
 * @author Marcel Stepien
 *
 */
public class XPlanungCadLoader {
	
	private XPlanungModel currentModel = null;
	private XPlanungSelectionModel selectionModel = null;

//	private static XPlanungCadLoader instance;
//	
//	public static XPlanungCadLoader getInstance() {
//		if (instance == null)
//			instance = new XPlanungCadLoader();
//		return instance;
//	}
	
	/**
	 * Loads the saved context into a specific viewer.
	 * 
	 * @param view
	 */
	public void loadIntoViewer(XPlanungModel model, Ifc3DViewJ3D view) {
		this.currentModel = model;
				
//		BoundingShapeType boundingShape = model.getFeatureCollectionType().getBoundedBy();
//		DirectPositionType lowerCorner = boundingShape.getEnvelope().getLowerCorner();
//		
//		List<Double> values = lowerCorner.getValue();
//		double x = values.get(0);
//		double y = values.get(1);
//		
//		Transform3D transform3d = new Transform3D();
//		transform3d.setTranslation(new Vector3d(-x, -y, 0));
		
		
//		if(transformToOrigin) {			
//			applyTransformationToCurrentModel(transform3d);
//		}
		
		
		
		
		
		BranchGroup group = new BranchGroup();
		group.addChild(model.getModelGroup());
		
		view.getModel().addChild(group);
	}
	
	public void applyTransformationToCurrentModel(Transform3D transform) {
		if(currentModel != null) {
			currentModel.getModelGroup().setTransform(transform);
		}
	}

	public XPlanungSelectionModel getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(XPlanungSelectionModel selectionModel) {
		this.selectionModel = selectionModel;
	}

	public final XPlanungModel getCurrentModel() {
		return this.currentModel;
	}

	private void adjustCapabilities(Appearance appearance) {
		appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		appearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);
//		appearance.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
//		appearance.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_READ);
//		appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
//		appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
//		appearance.setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
//		appearance.setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_READ);
//		appearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
//		appearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_READ);
//		appearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
//		appearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
//		appearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
//		appearance.setCapability(Appearance.ALLOW_TEXGEN_READ);
//		appearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
//		appearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_READ);
//		appearance.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE);
//		appearance.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
//		appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
//		appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
//		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
//		appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
	}

	
	
	private static void adjustCapabilities(LineAttributes lineAttributes) {
		lineAttributes.setCapability(LineAttributes.ALLOW_WIDTH_WRITE);
		lineAttributes.setCapability(LineAttributes.ALLOW_WIDTH_READ);
		lineAttributes.setCapability(LineAttributes.ALLOW_ANTIALIASING_WRITE);
		lineAttributes.setCapability(LineAttributes.ALLOW_ANTIALIASING_READ);
		lineAttributes.setCapability(LineAttributes.ALLOW_PATTERN_WRITE);
		lineAttributes.setCapability(LineAttributes.ALLOW_PATTERN_READ);
	}
	
	private static void adjustCapabilities(PointAttributes pointAttributes) {
		pointAttributes.setCapability(PointAttributes.ALLOW_SIZE_WRITE);
		pointAttributes.setCapability(PointAttributes.ALLOW_SIZE_READ);
		pointAttributes.setCapability(PointAttributes.ALLOW_ANTIALIASING_WRITE);
		pointAttributes.setCapability(PointAttributes.ALLOW_ANTIALIASING_READ);
	}
	
	private void adjustCapabilities(GeometryArray geometryArray) {
		geometryArray.setCapability(GeometryArray.ALLOW_COLOR_READ);
		geometryArray.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
		geometryArray.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
		geometryArray.setCapability(GeometryArray.ALLOW_VERTEX_ATTR_READ);
		geometryArray.setCapability(GeometryArray.ALLOW_NORMAL_READ);
	}

	public void interpretGeometry(XPlanungModel model) {
		FeaturePropertyType[] features = model.getFeatures();
		for (FeaturePropertyType feature : features) {
			
			try {
			
				AbstractFeatureType abstractFeatureType = feature.getAbstractFeature().getValue();

				Method positionMethod = null;
				try {
					positionMethod = abstractFeatureType.getClass().getMethod("getPosition");
				} catch (NoSuchMethodException e) {
					//DO NOTHING
				}

				if(positionMethod != null) {
					GeometryPropertyType position = (GeometryPropertyType)positionMethod.invoke(abstractFeatureType);
					
					interpretGeometryPropertyType(model, abstractFeatureType, position);
				}
				
				Method geltungsbereichMethod = null;
				try {
					geltungsbereichMethod = abstractFeatureType.getClass().getMethod("getRaeumlicherGeltungsbereich");
				} catch (NoSuchMethodException e) {
					//DO NOTHING
				}

				if(geltungsbereichMethod != null) {
					GeometryPropertyType position = (GeometryPropertyType)geltungsbereichMethod.invoke(abstractFeatureType);
					
					interpretGeometryPropertyType(model, abstractFeatureType, position);
				}

//				Method roomMethod = null;
//				try {
//					roomMethod = abstractFeatureType.getClass().getMethod("getRest");
//				} catch (NoSuchMethodException e) {
//					//DO NOTHING
//				}
//
//				if(roomMethod != null) {	
//					ArrayList<JAXBElement<?>> list = (ArrayList<JAXBElement<?>>) roomMethod.invoke(abstractFeatureType);
//					for(JAXBElement<?> ele : list) {
//						
//						if(ele.getValue() instanceof GeometryPropertyType) {
//
//							interpretGeometryPropertyType(model, abstractFeatureType, (GeometryPropertyType)ele.getValue());	
//						}	
//					}
//				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void interpretGeometryPropertyType(XPlanungModel model, AbstractFeatureType abstractFeatureType, GeometryPropertyType position) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		AbstractGeometryType agType = position.getAbstractGeometry().getValue();
		String typeName = abstractFeatureType.getClass().getSimpleName();
		
		if(abstractFeatureType.getId().contains("6599"))
			System.out.println();
		
		BoundingShapeType boundingShape = model.getFeatureCollectionType().getBoundedBy();
		DirectPositionType lowerCorner = boundingShape.getEnvelope().getLowerCorner();
		List<Double> values = lowerCorner.getValue();
		double x = values.get(0);
		double y = values.get(1);
		
		Transform3D transform3d = new Transform3D();
		transform3d.setTranslation(new Vector3d(-x, -y, 0));

		Appearance appearance = getAppearanceForType(typeName);
		
		ArrayList<GeometryArray> geometryArrays = null;
		if (agType instanceof PolygonType) {
			PolygonType polyType = (PolygonType) agType;
			geometryArrays = interpretPolygon(polyType, transform3d);			
		}else if(agType instanceof PointType) {
			PointType pointType = (PointType)agType;
			Color3f color = new Color3f();
			appearance.getColoringAttributes().getColor(color);
			geometryArrays = interpretPoint(pointType, color, transform3d);
		}else if(agType instanceof LineStringType) {
			//typeName = "LINEELEMENT";
			LineStringType pointType = (LineStringType)agType;
			geometryArrays = interpretLineString(pointType, transform3d);
		}else if(agType instanceof CurveType) {
			//typeName = "LINEELEMENT";
			CurveType pointType = (CurveType)agType;
			geometryArrays = interpretCurve(pointType, transform3d);
		}else if(agType instanceof MultiSurfaceType) {
			//typeName = "MULTISURFACETYPE";
			MultiSurfaceType pointType = (MultiSurfaceType)agType;
			geometryArrays = interpretMultiSurface(pointType, transform3d);
		}else{
			System.out.println("Geometry not recognized: " + agType.getClass().getSimpleName());
		}
		
		
		if (geometryArrays !=null){
			for(GeometryArray arr : geometryArrays) {

				Shape3D faceShape = new Shape3D(arr, appearance);
				
				// Create CadObjects containing all Shape3D's
				XPlanungCadObjectJ3D cadObj = new XPlanungCadObjectJ3D();
				cadObj.setName(abstractFeatureType.getId());
				//XPlanungCadLoader.adjustCapabilities((CadObjectJ3D) cadObj);

				faceShape.setName("Shape3D_of_" + abstractFeatureType.getId());
				faceShape.setUserData(cadObj); //Needed for selection
				//XPlanungCadLoader.adjustCapabilities(faceShape);

				//MultiAppearanceShape3D muiltiShape3D = new MultiAppearanceShape3D(faceShape);
				
				//PickTool.setCapabilities(faceShape, PickTool.INTERSECT_FULL);
		        
				cadObj.addShape3D(faceShape);
				//cadObj.setAppearance(selectionApp);

				// Save context for project explorer
				model.addCadObject(abstractFeatureType, cadObj);
			}
		}
	}
		
	
	private ArrayList<GeometryArray> interpretPolygon(PolygonType polygonType, Transform3D transform3d) {
		
		ArrayList<GeometryArray> geoList = new ArrayList<>();
		
		if (polygonType.getExterior() != null) {
			AbstractRingPropertyType arpType = polygonType.getExterior();
			AbstractRingType exteriorRing = arpType.getAbstractRing().getValue(); //exterior ring

			ArrayList<Integer> stripCountList = new ArrayList<>();
			Collection<Point3d> coordinateList = new ArrayList<>();
			
			ringTypeToPointList(coordinateList, stripCountList, exteriorRing); //fills list with information
			
			if(polygonType.getInterior().size() > 0) {
				polygonType.getInterior().forEach(propType -> {
					
					AbstractRingType interiorRing = propType.getAbstractRing().getValue(); //inner ring
					ringTypeToPointList(coordinateList, stripCountList, interiorRing); //fills list with information
												
				});
			}
			
			//Apply local transform
			coordinateList.forEach(c -> transform3d.transform(c));
			
			GeometryInfo geometryInfo = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
						
			Point3d[] coordinates = new Point3d[coordinateList.size()];
			coordinateList.toArray(coordinates);
			geometryInfo.setCoordinates(coordinates);
			geometryInfo.setStripCounts(stripCountList.stream().mapToInt(Integer::intValue).toArray());
			geometryInfo.setContourCounts(new int[] {stripCountList.size()});
			
			GeometryArray geoArray = geometryInfo.getGeometryArray();
			adjustCapabilities(geoArray);
	        
			geoList.add(geoArray);
			
		}
		
		return geoList;	
	}
	
	public static void ringTypeToPointList(Collection<Point3d> coordinateList, Collection<Integer> stripCountList, AbstractRingType ring){

		if (ring instanceof LinearRingType) {	
			LinearRingType linearRingType = (LinearRingType) ring;
			ArrayList<Point3d> tempPointList = GeometryUtil.directPositionToPointList(linearRingType.getPosList());

			//System.out.println(tempPointList.size());
			
			stripCountList.add(tempPointList.size());
			coordinateList.addAll(tempPointList);
		}else if (ring instanceof RingType) {
			RingType ringType = (RingType)ring;
			for(CurvePropertyType curvePropertyType : ringType.getCurveMember()) {
				
				JAXBElement<? extends AbstractCurveType> element = curvePropertyType.getAbstractCurve();
				if(element.getValue() instanceof CurveType) {
					CurveType type = (CurveType)element.getValue();
					ArrayList<Point3d> tempPointList = curveTypeToPointList(type);
					stripCountList.add(tempPointList.size());
					coordinateList.addAll(tempPointList);
				}else if(element.getValue() instanceof LineStringType) {
					LineStringType type = (LineStringType)element.getValue();
					ArrayList<Point3d> tempPointList = GeometryUtil.directPositionToPointList(type.getPosList());
					stripCountList.add(tempPointList.size());
					coordinateList.addAll(tempPointList);	
				}else if(element.getValue() instanceof CompositeCurveType) {
					System.out.println("CompositeCureType Not jet implemented!");
				}
			}
			
		}	
	}
	
	private static ArrayList<Point3d> curveTypeToPointList(CurveType type){
		List<JAXBElement<? extends AbstractCurveSegmentType>> elements = type.getSegments().getAbstractCurveSegment();
		
		ArrayList<Point3d> pointList = new ArrayList<>();
		for(JAXBElement<? extends AbstractCurveSegmentType> ele : elements) {
			if(ele.getValue() instanceof LineStringSegmentType) {
				LineStringSegmentType segmentType = (LineStringSegmentType)ele.getValue();
				ArrayList<Point3d> tp = GeometryUtil.directPositionToPointList(segmentType.getPosList());
				//System.out.println(tp.size());
				pointList.addAll(tp);
			}else if(ele.getValue() instanceof ArcStringType) {
				ArcStringType segmentType = (ArcStringType)ele.getValue();
				ArrayList<Point3d> tp = GeometryUtil.directPositionToPointList(segmentType.getPosList());
				pointList.addAll(tp);
			}else {
				System.out.println("Not recognized CurveType: " + type.getClass().getSimpleName());
			}

		}
		
		return pointList;
	}
	
	private ArrayList<GeometryArray> interpretPoint(PointType pointType, Color3f color, Transform3D transform3d) {
		
		ArrayList<GeometryArray> geoList = new ArrayList<>();
		
		if (pointType.getPos() != null) {
			
			DirectPositionType positionType = pointType.getPos();
			
			double[] pos = new double[positionType.getValue().size()];
			for(int i = 0; i < positionType.getValue().size(); i++) {
				pos[i] = positionType.getValue().get(i);
			}
			
			Point3d point = GeometryUtil.createPosition(pos);
			
			//Apply local transform
			transform3d.transform(point);
			
			PointArray pointArray = new PointArray(1, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
			pointArray.setCoordinate(0, point);
			adjustCapabilities(pointArray);
	        
			pointArray.setColor(0, color);
			
			geoList.add(pointArray);
		}
		
		return geoList;
		
	}

	private ArrayList<GeometryArray> interpretLineString(LineStringType lineType, Transform3D transform3d) {
		
		ArrayList<GeometryArray> geoList = new ArrayList<>();
		
		if (lineType.getPosList() != null) {
			
			ArrayList<Point3d> pointList = GeometryUtil.directPositionToPointList(lineType.getPosList());
			
			//Apply local transform
			pointList.forEach(c -> transform3d.transform(c));
			
			double[] coords = new double[pointList.size() * 3];
			for(int i = 0; i < pointList.size(); i++) {
				coords[i * 3] = pointList.get(i).getX();
				coords[(i * 3) + 1] = pointList.get(i).getY();
				coords[(i * 3) + 2] = pointList.get(i).getZ();		
			}
			
			LineStripArray lineArray = new LineStripArray(pointList.size(), LineArray.COORDINATES, new int[] { pointList.size()});
			lineArray.setCoordinates(0, coords);
			adjustCapabilities(lineArray);
	        
			geoList.add(lineArray);
		}
		
		return geoList;
		
	}
	
	private ArrayList<GeometryArray> interpretCurve(CurveType curveType, Transform3D transform3d) {
		
		ArrayList<GeometryArray> geoList = new ArrayList<>();
		
		ArrayList<Point3d> pointList = curveTypeToPointList(curveType);
		
		//Apply local transform
		pointList.forEach(c -> transform3d.transform(c));
		
		if(pointList != null) {
			double[] coords = new double[pointList.size() * 3];
			for(int i = 0; i < pointList.size(); i++) {
				coords[i * 3] = pointList.get(i).getX();
				coords[(i * 3) + 1] = pointList.get(i).getY();
				coords[(i * 3) + 2] = pointList.get(i).getZ();		
			}
			
			LineStripArray lineArray = new LineStripArray(pointList.size(), LineArray.COORDINATES, new int[] { pointList.size()});
			lineArray.setCoordinates(0, coords);
			adjustCapabilities(lineArray);
	        
			geoList.add(lineArray);
		}			
		
		return geoList;
	}
	
	private ArrayList<GeometryArray> interpretMultiSurface(MultiSurfaceType surfType, Transform3D transform3d) {
		
		ArrayList<GeometryArray> geoList = new ArrayList<>();
		
		for(SurfacePropertyType surface : surfType.getSurfaceMember()) {
			AbstractSurfaceType aType = surface.getAbstractSurface().getValue();
			
			if(aType instanceof PolygonType) {
				ArrayList<GeometryArray> arrs = interpretPolygon((PolygonType)aType, transform3d);
				geoList.addAll(arrs);
			}else {
				System.out.println("Not recognized geometry type in MultiSurfaceType: " + aType.getClass().getSimpleName());
			}
			
		}
		
		return geoList;
	}

	private Appearance getAppearanceForType(String type) {
		PolygonAttributes p = new PolygonAttributes();
		p.setCullFace(PolygonAttributes.CULL_NONE); // Renders both sites of the face
		
		Appearance appearance = new Appearance();
		appearance.setPolygonAttributes(p);
		
		//appearance.setMaterial(new Material());
		
		ColoringAttributes planeCA = new ColoringAttributes(getColorForType(type), 1);
		appearance.setColoringAttributes(planeCA);

		// line style, thickness, pattern and antialiasing
		LineAttributes lineAttriutes = new LineAttributes(GeometryUtil.getGeometryLineThickness(), LineAttributes.PATTERN_SOLID, true);
		adjustCapabilities(lineAttriutes);		
		appearance.setLineAttributes(lineAttriutes);
		
	    // point style, thickness and antialiasing 
		PointAttributes pointAttriutes = new PointAttributes(GeometryUtil.getGeometryPointThickness(), true);
		adjustCapabilities(pointAttriutes);		
		appearance.setPointAttributes(pointAttriutes);	
		
		adjustCapabilities(appearance);
		return appearance;
	}

	
	private static HashMap<String, Color3f> colorMap = new HashMap<>();
	
	public static Color3f getColorForType(String type) {
		Color3f color;
		switch (type) {
		case "BPBaugebietsTeilFlaecheType":
			color = new Color3f(0.5f, 0.2f, 0.2f);
			break;
		case "BPGruenFlaecheType":
			color = new Color3f(0.4f, 0.7f, 0.7f);
			break;
		case "BPAbgrabungsFlaecheType":
			color = new Color3f(0.1f, 0.5f, 0.5f);
			break;
		case "BPAufschuettungsFlaecheType":
			color = new Color3f(0.4f, 0.4f, 0.4f);
			break;
		case "BPAusgleichsFlaecheType":
			color = new Color3f(0.3f, 0.7f, 0.7f);
			break;
		case "BPBesondererNutzungszweckFlaecheType":
			color = new Color3f(0.8f, 0.7f, 0.8f);
			break;
		case "BPBodenschaetzeFlaecheType":
			color = new Color3f(0.3f, 0.7f, 0.3f);
			break;
		case "BPFlaechenschlussobjektType":
			color = new Color3f(0.1f, 0.1f, 0.1f);
			break;
		case "BPKennzeichnungsFlaecheType":
			color = new Color3f(0.8f, 0.6f, 0.6f);
			break;
		case "BPRekultivierungsFlaecheType":
			color = new Color3f(0.6f, 0.9f, 0.9f);
			break;
		case "BPSchutzPflegeEntwicklungsFlaecheType":
			color = new Color3f(0.1f, 1.0f, 0.5f);
			break;
		case "BPUeberbaubareGrundstuecksFlaecheType":
			color = new Color3f(0.3f, 0.7f, 0.7f);
			break;
		case "BPWasserwirtschaftsFlaecheType":
			color = new Color3f(0.6f, 0.6f, 0.9f);
			break;
		case "BPBauGrenzeType":
			color = new Color3f(0.5f, 0.5f, 0.7f);
			break;
		case "BPBauLinieType":
			color = new Color3f(0.1f, 0.1f, 0.1f);
			break;
		case "BPBereichType":
			color = new Color3f(0.5f, 0.5f, 0.5f);
			break;
		case "BPEinfahrtsbereichLinieType":
			color = new Color3f(0.8f, 0.8f, 0.8f);
			break;
		case "BPGemeinbedarfsFlaecheType":
			color = new Color3f(0.2f, 0.5f, 0.7f);
			break;
		case "BPGemeinschaftsanlagenZuordnungType":
			color = new Color3f(0.2f, 0.5f, 0.3f);
			break;
		case "BPGemeinschaftsanlagenFlaecheType":
			color = new Color3f(0.4f, 0.5f, 0.2f);
			break;
		case "BPGenerischesObjektType":
			color = new Color3f(0.3f, 0.3f, 0.3f);
			break;
		case "BPGewaesserFlaecheType":
			color = new Color3f(0.8f, 0.8f, 1.0f);
			break;
		case "BPHoehenMassType":
			color = new Color3f(0.9f, 0.9f, 0.9f);
			break;
		case "BPNutzungsartenGrenzeType":
			color = new Color3f(0.5f, 0.2f, 0.1f);
			break;
		case "BPPlanType":
			color = new Color3f(0.5f, 0.1f, 0.1f);
			break;
		case "BPSpezielleBauweiseType":
			color = new Color3f(0.7f, 0.4f, 0.7f);
			break;
		case "BPStrassenVerkehrsFlaecheType":
			color = new Color3f(0.4f, 0.4f, 0.4f);
			break;
		case "BPStrassenbegrenzungsLinieType":
			color = new Color3f(0.5f, 0.2f, 0.2f);
			break;
		case "BPVerEntsorgungType":
			color = new Color3f(0.6f, 0.6f, 0.2f);
			break;
		case "BPVerkehrsflaecheBesondererZweckbestimmungType":
			color = new Color3f(0.2f, 0.2f, 0.0f);
			break;
		case "BPWegerechtType":
			color = new Color3f(0.6f, 0.6f, 0.6f);
			break;
		case "SOWasserrechtType":
			color = new Color3f(0.6f, 0.6f, 0.9f);
			break;
		case "XPPPOType":
			color = new Color3f(0.5f, 0.8f, 0.3f);
			break;
		case "XPPTOType":
			color = new Color3f(0.5f, 0.8f, 0.3f);
			break;
		case "XPRasterplanBasisType":
			color = new Color3f(0.2f, 0.7f, 0.2f);
			break;
		case "XPTextAbschnittType":
			color = new Color3f(1.0f, 1.0f, 1.0f);
			break;
		default:
			if(colorMap.containsKey(type)) {
				color = colorMap.get(type);
			}else {
				Random random = new Random();
				color = new Color3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
				colorMap.put(type, color);
			}
			break;
		}
		return color;
	}
}
