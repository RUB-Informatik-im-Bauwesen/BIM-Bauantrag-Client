package de.rub.bi.inf.baclient.core.model;

import java.util.ArrayList;
import java.util.HashMap;

import net.opengis.gml._3.AbstractFeatureType;

public class XPlanungModelContainer {

	private static XPlanungModelContainer instance;
	
	public static XPlanungModelContainer getInstance() {
		if (instance==null) {
			instance = new XPlanungModelContainer();
		}
		return instance;
	}
	
	private ArrayList<XPlanungModel> models = new ArrayList<>();
	
	HashMap<AbstractFeatureType, XPlanungModel> featureToModelMap = new HashMap<>();
	
	private XPlanungModelContainer() {
		
	}
	
	public ArrayList<XPlanungModel> getModels() {
		return models;
	}
	
	public XPlanungModel getModelOfFeature(AbstractFeatureType abstractFeatureType) {
		return featureToModelMap.get(abstractFeatureType);
	}
	
	
}
