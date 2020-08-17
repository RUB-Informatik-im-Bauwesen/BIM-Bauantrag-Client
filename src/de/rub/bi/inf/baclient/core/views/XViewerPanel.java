package de.rub.bi.inf.baclient.core.views;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.apstex.gui.bcf.views.commentview.CommentsView;
import com.apstex.gui.core.controller.ApplicationControllerListener;
import com.apstex.gui.core.controller.LoadManagerListener;
import com.apstex.gui.core.dockable.DockableComponent;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationinfo.ApplicationInfo;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelRootListener;
import com.apstex.gui.core.util.filefilter.JpgFileFilter;
import com.apstex.gui.core.util.filefilter.PngFileFilter;
import com.apstex.gui.core.views.stepfileview.StepFileView;
import com.apstex.gui.core.views.sysinfoview.SysInfoViewDialog;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.gui.ifc.views.layerview.LayerDialog;
import com.apstex.gui.ifc.views.modelexplorer.IfcModelExplorer;
import com.apstex.gui.ifc.views.propertiesview.PropertiesView;
import com.apstex.gui.ifc.views.quantitiesview.QuantitiesView;
import com.apstex.gui.ifc.views.spatialview.IfcSpatialStructurePanel;
import com.apstex.gui.ifc.views.spatialview.IfcSpatialStructureToolbar;
import com.apstex.gui.ifc.views.spatialview.IfcSpatialStructureView;
import com.apstex.gui.ifc.views.storeytranslation.StoreyTranslationDialog;
import com.apstex.gui.ifc.views.typeview.IfcTypeView;
import com.apstex.gui.util.dialogs.IfcLoaderErrorReportDialog;
import com.apstex.gui.util.dialogs.ProgressDialog;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.j3d.loaders.Scene;
import com.apstex.loader3d.core.StepLoaderErrorListener;
import com.apstex.loader3d.ifc.AppearanceLoader;
import com.apstex.loader3d.ifc.IfcLoader;
import com.apstex.step.core.ClassInterface;
import com.vldocking.swing.docking.DockableState;
import com.vldocking.swing.docking.DockingConstants;
import com.vldocking.swing.docking.DockingDesktop;
import com.vldocking.swing.docking.DockingPreferences;

import de.rub.bi.inf.baclient.core.ifc.CustomIfcLoaderManager;
import de.rub.bi.inf.baclient.core.views.bcf.XBCFCommentsView;
import de.rub.bi.inf.baclient.core.views.ifc.IfcSelectionSetViewFX;
import de.rub.bi.inf.baclient.core.views.modelviewer.XModelViewer;
import de.rub.bi.inf.baclient.core.views.xbau.XbauExplorer;
import de.rub.bi.inf.baclient.core.views.xplanung.browseFX.XPlanungProjectExplorerViewFX;
import de.rub.bi.inf.baclient.core.views.xplanung.prop.XplanungPropertyTreeTableView;

