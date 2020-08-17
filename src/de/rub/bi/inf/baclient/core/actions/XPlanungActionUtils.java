package de.rub.bi.inf.baclient.core.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.XViewerPanel;
import de.rub.bi.inf.baclient.core.views.xplanung.browseFX.XPlanungProjectExplorerViewFX;
import javafx.util.Pair;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.FeaturePropertyType;

public class XPlanungActionUtils {

	private static void select(XPlanungModel model) {
		for(ArrayList<AbstractFeatureType> features : model.getAbstractFeatures()) {
			select(features);
		}
	}
	
		
	private static void select(AbstractFeatureType feature) {
		XPlanungModel model = (XPlanungModel)findXPlanungModel(feature);
		if(model != null) {
			XPlanungCadObjectJ3D cadObj = model.getCadObjOfFeature(feature);
			if(cadObj != null) {		
				cadObj.setSelected(true);
			}else {
				System.err.println("Feature " + feature.getClass().getSimpleName() + " : " + feature.getId() + " has no Cad Object");
			}
		}
	}

	private static void deselect(XPlanungModel model) {
		for(ArrayList<AbstractFeatureType> features : model.getAbstractFeatures()) {
			deselect(features);
		}
	}
		
	private static void select(Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair) {
		for(Object o : pair.getValue().keySet()) {
			select(pair.getValue().get(o));
		}
	}
	
	private static void deselect(Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair) {
		for(Object o : pair.getValue().keySet()) {
			deselect(pair.getValue().get(o));
		}
	}
		
	private static void deselect(AbstractFeatureType feature) {
		XPlanungModel model = (XPlanungModel)findXPlanungModel(feature);
		if(model != null) {
			XPlanungCadObjectJ3D cadObj = model.getCadObjOfFeature(feature);
			if(cadObj != null) {		
				cadObj.setSelected(false);
			}else {
				System.err.println("Feature " + feature.getClass().getSimpleName() + " : " + feature.getId() + " has no Cad Object");
			}
		}
	}

	private static void setInvisible(XPlanungModel model) {
		for(ArrayList<AbstractFeatureType> features : model.getAbstractFeatures()) {
			setInvisible(features);
		}
	}
	
	private static void setInvisible(AbstractFeatureType feature) {
		XPlanungModel model = (XPlanungModel)findXPlanungModel(feature);
		if(model != null) {
			XPlanungCadObjectJ3D cadObj = model.getCadObjOfFeature(feature);
			if(cadObj != null) {		
				cadObj.setVisible(false);
			}else {
				System.err.println("Feature " + feature.getClass().getSimpleName() + " : " + feature.getId() + " has no Cad Object");
			}
		}
	}
	
	private static void setInvisible(Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair) {
		for(Object o : pair.getValue().keySet()) {
			setInvisible(pair.getValue().get(o));
		}
	}
	
	private static void setVisible(Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair) {
		for(Object o : pair.getValue().keySet()) {
			setVisible(pair.getValue().get(o));
		}
	}
	
	private static void setVisible(XPlanungModel model) {
		for(ArrayList<AbstractFeatureType> features : model.getAbstractFeatures()) {
			setVisible(features);
		}
	}
	
	
	private static void setVisible(AbstractFeatureType feature) {
		XPlanungModel model = (XPlanungModel)findXPlanungModel(feature);
		if(model != null) {
			XPlanungCadObjectJ3D cadObj = model.getCadObjOfFeature(feature);
			if(cadObj != null) {		
				cadObj.setVisible(true);
				cadObj.setTransparency(1.0f);
			}else {
				System.err.println("Feature " + feature.getClass().getSimpleName() + " : " + feature.getId() + " has no Cad Object");
			}
		}
	}
	
	
	private static void setTransparent(XPlanungModel model) {
		for(ArrayList<AbstractFeatureType> features : model.getAbstractFeatures()) {
			setTransparent(features);
		}
	}
	
	private static void setTransparent(Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair) {
		for(Object o : pair.getValue().keySet()) {
			setTransparent(pair.getValue().get(o));
		}
	}
	
	
	private static void setTransparent(AbstractFeatureType feature) {
		XPlanungModel model = (XPlanungModel)findXPlanungModel(feature);
		if(model != null) {
			XPlanungCadObjectJ3D cadObj = model.getCadObjOfFeature(feature);
			if(cadObj != null) {
				cadObj.setTransparency(0.5f);
			}else {
				System.err.println("Feature " + feature.getClass().getSimpleName() + " : " + feature.getId() + " has no Cad Object");
			}
		}
	}

