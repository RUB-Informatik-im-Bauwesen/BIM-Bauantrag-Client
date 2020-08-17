package de.rub.bi.inf.baclient.core.views;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.apstex.gui.core.controller.MacOSPref;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationinfo.ApplicationInfo;
import com.apstex.gui.menu.IfcMenuBar;
import com.apstex.gui.toolbar.IfcModelViewerToolbar;

public class XViewer extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static XViewer instance;
	
	public static XViewer getInstance() {
		if (instance == null)
			instance = new XViewer();
		return instance;
	}
	
	
	protected XViewerPanel viewerPanel;
	
	private XViewer() {

		viewerPanel = new XViewerPanel(new ProgramInfo(), this);
		this.add(viewerPanel, BorderLayout.CENTER);
		if ( ! Kernel.getApplicationController().isRestrictedAccessMode() && MacOSPref.isMacOS())
		{
			URL imageUrl = Thread.currentThread().getContextClassLoader().getResource("icons/app_icon_128.png");
			MacOSPref.setOSXDockIcon(new ImageIcon(imageUrl).getImage());
		}
		this.setJMenuBar(new XMenuBar(viewerPanel));
		this.add(new XViewerToolbar(viewerPanel), BorderLayout.NORTH);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				Kernel.getApplicationController().closeProgram();
				System.exit(0);
			}
		});
		// Pack
		this.setPreferredSize(new Dimension(1000, 800));
		this.pack();
		this.validate();
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
	
	}
	
	
	public static final String PROGRAM_TITLE = "IFC JAVA3D VIEWER";
	public static final String PROGRAM_VERSION = "4.2.0";
	public static final String PROGRAM_DATE = "2016-03-09";
	public static final String PROGRAM_VENDOR = "APSTEX";
	
	private class ProgramInfo implements ApplicationInfo
	{
		@Override
		public String getProgramTitle()
		{
			return PROGRAM_TITLE;
		}

		@Override
		public String getProgramVersion()
		{
			return PROGRAM_VERSION;
		}

		@Override
		public String getProgramDate()
		{
			return PROGRAM_DATE;
		}
		
		@Override
		public String getProgramVendor()
		{
			return PROGRAM_VENDOR;
		}

		@Override
		public boolean isApplet()
		{
			return false;
		}
	}


	
	public XViewerPanel getViewerPanel() {
		return viewerPanel;
	}
}