public class XViewerPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	//Models:
	private HashMap<Throwable, ClassInterface> ifcLoaderErrors;
	//Views:
	private DockingDesktop desk;
	private XModelViewer modelviewer;
	private IfcSpatialStructureView ifcTreeView;
	private IfcTypeView ifcTypeView;
	private IfcModelExplorer ifcModelExplorer;
	private StepFileView stepFileView;
	private CommentsView commentView;
	private QuantitiesView quantitiesView;
	private PropertiesView propertiesView;
	
	
	
	private XPlanungProjectExplorerViewFX xPlanungProjectExplorerViewFX;
	private XplanungPropertyTreeTableView xplanungPropertyTreeTableView;
	
	private XbauExplorer xbauExplorer;

	private IfcSelectionSetViewFX selectionSetViewFX;

	//GUI:
	private StatusBarMouseListener statusBarMouseListener;
	private JToolBar statusToolBar;
	private JLabel statusTextLabel, statusIconLabel;
	private ImageIcon warningIcon, nullIcon;
	//Util:
	private ProgressDialog progressDialog;
	private StoreyTranslationDialog storeyTranslationDialog;
	private SysInfoViewDialog sysInfoDialog;
	private LayerDialog layerDialog;
	//Dockables:
	private DockableComponent modelviewerDockable, ifcTreeDockable, ifcTypeDockable, ifcModelExplorerDockable, 
	commentsDockable, stepFileDockable, quantitiesDockable, propertiesDockable;
	
	public XViewerPanel(ApplicationInfo applicationInfo, Frame mainFrame)
	{
		Kernel.setApplicationInfo(applicationInfo);
		Kernel.getApplicationController().setMainFrame(mainFrame);
		this.setLayout(new BorderLayout());
		initComponents();
		initDockingView();
		initDisableClosingDockWindows();
		initStatusBar();
		initDragNDrop();
		initSettings();
		Kernel.getApplicationModelRoot().addListener(new ApplicationModelRootListener() {
			@Override
			public void nodesAdded(Collection<ApplicationModelNode> nodes)
			{
				
			}
			
			@Override
			public void nodesRemoved(Collection<ApplicationModelNode> nodes)
			{
				HashSet<Throwable> toRemove = new HashSet<Throwable>();
				for (ApplicationModelNode node : nodes)
				{
					for (Throwable t : ifcLoaderErrors.keySet())
					{
						ClassInterface ifcObject = ifcLoaderErrors.get(t);
						if(node.getStepModel().containsObject(ifcObject))
						{
							toRemove.add(t);
						}
					}
				}
				for (Throwable throwable : toRemove)
				{
					ifcLoaderErrors.remove(throwable);
				}
				resetStatusBar();
			}
		});
	}
	
	private void initComponents()
	{
		ifcLoaderErrors = new HashMap<Throwable, ClassInterface>();
		desk = new DockingDesktop();
		Kernel.getApplicationController().setViewerDirectoryPath(System.getProperty("user.home")+"\\.ifctp-ifcviewer\\");
		modelviewer = new XModelViewer(false, Color.white);
		
		//System.out.println(Arrays.toString(modelviewer.getWidget3d().getMouseListeners()));
		
		ifcTreeView = new IfcSpatialStructureView() {
			@Override
			public void closeView(ApplicationModelNode arg0, IfcSpatialStructurePanel arg1,
					IfcSpatialStructureToolbar arg2) {
				super.closeView(arg0, arg1, arg2);

				//Remove IfcModel routine 
				try {
					File file = new File(arg0.getUserData("TEMP_FILE_PATH").toString());
					boolean result = Files.deleteIfExists(file.toPath());
					if(result) {						
						System.out.println("Removed: " + arg0.getUserData("TEMP_FILE_PATH").toString());
					}else {
						System.err.println("File " + file.getName() + " could not be removed from temp folder. Does it exist?");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		
		ifcTypeView = new IfcTypeView();
		ifcModelExplorer = new IfcModelExplorer();
		stepFileView = new StepFileView();
		commentView = new XBCFCommentsView();
		quantitiesView = new QuantitiesView();
		propertiesView = new PropertiesView();
	}
	
	private void initDockingView()
	{
		DockingPreferences.initHeavyWeightUsage();
		this.add(desk, BorderLayout.CENTER);
		//top-right: 3D View
		modelviewerDockable = new DockableComponent(modelviewer, "3D View");
		modelviewerDockable.getDockKey().setResizeWeight(1.0f);
		desk.addDockable(modelviewerDockable);
		

		//		//top-left: Spatial Structure, Types
		
		//xPlanungProjectExplorerView = new XPlanungProjectExplorerView(modelviewer.getWidget3d(), null);
		//DockableComponent xPlanungDockable = new DockableComponent(xPlanungProjectExplorerView, "XPlanung");
		//desk.split(modelviewerDockable, xPlanungDockable, DockingConstants.SPLIT_LEFT);
		//desk.setDockableWidth(modelviewerDockable, 0.8);
		
		
		xPlanungProjectExplorerViewFX = XPlanungProjectExplorerViewFX.getInstance();
		DockableComponent xPlanungDockableFX = new DockableComponent(xPlanungProjectExplorerViewFX, "XPlanung");
		//desk.split(modelviewerDockable, xPlanungDockableFX, DockingConstants.SPLIT_LEFT);
		//desk.setDockableWidth(modelviewerDockable, 0.8);
		

		desk.split(modelviewerDockable, xPlanungDockableFX, DockingConstants.SPLIT_LEFT);
		desk.setDockableWidth(xPlanungDockableFX, 0.3);		
		//desk.createTab(xPlanungDockable, xPlanungDockable, 1);
		//desk.createTab(xPlanungDockableFX, xPlanungDockableFX, 1);
		
		
		selectionSetViewFX = IfcSelectionSetViewFX.getInstance();
		DockableComponent selectionSetDockableFX = new DockableComponent(selectionSetViewFX, "Selection Sets");
		
		
		
		//top-left: Property, Types and Values stored inside
//		xPlanungPropertyView = new XPlanungPropertyView(modelviewer.getWidget3d());
//		DockableComponent xPlanungPropDockable = new DockableComponent(xPlanungPropertyView, "XPlanung-Property");
//		desk.split(xPlanungDockable, xPlanungPropDockable, DockingConstants.SPLIT_BOTTOM);
//		desk.setDockableHeight(xPlanungDockable, 0.5);
		
		xplanungPropertyTreeTableView = new XplanungPropertyTreeTableView();
		DockableComponent xPlanungPropTreeTableDockable = new DockableComponent(xplanungPropertyTreeTableView, "Properties (XPlan)");
		desk.split(xPlanungDockableFX, xPlanungPropTreeTableDockable, DockingConstants.SPLIT_BOTTOM);
		desk.setDockableHeight(xPlanungPropTreeTableDockable, 0.5);
		
		
		ifcTreeDockable = new DockableComponent(ifcTreeView, "IFC Spatial Structure");
		ifcTypeDockable = new DockableComponent(ifcTypeView, "IFC Types");
		ifcModelExplorerDockable = new DockableComponent(ifcModelExplorer, "Attributes");
		commentsDockable = new DockableComponent(commentView, "Comments");
		stepFileDockable = new DockableComponent(stepFileView, "STEP File View");
		quantitiesDockable = new DockableComponent(quantitiesView, "Quantities");
		propertiesDockable = new DockableComponent(propertiesView, "Properties");
		
		
		desk.split(modelviewerDockable, ifcTreeDockable, DockingConstants.SPLIT_RIGHT);
		desk.setDockableWidth(ifcTreeDockable, 0.3);		
		desk.createTab(ifcTreeDockable, ifcTypeDockable, 1);
		desk.createTab(ifcTreeDockable, commentsDockable, 2);
		desk.createTab(ifcTreeDockable, selectionSetDockableFX, 3);

		desk.split(ifcTreeDockable, ifcModelExplorerDockable, DockingConstants.SPLIT_BOTTOM);
		desk.setDockableHeight(ifcTreeDockable, 0.33);
		desk.createTab(ifcModelExplorerDockable, propertiesDockable, 1);
		desk.createTab(ifcModelExplorerDockable, quantitiesDockable, 1);
		
		
		xbauExplorer = new XbauExplorer();
		DockableComponent xbauExplorerDockable = new DockableComponent(xbauExplorer, "XBau");
		desk.split(ifcModelExplorerDockable, xbauExplorerDockable, DockingConstants.SPLIT_BOTTOM);
		desk.setDockableHeight(ifcModelExplorerDockable, 0.33);
		
		
	}
	
	private void initDisableClosingDockWindows()
	{
		for (DockableState ds : desk.getDockables())
		{
			ds.getDockable().getDockKey().setCloseEnabled(false);
		}
	}
	
	private void initStatusBar()
	{		
		URL imageUrl = Thread.currentThread().getContextClassLoader().getResource("icons/warning.png");
		ImageIcon icon = new ImageIcon(imageUrl);
		warningIcon = new ImageIcon(icon.getImage().getScaledInstance(13, 13, Image.SCALE_SMOOTH));
		nullIcon = new ImageIcon();
		statusTextLabel = new JLabel(" ");
		statusTextLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		statusIconLabel = new JLabel(nullIcon);
		statusIconLabel.setBorder(BorderFactory.createEmptyBorder());
		statusToolBar = new JToolBar();
		statusToolBar.setFloatable(false);
		statusToolBar.setPreferredSize(new Dimension(0, 20));
		statusToolBar.add(new JLabel("  "));
		statusToolBar.add(statusIconLabel);
		statusToolBar.add(new JLabel("  "));
		statusToolBar.add(statusTextLabel);
		statusBarMouseListener = new StatusBarMouseListener();
		statusToolBar.addMouseListener(statusBarMouseListener);		
		this.add(statusToolBar, BorderLayout.SOUTH);
	}
	
	private void initDragNDrop()
	{
		// ---FILE DRAG N DROP---
		this.setDropTarget(new DropTarget(this, new DropTargetAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void drop(DropTargetDropEvent dtde) 
			{
				if (!dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
					return;
				dtde.acceptDrop(dtde.getDropAction());
				try 
				{
					List<File> fileList = (List<File>) dtde.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);
					if (fileList.isEmpty() || fileList.size() > 1)
						return;
					IfcLoadManager.getInstance().loadFile(fileList.get(0), false);
				} 
				catch (Exception e) 
				{
					Kernel.getApplicationController().showErrorMessage("The dragged file could not be loaded!", e);
				}
			}
		}));
	}
	
	private void initSettings()
	{
		AppearanceLoader.setStandardColor(IfcSpace.class, new Color(164, 244, 111));
		IfcLoader.addErrorListener(new InternalIfcLoaderErrorListener());
		IfcLoader.CATCH_CSG_ERRORS = true;
		Kernel.getViewController().setOriginEnabled(true, null);
		Kernel.getApplicationController().addListener(new InternalApplicationControllerListener());
		CustomIfcLoaderManager.getInstance().addListener(new InternalLoadListener());
	}
	
	public void saveSnapshot()
	{
		try
		{
			if(Kernel.getApplicationController().isRestrictedAccessMode())
			{
				Kernel.getApplicationController().saveSnapshot();
				return;
			}
			ArrayList<FileFilter> fileFilters = new ArrayList<FileFilter>();
			JpgFileFilter jpgFileFilter = new JpgFileFilter();
			fileFilters.add(jpgFileFilter);
			fileFilters.add(new PngFileFilter());
			File file = Kernel.getApplicationController().openSaveFileDialog(null, fileFilters);
			if(file != null)
			{
				BufferedImage image = modelviewer.getWidget3d().createSnapshot();
				FileOutputStream fos = new FileOutputStream(file);
				String formatName;
				if(jpgFileFilter.accept(file))
					formatName = "jpg";
				else
					formatName = "png";
				ImageIO.write(image, formatName, fos);
				fos.close();
			}
		}
		catch (Exception e) 
		{
			Kernel.getApplicationController().showErrorMessage("Error while saving snapshot!", e);
		}
	}
	
	public void showStoreyTranslationDialog()
	{
		if(storeyTranslationDialog == null)
		{
			storeyTranslationDialog = new StoreyTranslationDialog();
			Point widgetLocation = modelviewer.getWidget3d().getLocationOnScreen();
			storeyTranslationDialog.setLocation(widgetLocation.x, widgetLocation.y);
		}
		storeyTranslationDialog.setVisible(true);
	}
	
	public void showSysInfoDialog() 
	{
		if(sysInfoDialog == null)
		{
			sysInfoDialog = new SysInfoViewDialog();
			Point widgetLocation = modelviewer.getWidget3d().getLocationOnScreen();
			sysInfoDialog.setLocation(widgetLocation.x, widgetLocation.y);
		}
		sysInfoDialog.setVisible(true);
	}
	
	public void showLayersDialog()
	{
		if(layerDialog == null)
		{
			layerDialog = new LayerDialog();
			Point widgetLocation = modelviewer.getWidget3d().getLocationOnScreen();
			layerDialog.setLocation(widgetLocation.x, widgetLocation.y);
		}
		layerDialog.setVisible(true);
	}
	
	public void openAboutProgramDialog()
	{
		URL imageUrl = Thread.currentThread().getContextClassLoader().getResource("icons/app_icon_128.png");
		ImageIcon icon = new ImageIcon(imageUrl);
		JLabel urlLabel = new JLabel("www.ifctoolsproject.com");
		urlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		urlLabel.setForeground(Color.BLUE);
		urlLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e))
				{
					Kernel.getApplicationController().openWebSiteInDefaultBrowser("http://www.ifctoolsproject.com");
				}
			}
		});
		JLabel mailLabel = new JLabel("info@apstex.com");
		mailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		mailLabel.setForeground(Color.BLUE);
		mailLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e))
				{
					Kernel.getApplicationController().writeMailInDefaultMailProgram("info@apstex.com");
				}
			}
		});
		Object[] message = 
			{
				Kernel.getApplicationInfo().getProgramVendor()+" - "+Kernel.getApplicationInfo().getProgramTitle()+"                ",
				"Version "+Kernel.getApplicationInfo().getProgramVersion()+" of "+Kernel.getApplicationInfo().getProgramDate(),
				" ",
				"Contact:",
				urlLabel, 
				mailLabel, 
				" ",
				"This program uses the following open source libraries: ",
				" - VLDocking from Lilian Chamontin - http://code.google.com/p/vldocking",
				" ",
			};
		JOptionPane.showMessageDialog(Kernel.getApplicationController().getMainFrame(), message, 
				"About "+Kernel.getApplicationInfo().getProgramTitle(), JOptionPane.INFORMATION_MESSAGE, icon);
	}
	
	public void resetStatusBar()
	{
		if( ! ifcLoaderErrors.isEmpty())
		{
			int number = ifcLoaderErrors.size();
			HashSet<ClassInterface> objects = new HashSet<ClassInterface>();
			for (Throwable t : ifcLoaderErrors.keySet())
			{
				objects.add(ifcLoaderErrors.get(t));
			}
			setStatusBarText("Warning: "+number+" internal error"+(number!=1?"s":"")+" occured " +
					"while interpreting IFC-Object's geometries. "+objects.size()+" IFC-Objects cannot be illustrated accurately in 3D-View." +
					" Click here for more information...");
			setStatusBarIcon(warningIcon);
		}
		else
		{
			setStatusBarText(" ");
			setStatusBarIcon(nullIcon);
		}
	}
	
	public void setStatusBarIcon(ImageIcon icon)
	{
		statusIconLabel.setIcon(icon);
	}
	
	public void setStatusBarText(String text)
	{
		statusTextLabel.setText(text);
	}
	
	public void setStatusBarTextAndIcon(String text, ImageIcon icon)
	{
		setStatusBarText(text);
		if(icon == null)
			setStatusBarIcon(nullIcon);
		else
			setStatusBarIcon(icon);
	}
	
	private void showProgressDialog(final boolean show)
	{
		if(progressDialog == null)
			progressDialog = new ProgressDialog();
		if(show)
			Kernel.getApplicationController().centerDialogOnComponent(progressDialog, modelviewer.getWidget3d());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				progressDialog.setVisible(show);
			}
		});
	}
	
	private void updateProgressBar(int newValue, String message)
	{
		if(progressDialog != null)
			progressDialog.updateProgressBar(newValue, message);
	}
	
	class StatusBarMouseListener extends MouseAdapter
	{
		private Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
		private Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR); 
		
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if(ifcLoaderErrors.size() > 0)
				statusToolBar.setCursor(HAND_CURSOR);
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			if(ifcLoaderErrors.size() > 0)
				statusToolBar.setCursor(DEFAULT_CURSOR);
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(SwingUtilities.isLeftMouseButton(e))
			{
				if(ifcLoaderErrors.size() > 0)
				{
					JDialog dialog = new IfcLoaderErrorReportDialog(Kernel.getApplicationController().getMainFrame(), 
							ifcLoaderErrors);
					Kernel.getApplicationController().centerDialogOnMainFrame(dialog);
					dialog.setVisible(true);
				}
			}
		}
	}
	
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
			
		}

		@Override
		public void progressBarUpdated(int value, String message)
		{
			updateProgressBar(value, message);
		}

		@Override
		public void selectionColorChanged(Color color)
		{
			
		}
	}
	
	class InternalLoadListener implements LoadManagerListener
	{
		@Override
		public void willLoadFile(Object source)
		{
			showProgressDialog(true);
		}
		
		@Override
		public void fileLoaded(Object source)
		{
			showProgressDialog(false);
		}
		
		@Override
		public void loadFailed(Object source, Throwable exception)
		{
			showProgressDialog(false);
		}
	}
	
	class InternalIfcLoaderErrorListener implements StepLoaderErrorListener
	{
		@Override
		public void errorHappened(ClassInterface product, Throwable t)
		{
			ifcLoaderErrors.put(t, product);
			resetStatusBar();
		}
	}
	
	public XModelViewer getModelviewer() {
		return modelviewer;
	}
	
	public XPlanungProjectExplorerViewFX getxPlanungProjectExplorerViewFX() {
		return xPlanungProjectExplorerViewFX;
	}
	
	public XplanungPropertyTreeTableView getXplanungPropertyTreeTableView() {
		return xplanungPropertyTreeTableView;
	}
	
	public XbauExplorer getXbauExplorer() {
		return xbauExplorer;
	}
	
	public IfcSelectionSetViewFX getSelectionSetView() {
		return selectionSetViewFX;
	}
	
	public XBCFCommentsView getXBCFCommentsView() {
		if(commentView instanceof XBCFCommentsView) {			
			return (XBCFCommentsView)commentView;
		}
		return null;
	}
}
