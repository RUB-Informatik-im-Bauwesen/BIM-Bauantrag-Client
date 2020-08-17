package de.rub.bi.inf.baclient.core.views;


import javax.swing.JButton;
import javax.swing.JToolBar;

import com.apstex.gui.core.views.view3d.ModelViewerToolbar;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.gui.toolbar.RecentLoadButton;
import com.apstex.ifcjava3dviewerpro.IfcViewerPanel;

import de.rub.bi.inf.baclient.io.actions.IfcLoadAction;


public class XViewerToolbar extends ModelViewerToolbar
{
	private static final long serialVersionUID = 1L;
	
	protected static final int LOAD_FILE = 100;
	protected static final int SNAPSHOT = 101;
	protected static final int STOREY_SHIFTING = 102;
//	protected static final int OPTIONS_DIALOG = 103;
	protected static final int LAYERS = 104;
	
	protected XViewerPanel viewerPanel;
	protected JButton storeyShiftingButton, optionsButton, layersButton;

	public XViewerToolbar(XViewerPanel viewerPanel)
	{
		super();
		this.viewerPanel = viewerPanel;
		init();
	}
	
	private void init()
	{
		int pos = 0;
		//add to the beginning:
//		JButton loadButton = createButton("icons/folder_add.png", null, null, "Add File", "Add IFC File", LOAD_FILE);
//		this.add(loadButton, pos++);
		
//		JButton recentLoadButton = new RecentLoadButton(loadButton);
//		this.add(recentLoadButton, pos++);
		
		JButton snapButton = createButton("icons/screenshot.png", null, null, "Snapshot", "Save Snapshot of the 3D View", SNAPSHOT);
		this.add(snapButton, pos++);
		
		this.add(new JToolBar.Separator(null), pos++);
		
		//add to the end:
		this.addSeparator();
		
		storeyShiftingButton = createButton("icons/storey_trans.png", null, null, "Shift", 
				"Show Storey Shifting Dialog", STOREY_SHIFTING);
		this.add(storeyShiftingButton);
		
		layersButton = createButton("icons/layers.png", null, null, "Layers", 
				"Change Visibility of Layers", LAYERS);
		this.add(layersButton);
		
//		this.addSeparator();
//		
//		optionsButton = createButton("icons/options50.png", null, null, "Preferences", "Shows Options Dialog", OPTIONS_DIALOG);
//		this.add(optionsButton);
	}
	
	@Override
	protected void actionPerformed(int eventCode)
	{
		super.actionPerformed(eventCode);
		switch(eventCode)
		{
		case LOAD_FILE:
			IfcLoadManager.getInstance().openLoadFileDialogAndStartLoading(false);
			break;
		case SNAPSHOT:
			viewerPanel.saveSnapshot();
			break;
		case STOREY_SHIFTING:
			viewerPanel.showStoreyTranslationDialog();
			break;
		case LAYERS:
			viewerPanel.showLayersDialog();
			break;
		}
	}
}
