package de.rub.bi.inf.baclient.core.actions;

import java.util.ArrayList;

import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;

import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.views.XViewer;
import net.opengis.gml._3.AbstractFeatureType;

/**
 * A specified CadMouseAdapter for selecting Shape3D in the scene.
 * 
 * @author Marcel Stepien
 *
 */
public class CadSelectionColorizeMouseListener extends CadMouseAdapter {
	
	private XPlanungModel xPlanungModel;

	public CadSelectionColorizeMouseListener(Ifc3DViewJ3D view, XPlanungModel xPlanungModel) {
		super(view);
		this.xPlanungModel = xPlanungModel;
	}

	public void reset() {
		for (XPlanungCadObjectJ3D obj : xPlanungModel.getCadObjects()) {
			obj.setSelected(false);
		}
	}

	@Override
	public void objectPicked(XPlanungCadObjectJ3D obj) {
		objectPicked(obj, !obj.isSelected());
	}

	public void objectPicked(XPlanungCadObjectJ3D obj, boolean select) {
		// deselect all if nothing is picked
		if (obj == null) {
			reset();
			return;
		}

		obj.setSelected(select);
		XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX().repaint(); //causes update of selection status
		
		AbstractFeatureType featureType = XPlanungModel.getFeatureOfCadObj(obj);
		if (featureType!=null)
			XViewer.getInstance().getViewerPanel().getXplanungPropertyTreeTableView().loadProperties(featureType); //causes update of property table status
	}

	public XPlanungCadObjectJ3D[] getSelection() {
		ArrayList<XPlanungCadObjectJ3D> objList = new ArrayList<>();
		for (XPlanungCadObjectJ3D obj : xPlanungModel.getCadObjects()) {
			if (obj.isSelected()) {
				objList.add(obj);
			}
		}

		XPlanungCadObjectJ3D[] nodes = new XPlanungCadObjectJ3D[objList.size()];
		objList.toArray(nodes);
		return nodes;
	}
}
