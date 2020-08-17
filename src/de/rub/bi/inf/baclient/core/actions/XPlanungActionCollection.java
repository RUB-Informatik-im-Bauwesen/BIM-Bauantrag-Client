package de.rub.bi.inf.baclient.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;

import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import de.rub.bi.inf.baclient.core.model.ChoiceProperty;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.views.ifc.AddXPlanungToIfcModelView;
import de.rub.bi.inf.baclient.core.views.ifc.IfcExtrusionModelCreator;
import javafx.application.Platform;
import net.opengis.gml._3.AbstractFeatureType;

public class XPlanungActionCollection {

	public static class SelectAction implements ActionListener {
	
		private Object obj = null;
		
		public SelectAction(Object obj){
			this.obj = obj;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(obj instanceof ArrayList) {
				XPlanungActionUtils.select((ArrayList)obj);
			}else {				
				XPlanungActionUtils.select(obj);			
			}
			
		}
	};
	
	public static class DeselectAction implements ActionListener {
		
		private Object obj = null;
		
		public DeselectAction(Object obj){
			this.obj = obj;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
		
			if(obj instanceof ArrayList) {
				XPlanungActionUtils.deselect((ArrayList)obj);
			}else {				
				XPlanungActionUtils.deselect(obj);			
			}
		
		}
	};
	
	public static class HideAction implements ActionListener {
		
		private Object obj = null;
		
		public HideAction(Object obj){
			this.obj = obj;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(obj instanceof ArrayList) {
				XPlanungActionUtils.setInvisible((ArrayList)obj);
			}else {				
				XPlanungActionUtils.setInvisible(obj);			
			}
		}
	};
	
	public static class ShowAction implements ActionListener {
		
		private Object obj = null;
		
		public ShowAction(Object obj){
			this.obj = obj;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(obj instanceof ArrayList) {
				XPlanungActionUtils.setVisible((ArrayList)obj);
			}else {				
				XPlanungActionUtils.setVisible(obj);			
			}
		}
	};
	
	public static class ShowOnlyAction implements ActionListener {
		
		private Object obj = null;
		
		public ShowOnlyAction(Object obj){
			this.obj = obj;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			for(XPlanungModel model : XPlanungModelContainer.getInstance().getModels()) {
				XPlanungActionUtils.setInvisible(model);
			}
			
			if(obj instanceof ArrayList) {
				XPlanungActionUtils.setVisible((ArrayList)obj);
			}else {				
				XPlanungActionUtils.setVisible(obj);			
			}
		}
	};
	
	public static class TransparencyAction implements ActionListener {
		
		private Object obj = null;
		
		public TransparencyAction(Object obj){
			this.obj = obj;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(obj instanceof ArrayList) {
				XPlanungActionUtils.setTransparent((ArrayList)obj);
			}else {				
				XPlanungActionUtils.setTransparent(obj);			
			}
		}
	};
	
	public static class AddToIfcAction implements ActionListener {
			
		private ArrayList<Object> objs = null;
		
		public AddToIfcAction(ArrayList<Object> objs){
			this.objs = objs;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			CountDownLatch doneLatch = new CountDownLatch(1);
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
				
					ArrayList<ChoiceProperty<IfcModel>> nodes = new ArrayList<>();
					
					for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
						nodes.add(new ChoiceProperty<IfcModel>((IfcModel)node.getStepModel()) {
							@Override
							public String toString() {
								return node.toString();
							}
						});
					}
					
					ChoiceProperty<IfcModel> newIFC4Model = new ChoiceProperty<IfcModel>(IfcUtil.createEmptyModel(IfcSchema.IFC4)) {
						@Override
						public String toString() {
							return "New IFC4 Model";
						}
					};
					nodes.add(newIFC4Model);
					
					ChoiceProperty<IfcModel> newIFC2x3Model = new ChoiceProperty<IfcModel>(IfcUtil.createEmptyModel(IfcSchema.IFC2X3)) {
						@Override
						public String toString() {
							return "New IFC2x3 Model";
						}
					};
					nodes.add(newIFC2x3Model);
					
					XPlanungModel xModel = null;
					ArrayList<AbstractFeatureType> features = new ArrayList<>();

					for(Object o : objs) {

						xModel = XPlanungActionUtils.findXPlanungModel(o);
						
						if (o instanceof AbstractFeatureType) {
							AbstractFeatureType feature = (AbstractFeatureType) o;
							features.add(feature);
						}else if(o instanceof Entry<?, ?>) {
							
							if(((Entry<?, ?>)o).getKey() instanceof Class) {
								features.addAll((ArrayList<AbstractFeatureType>) ((Entry) o).getValue());
							}else if(((Entry<?, ?>)o).getKey() instanceof String) {
								
								HashMap<Class, ArrayList<AbstractFeatureType>> featureMaps = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
								
								for(ArrayList<AbstractFeatureType> afList : featureMaps.values()) {
									features.addAll(afList);
								}
								
							}
						}
					}

					if(xModel != null) {						
						new AddXPlanungToIfcModelView(newIFC4Model, nodes, features, xModel.getLocalTranslation());
					}
					
					doneLatch.countDown();
				}
			
			});
		}
	}
	
	
	public static class CreateExtusionModelAction implements ActionListener {
		
		private ArrayList<Object> objs = null;
		
		public CreateExtusionModelAction(ArrayList<Object> objs){
			this.objs = objs;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {

			CountDownLatch doneLatch = new CountDownLatch(1);
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {

					XPlanungModel model = null;
					ArrayList<AbstractFeatureType> features = new ArrayList<>();
					
					for(Object o : objs) {
						model = (XPlanungModel)XPlanungActionUtils.findXPlanungModel(o);
						
						if (o instanceof AbstractFeatureType) {
							AbstractFeatureType feature = (AbstractFeatureType) o;
							features.add(feature);
						}else if(o instanceof Entry<?, ?>) {
							
							if(((Entry<?, ?>)o).getKey() instanceof Class) {
								features.addAll((ArrayList<AbstractFeatureType>) ((Entry) o).getValue());
							}else if(((Entry<?, ?>)o).getKey() instanceof String) {
								
								HashMap<Class, ArrayList<AbstractFeatureType>> featureMap = (HashMap<Class, ArrayList<AbstractFeatureType>>) ((Entry) o).getValue();
								
								for(ArrayList<AbstractFeatureType> afList : featureMap.values()) {
									features.addAll(afList);
								}

							}
						}
					}
					
					new IfcExtrusionModelCreator(features, model);
					
				}
			});
			doneLatch.countDown();
		}
	}
					
}
