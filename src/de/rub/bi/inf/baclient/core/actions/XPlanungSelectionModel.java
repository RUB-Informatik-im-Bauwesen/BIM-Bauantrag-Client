package de.rub.bi.inf.baclient.core.actions;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;
import com.apstex.javax.media.j3d.Node;

import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;

/**
 * A container object for handling selection of CadObject geometry using a
 * specific canvas viewer as content.
 * 
 * @author Marcel Stepien
 *
 */
public class XPlanungSelectionModel {

	private Ifc3DViewJ3D view = null;
	private Set<MouseAdapter> listeners = null;

	/**
	 * The viewer is being used as context for the listeners. Each listener will be
	 * added to the viewer.
	 * 
	 * @param view
	 */
	public XPlanungSelectionModel(Ifc3DViewJ3D view) {
		this.view = view;
		this.listeners = new HashSet<>();
	}

	/**
	 * Retrieve the container of the Node Objects. If multiple Nodes are children of
	 * the same specific CadObject, then the instance will be returned only once. No
	 * duplication allowed. The children of the CadObject can be a mix of selected
	 * and unselected Nodes.
	 * 
	 * @return
	 */
	public XPlanungCadObjectJ3D[] getSelectedCadObjects() {
		HashSet<XPlanungCadObjectJ3D> selectedObjects = new HashSet<>();
		for (MouseAdapter l : listeners) {
			
			if(l instanceof CadMouseAdapter) {
				XPlanungCadObjectJ3D[] nodes = ((CadMouseAdapter)l).getSelection();
				for (XPlanungCadObjectJ3D n : nodes) {
					selectedObjects.add(n);
				}
			}
			
		}

		XPlanungCadObjectJ3D[] objects = new XPlanungCadObjectJ3D[selectedObjects.size()];
		selectedObjects.toArray(objects);
		return objects;
	}

	/**
	 * Retrieve all Nodes selected in the viewer context. A Node will be usually of
	 * the type Shape3D.
	 * 
	 * @return
	 */
	public Node[] getSelectedNodes() {
		HashSet<Node> selectedObjects = new HashSet<>();
		for (MouseAdapter l : listeners) {

			if(l instanceof CadMouseAdapter) {
				XPlanungCadObjectJ3D[] nodes = ((CadMouseAdapter)l).getSelection();
				for (XPlanungCadObjectJ3D n : nodes) {
					for(MultiAppearanceShape3D multiAppearanceShape3D : n.getSolidShapes()) {
						selectedObjects.add(multiAppearanceShape3D);
					}
				}
			}
		}

		Node[] objects = new Node[selectedObjects.size()];
		selectedObjects.toArray(objects);
		return objects;
	}

	/**
	 * Adds a new CadMouseAdapter to the viewer.
	 * 
	 * @param listener
	 */
	public void addListener(MouseAdapter listener) {
		view.addMouseListener(listener);
		listeners.add(listener);
	}

	/**
	 * Activate all registered listeners manually on a specific Node.
	 * 
	 * @param n
	 */
	public void fireEvent(XPlanungCadObjectJ3D n) {
		for (MouseAdapter adapter : listeners) {
			if(adapter instanceof CadMouseAdapter) {
				((CadMouseAdapter)adapter).objectPicked(n);
			}
		}
	}

	public void select(ArrayList<XPlanungCadObjectJ3D> obj) {
		for (XPlanungCadObjectJ3D o : obj) {
			select(o);
		}
	}

	public void deselect(ArrayList<XPlanungCadObjectJ3D> obj) {
		for (XPlanungCadObjectJ3D o : obj) {
			deselect(o);
		}
	}

	public void select(XPlanungCadObjectJ3D[] objs) {
		for(XPlanungCadObjectJ3D cObj : objs) {
			select(cObj);
		}
	}

	public void select(XPlanungCadObjectJ3D obj) {
		Iterator<MouseAdapter> iter = listeners.iterator();
		while (iter.hasNext()) {
			MouseAdapter adapter = iter.next();
			if(adapter instanceof CadMouseAdapter) {				
				((CadMouseAdapter)adapter).objectPicked(obj, true);
			}
		}
	}

	public void deselect(XPlanungCadObjectJ3D obj) {
		Iterator<MouseAdapter> iter = listeners.iterator();
		while (iter.hasNext()) {
			MouseAdapter adapter = iter.next();
			if(adapter instanceof CadMouseAdapter) {				
				((CadMouseAdapter)adapter).objectPicked(obj, false);
			}
		}
	}

	public void deselect(XPlanungCadObjectJ3D[] objs) {
		for(XPlanungCadObjectJ3D cObj : objs) {
			deselect(cObj);
		}
	}

	public void clearSelection() {

		Iterator<MouseAdapter> iter = listeners.iterator();
		while (iter.hasNext()) {
			MouseAdapter adapter = iter.next();
			if(adapter instanceof CadMouseAdapter) {				
				((CadMouseAdapter)adapter).reset();
			}
		}
	
	}

}
