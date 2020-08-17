package de.rub.bi.inf.baclient.core.views;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import com.apstex.gui.core.controller.ViewController.NavigationMode;
import com.apstex.gui.core.controller.ViewControllerAdapter;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.gui.ifc.controller.StoreyTranslationController;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.views.bcf.BCFExtractionFrame;
import de.rub.bi.inf.baclient.core.views.bcf.XBCFCommentsView;
import de.rub.bi.inf.baclient.core.views.extraction.DatenextraktionView;
import de.rub.bi.inf.baclient.core.views.ifc.SelectionSet;
import de.rub.bi.inf.baclient.core.views.pruefungen.ModellpruefungView;
import de.rub.bi.inf.baclient.io.actions.IfcLoadAction;
import de.rub.bi.inf.baclient.io.actions.IfcSaveAction;
import de.rub.bi.inf.baclient.io.actions.XBauLoadAction;
import de.rub.bi.inf.baclient.io.actions.XBauSaveAction;
import de.rub.bi.inf.baclient.io.actions.XPlanungLoadAction;
import de.rub.bi.inf.baclient.io.actions.ZipLoadAction;
import de.rub.bi.inf.baclient.io.actions.ZipSaveActionByTemp;

public class XMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	protected XViewerPanel viewerPanel;
	protected JRadioButtonMenuItem orbitItem, walkItem;
	protected JCheckBoxMenuItem gridItem, edgeViewItem, transViewItem, coordSysItem, selectionItem;
	protected JMenuItem storeyTransItem;
	
	public XMenuBar(XViewerPanel viewerPanel)
	{
		this.viewerPanel = viewerPanel;
		Kernel.getViewController().addListener(new InternalViewControllerListener());
		init();
	}
	
	private void init()
	{
		createFileMenu();
		createToolsMenu();
		createViewMenu();
		createSelectionMenu();
		createHelpMenu();
	}
	
	protected ImageIcon getIcon(String iconPath)
	{
		URL imageUrl = Thread.currentThread().getContextClassLoader().getResource(iconPath);
		ImageIcon icon = new ImageIcon(imageUrl);
		return new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	}
	
	protected void createFileMenu()
	{
		JMenuItem item;
		JMenu menu;
		
		menu = new JMenu("Datei");
		this.add(menu);
		
		// Create new Menu Item and insert to menu: TestButton
		//JMenuItem loadXPlanung = new JMenuItem("TEST: Load XPlanung");
//		loadXPlanung.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				// === CLEAR - dont show 2 different file's geometry in the same view ===
//				// XPlanungCadLoader.clearModel(view);
//
//				// ========== CREATE GEOMETRY ===========
//
//				// ArrayList<CadObject> objectJ3D = BuildGeometryTest.create(view);
//				XPlanungModel model = XPlanungParser.createModel();
//
//				// ===== ADD SELECTION LISTENER ========
//
//				Ifc3DViewJ3D view3d = XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d();
//				
//				// Create and register listener to the viewer
//				XPlanungCadLoader cadLoader = new XPlanungCadLoader();
//				cadLoader.setSelectionModel(new XPlanungSelectionModel(view3d));
//
//				
//				CadSelectionColorizeMouseListener selectionMouseListener2 = 
//						new CadSelectionColorizeMouseListener(view3d, model);
//				cadLoader.getSelectionModel().addListener(selectionMouseListener2);
//				
////				CadPointPickingMouseListener pointPickingMouseListener = 
////						new CadPointPickingMouseListener(view3d);
////				cadLoader.getSelectionModel().addListener(pointPickingMouseListener);
//
//				
//				// XPlanungCadLoader.getSelectionModel().addListener(selectionMouseListener);
//
//				// Load geometry into the viewer
//				cadLoader.interpretGeometry(model);
//				
//				// XPlanungCadLoader.addCadObject((CadObjectJ3D)objectJ3D);
//				cadLoader.loadIntoViewer(model, XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d());
//
//				
//				XPlanungModelContainer.getInstance().getModels().add(model);
//				
//				SwingUtilities.invokeLater(new Runnable() {
//					
//					@Override
//					public void run() {
//						XPlanungProjectExplorerView explorerView = XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView();
//						explorerView.refresh();
//					}
//				});
//				
//				
//			}
//		});
		//menu.insert(loadXPlanung, 0);

		// Create new Menu Item and insert to menu: "Load File"-Button
		JMenuItem loadFile = new JMenuItem("BPlan hinzufügen", getIcon("resources/icon/xplanung_32x32.png"));
		loadFile.addActionListener(new XPlanungLoadAction());
		menu.add(loadFile);
		
		JMenuItem loadXBauFile = new JMenuItem("XBau hinzufügen", getIcon("resources/icon/xbau_32x32.png"));
		loadXBauFile.addActionListener(new XBauLoadAction());
		menu.add(loadXBauFile);
		
		menu.addSeparator();
		
		JMenuItem saveXBauFile = new JMenuItem("XBau Speichern", getIcon("resources/icon/xbau_32x32.png"));
		saveXBauFile.addActionListener(new XBauSaveAction());
		menu.add(saveXBauFile);
		
		menu.addSeparator();
		
		item = new JMenuItem("IFC Modell hinzufügen", getIcon("resources/icon/ifc.png"));
		item.addActionListener(new IfcLoadAction());
		menu.add(item);
		
		
		item = new JMenuItem("IFC Modell exportieren", getIcon("resources/icon/ifc.png"));
//		item.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				try
//				{
//					List<ApplicationModelNode> nodes = Kernel.getApplicationModelRoot().getNodes();
//					
//					
//					
//					
//					for (ApplicationModelNode node : nodes)
//					{
//						if(nodes.size() > 1)
//						{
//							int option = JOptionPane.showConfirmDialog(Kernel.getApplicationController().getMainFrame(), 
//									"Do you want to save the IFC model \""+node.getModelName()+"\"?");
//							if(option == JOptionPane.CANCEL_OPTION)
//								break;
//							if(option == JOptionPane.NO_OPTION)
//								continue;
//						}
//						List<FileFilter> fileFilters = new ArrayList<FileFilter>();
//						fileFilters.add(new IfcStepFileFilter());
//						fileFilters.add(new IfcZipFileFilter());
//						File file = Kernel.getApplicationController().openSaveFileDialog(null, fileFilters);
//						SaveManager.getInstance().saveFile(file, node.getStepModel());
////						
//					}
//				}
//				catch(Exception e1)
//				{
//					Kernel.getApplicationController().showErrorMessage("Error while saving IFC File!", e1);
//				}
//			}
//		});
		
		item.addActionListener(new IfcSaveAction());
		menu.add(item);
		
		menu.addSeparator();
		item = new JMenuItem("Bauantrag öffnen", getIcon("resources/icon/package.png"));
		item.addActionListener(new ZipLoadAction());
		menu.add(item);
		
		/*
		item = new JMenuItem("Export Zip", getIcon("icons/save-32x32.png"));
		item.addActionListener(new ZipSaveAction());
		menu.add(item);
		*/
		
		item = new JMenuItem("Bauantrag speichern", getIcon("resources/icon/package.png"));
		item.addActionListener(new ZipSaveActionByTemp());
		menu.add(item);
		
		
//		menu.addSeparator();
		item = new JMenuItem("Save Snapshot", getIcon("icons/screenshot.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				viewerPanel.saveSnapshot();
			}
		});
