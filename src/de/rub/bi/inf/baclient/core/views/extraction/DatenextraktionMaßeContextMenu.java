package de.rub.bi.inf.baclient.core.views.extraction;


import java.util.ArrayList;
import java.util.Collection;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.ifctoolbox.ifc.IfcProduct;
import com.apstex.ifctoolbox.ifc.IfcSpace;

import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.workflow.extraktion.ExtraktionsVorgang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeTableRow;

public class DatenextraktionMaßeContextMenu extends ContextMenu {

	private Object item;

	public DatenextraktionMaßeContextMenu(TreeTableRow<Object> row) {
        
		this.item = row.getItem();
		
		if(item instanceof ExtraktionsVorgang) {
			
			Menu menu = new Menu("Zeige Prüfobjekte");
			
	        MenuItem menuItem = new MenuItem("Markieren");  
	        menuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if (item instanceof ExtraktionsVorgang) {
						ExtraktionsVorgang po =  (ExtraktionsVorgang) item;
						po.getBimObjekte().forEach(classinterface ->{
							po.getModelNode().getSelectionModel().select(null, classinterface);
						});
						
					
					}
					
				}
			});
	        menu.getItems().add(menuItem);
	        
	        menuItem = new MenuItem("Markieren, Spaces aus");  
	        menuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if (item instanceof ExtraktionsVorgang) {
						ExtraktionsVorgang po =  (ExtraktionsVorgang) item;
						
						po.getBimObjekte().forEach(space ->{
							po.getModelNode().getSelectionModel().select(null, space);
						});
						
						Collection<IfcSpace> allSpaces = po.getModelNode().getStepModel().getCollection(IfcSpace.class);
						allSpaces.forEach(space -> {
							if (po.getBimObjekte().contains(space)==false) {
								CadObject cad = po.getModelNode().getCadObjectModel().getCadObject(space);
								if (cad != null)
									po.getModelNode().getVisibilityModel().setVisible(cad, false, null);
								
							}
						});
						
					
					}
					
				}
			});
	        menu.getItems().add(menuItem);
	        
	        menuItem = new MenuItem("Markieren, Gebäude transparent"); 
	        menuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if (item instanceof ExtraktionsVorgang) {
						ExtraktionsVorgang po =  (ExtraktionsVorgang) item;
						
						po.getBimObjekte().forEach(space ->{
							po.getModelNode().getSelectionModel().select(null, space);
						});
						
						Collection<IfcProduct> allProducts = po.getModelNode().getStepModel().getCollection(IfcProduct.class);
						allProducts.forEach(product -> {
							if (po.getBimObjekte().contains(product)==false) {
								CadObject cad = po.getModelNode().getCadObjectModel().getCadObject(product);
								if (cad != null)
									po.getModelNode().getVisibilityModel().setTransparent(cad, true, null);
								
							}
						});
					
					
					}
					
				}
			});
	        menu.getItems().add(menuItem);
	        
	        menuItem = new MenuItem("Markieren, Gebäude transparent, Spaces aus"); 
	        menuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if (item instanceof ExtraktionsVorgang) {
						ExtraktionsVorgang po =  (ExtraktionsVorgang) item;
						
						po.getBimObjekte().forEach(space ->{
							po.getModelNode().getSelectionModel().select(null, space);
						});
						
						Collection<IfcProduct> allProducts = po.getModelNode().getStepModel().getCollection(IfcProduct.class);
						allProducts.forEach(product -> {
							if (po.getBimObjekte().contains(product)==false) {
								CadObject cad = po.getModelNode().getCadObjectModel().getCadObject(product);
								if (cad != null)
									po.getModelNode().getVisibilityModel().setTransparent(cad, true, null);
								
							}
						});
						
						Collection<IfcSpace> allSpaces = po.getModelNode().getStepModel().getCollection(IfcSpace.class);
						allSpaces.forEach(space -> {
							if (po.getBimObjekte().contains(space)==false) {
								CadObject cad = po.getModelNode().getCadObjectModel().getCadObject(space);
								if (cad != null)
									po.getModelNode().getVisibilityModel().setVisible(cad, false, null);
								
							}
						});
						
					
					}
					
				}
			});
	        menu.getItems().add(menuItem);
	        
	        this.getItems().add(menu);
	        
	        ExtraktionsVorgang po =  (ExtraktionsVorgang) item;
	        if(po.getErstellteGeometrien().size()>0) {
	        	
	        	for (String key: po.getErstellteGeometrien().keySet()) {
	        		
	        		Menu menu2 = new Menu(key);
	        		ArrayList<CadObjectJ3D> cads = po.getErstellteGeometrien().get(key);
	        		
	        		menuItem = new MenuItem("Anzeigen");
	        		menuItem.setOnAction(new EventHandler<ActionEvent>() {
	       				
	       				@Override
	       				public void handle(ActionEvent event) {
	       					cads.forEach(cad -> {
	       						cad.setVisible(true);		
	       					});
	       					
	       					XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();	
	       				}
	       			});
	        		menu2.getItems().add(menuItem);
	        		
	        		menuItem = new MenuItem("Verstecken");
	        		menuItem.setOnAction(new EventHandler<ActionEvent>() {
	       				
	       				@Override
	       				public void handle(ActionEvent event) {
	       					cads.forEach(cad -> {
	       						cad.setVisible(false);		
	       					});
	       					
	       					XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();	
	       				}
	       			});
	        		menu2.getItems().add(menuItem);
	        		
	        		this.getItems().add(menu2);
	        		
	        	}
	        	       	
	        	
	        	
	        	
				
	        }
	        
	        
	        if (po.getGruppierteBimObjekte().size()>0) {
	        	
	        	Menu menu3 = new Menu("Gruppierte BIM-Objekt");
	   
	        	
	        	po.getGruppierteBimObjekte().entrySet().forEach(grBimObj ->{
	        		
	        		MenuItem item3 = new MenuItem(grBimObj.getKey());
	        		item3.setOnAction(new EventHandler<ActionEvent>() {
	       				
	       				@Override
	       				public void handle(ActionEvent event) {
	       					grBimObj.getValue().forEach(ifcObj -> {
	       						po.getModelNode().getSelectionModel().select(null, ifcObj);
	       					});
	       					
	       					XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d().repaint();	
	       				}
	       			});
	        		menu3.getItems().add(item3);
	        		
	        	});
	        	
	        	this.getItems().add(menu3);
	        	
	        }
	        
	       
			
		}
        
       
	}
	
	
}
