package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement3D;
import com.apstex.ifctoolbox.ifc.IfcCartesianPoint;
import com.apstex.ifctoolbox.ifc.IfcDirection;
import com.apstex.ifctoolbox.ifc.IfcLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcLocalPlacement;
import com.apstex.ifctoolbox.ifc.IfcObjectPlacement;
import com.apstex.ifctoolbox.ifc.IfcSite;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Vector3d;
import com.apstex.step.core.LIST;
import com.apstex.step.core.SET;
import com.apstex.step.model.StepModel;

import de.rub.bi.inf.baclient.core.model.ChoiceProperty;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class IfcSaveAction implements ActionListener {
	
	private ConcurrentHashMap<String, ApplicationModelNode> applicationModelNodeMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, File> fileMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Boolean> useLocalCoordinatesMap = new ConcurrentHashMap<>();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		final String sessionID = UUID.randomUUID().toString();
		
		CountDownLatch doneLatch = new CountDownLatch(1);
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				
				ArrayList<ChoiceProperty<ApplicationModelNode>> choices = new ArrayList<>(Kernel.getApplicationModelRoot().getNodes().size());
				Kernel.getApplicationModelRoot().getNodes().forEach(node -> {
					ChoiceProperty<ApplicationModelNode> property = new ChoiceProperty<ApplicationModelNode>(node);
					choices.add(property);
				});
				
				ChoiceDialog<ChoiceProperty<ApplicationModelNode>> dialog = 
						new ChoiceDialog<ChoiceProperty<ApplicationModelNode>>(null, choices);
				dialog.setTitle("Select Ifc Model");
				dialog.setHeaderText("Select IFC Model to be exported:");
			    dialog.setContentText("Ifc Model: ");
			    GridPane grid = (GridPane) dialog.getDialogPane().getContent();
				
			    grid.setHgap(10);
			    grid.setVgap(10);
			    grid.add(new Label("Use local coordinates?"), 0, 1);
			    CheckBox checkBox = new CheckBox();
			    checkBox.selectedProperty().set(true);
			    grid.add(checkBox, 1, 1);
			    	   
				Optional<ChoiceProperty<ApplicationModelNode>> result = dialog.showAndWait();
			   
				
			    if (result.isPresent()) {
			    	applicationModelNodeMap.put(sessionID, result.get().getValue());
			    	useLocalCoordinatesMap.put(sessionID, checkBox.selectedProperty().get());
			    	
			    	FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Save IFC Model: "+result.get().getValue().getModelName());
					fileChooser.getExtensionFilters().addAll(
					         new ExtensionFilter("IFC-Files", "*.ifc"));
					File selectedFile = fileChooser.showSaveDialog(null);
					
					if(selectedFile!=null) {
						fileMap.put(sessionID, selectedFile);
					}
				
			    }
			   	    
				doneLatch.countDown();
				
			}
		});
		
		try {
			doneLatch.await();
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ApplicationModelNode node = applicationModelNodeMap.get(sessionID);
		File exportFile = fileMap.get(sessionID);
		Boolean useLocalCoordiantes = useLocalCoordinatesMap.get(sessionID);
				
		if(node !=null && 
		   exportFile!=null) {
			
			StepModel stepModel = node.getStepModel();
			Collection<IfcSite> sites = stepModel.getCollection(IfcSite.class);
			IfcSite site = sites.iterator().next();
			boolean isIFC2x3 = (site instanceof IfcSite.Ifc2x3);
			IfcObjectPlacement oldplacement = site.getObjectPlacement();
			
			
			((IfcObjectPlacement.Ifc2x3)oldplacement).getReferencedByPlacements_Inverse();

			SET<? extends IfcLocalPlacement> referencedBy;
			
			if(oldplacement != null) {
				referencedBy = isIFC2x3?
						((IfcObjectPlacement.Ifc2x3)oldplacement).getReferencedByPlacements_Inverse():
						((IfcObjectPlacement.Ifc4)oldplacement).getReferencedByPlacements_Inverse();
			}else {
				referencedBy = new SET<>();
			}
	

			IfcCartesianPoint newLocation = null;
			IfcAxis2Placement3D newAxis2Placement = null;
			IfcLocalPlacement newLocalPlacement = null;
			
			if(useLocalCoordiantes){
				//Assign local Coordinates
				Transform3D transform = node.getCadObjectModel().getRootBranchGroup().getTransform();
				Vector3d translation = new Vector3d();
				transform.get(translation);
				System.out.println(translation);
				
				//Get current translation
				IfcDirection axis = null;
				IfcDirection refDirection = null;
				double x=0, y=0, z=0;
				if(oldplacement !=null) {
					IfcLocalPlacement currentPlacement = (IfcLocalPlacement) oldplacement;
					IfcAxis2Placement3D currentAxis2Placement3d = (IfcAxis2Placement3D) currentPlacement.getRelativePlacement();
					axis = currentAxis2Placement3d.getAxis();
					refDirection = currentAxis2Placement3d.getRefDirection();
					IfcCartesianPoint currentLocation = currentAxis2Placement3d.getLocation();
					x = currentLocation.getCoordinates().get(0).getValue();
					y = currentLocation.getCoordinates().get(1).getValue();
					z = currentLocation.getCoordinates().get(2).getValue();
				}
				
				
				
				//create new substitute placement
				LIST<IfcLengthMeasure> measures = new LIST<>(
						isIFC2x3?new IfcLengthMeasure.Ifc2x3(x+translation.x):new IfcLengthMeasure.Ifc4(x+translation.x),
						isIFC2x3?new IfcLengthMeasure.Ifc2x3(y+translation.y):new IfcLengthMeasure.Ifc4(y+translation.y),
						isIFC2x3?new IfcLengthMeasure.Ifc2x3(z):new IfcLengthMeasure.Ifc4(z));
				newLocation = isIFC2x3? new IfcCartesianPoint.Ifc2x3.Instance(measures):
					new IfcCartesianPoint.Ifc4.Instance(measures);
				
				
				newAxis2Placement = isIFC2x3? new IfcAxis2Placement3D.Ifc2x3.Instance():
					new IfcAxis2Placement3D.Ifc4.Instance();
				newAxis2Placement.setLocation(newLocation);
				if (axis!=null) newAxis2Placement.setAxis(axis);
				if (refDirection!=null) newAxis2Placement.setRefDirection(refDirection);
				
				newLocalPlacement = isIFC2x3? new IfcLocalPlacement.Ifc2x3.Instance():
					new IfcLocalPlacement.Ifc4.Instance();
				newLocalPlacement.setRelativePlacement((IfcAxis2Placement) newAxis2Placement);
				
				stepModel.addObject(newLocation);
				stepModel.addObject(newAxis2Placement);
				stepModel.addObject(newLocalPlacement);
				
				
				//tranform placement
				
				for (IfcLocalPlacement ref : referencedBy) {
					ref.setPlacementRelTo((IfcObjectPlacement) newLocalPlacement);
				};
				site.setObjectPlacement((IfcObjectPlacement) newLocalPlacement);
				
			}
	        
			
			try {
				//write file
				node.getStepModel().writeStepFile(exportFile);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				//rollback site transform
				if(useLocalCoordiantes){	
					site.setObjectPlacement(oldplacement);
					for (IfcLocalPlacement ref : referencedBy) {
						ref.setPlacementRelTo((IfcObjectPlacement) oldplacement);
					};
					stepModel.removeObject(newLocation);
					stepModel.removeObject(newAxis2Placement);
					stepModel.removeObject(newLocalPlacement);
				}
			}
		}
		
		applicationModelNodeMap.remove(sessionID);
		fileMap.remove(sessionID);
		useLocalCoordinatesMap.remove(sessionID);
		
		

	}

	

}
