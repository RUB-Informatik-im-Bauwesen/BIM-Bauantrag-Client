package de.rub.bi.inf.baclient.core.views.xplanung;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;

public class XPlanungRepresentationSettingsView extends JFrame{
	
	private Dimension lblDimension = new Dimension(100, 25);
	
	private JSlider lineThicknessSlider;
	private JSlider pointThicknessSlider;
	
	public XPlanungRepresentationSettingsView() {
		setTitle("Representation Settings");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel centerContent = new JPanel();
		centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
		
		this.initContentPanel(centerContent);
		
		
		JScrollPane scrollPane = new JScrollPane(centerContent);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
//		JButton applyButton = new JButton("Apply");
//		applyButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				//TODO
//				
//			}
//		});
//		
//		JPanel buttonPanel = new JPanel();
//		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
//		buttonPanel.add(applyButton);
//		
//		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	
	private void initContentPanel(JPanel contentPanel) {

		JPanel translateContent = new JPanel();
		translateContent.setLayout(new BoxLayout(translateContent, BoxLayout.Y_AXIS));
		translateContent.setBorder(new TitledBorder(null, "Thickness", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		Hashtable<Integer, JLabel> labels = new Hashtable<>();
		labels.put(1, new JLabel("1"));
		labels.put(5, new JLabel("5"));
		labels.put(10, new JLabel("10"));
		labels.put(15, new JLabel("15"));
		labels.put(20, new JLabel("20"));
		labels.put(25, new JLabel("25"));
		
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBorder(new EmptyBorder(1, 0, 1, 0));
		//horizontalBox.setMaximumSize(dimension);
		translateContent.add(horizontalBox);
		
		JLabel lblValue = new JLabel("Line:");
		lblValue.setPreferredSize(lblDimension);
		horizontalBox.add(lblValue);
		
		lineThicknessSlider = new JSlider();
		lineThicknessSlider.setMinimum(1);
		lineThicknessSlider.setMaximum(25);	
		lineThicknessSlider.setValue(GeometryUtil.getGeometryLineThickness());	
		lineThicknessSlider.setSnapToTicks(true);
		lineThicknessSlider.setLabelTable(labels);
		lineThicknessSlider.setPaintLabels(true);		
		lineThicknessSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				super.mouseReleased(arg0);
				
				if(arg0.getSource() instanceof JSlider) {
					int value = ((JSlider)arg0.getSource()).getValue();
					
					for(XPlanungModel model : XPlanungModelContainer.getInstance().getModels()) {
						for(XPlanungCadObjectJ3D cadObj : model.getCadObjects()) {
							cadObj.setLineThickness(value);
						}
					}
					GeometryUtil.setGeometryLineThickness(value);
				}
				
			}
		});
		horizontalBox.add(lineThicknessSlider);
		

		Box horizontalBox2 = Box.createHorizontalBox();
		horizontalBox2.setBorder(new EmptyBorder(1, 0, 1, 0));
		//horizontalBox2.setMaximumSize(hBoxDimension);
		translateContent.add(horizontalBox2);
		
		
		JLabel lblValue2 = new JLabel("Point:");
		lblValue2.setPreferredSize(lblDimension);
		horizontalBox2.add(lblValue2);
		
		
		pointThicknessSlider = new JSlider();
		pointThicknessSlider.setMinimum(1);
		pointThicknessSlider.setMaximum(25);	
		pointThicknessSlider.setValue(GeometryUtil.getGeometryPointThickness());	
		pointThicknessSlider.setSnapToTicks(true);
		pointThicknessSlider.setLabelTable(labels);
		pointThicknessSlider.setPaintLabels(true);
		pointThicknessSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				super.mouseReleased(arg0);
				
				if(arg0.getSource() instanceof JSlider) {
					int value = ((JSlider)arg0.getSource()).getValue();
					
					for(XPlanungModel model : XPlanungModelContainer.getInstance().getModels()) {
						for(XPlanungCadObjectJ3D cadObj : model.getCadObjects()) {
							cadObj.setPointThickness(value);
						}
					}
					GeometryUtil.setGeometryPointThickness(value);
					
				}
				
			}
		});
		horizontalBox2.add(pointThicknessSlider);
		
		contentPanel.add(translateContent);
	}
}
