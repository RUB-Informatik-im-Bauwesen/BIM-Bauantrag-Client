package de.rub.bi.inf.baclient.core.views.ifc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.actions.XPlanungActionCollection;
import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import de.rub.bi.inf.baclient.core.model.ChoiceProperty;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.utils.UIUtilities;
import de.rub.bi.inf.baclient.core.views.ifc.AddXPlanungToIfcModelView;
import de.rub.bi.inf.baclient.core.views.ifc.IfcExtrusionModelCreator;
import javafx.application.Platform;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.util.Pair;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.FeaturePropertyType;
import net.opengis.gml._3.PolygonType;

/**
 * Contains the PopUp-Menu Content if a template is right clicked.
 *  
 * @author Marcel Stepien
 *
 */
public class IfcSelectionSetPopupMenuFX extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3338293517145854675L;
	
	private Object item;

	public IfcSelectionSetPopupMenuFX(String title, Object item) {
		super(title);
		this.item = item;
	}	
	
	private ArrayList<ClassInterface> retriveIfcObjects(Object item){
		ArrayList<ClassInterface> ifcObjs = new ArrayList<ClassInterface>();
		if(item instanceof SelectionSet) {
			ifcObjs.addAll(((SelectionSet)item).getSelection());
		}
		if(item instanceof ArrayList<?>) {
			for(Object innerItem : ((ArrayList<?>)item)) {
				if(innerItem instanceof SelectionSet) {
					ifcObjs.addAll(((SelectionSet)innerItem).getSelection());
				}
				if(innerItem instanceof ArrayList<?>) {
					ifcObjs.addAll((ArrayList)innerItem);
				}
				if(innerItem instanceof ClassInterface) {
					ifcObjs.add((ClassInterface)innerItem);
				}
			}
		}
		if(item instanceof ClassInterface) {
			ifcObjs.add((ClassInterface)item);
		}
		return ifcObjs;
	}
	
	public void inititializeContent() throws IOException {
		//add action bindings
		this.add(UIUtilities.createMenuItem(
				"Select", 
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
							node.getSelectionModel().select(null, retriveIfcObjects(item));
						}
					}
				}, 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
	
		this.add(UIUtilities.createMenuItem(
				"Deselect", 
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
							node.getSelectionModel().deselect(null, retriveIfcObjects(item));
						}
					}
				},
				this.getClass().getResourceAsStream("icons/play.png"))
		);
	
		this.addSeparator();

		this.add(UIUtilities.createMenuItem(
				"Hide", 
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {

							for(ClassInterface ifcObj : retriveIfcObjects(item)) {			
								CadObject cadObj = node.getCadObjectModel().getCadObject(ifcObj);
								if(cadObj != null) {
									node.getVisibilityModel().setVisible(
										cadObj,
										false, 
										null
									);
								}
							}
			
						}
					}
				},
				this.getClass().getResourceAsStream("icons/play.png"))
		);
		
		this.add(UIUtilities.createMenuItem(
				"Show", 
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {

							for(ClassInterface ifcObj : retriveIfcObjects(item)) {			
								CadObject cadObj = node.getCadObjectModel().getCadObject(ifcObj);
								if(cadObj != null) {
									node.getVisibilityModel().setVisible(
										cadObj,  
										true, 
										null
									);
								}
							}
			
						}
					}
				},
				this.getClass().getResourceAsStream("icons/play.png"))
		);

		this.add(UIUtilities.createMenuItem(
				"Show Only", 
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
							node.getVisibilityModel().setVisible(
									node.getCadObjectModel().getCadObjects(), 
									false, 
									null
							);
						}
						
						for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {

							for(ClassInterface ifcObj : retriveIfcObjects(item)) {
								CadObject cadObj = node.getCadObjectModel().getCadObject(ifcObj);
								if(cadObj != null) {
									node.getVisibilityModel().setVisible(
											cadObj, 
											true, 
											null
									);
								}
								
							}
			
						}
					}
				}, 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
		
	}

}
