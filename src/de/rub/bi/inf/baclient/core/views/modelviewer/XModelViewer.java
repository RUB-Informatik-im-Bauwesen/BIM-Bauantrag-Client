package de.rub.bi.inf.baclient.core.views.modelviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.apstex.gui.core.controller.ApplicationControllerListener;
import com.apstex.gui.core.controller.MacOSPref;
import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;
import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewKeyListener;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.util.ExtendToolBarButton;
import com.apstex.gui.core.views.view3d.ModelViewerToolbar;
import com.apstex.gui.ifc.controller.StoreyTranslation;
import com.apstex.gui.ifc.controller.StoreyTranslationController;
import com.apstex.gui.ifc.controller.StoreyTranslationListener;
import com.apstex.gui.ifc.views.view3d.j3d.Ifc3DViewMouseListener;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.j3d.loaders.Scene;
import com.apstex.javax.media.j3d.BoundingBox;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Vector3d;
import com.apstex.javax.vecmath.Vector4d;


/** Description of the Class */
public class XModelViewer extends JPanel
{
	public static final String DOCKNAME = "3D View";
	
	private static final long serialVersionUID = 1L;
	protected Point3d eyePosn = new Point3d();
	protected Transform3D t3d = new Transform3D();
	protected Transform3D trans = new Transform3D();
	protected Point3d rotationCenter = new Point3d(0, 0, 0);

	public static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private static Ifc3DViewJ3D w3D = null;
	private Component wrappedToolBar = null;

	public final static int SCENEGRAPH_CHANGED = 0;
	public final static int MODEL_CHANGED = 1;
	public final static int WALK_MODUS = 2;
	public final static int ORBIT_MODUS = 3;
	public final static int DRAW_MODUS = 4;
	public final static int SELECT_MODUS = 5;
	public final static int GRID_SWITCHED = 6;
	public final static int WIREFRAME_SWITCHED = 7;
	public final static int OBJECT_PICKED = 8;
	public final static int SELECTION_CLEARED = 9;
	public final static int SELECT_ALL_VISIBEL = 10;
	public final static int SELECTION_INVERTED = 11;
	public final static int MASKLIST_CHANGED = 12;

	private JSlider horizontalSlider;
	private JSlider verticalLeft;
	private JSlider verticalRight;
	private LeftChangeListener leftChangeListener;
	private RightChangeListener rightChangeListener;
	private Ifc3DViewMouseListener mouseListener;

	public XModelViewer() 
	{
		this(true);
	}
	
	public XModelViewer(boolean isToolbarVisible)
	{
		this(isToolbarVisible, null);
	}
	
	public XModelViewer(boolean isToolbarVisible, Color backgroundColor)
	{
		Kernel.getApplicationController().addListener(new InternalApplicationControllerListener());
		StoreyTranslationController.getInstance().addListener(new InternalStoreyTranslationListener());
		GraphicsConfiguration graphicsConfiguration = Kernel.getApplicationController().getMainFrame() == null ?
				null : Kernel.getApplicationController().getMainFrame().getGraphicsConfiguration();
		w3D = new Ifc3DViewJ3D(graphicsConfiguration, backgroundColor);
		this.initialize(isToolbarVisible);
		mouseListener = new Ifc3DViewMouseListener(w3D);
		w3D.addMouseListener(mouseListener);
		w3D.addMouseMotionListener(mouseListener);
		w3D.addKeyListener(new Ifc3DViewKeyListener());
	}
	
	public void showToolbar(boolean showToolbar)
	{
		if(showToolbar)
		{
			if( ! isToolbarVisible())
			{
				wrappedToolBar = ExtendToolBarButton.wrapToolBar(new ModelViewerToolbar());
				this.add(wrappedToolBar, BorderLayout.NORTH);
				this.validate();
				this.repaint();
			}
		}
		else
		{
			if(isToolbarVisible())
			{
				this.remove(wrappedToolBar);
				wrappedToolBar = null;
				this.validate();
				this.repaint();
			}
		}
	}
	
	public boolean isToolbarVisible()
	{
		return wrappedToolBar != null;
	}

