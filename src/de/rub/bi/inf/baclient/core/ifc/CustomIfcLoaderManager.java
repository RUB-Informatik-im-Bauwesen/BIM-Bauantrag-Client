package de.rub.bi.inf.baclient.core.ifc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement3D;
import com.apstex.ifctoolbox.ifc.IfcCartesianPoint;
import com.apstex.ifctoolbox.ifc.IfcDirection;
import com.apstex.ifctoolbox.ifc.IfcGeometricRepresentationContext;
import com.apstex.ifctoolbox.ifc.IfcGeometricRepresentationSubContext;
import com.apstex.ifctoolbox.ifc.IfcLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcLocalPlacement;
import com.apstex.ifctoolbox.ifc.IfcObjectPlacement;
import com.apstex.ifctoolbox.ifc.IfcReal;
import com.apstex.ifctoolbox.ifc.IfcSite;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Vector3d;
import com.apstex.step.core.LIST;
import com.apstex.step.model.StepModel;

import de.rub.bi.inf.baclient.core.model.ChoiceProperty;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import net.opengis.gml._3.BoundingShapeType;
import net.opengis.gml._3.DirectPositionType;

public class CustomIfcLoaderManager extends IfcLoadManager {
	 
    private static CustomIfcLoaderManager instance = null;
	
    
    public static CustomIfcLoaderManager getInstance() {
    	if(instance==null) {
			instance = new CustomIfcLoaderManager();
		}	
		return instance;
	}
	
	private CustomIfcLoaderManager() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	@Override
	protected ApplicationModelNode createApplicationModelNode(StepModel stepModel, Map<String, Object> arg1, Object arg2) {
		// TODO Auto-generated method stub
		
		
		ArrayList<ChoiceProperty<XPlanungModel>> choices = new ArrayList<>(XPlanungModelContainer.getInstance().getModels().size());
		XPlanungModelContainer.getInstance().getModels().forEach(model -> {
			ChoiceProperty<XPlanungModel> property = new ChoiceProperty<XPlanungModel>(model);
			choices.add(property);
		});
				
		final ArrayList<XPlanungModel> resultList = new ArrayList<>(1);
		final ArrayList<Boolean> useTrueNorthList = new ArrayList<>(1);
		

		if (choices.size() > 0) {
			CountDownLatch doneLatch = new CountDownLatch(1);
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					
					
					Dialog<ChoiceProperty<XPlanungModel>> dialog = 
							new ChoiceDialog<ChoiceProperty<XPlanungModel>>(null, choices);
					dialog.setTitle("Ausrichtung am BPlan");
					dialog.setHeaderText("Am welchem BPlan soll ausgerichtet werden?");
				    dialog.setContentText("BPlan: ");
				    
				    GridPane grid = (GridPane) dialog.getDialogPane().getContent();
				    grid.setHgap(10);
				    grid.setVgap(10);
				    Label label = new Label("Nachträglich am Projektnorden ausrichten? \n (Archicad=ja, Revit=nein)");
				    label.setWrapText(true);
				    grid.add(label, 0, 1);
				    CheckBox checkBox = new CheckBox();
				    checkBox.selectedProperty().set(true);
				    grid.add(checkBox, 1, 1);
				    
				    
				    
					Optional<ChoiceProperty<XPlanungModel>> result = dialog.showAndWait();
				    
				    if (result.isPresent()) {
				    	resultList.add(result.get().getValue());
				    	useTrueNorthList.add(checkBox.isSelected());
				    }
					doneLatch.countDown();
				}
			});
			
		
			
			try {
				doneLatch.await(); //Wait for JavaFX Thread to be ended
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Decoding dialog results to proper variables
		XPlanungModel bplanModel = resultList.size()>0?resultList.get(0):null;
		boolean useTrueNorth = useTrueNorthList.size()>0?useTrueNorthList.get(0):false; 
	
		
		
		
		Collection<IfcSite> sites = stepModel.getCollection(IfcSite.class);
		IfcSite site = sites.iterator().next();
		
		Collection<IfcGeometricRepresentationContext> contexts = stepModel.getCollection(IfcGeometricRepresentationContext.class);
		IfcGeometricRepresentationContext modelContext=null;
		for(IfcGeometricRepresentationContext context : contexts){
			if (! (context instanceof IfcGeometricRepresentationSubContext) &&
					context.getContextType().getDecodedValue().equals("Model")) {
				modelContext = context;
				break;
			}	
		}
		
		if(modelContext!=null && useTrueNorth) {
			IfcDirection trueNorth = modelContext.getTrueNorth();
			trueNorth.getDirectionRatios();
			
			IfcLocalPlacement placement = (IfcLocalPlacement) site.getObjectPlacement();
			IfcAxis2Placement3D axis2placement3d = (IfcAxis2Placement3D) placement.getRelativePlacement();
			
			LIST<IfcReal> newRatios = new LIST<>();
			newRatios.add(trueNorth.getDirectionRatios().get(0));
			newRatios.add(trueNorth.getDirectionRatios().get(1));
			newRatios.add(axis2placement3d.getRefDirection().getDirectionRatios().get(2));
			
			axis2placement3d.getRefDirection().setDirectionRatios(newRatios);
			
			System.out.println("TrueNorth:"+trueNorth.getDirectionRatios().get(0)+", "+trueNorth.getDirectionRatios().get(1));
			
		}

		
		ApplicationModelNode node = super.createApplicationModelNode(stepModel, arg1, arg2);

		
		if (bplanModel!=null) {
			
			Double[] wgsLocation = IfcUtil.getSiteLocation(site);
			

			double[] epsgLoc = Geolocation.getInstance()
					.transformWGS84ToEPSG25832(wgsLocation[0],wgsLocation[1]);
			Logger.getLogger("hsqldb.db").setLevel(Level.WARNING); //silence the hsqldb logger
			
			System.out.println("EPSG: "+epsgLoc[0]+", "+epsgLoc[1]);
					
			BoundingShapeType boundingShape = bplanModel.getFeatureCollectionType().getBoundedBy();
			DirectPositionType lowerCorner = boundingShape.getEnvelope().getLowerCorner();
			List<Double> values = lowerCorner.getValue();
			double x = values.get(0);
			double y = values.get(1);
			
			
			Transform3D ifcTransform = new Transform3D();
			ifcTransform.setTranslation(new Vector3d
					(epsgLoc[0]-x, 
					 epsgLoc[1]-y, 
					 0));
			node.getCadObjectModel().getRootBranchGroup().setTransform(ifcTransform);
		}
		
		
		if(stepModel instanceof IfcModel && arg1 != null) {
			if(arg1.get("TEMP_FILE_NAME") != null) {
				node.setModelName(arg1.get("TEMP_FILE_NAME").toString());
			}
		}
		
		return node;
	}


}
