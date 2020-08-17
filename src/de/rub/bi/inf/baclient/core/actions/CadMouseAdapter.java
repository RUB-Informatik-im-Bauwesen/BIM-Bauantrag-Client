package de.rub.bi.inf.baclient.core.actions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.j3d.utils.picking.PickCanvas;
import com.apstex.j3d.utils.picking.PickIntersection;
import com.apstex.j3d.utils.picking.PickResult;
import com.apstex.javax.media.j3d.BranchGroup;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.Material;
import com.apstex.javax.media.j3d.Node;
import com.apstex.javax.media.j3d.TransformGroup;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Color4f;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Vector3d;
import com.apstex.javax.vecmath.Vector3f;

import de.rub.bi.inf.baclient.core.geometry.BuildGeometryTest;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadLoader;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.views.XViewer;

/**
 * A extension for the MouseAdaper used to define mouse handling on viewer.
 * 
 * @author Marcel Stepien
 *
 */
public abstract class CadMouseAdapter extends MouseAdapter {
	
	protected PickCanvas pickCanvas;
	
	public CadMouseAdapter(Ifc3DViewJ3D view) {
		this.pickCanvas = new PickCanvas(view, view.getModel()); //Create Picker with view and model as context
		this.pickCanvas.setMode(PickCanvas.GEOMETRY);
		this.pickCanvas.setTolerance(4.0f);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		pickCanvas.setShapeLocation(e);
		PickResult result = pickCanvas.pickClosest();
		Point3d eyePos = pickCanvas.getStartPosition();
		
		if (result != null) {
			Node n = result.getNode(PickResult.SHAPE3D);

			if(n.getUserData() instanceof XPlanungCadObjectJ3D) {
				objectPicked((XPlanungCadObjectJ3D)n.getUserData());				
			}
			
		}
	}

	/**
	 * Retrieve the used PickCanvas object by this listener. 
	 * 
	 * @return
	 */
	public PickCanvas getPicker() {
		return pickCanvas;
	}
	
	
	/**
	 * Definition of method should be used to define what to do with the picked object.
	 * 
	 * @param obj
	 */
	public abstract void objectPicked(XPlanungCadObjectJ3D obj);
	
	public abstract void objectPicked(XPlanungCadObjectJ3D obj, boolean select);
	
	public abstract XPlanungCadObjectJ3D[] getSelection();

	public abstract void reset();
}