	/**
	 * Initializes the view.
	 * @param showToolbar whether toolbar should be added to the view or not.
	 */
	protected void initialize(boolean showToolbar) {

		this.setPreferredSize(new Dimension(1024, 768));
		this.setLayout(new BorderLayout());

		// Build toolbar
		if(showToolbar)
		{
			wrappedToolBar = ExtendToolBarButton.wrapToolBar(new ModelViewerToolbar());
			this.add(wrappedToolBar, BorderLayout.NORTH);
		}
		verticalLeft = new JSlider(JSlider.VERTICAL, 0, 100000, 0);
		// Mac OS *********************************************************
		if (MacOSPref.isMacOS())
			this.setMacSliderPref(verticalLeft);
		// ******************************************************************************
		verticalLeft.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		verticalLeft.setPaintTrack(true);
		verticalLeft.setPaintTicks(false);
		verticalLeft.setPaintLabels(false);
		verticalLeft.addChangeListener(leftChangeListener = new LeftChangeListener());

		verticalRight = new JSlider(JSlider.VERTICAL, 0, 100000, 100000);
		// Mac OS *********************************************************
		if (MacOSPref.isMacOS()) 
			this.setMacSliderPref(verticalRight);
		// ******************************************************************************
		verticalRight.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		verticalRight.setPaintTrack(true);
		verticalRight.setPaintTicks(false);
		verticalRight.setPaintLabels(false);
		verticalRight.addChangeListener(rightChangeListener = new RightChangeListener());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(verticalLeft, BorderLayout.WEST);
		panel.add(verticalRight, BorderLayout.EAST);

		horizontalSlider = new JSlider(JSlider.HORIZONTAL, 0, 100000, 0);
		// Mac OS *********************************************************
		if (MacOSPref.isMacOS()) 
			this.setMacSliderPref(horizontalSlider);
		// ****************************************************************
		horizontalSlider.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		horizontalSlider.setToolTipText("clips the model in viewers direction parallel to XY-plane");
		horizontalSlider.setPaintTrack(true);
		horizontalSlider.setPaintTicks(false);
		horizontalSlider.setPaintLabels(false);
		horizontalSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent changeevent) {
				Point3d lower = new Point3d();
				Point3d upper = new Point3d();
				BoundingBox bounds = w3D.getBoundsInVworld();
				bounds.getLower(lower);
				bounds.getUpper(upper);

				Vector3d normal = w3D.getViewNormal();
				normal.normalize();
				double dxMax, dyMax, dxMin, dyMin;
				if(normal.x>0){ 
					dxMax = upper.x;
					dxMin = lower.x;
				}
				else{
					dxMax = lower.x;
					dxMin = upper.x;
				}
				if(normal.y>0){
					dyMax = upper.y;
					dyMin = lower.y;
				}
				else{
					dyMax = lower.y;
					dyMin = upper.y;
				}
				double limitMax = Math.abs(normal.x * dxMax) + Math.abs(normal.y * dyMax);
				double limitMin = Math.abs(normal.x * dxMin) + Math.abs(normal.y * dyMin);
				w3D.setClipPlane(0, new Vector4d(normal.x, normal.y, 0, -limitMin-0.1 +(limitMin+limitMax+0.2) * horizontalSlider.getValue() / 100000));
			}
		});
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		centerPanel.add(w3D, constraints);
		
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.VERTICAL;
		centerPanel.add(panel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		centerPanel.add(horizontalSlider, constraints);
		
		this.add(centerPanel, BorderLayout.CENTER);
	}
	
	/**
	 * set some Mac OS Properties for Slider component
	 * 
	 * @param jSlider
	 */
	private void setMacSliderPref(JSlider jSlider)
	{
		jSlider.putClientProperty("JComponent.sizeVariant", "mini");
		jSlider.putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);
	}

	public Ifc3DViewJ3D getWidget3d() {
		return w3D;
	}
	
	public void resetModelClipping(){
		w3D.resetModelClipping();
		verticalLeft.setValue(0);
		verticalRight.setValue(100000);
		horizontalSlider.setValue(-50000);
	}

//	public void loadModel(Scene scene, double scale) 
//	{
//		w3D.putSceneInVWorld(scene, scale);
//		applicationModel.getApplicationController().getViewController()
//			.setNavigationMode(ViewController.NAVIGATION_MODE_ORBIT, null);
//
//		notifyManager(SCENEGRAPH_CHANGED, null);
//		notifyManager(MODEL_CHANGED, null);
//		notifyManager(ORBIT_MODUS, null);
//	}
	
	class InternalApplicationControllerListener implements ApplicationControllerListener
	{
		@Override
		public void programClosing()
		{
			
		}

		@Override
		public void willClearModels()
		{
			
		}

		@Override
		public void loadScene(Scene scene)
		{
			
		}

		@Override
		public void saveSnapshot()
		{
			saveImage();
		}

		@Override
		public void progressBarUpdated(int value, String message)
		{
			
		}

		@Override
		public void selectionColorChanged(Color color)
		{
			
		}
	}
	
	class InternalStoreyTranslationListener implements StoreyTranslationListener
	{
		@Override
		public void storeyTranslationChanged(StoreyTranslation storeyTranslation)
		{
			
		}
		
		@Override
		public void storeyTranslationFinished(IfcBuildingStorey ifcBuildingStorey)
		{
			w3D.updateModelBounds();
		}
		
		@Override
		public void storeyTranslationOnGoing(IfcBuildingStorey ifcBuildingStorey)
		{
			resetModelClipping();
		}
	}

	public void saveImage() {
		w3D.writeJPEG_ = true;
		w3D.saveJPEG_ = true;
		w3D.repaint();
	}

	public void printJPGImage() {
		w3D.writeJPEG_ = true;
		w3D.saveJPEG_ = false;
		w3D.repaint();
	}

	public void stopRenderer() {
		w3D.stopRenderer();
	}

	public void startRenderer() {
		w3D.reStartRender();
	}

	private class LeftChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			Point3d lower = new Point3d();
			Point3d upper = new Point3d();
			BoundingBox bounds = w3D.getBoundsInVworld();
			bounds.getLower(lower);
			bounds.getUpper(upper);

			if (verticalLeft.getValue() >= verticalRight.getValue()) {
				verticalLeft.removeChangeListener(leftChangeListener);
				verticalRight.setValue(verticalLeft.getValue());
				verticalLeft.addChangeListener(leftChangeListener);
			}
			double limit = upper.z - lower.z + 0.2;
			limit *= w3D.getSceneScale();
			w3D.setClipPlane(2, new Vector4d(0, 0, -1, limit
					* verticalLeft.getValue() / 100000 + lower.z - 0.1));
		}
	}

	private class RightChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			Point3d lower = new Point3d();
			Point3d upper = new Point3d();
			BoundingBox bounds = w3D.getBoundsInVworld();
			bounds.getLower(lower);
			bounds.getUpper(upper);

			if (verticalLeft.getValue() >= verticalRight.getValue()) {
				verticalRight.removeChangeListener(rightChangeListener);
				verticalLeft.setValue(verticalRight.getValue());
				verticalRight.addChangeListener(rightChangeListener);
			}
			double limit = upper.z - lower.z + 0.2;
			w3D.setClipPlane(1, new Vector4d(0, 0, 1, -(limit
					* verticalRight.getValue() / 100000 + lower.z - 0.1)));
		}
	}
}
