package de.rub.bi.inf.baclient.core.views.pruefungen;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.workflow.pruefung.Baugrenzen;
import de.rub.bi.inf.baclient.workflow.pruefung.Brandschutzklassifizierung;
import de.rub.bi.inf.baclient.workflow.pruefung.ErsterUndZweiterRettungsweg;
import de.rub.bi.inf.baclient.workflow.pruefung.Gebaeudeklassifizierung;
import de.rub.bi.inf.baclient.workflow.pruefung.Geschossigkeit;
import de.rub.bi.inf.baclient.workflow.pruefung.Grundflaechenzahl;
import de.rub.bi.inf.baclient.workflow.pruefung.NotwendigeFlure;
import de.rub.bi.inf.baclient.workflow.pruefung.Pruefvorgang;
import de.rub.bi.inf.baclient.workflow.pruefung.RO_Abstandsfleachen;
import de.rub.bi.inf.baclient.workflow.pruefung.Stellplatzordnung;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.FeaturePropertyType;

public class ModellpruefungView extends JFrame {

	private static ArrayList<Pruefvorgang> rules;

	private TreeTableView treeTableView = null;
	private ModellpruefungView self;

	public ModellpruefungView() throws IOException {	
		
		if(ModellpruefungView.rules == null) {
			ModellpruefungView.rules = generateRegeln(null, null);
		}
		
		setTitle("Modellprüfung");
		setSize(1100, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		JPanel centerContent = new JPanel();
		centerContent.setLayout(new BorderLayout());

		this.initMenuBar();
		this.initContentPanel(centerContent);

		JScrollPane scrollPane = new JScrollPane(centerContent);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.self = this;
		setVisible(true);
		
		rebuild();
	}

	
	public ArrayList<Pruefvorgang> generateRegeln(ApplicationModelNode ifcModel, AbstractFeatureType bpBereich) {
		ArrayList<Pruefvorgang> mea = new ArrayList<>();
		
		Baugrenzen baugrenzen = new Baugrenzen();
		mea.add(baugrenzen);
		
		Grundflaechenzahl grundflaechenzahl = new Grundflaechenzahl();
		mea.add(grundflaechenzahl);
		
		Geschossigkeit geschossigkeit = new Geschossigkeit();
		mea.add(geschossigkeit);
		
		Stellplatzordnung stellplatzordnung = new Stellplatzordnung();
		mea.add(stellplatzordnung);
		
		Gebaeudeklassifizierung gebaeudeklassifizierung = new Gebaeudeklassifizierung();
		mea.add(gebaeudeklassifizierung);
		
		Pruefvorgang abstandsflaechen = new RO_Abstandsfleachen();
		mea.add(abstandsflaechen);
		
		ErsterUndZweiterRettungsweg ersterUndZweiterRettungsweg = new ErsterUndZweiterRettungsweg();
		mea.add(ersterUndZweiterRettungsweg);
		
		NotwendigeFlure notwendigeFlure = new NotwendigeFlure();
		mea.add(notwendigeFlure);
		
		Brandschutzklassifizierung brandschutzklassifizierung = new Brandschutzklassifizierung();
		mea.add(brandschutzklassifizierung);

		return mea;
	}

	
	private void initMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu menu = new JMenu("Options");
		menubar.add(menu);
		
		JMenuItem item = new JMenuItem("To XBau");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//TODO Action
				
			}
		});
		
		JMenuItem item2 = new JMenuItem("Export");
		item2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//TODO Action
				
			}
		});

		menu.add(item);
		menu.add(item2);
		
		this.setJMenuBar(menubar);
	}

	private void initContentPanel(JPanel contentPanel) throws IOException {
		
		JFXPanel centerPanel = new JFXPanel();
		centerPanel.setBorder(new EmptyBorder(1, 0, 1, 0));
		contentPanel.add(centerPanel, BorderLayout.CENTER);

		initContentChain(centerPanel);
		
		JFXPanel topPanel = new JFXPanel();
		//topPanel.setMinimumSize(new Dimension(0, 150));
		topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		contentPanel.add(topPanel, BorderLayout.NORTH);
						
		ArrayList<ApplicationModelNode> modelList = new ArrayList<>();
		for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
			modelList.add(node); //node.getModelName(), der name des modells
		}

		//Create ComboboxItems
		ObservableList<ApplicationModelNode> options = FXCollections.observableArrayList(
			modelList
		);
				
		final ComboBox comboBox = new ComboBox(options);
		comboBox.setMaxWidth(200);
		comboBox.setMinWidth(200);
		final Label label = new Label("IFC Model: ");
		
		ArrayList<XPlanungModel> xPlanList = new ArrayList<>();
		
		for(XPlanungModel xPlan : XPlanungModelContainer.getInstance().getModels()) {
			xPlanList.add(xPlan);
		}
		
		ObservableList<XPlanungModel> options2 =  FXCollections.observableArrayList(
			xPlanList
		);
		
		final ComboBox<?> comboBox2 = new ComboBox(options2);
		comboBox2.setMaxWidth(200);
		comboBox2.setMinWidth(200);
		final Label label2 = new Label("BPlan: ");
		
		
		
		
		ArrayList<Object> bereiche = new ArrayList<>();
		ObservableList<Object> oBereiche = FXCollections.observableArrayList(bereiche);
		final ComboBox<Object> comBoxTeilFlaeche = new ComboBox<>(oBereiche);
		comBoxTeilFlaeche.setMaxWidth(200);
		comBoxTeilFlaeche.setMinWidth(200);
		final Label labelBereiche = new Label("Bereich: ");
		
		
		
		comboBox2.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent arg0) {
				if(comboBox2.getSelectionModel().getSelectedItem() == null)
					return;
				
				if ((comboBox2.getSelectionModel().getSelectedItem() instanceof XPlanungModel)==false);
				
		     
				XPlanungModel xPlanModel = (XPlanungModel)comboBox2.getSelectionModel().getSelectedItem();
				
				for (FeaturePropertyType featurePropertyType: xPlanModel.getFeatures()) {
					AbstractFeatureType abstractFeatureType = featurePropertyType.getAbstractFeature().getValue();
					if(abstractFeatureType.getClass().getSimpleName().contains("BPBereichType")) {
						oBereiche.add(abstractFeatureType);
					}
				}
				
//				JOptionPane.showMessageDialog(null, bereiche.size());
		
			}
		});
		
		
		Callback<ListView<Object>, ListCell<Object>> cellFactory = new Callback<ListView<Object>, ListCell<Object>>() {

			@Override
			public ListCell<Object> call(ListView<Object> param) {
				return new ListCell<Object>() {
					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                	AbstractFeatureType featureType = (AbstractFeatureType) item;
		                  
		                	setText(featureType.getId().toString());
		                	
		                }
					}
				};
			}
		};
		
		
		comBoxTeilFlaeche.setCellFactory(cellFactory);
		comBoxTeilFlaeche.setButtonCell(cellFactory.call(null));
		
		
		Button buttonUpdate = new Button("Update");
		buttonUpdate.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			
			@Override
			public void handle(javafx.event.ActionEvent event) {
				ApplicationModelNode ifcModel = (ApplicationModelNode)comboBox.getSelectionModel().getSelectedItem();
				XPlanungModel xPlanModel = (XPlanungModel)comboBox2.getSelectionModel().getSelectedItem();
				AbstractFeatureType bpBereich = (AbstractFeatureType) comBoxTeilFlaeche.getSelectionModel().getSelectedItem();
				
//				if(ifcModel != null) {					
					ModellpruefungView.rules = generateRegeln(ifcModel, bpBereich);
					
					rules.forEach(pv -> pv.setxPlanungModel(xPlanModel));
					rules.forEach(pv -> pv.pruefe(ifcModel, bpBereich));
					
					rebuild();
//					
//				}else {
//					System.out.println("Something went wrong! Check IFC and XPlanungs Model");
//				}
			}
		});
		
		final HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setMinHeight(45);
		hbox.getChildren().addAll(
				label, 
				comboBox, 
				new Label("   "), //spacer 1
				label2, comboBox2, 
				new Label("   "), //spacer 2 
				labelBereiche, comBoxTeilFlaeche,
				new Label("   "), //spacer 3
				buttonUpdate
		);
        
        final Scene scene = new Scene(hbox);
        
        
        javafx.application.Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				topPanel.setScene(scene);
				
			}
		});
        
	
	}

	private EventHandler selectionEvent = new EventHandler<Event>() {

		@Override
		public void handle(Event event) {
			TreeTableView tableView = (TreeTableView) event.getSource();

			Object obj = tableView.getSelectionModel().getSelectedItem();

			if (obj instanceof TreeItem) {
				Object itemValue = ((TreeItem) obj).getValue();

				if (itemValue instanceof AbstractFeatureType) {
					XViewer.getInstance().getViewerPanel().getXplanungPropertyTreeTableView()
							.loadProperties((AbstractFeatureType) itemValue);
				}
			}

		}
	};

	private void initContentChain(JFXPanel content) {

		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {

				treeTableView = new TreeTableView<>();
				treeTableView.setEditable(true);
				treeTableView.setShowRoot(false);
				
				//COLUMN: ID
				//====================================================================
				TreeTableColumn<Object, Object> columnID = new TreeTableColumn<>("");
				
				columnID.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				columnID.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

		            @Override
		            protected void updateItem(Object item, boolean empty) {
		                super.updateItem(item, empty);
		                
		                if(item != null && !empty) {
		                	if(item instanceof Pruefvorgang) {
				                setText(new Integer(rules.indexOf((Pruefvorgang)item)).toString());
		                	}
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
				columnID.setPrefWidth(40);
				columnID.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(columnID);
				
				//COLUMN: Maß
				//====================================================================
				TreeTableColumn<Object, Object> column = new TreeTableColumn<>("Maß");
			
				column.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				column.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

		            @Override
		            protected void updateItem(Object item, boolean empty) {
		                super.updateItem(item, empty);
		                
		                if(item != null && !empty) {
		           
		                	if(item instanceof Pruefvorgang) {
			                	setText(((Pruefvorgang)item).getName());
	                		
		                	}
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
				column.setPrefWidth(400);
				//column.setStyle("-fx-alignment: center;");
				
				treeTableView.getColumns().add(column);

				//COLUMN: Wert
				//====================================================================
				TreeTableColumn<Object, Object> columnTitel = new TreeTableColumn<Object, Object>("Wert");
				columnTitel.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));
				
				columnTitel.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

		            @Override
		            protected void updateItem(Object item, boolean empty) {
		                super.updateItem(item, empty);
		                
		                if(item != null && !empty) {
		    
		                	if(item instanceof Pruefvorgang) {
			                	setText(((Pruefvorgang)item).getValue());
		                	}
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
				columnTitel.setPrefWidth(200);
				columnTitel.setStyle("-fx-alignment: center;");
				
				treeTableView.getColumns().add(columnTitel);


				//COLUMN: Nachricht
				//====================================================================
				TreeTableColumn<Object, Object> columnComment = new TreeTableColumn<Object, Object>("Nachricht");
				
				columnComment.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));
				
				columnComment.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

		            @Override
		            protected void updateItem(Object item, boolean empty) {
		                super.updateItem(item, empty);
		                
		                if(item != null && !empty) {
		         
		                	if(item instanceof Pruefvorgang) {
			                	setText(((Pruefvorgang)item).getComment());
			                	
			                	final Tooltip tooltip = new Tooltip();
			                	tooltip.setPrefWidth(300);
			                	tooltip.setWrapText(true);
			    				tooltip.setText(((Pruefvorgang)item).getComment());
			                	setTooltip(tooltip);
		                	}
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
				columnComment.setPrefWidth(350);
				//columnComment.setStyle("-fx-alignment: center;");
				
				treeTableView.getColumns().add(columnComment);
				
				
				//COLUMN: OPTIONS
				//====================================================================
				TreeTableColumn<Object, Object> columnOptions = new TreeTableColumn<>("");
				
				columnOptions.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				columnOptions.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

		            @Override
		            protected void updateItem(Object item, boolean empty) {
		                super.updateItem(item, empty);
		                
		                if(item != null && !empty) {
		               
		                	if(item instanceof Pruefvorgang) {
				                setText(""); //TODO
		                	}
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
				columnOptions.setPrefWidth(40);
				columnOptions.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(columnOptions);
				
				treeTableView.setRowFactory(new Callback<TreeTableView<Object>, TreeTableRow<Object>>() {

					@Override
					public TreeTableRow<Object> call(TreeTableView<Object> param) {
						final TreeTableRow<Object> row = new TreeTableRow<>();  
		                row.contextMenuProperty().bind(new ModellpruefungMenuObservable(row));
		                return row;
					}
				});
				
				
				/*
				String cellStyle = "-fx-border-width: 0 0 1 0; -fx-border-color: lightgray; -fx-border-style: solid;";
				column.setStyle(cellStyle);
				columnTitel.setStyle(cellStyle);
				columnComment.setStyle(cellStyle);
				*/
				
				// Wrap to jfxpanel
				VBox vBox = new VBox();
				vBox.getChildren().setAll(treeTableView);
				VBox.setVgrow(treeTableView, Priority.ALWAYS);

				javafx.scene.Scene scene = new javafx.scene.Scene(vBox);

				content.setScene(scene);

				treeTableView.setOnMouseClicked(selectionEvent);
				treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			}
		});
	}

	public void refresh() {
		this.treeTableView.refresh();
	}
	
	public void rebuild() {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	TreeItem root = createTree();
		    	treeTableView.setRoot(root);
		    }
		});
		refresh();
	}
	
	private TreeItem createTree() {
		TreeItem<Object> top = new TreeItem<Object>("Nutzungsmaße");
		top.setExpanded(true);
		for(Pruefvorgang rule : ModellpruefungView.rules) {
			TreeItem<Object> modelNode = new TreeItem<Object>(rule);
			top.getChildren().add(modelNode);
		}
		
		return top;
	}
}
