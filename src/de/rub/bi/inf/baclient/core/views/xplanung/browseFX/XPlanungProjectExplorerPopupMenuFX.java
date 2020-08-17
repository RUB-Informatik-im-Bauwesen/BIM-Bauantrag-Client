package de.rub.bi.inf.baclient.core.views.xplanung.browseFX;

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
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;

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
public class XPlanungProjectExplorerPopupMenuFX extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3338293517145854675L;
	
	private ArrayList<Object> selectedItems;

	public XPlanungProjectExplorerPopupMenuFX(String title, ArrayList<Object> selection) {
		super(title);
		this.selectedItems = selection;
	}	
	
	public void inititializeContent() throws IOException {
		//add action bindings
		this.add(UIUtilities.createMenuItem(
				"Select", 
				new XPlanungActionCollection.SelectAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
	
		this.add(UIUtilities.createMenuItem(
				"Deselect", 
				new XPlanungActionCollection.DeselectAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
	
		
		this.addSeparator();

		this.add(UIUtilities.createMenuItem(
				"Hide", 
				new XPlanungActionCollection.HideAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
		
		this.add(UIUtilities.createMenuItem(
				"Show", 
				new XPlanungActionCollection.ShowAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);

		this.add(UIUtilities.createMenuItem(
				"Show Only", 
				new XPlanungActionCollection.ShowOnlyAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
		
		this.add(UIUtilities.createMenuItem(
				"Make Transparent", 
				new XPlanungActionCollection.TransparencyAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
		
		this.addSeparator();
		
		this.add(UIUtilities.createMenuItem(
				"Add XPlanung to IFC", 
				new XPlanungActionCollection.AddToIfcAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png"))
		);
		
		
		JMenuItem item = UIUtilities.createMenuItem(
				"Create Extrusion Model", 
				new XPlanungActionCollection.CreateExtusionModelAction(selectedItems), 
				this.getClass().getResourceAsStream("icons/play.png")
		);
		

		item.setEnabled(true);
		if(selectedItems != null) {
			for(Object obj : selectedItems) {
				if (obj instanceof AbstractFeatureType) {
					AbstractFeatureType feature = (AbstractFeatureType)obj;
					AbstractGeometryType geoType = GeometryUtil.hasGeometry(feature);
					if(geoType instanceof PolygonType) {
						item.setEnabled(true);					
					}
				}else if(obj instanceof Entry<?, ?>) {
					
					if(((Entry<?, ?>)obj).getKey() instanceof Class) {
						ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) ((Entry)obj).getValue();	
						for(AbstractFeatureType feature : features) {
							AbstractGeometryType geoType = GeometryUtil.hasGeometry(feature);
							if(!(geoType instanceof PolygonType)) {
								item.setEnabled(false);	
								break;
							}
						}
					}else if(((Entry<?, ?>)obj).getKey() instanceof String) {
						HashMap<Class, ArrayList<AbstractFeatureType>> featureMap = (HashMap<Class, ArrayList<AbstractFeatureType>>)((Entry<?, ?>)obj).getValue();
						
						for(Class classKey : featureMap.keySet()) {
							ArrayList<AbstractFeatureType> features = (ArrayList<AbstractFeatureType>) featureMap.get(classKey);	
							for(AbstractFeatureType feature : features) {
								AbstractGeometryType geoType = GeometryUtil.hasGeometry(feature);
								if(!(geoType instanceof PolygonType)) {
									item.setEnabled(false);	
									break;
								}
							}
						}
					}
				}
			}
		}
		this.add(item);
	}

}
