package de.rub.bi.inf.baclient.core.views.pruefungen;


import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.workflow.pruefung.Pruefvorgang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeTableRow;

public class ModellpruefungContextMenu extends ContextMenu {

	private Object item;

	public ModellpruefungContextMenu(TreeTableRow<Object> row) {
        
		this.item = row.getItem();

        if (item instanceof Pruefvorgang) {
        	MenuItem menuItem = new MenuItem("Selektiere Regelobjekte");  
	        menuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {	
					Pruefvorgang ro =  (Pruefvorgang) item;
					
					ro.getRegelObjekte().forEach(space ->{
						space.setSelected(true);
					});

	    			XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
				}
			});

	        this.getItems().add(menuItem);
	        
	        MenuItem menuItem2 = new MenuItem("Deselektiere Regelobjekte");  
	        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {	
					Pruefvorgang ro =  (Pruefvorgang) item;
					
					ro.getRegelObjekte().forEach(space ->{
						space.setSelected(false);
					});

	    			XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
				}
			});

	        this.getItems().add(menuItem2);
	        
	        this.getItems().add(new SeparatorMenuItem());
	        
	        MenuItem menuItem3 = new MenuItem("Verstecken");
	        menuItem3.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {	
					Pruefvorgang ro =  (Pruefvorgang) item;
					
					ro.getRegelObjekte().forEach(space ->{
						space.setVisible(false);
					});

	    			XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
				}
			});

	        this.getItems().add(menuItem3);
	        
	        MenuItem menuItem4 = new MenuItem("Anzeigen");
	        menuItem4.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {	
					Pruefvorgang ro =  (Pruefvorgang) item;
					
					ro.getRegelObjekte().forEach(space ->{
						space.setVisible(true);
					});

	    			XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();
				}
			});

	        this.getItems().add(menuItem4);
		}
        
       
	}
	
	
}
