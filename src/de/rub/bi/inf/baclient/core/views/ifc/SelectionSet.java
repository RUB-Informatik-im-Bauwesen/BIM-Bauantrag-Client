package de.rub.bi.inf.baclient.core.views.ifc;

import java.util.ArrayList;
import java.util.List;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcWall;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.views.coloring.ColorRule;
import de.rub.bi.inf.baclient.core.views.coloring.ColorSchema;
import de.rub.bi.inf.baclient.core.views.coloring.ColorSchemaManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;

public class SelectionSet {
	
	private String name; 
	private ArrayList<ClassInterface> selection;
	private ComboBox<ColorSchema> colorSchemaSelector = null;
	
	public SelectionSet(String name, ArrayList<ClassInterface> arrayList) {
		this.name = name;
		this.selection = arrayList;
		this.colorSchemaSelector = new ComboBox<ColorSchema>();
		this.colorSchemaSelector.setPrefSize(175, 10);
		
		this.colorSchemaSelector.setOnAction(new EventHandler() {
			@Override
			public void handle(Event event) {
				applyColorSchema();				
			}
		});
		
		//Register Combobox in ColorSchemaManager
		ColorSchemaManager.getInstance().registerComboBox(this.colorSchemaSelector);

		
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<ClassInterface> getSelection(){
		return selection;
	}

	public Color getColorSchemaOf(ClassInterface ifcObj) {
		return Color.BLUE; //TODO
	}
	
	public void setColorSchema(ColorSchema schema) {
		colorSchemaSelector.getSelectionModel().select(schema);
	}
	
	public ColorSchema getColorSchema() {
		return colorSchemaSelector.getSelectionModel().getSelectedItem();
	}
	
	public void applyColorSchema() {
		for(ClassInterface ifcObj : selection) {
			if(getColorSchema() != null) {
				for(ColorRule rule : getColorSchema().getRules()) {
					
					if(rule.isValid(ifcObj)) {
						
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
							CadObjectJ3D cadobj = (CadObjectJ3D)node.getCadObjectModel().getCadObject(ifcObj);
							
							if(cadobj != null) {
								java.awt.Color color = new java.awt.Color(
										(float)rule.getColor().getRed(), 
										(float)rule.getColor().getGreen(), 
										(float)rule.getColor().getBlue(), 
										(float)rule.getColor().getOpacity()
								);
								
								cadobj.colorize(color);
							}
							
						}
						
					}
				}
			}
		}
	}
	
	public void resetColorSchema() {
		for(ClassInterface ifcObj : selection) {
			for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
				CadObjectJ3D cadobj = (CadObjectJ3D)node.getCadObjectModel().getCadObject(ifcObj);
				
				if(cadobj != null) {
					cadobj.resetColorization();					
				}
				
			}
		}
		colorSchemaSelector.getSelectionModel().clearSelection();
	}
	
	public ComboBox<ColorSchema> getColorSchemaCombobox() {
		return colorSchemaSelector;
	}
}
