package de.rub.bi.inf.baclient.core.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.media.j3d.TransformGroup;

import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import net.opengis.gml._3.AbstractFeatureCollectionType;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.BoundingShapeType;
import net.opengis.gml._3.DirectPositionType;
import net.opengis.gml._3.FeaturePropertyType;

public class XPlanungModel {

	protected ArrayList<FeaturePropertyType> features;
	protected ArrayList<XPlanungCadObjectJ3D> cadObjects;
	private String modelName;
	private AbstractFeatureCollectionType featureCollectionType;
	
	private String versionInfo = "none";

	protected TransformGroup modelGroup;
	
	private double[] localTranslation;
	
	
	private static HashMap<AbstractFeatureType, XPlanungCadObjectJ3D> featureToCad = new HashMap<>();
	private static HashMap<XPlanungCadObjectJ3D,AbstractFeatureType> cadToFeature = new HashMap<>();
	private HashMap<String, AbstractFeatureType> featureByID = new HashMap<>();
	
	private HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> featureTypes;
	

	public XPlanungModel(String modelName, AbstractFeatureCollectionType featureCollectionType) {
		this.features = new ArrayList<>();
		this.cadObjects = new ArrayList<>();
		this.modelGroup = new TransformGroup();
		this.modelGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.modelGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.featureTypes = new HashMap<>();
		this.modelName = modelName;
		
		this.featureCollectionType = featureCollectionType;
		addFeatures(featureCollectionType.getFeatureMember());
		
		BoundingShapeType boundingShape = getFeatureCollectionType().getBoundedBy();
	
		double x = 0;
		double y = 0;
		
		if(boundingShape != null) {
			if(boundingShape.getEnvelope() != null) {
				DirectPositionType lowerCorner = boundingShape.getEnvelope().getLowerCorner();
				List<Double> values = lowerCorner.getValue();
				x = values.get(0);
				y = values.get(1);
			}
		}
		
		localTranslation = new double[] {-x,-y};
	}
	
	
	public void addFeature(FeaturePropertyType feature) {
		this.features.add(feature);
				
		ArrayList<AbstractFeatureType> featureTypeList = featureTypes.get(feature.getAbstractFeature().getValue().getClass());
		if(featureTypeList == null) {
			featureTypeList = new ArrayList<>();
			featureTypes.put((Class<AbstractFeatureType>) feature.getAbstractFeature().getValue().getClass(), featureTypeList);
		}
		featureTypeList.add(feature.getAbstractFeature().getValue());
		
		XPlanungModelContainer.getInstance().featureToModelMap.put(
				feature.getAbstractFeature().getValue(), this);
		
		AbstractFeatureType featureType = feature.getAbstractFeature().getValue();
		featureByID.put(featureType.getId(), featureType);
	}
	
	public void addFeatures(List<FeaturePropertyType> features) {
//		this.features.addAll(features);
		features.forEach(f -> addFeature(f));
	}
	
	
	public void addCadObject(AbstractFeatureType abstractFeatureType, XPlanungCadObjectJ3D cadObj) {
		this.cadObjects.add(cadObj);
		this.modelGroup.addChild(cadObj);

		XPlanungModel.featureToCad.put(abstractFeatureType, cadObj);
		XPlanungModel.cadToFeature.put(cadObj, abstractFeatureType);
	}
	
//	public void addCadObjects(List<XPlanungCadObjectJ3D> cadObjs) {
//		this.cadObjects.addAll(cadObjs);
//	}
	
	public FeaturePropertyType[] getFeatures() {
		FeaturePropertyType[] featureMembers = new FeaturePropertyType[this.features.size()];
		this.features.toArray(featureMembers);
		return featureMembers;
	}
	
	public XPlanungCadObjectJ3D[] getCadObjects() {
		XPlanungCadObjectJ3D[] cadObjectJ3Ds = new XPlanungCadObjectJ3D[this.cadObjects.size()];
		this.cadObjects.toArray(cadObjectJ3Ds);
		return cadObjectJ3Ds;
	}
	
	
	private Object [] featureTypeEntries;
	
	public Object [] getFeatureTypes() {
		if(featureTypeEntries == null) {
	
			featureTypeEntries = featureTypes.entrySet().stream().sorted(
					new Comparator<Entry<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>>>() {

						@Override
						public int compare(Entry<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> o1,
								Entry<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> o2) {
							return o1.getKey().getSimpleName().compareTo(o2.getKey().getSimpleName());
						}
			}).toArray();
		
		}
		
		return featureTypeEntries;
	}
	
	public TransformGroup getModelGroup() {
		return modelGroup;
	}
	
	public void setTransform(Transform3D transformation) {
		this.modelGroup.setTransform(transformation);
	}
	
	public static XPlanungCadObjectJ3D getCadObjOfFeature(AbstractFeatureType type) {
		if(featureToCad.containsKey(type)) {
			return featureToCad.get(type);
		}
		return null;
	}
	
	public static AbstractFeatureType getFeatureOfCadObj(XPlanungCadObjectJ3D cadObj) {
		if(cadToFeature.containsKey(cadObj)) {
			return cadToFeature.get(cadObj);
		}
		return null;
	}
	
	public ArrayList<AbstractFeatureType>[] getAbstractFeatures(){
		ArrayList<AbstractFeatureType>[] aFeature = new ArrayList[featureTypes.values().size()];
		featureTypes.values().toArray(aFeature);
		return aFeature;
	}
	
	public String getModelName() {
		return modelName;
	}

	public AbstractFeatureCollectionType getFeatureCollectionType() {
		return featureCollectionType;
	}
	
	public double[] getLocalTranslation() {
		return localTranslation;
	}
	
	public AbstractFeatureType getFeatureById(String id) {
		return featureByID.get(id);
	}
	
	public boolean contains(AbstractFeatureType feature) {
		return this.features.contains(feature);
	}
	
	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

}