	public static XPlanungModel findXPlanungModel(Object obj) {
		
		if(obj instanceof XPlanungModel) {
			return (XPlanungModel)obj;
		}
		
		ArrayList<Object> arrTemp = null; 
		
		if(obj instanceof Entry<?, ?>) {
			if(((Entry)obj).getKey() instanceof Class) {
				arrTemp = ((ArrayList)((Entry)obj).getValue());
			}else if(((Entry)obj).getKey() instanceof String){

				arrTemp = new ArrayList<>();
				HashMap<Class, ArrayList<AbstractFeatureType>> afMap = new HashMap<>();
				
				for(ArrayList<AbstractFeatureType> afList : afMap.values()) {
					arrTemp.addAll(afList);
				}
				
			}
		}
		
		for(XPlanungModel model : XPlanungModelContainer.getInstance().getModels()) {
			for(ArrayList<AbstractFeatureType> arrTypes : model.getAbstractFeatures()) {
				
				if(arrTemp != null) {
					if(arrTypes.containsAll(arrTemp)) {
						return model;
					}
					
				}else if(arrTypes.contains(obj)) {
					return model;
				}
				
			}
		}
				
		return null;
	}
	
	public static boolean isVisible(Object obj) {
		if(obj instanceof FeaturePropertyType) {
			AbstractFeatureType aType = ((FeaturePropertyType)obj).getAbstractFeature().getValue();
			XPlanungModel model = findXPlanungModel(obj);
			
			if(model.getCadObjOfFeature(aType) != null) {				
				return model.getCadObjOfFeature(aType).isVisible();
			}
		}else if(obj instanceof AbstractFeatureType) {
		
			AbstractFeatureType aType = (AbstractFeatureType)obj;
			XPlanungModel model = findXPlanungModel(obj);
			if(model.getCadObjOfFeature(aType) != null) {				
				return model.getCadObjOfFeature(aType).isVisible();
			}
		}else if(obj instanceof XPlanungModel) {
			
			for(ArrayList<AbstractFeatureType> aTypes : ((XPlanungModel)obj).getAbstractFeatures()) {
				for(AbstractFeatureType aType : aTypes) {
					if(((XPlanungModel)obj).getCadObjOfFeature(aType) != null) {
						if(((XPlanungModel)obj).getCadObjOfFeature(aType).isVisible()) {
							return true;
						}
					}
				}
			}
			
		}else if(obj instanceof Entry<?, ?>) {
			if(((Entry)obj).getValue() instanceof ArrayList) {
			
				ArrayList list = (ArrayList)((Entry)obj).getValue();
				
				for(Object o : list) {
					if(o instanceof AbstractFeatureType) {
						
						XPlanungModel model = findXPlanungModel(o);
						
						if(model.getCadObjOfFeature((AbstractFeatureType) o) != null) {
							if(model.getCadObjOfFeature((AbstractFeatureType) o).isVisible()) {
								return true;
							}
						}
					}
				}
			}
			if(((Entry)obj).getValue() instanceof HashMap) {
				
				HashMap map = (HashMap)((Entry)obj).getValue();
				
				for(Object key : map.values()) {
					
					if(key instanceof ArrayList) {
						
						ArrayList list = (ArrayList)key;
						
						for(Object o : list) {
							if(o instanceof AbstractFeatureType) {
								
								XPlanungModel model = findXPlanungModel(o);
								
								if(model.getCadObjOfFeature((AbstractFeatureType) o) != null) {
									if(model.getCadObjOfFeature((AbstractFeatureType) o).isVisible()) {
										return true;
									}
								}
							}
						}
					}
					
				}
			}
		}

		return false;
	}
	
