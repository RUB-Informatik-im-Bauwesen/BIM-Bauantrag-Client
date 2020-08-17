package de.rub.bi.inf.baclient.core.utils;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

/**
 * Some general needed functions.
 * 
 * @author Marcel Stepien
 *
 */
public class UIUtilities {
	
	private static Dimension hBoxDimension = new Dimension(Integer.MAX_VALUE, 25);
	private static Dimension lblDimension = new Dimension(100, 25);
	
	public static JMenuItem createMenuItem(String text, ActionListener listener, InputStream icon) throws IOException {
		JMenuItem item = null;
		if(icon == null) {
			item = new JMenuItem(text);
		}else {			
			item = new JMenuItem(text, new ImageIcon(ImageIO.read(icon)));
		}
		
		item.addActionListener(listener);
		return item;
	}
	
	public static JToggleButton createToggleButton(String text, ActionListener listener, InputStream icon) throws IOException {
		int sizeParam = 20;
		
		JToggleButton item = null;
		if(icon == null) {
			item = new JToggleButton();
		}else {			
			ImageIcon imIcon = new ImageIcon(ImageIO.read(icon));
			sizeParam = (int)((imIcon.getIconWidth() + imIcon.getIconHeight())/2);
			item = new JToggleButton(imIcon);
		}
		
		item.setPreferredSize(new Dimension(sizeParam, sizeParam));
		
		item.addActionListener(listener);
		return item;
	}
	
	public static JButton createButton(String text, ActionListener listener, ImageIcon icon) throws IOException {
		int sizeParam = 20;
		int sizeParamOffset = 6;
		
		JButton item = null;
		if(icon == null) {
			item = new JButton();
		}else {			
			ImageIcon imIcon = icon;
			sizeParam = (int)((imIcon.getIconWidth() + imIcon.getIconHeight())/2) + sizeParamOffset;
			item = new JButton(imIcon);
		}
		
		item.setPreferredSize(new Dimension(sizeParam, sizeParam));
		
		item.addActionListener(listener);
		return item;
	}
	

	public static Dimension getHorizontalBoxDimension() {
		return hBoxDimension;
	}

	public static void setHorizontalBoxDimension(Dimension hBoxDimension) {
		UIUtilities.hBoxDimension = hBoxDimension;
	}

	public static Dimension getLabelDimension() {
		return lblDimension;
	}

	public static void setLabelDimension(Dimension lblDimension) {
		UIUtilities.lblDimension = lblDimension;
	}


}