//		menu.add(item);
//		menu.addSeparator();
		item = new JMenuItem("Close All Models", getIcon("icons/clear.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getApplicationController().clearModels();
			}
		});
//		menu.add(item);
		
		//Note: It's not allowed, that an applet closes virtual machine by itself.
		if( ! Kernel.getApplicationInfo().isApplet())
		{
//			menu.addSeparator();
			item = new JMenuItem("Close Program", getIcon("icons/cancel256.png"));
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Kernel.getApplicationController().closeProgram();
				}
			});
//			menu.add(item);
		}
	}
	
	protected void createToolsMenu()
	{
		JMenuItem item;
		JMenu menu;
		
		menu = new JMenu("Tools");
		this.add(menu);
		
		item = new JMenuItem("Datenextraktion", getIcon("icons/folder.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try {
					new DatenextraktionView();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Modellpruefung", getIcon("icons/folder.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try {
					new ModellpruefungView();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		menu.add(item);
	
		
//		item = new JMenuItem("BCF zu Antrag", getIcon("icons/save-32x32.png"));
//		item.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				BCFExtractionFrame extractor = new BCFExtractionFrame();
//				extractor.ladeAbweichungen(XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot());
//				
//			}
//		});
//		menu.add(item);
		
		// Add new Separator to the menu
		//JMenuItem separator = new JMenuItem();
		//separator.add(new JSeparator());
		//menu.insert(separator, 0);
	}
	
	protected void createViewMenu()
	{
		JMenu menu = new JMenu("View");
		this.add(menu);
		
		ButtonGroup viewButtonGroup = new ButtonGroup();
		orbitItem = new JRadioButtonMenuItem("Orbit Mode", getIcon("icons/orbit.png"));
		orbitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		orbitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setNavigationMode(NavigationMode.ORBIT, null);
			}
		});
		viewButtonGroup.add(orbitItem);
		orbitItem.setSelected(Kernel.getViewController().getNavigationMode() == NavigationMode.ORBIT);
		menu.add(orbitItem);
		
		walkItem = new JRadioButtonMenuItem("Walk Mode", getIcon("icons/walk.png"));
		walkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		walkItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setNavigationMode(NavigationMode.WALK, null);
			}
		});
		viewButtonGroup.add(walkItem);
		walkItem.setSelected(Kernel.getViewController().getNavigationMode() == NavigationMode.WALK);
		menu.add(walkItem);
		
		menu.addSeparator();
		
		gridItem = new JCheckBoxMenuItem("Grid", getIcon("icons/grid.png"));
		gridItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		gridItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setGridEnabled( ! Kernel.getViewController().isGridEnabled(), null);
			}
		});
		gridItem.setSelected(Kernel.getViewController().isGridEnabled());
		menu.add(gridItem);
		
		coordSysItem = new JCheckBoxMenuItem("Origin", getIcon("icons/coord.png"));
		coordSysItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		coordSysItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setOriginEnabled( ! Kernel.getViewController().isOriginEnabled(), null);
			}
		});
		coordSysItem.setSelected(Kernel.getViewController().isOriginEnabled());
		menu.add(coordSysItem);
		
		menu.addSeparator();
		
		edgeViewItem = new JCheckBoxMenuItem("Edges", getIcon("icons/wiredFrame.png"));
		edgeViewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		edgeViewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setEdgeViewEnabled( ! Kernel.getViewController().isEdgeViewEnabled(), null);
			}
		});
		edgeViewItem.setSelected(Kernel.getViewController().isEdgeViewEnabled());
		menu.add(edgeViewItem);
		
		transViewItem = new JCheckBoxMenuItem("Transparent View", getIcon("icons/solid_trans.png"));
		transViewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		transViewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setTransparentViewEnabled( ! Kernel.getViewController().isTransparentViewEnabled(), null);
			}
		});
		transViewItem.setSelected(Kernel.getViewController().isTransparentViewEnabled());
		menu.add(transViewItem);
		
		menu.addSeparator();
		
		storeyTransItem = new JMenuItem("Storey Shifting", getIcon("icons/storey_trans.png"));
		storeyTransItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		storeyTransItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				viewerPanel.showStoreyTranslationDialog();
			}
		});
		menu.add(storeyTransItem);
	}
	
	protected void createSelectionMenu()
	{
		JMenu menu = new JMenu("Selection");
		JMenuItem item;
		this.add(menu);
		
		selectionItem = new JCheckBoxMenuItem("Selection Box Mode", getIcon("icons/selectElement.png"));
		selectionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		selectionItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(Kernel.getViewController().getNavigationMode() != NavigationMode.SELECT)
				{
					Kernel.getViewController().setNavigationMode(NavigationMode.SELECT, null);
				}
				else
				{
					Kernel.getViewController().setPreviousNavigationMode(null);
				}
			}
		});
		selectionItem.setSelected(Kernel.getViewController().getNavigationMode() == NavigationMode.SELECT);
		menu.add(selectionItem);
		
		menu.addSeparator();
		
		item = new JMenuItem("Clear Selection", getIcon("icons/unselect.png"));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setAllObjectsSelected(false);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Invert Selection", getIcon("icons/invertselection.png"));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().invertSelection();
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Show Selected Objects", getIcon("icons/visible.png"));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setSelectedObjectsVisible(true);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Hide Selected Objects", getIcon("icons/unvisible.png"));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setSelectedObjectsVisible(false);
			}
		});
		menu.add(item);
		
		menu.addSeparator();
		
		item = new JMenuItem("Reset View Settings", getIcon("icons/initialView_small.png"));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getViewController().setAllObjectsSelected(false);
				Kernel.getViewController().setAllObjectsVisible(true);
				StoreyTranslationController.getInstance().resetStoreyTranslation(null);
				Kernel.getViewController().resetView(null);
			}
		});
		menu.add(item);
	}
	
	protected void createHelpMenu()
	{
		JMenu menu;
		JMenuItem item;
		
		menu = new JMenu("Help");
		this.add(menu);
		item = new JMenuItem("IFC Tools Project Homepage", getIcon("icons/app_icon_32.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Kernel.getApplicationController().openWebSiteInDefaultBrowser("http://www.ifctoolsproject.com");
			}
		});
		menu.add(item);
		item = new JMenuItem("System Info ", getIcon("icons/sysinfo.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				viewerPanel.showSysInfoDialog();
			}
		});
		menu.add(item);
		item = new JMenuItem("About " + Kernel.getApplicationInfo().getProgramTitle(), getIcon("icons/info2.png"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				viewerPanel.openAboutProgramDialog();
			}
		});
		menu.add(item);
	}
	
	class InternalViewControllerListener extends ViewControllerAdapter
	{
		@Override
		public void navigationModeChanged(NavigationMode mode)
		{
			orbitItem.setSelected(mode == NavigationMode.ORBIT);
			walkItem.setSelected(mode == NavigationMode.WALK);
			selectionItem.setSelected(mode == NavigationMode.SELECT);
		}
		
		@Override
		public void gridVisibilityChanged(boolean isVisible)
		{
			gridItem.setSelected(isVisible);
		}
		
		@Override
		public void edgeViewStateChanged(boolean isEnabled)
		{
			edgeViewItem.setSelected(isEnabled);
		}
		
		@Override
		public void transparentViewStateChanged(boolean isEnabled)
		{
			transViewItem.setSelected(isEnabled);
		}
		
		@Override
		public void coordSystemVisibilityChanged(boolean isVisible)
		{
			coordSysItem.setSelected(isVisible);
		}
	}
}