	public static boolean isTransparent(Object obj) {
		if(obj instanceof FeaturePropertyType) {
			AbstractFeatureType aType = ((FeaturePropertyType)obj).getAbstractFeature().getValue();
			XPlanungModel model = findXPlanungModel(obj);
			
			if(model.getCadObjOfFeature(aType) != null) {				
				return model.getCadObjOfFeature(aType).isTransparent();
			}
		}else if(obj instanceof AbstractFeatureType) {
		
			AbstractFeatureType aType = (AbstractFeatureType)obj;
			XPlanungModel model = findXPlanungModel(obj);
			if(model.getCadObjOfFeature(aType) != null) {				
				return model.getCadObjOfFeature(aType).isTransparent();
			}
		}else if(obj instanceof XPlanungModel) {
			
			for(ArrayList<AbstractFeatureType> aTypes : ((XPlanungModel)obj).getAbstractFeatures()) {
				for(AbstractFeatureType aType : aTypes) {
					if(((XPlanungModel)obj).getCadObjOfFeature(aType) != null) {
						if(((XPlanungModel)obj).getCadObjOfFeature(aType).isTransparent()) {
							return true;
						}
					}
				}
			}
			
		}else if(obj instanceof Entry<?, ?>) {
			if(((Entry)obj).getValue() instanceof ArrayList) {
			
				ArrayList list = (ArrayList)((Entry)obj).getValue();
				
				for(Object o : list) {
					if(o instanceof AbstractFeatureType) {
						
						XPlanungModel model = findXPlanungModel(o);
						
						if(model.getCadObjOfFeature((AbstractFeatureType) o) != null) {
							if(model.getCadObjOfFeature((AbstractFeatureType) o).isTransparent()) {
								return true;
							}
						}
					}
				}
			}
			if(((Entry)obj).getValue() instanceof HashMap) {
				
				HashMap map = (HashMap)((Entry)obj).getValue();
				
				for(Object key : map.values()) {
					
					if(key instanceof ArrayList) {
						
						ArrayList list = (ArrayList)key;
						
						for(Object o : list) {
							if(o instanceof AbstractFeatureType) {
								
								XPlanungModel model = findXPlanungModel(o);
								
								if(model.getCadObjOfFeature((AbstractFeatureType) o) != null) {
									if(model.getCadObjOfFeature((AbstractFeatureType) o).isTransparent()) {
										return true;
									}
								}
							}
						}
					}
					
				}
			}
		}

		return false;
	}
	
