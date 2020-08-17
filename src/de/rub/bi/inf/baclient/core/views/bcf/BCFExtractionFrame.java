package de.rub.bi.inf.baclient.core.views.bcf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import com.apstex.ifctoolbox.ifc.IfcPropertySetTemplate;

import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xbau.param.Element;
import de.rub.bi.inf.xbau.io.CustomNamespacePrefixMapper;
import de.xleitstelle.xbau.schema._2._1.AbweichungBeantragt;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class BCFExtractionFrame extends JFrame {

	private TreeTableView<Object[]> bcfTable = null;

	public BCFExtractionFrame() {
		super();

		this.setTitle("Extract BCF");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.setSize(1250, 700);
		getContentPane().setLayout(new BorderLayout());

		JPanel headPanel = new JPanel();
		headPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		getContentPane().add(headPanel, BorderLayout.NORTH);

		JLabel lblFillInAll = new JLabel("Fuege BCF als Anhang zu Abweichungen hinzu.");
		headPanel.add(lblFillInAll);

		JPanel tablePanelMarginBorder = new JPanel();
		tablePanelMarginBorder.setBorder(new EmptyBorder(2, 2, 2, 2));
		getContentPane().add(tablePanelMarginBorder, BorderLayout.CENTER);
		tablePanelMarginBorder.setLayout(new BorderLayout(0, 0));

		JScrollPane centerScrollPane = new JScrollPane();
		tablePanelMarginBorder.add(centerScrollPane, BorderLayout.CENTER);
		
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// Adding template propertie table
				JFXPanel templatePanel = new JFXPanel();
				templatePanel.setBorder(new LineBorder(Color.GRAY));
				// tablePanelMarginBorder.add(templatePanel, BorderLayout.NORTH);
				templatePanel.setLayout(new BorderLayout(0, 0));
				bcfTable = new TreeTableView<>();
				bcfTable.setShowRoot(false);
				bcfTable.setRoot(new TreeItem<Object[]>());
				bcfTable.setEditable(true);
				
				TreeTableColumn<Object[], String> abweichungColumn = new TreeTableColumn<Object[], String>("Abweichung");
				abweichungColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Object[], String>, //
				        ObservableValue<String>>() {
				 
				            @Override
				            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object[], String> param) {
				                TreeItem<Object[]> treeItem = param.getValue();
				                Object[] emp = treeItem.getValue();
				                Object value = emp[0];
				                return new SimpleObjectProperty<String>(value.toString());
				            }
				        });
				abweichungColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
				abweichungColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Object[], String>>() {
					 
		            @Override
		            public void handle(TreeTableColumn.CellEditEvent<Object[], String> event) {
		                TreeItem<Object[]> item = event.getRowValue();
		                Object[] emp = item.getValue();
		                String newValue = event.getNewValue();
		                emp[0] = newValue;
		                
		                System.out.println("Single column commit. new Value:" + newValue);
		            }
		        });
				
				abweichungColumn.setPrefWidth(425);
				bcfTable.getColumns().add(abweichungColumn);

				TreeTableColumn<Object[], String> descColumn = new TreeTableColumn<Object[], String>("Beschreibung");
				descColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Object[], String>, //
				        ObservableValue<String>>() {
				 
				            @Override
				            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object[], String> param) {
				                TreeItem<Object[]> treeItem = param.getValue();
				                Object[] emp = treeItem.getValue();
				                Object value = emp[1];
				                return new SimpleObjectProperty<String>(value.toString());
				            }
				        });
				descColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
				descColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Object[], String>>() {
					 
		            @Override
		            public void handle(TreeTableColumn.CellEditEvent<Object[], String> event) {
		                TreeItem<Object[]> item = event.getRowValue();
		                Object[] emp = item.getValue();
		                String newValue = event.getNewValue();
		                emp[1] = newValue;
		                
		                System.out.println("Single column commit. new Value:" + newValue);
		            }
		        });
				
				descColumn.setPrefWidth(475);
				bcfTable.getColumns().add(descColumn);

				//retrieve all bcf comment keys
				String[] items = XViewer.getInstance().getViewerPanel().getXBCFCommentsView().getBCFKeys();
				
				TreeTableColumn<Object[], String> bcfColumn = new TreeTableColumn<Object[], String>("BCF");
				bcfColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Object[], String>, //
				        ObservableValue<String>>() {
				 
				            @Override
				            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object[], String> param) {
				                TreeItem<Object[]> treeItem = param.getValue();
				                Object[] emp = treeItem.getValue();
				                Object value = emp[2];
				                return new SimpleObjectProperty<String>(value.toString());
				            }
				        });

				bcfColumn.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(items));
				bcfColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Object[], String>>() {
					 
		            @Override
		            public void handle(TreeTableColumn.CellEditEvent<Object[], String> event) {
		                TreeItem<Object[]> item = event.getRowValue();
		                Object[] emp = item.getValue();
		                String newValue = event.getNewValue();
		                emp[2] = newValue;

		                System.out.println("Single column commit. new Value:" + newValue);
		            }
		        });
				bcfColumn.setPrefWidth(200);
				bcfTable.getColumns().add(bcfColumn);
				
				
				VBox vBox = new VBox();
				vBox.getChildren().setAll(bcfTable);
				VBox.setVgrow(bcfTable, Priority.ALWAYS);

				Scene scene = new Scene(vBox);

				templatePanel.setScene(scene);

				centerScrollPane.setViewportView(templatePanel);
			}
		});

		// Footer with buttons
		JPanel footerPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) footerPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(footerPanel, BorderLayout.SOUTH);

		JButton btnNext = new JButton("Hinzufügen");
		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//TODO READ BCFs

				//dispose();
			}
		});
		footerPanel.add(btnNext);
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}
	
	public void ladeAbweichungen(TreeItem root) {

		for (int i = 0; i < root.getChildren().size(); i++) {
			
			TreeItem child = (TreeItem)root.getChildren().get(i);
			
			Object value = child.getValue();
			
			if(value instanceof Element) {	
				if(((Element)value).getName() == "beantragteAbweichung") {
					if(((Element)value).getValue() != null && ((Element)value).getValue() instanceof AbweichungBeantragt) {						
						ladeAbweichungen((AbweichungBeantragt)((Element)value).getValue());
					}
				}
			}
			
			ladeAbweichungen(child);
		}
	}
	
	private void ladeAbweichungen(AbweichungBeantragt abweichung) {
		
		Object[] treeItem = new Object[3];
		treeItem[0] = abweichung.toString();
		treeItem[1] = abweichung.getBeschreibung();
		treeItem[2] = "DO BCF";
		
		bcfTable.getRoot().getChildren().add(new TreeItem<Object[]>(treeItem));
		
		//System.out.println(abweichung.getBeschreibung());
		
		bcfTable.refresh();

	}
	
}
