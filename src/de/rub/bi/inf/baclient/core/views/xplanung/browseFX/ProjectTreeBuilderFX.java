package de.rub.bi.inf.baclient.core.views.xplanung.browseFX;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.jogamp.common.util.IntIntHashMap.Entry;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import javafx.scene.control.TreeItem;
import javafx.util.Pair;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.FeaturePropertyType;

/**
 * Build and display the Template-Editor-Tree.
 * 
 * @author Marcel Stepien
 *
 */
public class ProjectTreeBuilderFX {
	
	
	/**
	 * Initialize new content for the editor tree.
	 * 
	 */
	public static TreeItem<?> createTree() {
		TreeItem<Object> top = new TreeItem<Object>("XPlanung");
		top.setExpanded(true);
		
		//HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> featureMap = new HashMap<>();
		
		for(XPlanungModel model : XPlanungModelContainer.getInstance().getModels()) {
			HashMap<String, HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>>> featureLevelMap = new HashMap<>();

			TreeItem<Object> modelNode = new TreeItem<Object>(model);
			top.getChildren().add(modelNode);

			for(FeaturePropertyType feature :  model.getFeatures()) {
				
				AbstractFeatureType aType = feature.getAbstractFeature().getValue();
				
				BeanInfo beanInfo = null;
				try {
					beanInfo = Introspector.getBeanInfo(aType.getClass());
				} catch (IntrospectionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				
				HashMap<String, PropertyDescriptor> propertyMap = new HashMap<>(propertyDescriptors.length);
				Arrays.stream(propertyDescriptors).forEach(p -> {
					propertyMap.put(p.getName().toLowerCase(), p);
				});
				
				PropertyDescriptor ebene = propertyMap.get("ebene");
				Object iVal = null;
				
				if(ebene != null) {
					try {
						iVal = ebene.getReadMethod().invoke(aType);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				
				HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> featureMap = null;
				if(iVal != null) {	
					if(!featureLevelMap.keySet().contains(iVal.toString())) {
						featureMap = new HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>>();
						featureLevelMap.put(iVal.toString(), featureMap);
					}else {
						featureMap = featureLevelMap.get(iVal.toString());
					}
				}else {
					if(!featureLevelMap.keySet().contains("NoLevel")) {
						featureMap = new HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>>();
						featureLevelMap.put("NoLevel", featureMap);
					}else {
						featureMap = featureLevelMap.get("NoLevel");
					}
				}
				
			
				if(!featureMap.containsKey(feature.getAbstractFeature().getValue().getClass())) {
					ArrayList featureList = new ArrayList<>();
					featureList.add(feature.getAbstractFeature().getValue());
					featureMap.put((Class<AbstractFeatureType>)feature.getAbstractFeature().getValue().getClass(), featureList);
				}else {
					featureMap.get(feature.getAbstractFeature().getValue().getClass()).add(feature.getAbstractFeature().getValue());
				}
			}
			
			for(java.util.Map.Entry<String, HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>>> levelKey : featureLevelMap.entrySet()) {
				HashMap<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> featureMap = featureLevelMap.get(levelKey.getKey());
				
				TreeItem<Object> levelItem = new TreeItem<Object>(levelKey);
				modelNode.getChildren().add(levelItem);
				
				for(java.util.Map.Entry<Class<AbstractFeatureType>, ArrayList<AbstractFeatureType>> aType : featureMap.entrySet()) {
					TreeItem<Object> abstractFeatureItem = new TreeItem<Object>(aType);
					levelItem.getChildren().add(abstractFeatureItem);
					
					for(AbstractFeatureType feature : featureMap.get(aType.getKey())) {
						TreeItem<Object> featureItem = new TreeItem<Object>(feature);
						abstractFeatureItem.getChildren().add(featureItem);
					}
				}
				
			}
			
			
		}
		
		return top;		
	}
	
}