	public static void setVisible(Object obj) {
		if(obj instanceof FeaturePropertyType) {
			AbstractFeatureType aType = ((FeaturePropertyType)obj).getAbstractFeature().getValue();
			setVisible(aType);
		}else if(obj instanceof AbstractFeatureType) {
			AbstractFeatureType aType = (AbstractFeatureType)obj;
			setVisible(aType);
		}else if(obj instanceof ArrayList) {
			ArrayList features = (ArrayList)obj;
			setVisible(features);
		}else  if(obj instanceof XPlanungModel) {
			for(ArrayList<AbstractFeatureType> aTypes : ((XPlanungModel)obj).getAbstractFeatures()) {
				setVisible(aTypes);
			}
		}else if(obj instanceof Entry<?, ?>) {
			/*if(((Entry)obj).getValue() instanceof ArrayList) {
				ArrayList list = (ArrayList)((Entry)obj).getValue();
				for(Object o : list) {
					if(o instanceof AbstractFeatureType) {
						setVisible((AbstractFeatureType)o);
					}
				}
			}*/
			
			if(((Entry<?, ?>)obj).getKey() instanceof Class) {
				ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) obj).getValue();
				setVisible(features);		
			}else if(((Entry<?, ?>)obj).getKey() instanceof String){
				HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) obj).getValue();
				for(Class classKey : features.keySet()) {					
					setVisible(features.get(classKey));
				}
			}
		}
		
		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	

	public static void setVisible(ArrayList<Object> objs) {
		for(Object o : objs) {			
			if(o instanceof FeaturePropertyType) {
				AbstractFeatureType aType = ((FeaturePropertyType)o).getAbstractFeature().getValue();
				setVisible(aType);
			}else if(o instanceof AbstractFeatureType) {
				AbstractFeatureType aType = (AbstractFeatureType)o;
				setVisible(aType);
			}else if(o instanceof ArrayList) {
				ArrayList features = (ArrayList)o;
				setVisible(features);
			}else if(o instanceof XPlanungModel) {
				for(ArrayList<AbstractFeatureType> aTypes : ((XPlanungModel)o).getAbstractFeatures()) {
					setVisible(aTypes);
				}
			}else if(o instanceof Entry<?, ?>) {
				/*if(((Entry)o).getValue() instanceof ArrayList) {
					ArrayList list = (ArrayList)((Entry)o).getValue();
					for(Object obj : list) {
						if(obj instanceof AbstractFeatureType) {
							setVisible((AbstractFeatureType)obj);
						}
					}
				}*/
				
				if(((Entry<?, ?>)o).getKey() instanceof Class) {
					ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) o).getValue();
					setVisible(features);		
				}else if(((Entry<?, ?>)o).getKey() instanceof String){
					HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
					for(Class classKey : features.keySet()) {					
						setVisible(features.get(classKey));
					}
				}
			}
		}	

		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	
	public static void setInvisible(Object obj) {
		if(obj instanceof FeaturePropertyType) {
			AbstractFeatureType aType = ((FeaturePropertyType)obj).getAbstractFeature().getValue();
			setInvisible(aType);
		}else if(obj instanceof AbstractFeatureType) {
			AbstractFeatureType aType = (AbstractFeatureType)obj;
			setInvisible(aType);
		}else if(obj instanceof ArrayList) {
			ArrayList features = (ArrayList) obj;
			setInvisible(features);
		}else if(obj instanceof XPlanungModel) {
			for(ArrayList<AbstractFeatureType> aTypes : ((XPlanungModel)obj).getAbstractFeatures()) {
				setInvisible(aTypes);
			}
		}else if(obj instanceof Entry<?, ?>) {
			/*if(((Entry)obj).getValue() instanceof ArrayList) {
				ArrayList list = (ArrayList)((Entry)obj).getValue();
				for(Object o : list) {
					if(o instanceof AbstractFeatureType) {
						setInvisible((AbstractFeatureType)o);
					}
				}
			}*/
			
			if(((Entry<?, ?>)obj).getKey() instanceof Class) {
				ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) obj).getValue();
				setInvisible(features);		
			}else if(((Entry<?, ?>)obj).getKey() instanceof String){
				HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) obj).getValue();
				for(Class classKey : features.keySet()) {					
					setInvisible(features.get(classKey));
				}
			}
			
		}
		
		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	
	public static void setInvisible(ArrayList<Object> objs) {
		for(Object o : objs) {			
			if(o instanceof FeaturePropertyType) {
				AbstractFeatureType aType = ((FeaturePropertyType)o).getAbstractFeature().getValue();
				setInvisible(aType);
			}else if(o instanceof AbstractFeatureType) {
				AbstractFeatureType aType = (AbstractFeatureType)o;
				setInvisible(aType);
			}else  if (o instanceof ArrayList) {
				ArrayList features = (ArrayList) o;
				setInvisible(features);
			}else if(o instanceof XPlanungModel) {
				for(ArrayList<AbstractFeatureType> aTypes : ((XPlanungModel)o).getAbstractFeatures()) {
					setInvisible(aTypes);
				}
			}else if(o instanceof Entry<?, ?>) {
				/*if(((Entry)o).getValue() instanceof ArrayList) {
					ArrayList list = (ArrayList)((Entry)o).getValue();
					for(Object obj : list) {
						if(obj instanceof AbstractFeatureType) {
							setInvisible((AbstractFeatureType)obj);
						}
					}
				}*/
				
				if(((Entry<?, ?>)o).getKey() instanceof Class) {
					ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) o).getValue();
					setInvisible(features);		
				}else if(((Entry<?, ?>)o).getKey() instanceof String){
					HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
					for(Class classKey : features.keySet()) {					
						setInvisible(features.get(classKey));
					}
				}
			}
		}
			
		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	
	public static void select(Object o) {
		if (o instanceof XPlanungModel) {
			select((XPlanungModel)o);
		}else if (o instanceof Pair<?, ?>) {
			Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair = (Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>>)o;
			select(pair);
		}else if (o instanceof ArrayList) {
			ArrayList features = (ArrayList) o;
			select(features);
		}else if (o instanceof AbstractFeatureType) {
			AbstractFeatureType feature = (AbstractFeatureType) o;
			select(feature);
		}else if(o instanceof Entry<?, ?>) {
			if(((Entry<?, ?>)o).getKey() instanceof Class) {
				ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) o).getValue();
				select(features);		
			}else if(((Entry<?, ?>)o).getKey() instanceof String){
				HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
				for(Class classKey : features.keySet()) {					
					select(features.get(classKey));
				}
			}
		}

	}
	
	public static void select(ArrayList<Object> objs) {
		for(Object o : objs) {			
			select(o);
		}
	}
	
	public static void deselect(Object o) {
		if (o instanceof XPlanungModel) {
			deselect((XPlanungModel)o);
		}else if (o instanceof Pair<?, ?>) {
			Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair = (Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>>)o;
			deselect(pair);
		}else if (o instanceof AbstractFeatureType) {
			AbstractFeatureType feature = (AbstractFeatureType) o;
			deselect(feature);
		}else if (o instanceof ArrayList) {
			ArrayList features = (ArrayList) o;
			deselect(features);
		}else if(o instanceof Entry<?, ?>) {
			if(((Entry<?, ?>)o).getKey() instanceof Class) {
				ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) o).getValue();
				deselect(features);		
			}else if(((Entry<?, ?>)o).getKey() instanceof String){
				HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
				for(Class classKey : features.keySet()) {					
					deselect(features.get(classKey));
				}
			}
		}
		
		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	
	public static void deselect(ArrayList<Object> objs) {
		for(Object o : objs) {			
			deselect(o);
		}
	}
	
	public static void setTransparent(Object o) {
		if (o instanceof XPlanungModel) {
			setTransparent((XPlanungModel)o);
		}else if (o instanceof Pair<?, ?>) {
			Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair = (Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>>)o;
			setTransparent(pair);
		}else if (o instanceof AbstractFeatureType) {
			AbstractFeatureType feature = (AbstractFeatureType) o;
			setTransparent(feature);
		}else if (o instanceof ArrayList) {
			ArrayList features = (ArrayList) o;
			setTransparent(features);
		}else if(o instanceof Entry<?, ?>) {
			if(((Entry<?, ?>)o).getKey() instanceof Class) {
				ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) o).getValue();
				setTransparent(features);		
			}else if(((Entry<?, ?>)o).getKey() instanceof String){
				HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
				for(Class classKey : features.keySet()) {					
					setTransparent(features.get(classKey));
				}
			}
		}

		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	
	public static void setTransparent(ArrayList<Object> objs) {
		for(Object o : objs) {
			if (o instanceof XPlanungModel) {
				setTransparent((XPlanungModel)o);
			}else if (o instanceof Pair<?, ?>) {
				Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>> pair = (Pair<Object, HashMap<Object, ArrayList<AbstractFeatureType>>>)o;
				setTransparent(pair);
			}else if(o instanceof ArrayList) {
				ArrayList features = (ArrayList)o;
				setTransparent(features);
			}else if (o instanceof AbstractFeatureType) {
				AbstractFeatureType feature = (AbstractFeatureType) o;
				setTransparent(feature);
			}else if(o instanceof Entry<?, ?>) {
				if(((Entry<?, ?>)o).getKey() instanceof Class) {
					ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry) o).getValue();
					setTransparent(features);		
				}else if(((Entry<?, ?>)o).getKey() instanceof String){
					HashMap<Class, ArrayList<AbstractFeatureType>> features = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
					for(Class classKey : features.keySet()) {					
						setTransparent(features.get(classKey));
					}
				}
			}
		}

		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().refresh();
		//XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView().refresh();
	}
	
	public static String getRepresentionString(Object obj) {
		if(obj instanceof Class<?>) {
			return ((Class) obj).getSimpleName();
		}else if(obj instanceof FeaturePropertyType) {
			return ((FeaturePropertyType)obj).getAbstractFeature().getValue().getId();
		}else if(obj instanceof AbstractFeatureType) {
			return ((AbstractFeatureType)obj).getId();
		}else if(obj instanceof XPlanungModel) {
			return ((XPlanungModel)obj).getModelName();
		}else if(obj instanceof Entry<?, ?>) {
			if(((Entry)obj).getKey() instanceof Class) {
				return ((Class)((Entry)obj).getKey()).getSimpleName();
			}else if(((Entry)obj).getKey() instanceof String) {
				return "Ebene: " + ((String)((Entry)obj).getKey());
			}
		}
		
		return obj.toString();
	}
}
