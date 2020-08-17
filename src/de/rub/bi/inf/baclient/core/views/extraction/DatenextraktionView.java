package de.rub.bi.inf.baclient.core.views.extraction;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xbau.XBauContextMenuObservable;
import de.rub.bi.inf.baclient.core.views.xbau.actions.AddElementAction;
import de.rub.bi.inf.baclient.workflow.extraktion.ArtDerMassnahme;
import de.rub.bi.inf.baclient.workflow.extraktion.ArtDesGebaeudes;
import de.rub.bi.inf.baclient.workflow.extraktion.Bauweise;
import de.rub.bi.inf.baclient.workflow.extraktion.BezeichnungDesBauvorhabens;
import de.rub.bi.inf.baclient.workflow.extraktion.Bruttogrundflaechen;
import de.rub.bi.inf.baclient.workflow.extraktion.ExtraktionsVorgang;
import de.rub.bi.inf.baclient.workflow.extraktion.Gebaeudeklasse;
import de.rub.bi.inf.baclient.workflow.extraktion.Nettogrundflachen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_AnzahlVollgeschosse;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Baumasse;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Baumassenzahl;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_BebauteGrundstuecksflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_ErforderlicheStellplaetze;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_FlaecheDerGemeinschaftsanlagen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_FlaecheDerNebenanlagen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_GeplanteStellplaetze;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Geschossflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Geschossflaechenzahl;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_GrundflaechenDerBaulichenAnlage;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Grundflaechenzahl;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_HoeheDerBaulichenAnlage;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_NichtBebauteGrundstuecksflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_NutzungseinheitenGewerbe;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_SpielUndFreizeitflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_StellplaetzeUndDerenZufahrten;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Verkaufsflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_VersiegelteFlaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenEigentumswohnungen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenGesamt;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenMietwohnungen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenSozialwohnungen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohnungenFreiberuflich;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohnungenGewerblich;
import de.rub.bi.inf.baclient.workflow.extraktion.Sonderbau;
import de.rub.bi.inf.baclient.workflow.pruefung.Pruefvorgang;
import de.rub.bi.inf.baclient.workflow.pruefung.RO_Abstandsfleachen;
import de.xleitstelle.xbau.schema._2._1.BaugenehmigungAntrag0200;
import de.xleitstelle.xbau.schema._2._1.BaulicheNutzungMass;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben;
import de.xleitstelle.xbau.schema._2._1.CodeBaulicheAnlagenGebaeude;
import de.xleitstelle.xbau.schema._2._1.CodeBaumassnahmeArt;
import de.xleitstelle.xbau.schema._2._1.Datenblatt;
import de.xleitstelle.xbau.schema._2._1.Grundstuecksflaechen;
import de.xleitstelle.xbau.schema._2._1.Kennzahlen;
import de.xleitstelle.xbau.schema._2._1.Nutzungseinheiten;
import de.xleitstelle.xbau.schema._2._1.StaedtebaulicheKennzahlen;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben.Gegenstand;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben.Gegenstand.ArtDerBaulichenAnlage;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import net.opengis.gml._3.AbstractFeatureType;

public class DatenextraktionView extends JFrame {

	private ArrayList<ExtraktionsVorgang> measures;

	private TreeTableView treeTableView = null;
	private DatenextraktionView self;

	public DatenextraktionView() throws IOException {	
		
		setTitle("Datenextraktion");
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

		initialize();
	}
	
	private void initMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu menu = new JMenu("Options");
		menubar.add(menu);
		
		JMenuItem item2 = new JMenuItem("Refresh");
		item2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				refresh();
				
			}
		});

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
		
		final ComboBox comboBox2 = new ComboBox(options2);
		comboBox2.setMaxWidth(200);
		comboBox2.setMinWidth(200);
		final Label label2 = new Label("BPlan: ");
		
		Button buttonUpdate = new Button("Update");
		buttonUpdate.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			
			@Override
			public void handle(javafx.event.ActionEvent event) {
				ApplicationModelNode ifcModel = (ApplicationModelNode)comboBox.getSelectionModel().getSelectedItem();
				XPlanungModel xPlanModel = (XPlanungModel)comboBox2.getSelectionModel().getSelectedItem();
				
				if(ifcModel != null) {					
					System.out.println("Start Calculationg Measure Data using");
					System.out.println("Using IFC: " + ifcModel.getModelName());
					// System.out.println("Using XPlanung: " + xPlanModel.getModelName()); 
					
//					DatenextraktionView.measures = generatePruefungen(ifcModel, xPlanModel);
					
					for(ExtraktionsVorgang vorgang: self.measures ) {
						vorgang.perform(ifcModel, xPlanModel);
					}
					
					refresh();
					
				}else {
					System.out.println("Something went wrong! Check IFC and XPlanungs Model");
				}
			}
		});
		
		Button buttonZuXBau = new Button("-> XBau");
		buttonZuXBau.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			
			@Override
			public void handle(javafx.event.ActionEvent event) {
				
				//create default XBau if not already existing
				//=====================================================
				TreeItem<Object> root = XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot();
				if(!(root.getChildren().size() > 0)) {					
					AddElementAction.createBaugenehmigungAntrag0200(root);
				}
				//=====================================================
				
				DatenextraktionTransferMethods transferMethods = null;
				
				try {
					transferMethods = new DatenextraktionTransferMethods();
				} catch (Exception e) {
					System.err.println(e.getMessage());
					return;
				}
				
				for(ExtraktionsVorgang pObj : measures) {
				
					transferMethods.transfer(pObj);
				}
				
				System.out.println("Daten wurden zu xBau Formular uebertragen.");
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
				buttonUpdate,
				new Label("   "), //spacer 2 
				buttonZuXBau
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
				
				treeTableView.addEventFilter(ScrollEvent.ANY, event -> refresh());
				
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
		                	
		                	if(item instanceof ExtraktionsVorgang) {
			                	setText(((ExtraktionsVorgang)item).getName());
			                }else {
		                	         
			                	setText(item.toString());
			                	
		                	}
		                	
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
				columnID.setPrefWidth(300);
//				columnID.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(columnID);


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
		                	if(item instanceof ExtraktionsVorgang) {
			                	setText(((ExtraktionsVorgang)item).getValue());
		                	}
		                	else if(item instanceof Pruefvorgang) {
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
		                	if(item instanceof ExtraktionsVorgang) {
			                	setText(((ExtraktionsVorgang)item).getComment());
			                	
			                	final Tooltip tooltip = new Tooltip();
			                	tooltip.setPrefWidth(300);
			                	tooltip.setWrapText(true);
			    				tooltip.setText(((ExtraktionsVorgang)item).getComment());
			                	setTooltip(tooltip);
		                	}
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
		                	if(item instanceof ExtraktionsVorgang) {
				                setText(""); //TODO
		                	}
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
		                row.contextMenuProperty().bind(new DatenextraktionContextMenuObservable(row));
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

	private void refresh() {
		this.treeTableView.refresh();
	}
	
	private void initialize() {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	TreeItem root = createTree();
		    	treeTableView.setRoot(root);
		    }
		});
		refresh();
	}
	
	@SuppressWarnings("unchecked")
	private TreeItem createTree() {
		
		BezeichnungDesBauvorhabens bezeichnungDesBauvorhabens = new BezeichnungDesBauvorhabens();
		ArtDerMassnahme artDerMassnahme = new ArtDerMassnahme();	
		ArtDesGebaeudes artDesGebaeudes = new ArtDesGebaeudes();
		Gebaeudeklasse gebaeudeklasse = new Gebaeudeklasse();
		Sonderbau sonderbau = new Sonderbau();
		Bauweise bauweise = new Bauweise();
		PO_HoeheDerBaulichenAnlage hoeheDerBaulichenAnlage = new PO_HoeheDerBaulichenAnlage(); //Is implemented
		PO_AnzahlVollgeschosse anzahlVollgeschosse = new PO_AnzahlVollgeschosse(); //Is implemented	
		PO_Geschossflaeche geschossflaeche = new PO_Geschossflaeche(anzahlVollgeschosse);
		PO_Baumasse baumasse = new PO_Baumasse(anzahlVollgeschosse);
		PO_Verkaufsflaeche verkaufsflaeche = new PO_Verkaufsflaeche();
		PO_BebauteGrundstuecksflaeche bebauteGrundstuecksflaeche = new PO_BebauteGrundstuecksflaeche();
		PO_NichtBebauteGrundstuecksflaeche nichtBebauteGrundstuecksflaeche = new PO_NichtBebauteGrundstuecksflaeche(bebauteGrundstuecksflaeche);
		PO_VersiegelteFlaeche versiegelteFlaeche = new PO_VersiegelteFlaeche();
		PO_SpielUndFreizeitflaeche spielUndFreizeitflaeche = new PO_SpielUndFreizeitflaeche();
		PO_FlaecheDerNebenanlagen flaecheDerNebenanlagen = new PO_FlaecheDerNebenanlagen();
		PO_FlaecheDerGemeinschaftsanlagen flaecheDerGemeinschaftsanlagen = new PO_FlaecheDerGemeinschaftsanlagen();
		PO_StellplaetzeUndDerenZufahrten stellplaetzeUndDerenZufahrten = new PO_StellplaetzeUndDerenZufahrten();
		PO_GrundflaechenDerBaulichenAnlage grundflaechenDerBaulichenAnlage = new PO_GrundflaechenDerBaulichenAnlage(
				bebauteGrundstuecksflaeche, 
				flaecheDerNebenanlagen, 
				stellplaetzeUndDerenZufahrten
				);
		PO_Geschossflaechenzahl geschossflaechenzahl = new PO_Geschossflaechenzahl(geschossflaeche);
		PO_Grundflaechenzahl grundflaechenzahl = new PO_Grundflaechenzahl(bebauteGrundstuecksflaeche);
		PO_Baumassenzahl baumassenzahl = new PO_Baumassenzahl(baumasse);
		
		PO_ErforderlicheStellplaetze erforderlicheStellplaetze = new PO_ErforderlicheStellplaetze();
		PO_GeplanteStellplaetze po_GeplanteStellplaetze = new PO_GeplanteStellplaetze(stellplaetzeUndDerenZufahrten);
		
		PO_WohneinheitenGesamt wohneinheitenGesamt = new PO_WohneinheitenGesamt();
		PO_WohneinheitenEigentumswohnungen wohneinheitenEigentumswohnungen = new PO_WohneinheitenEigentumswohnungen();
		PO_WohneinheitenMietwohnungen wohneinheitenMietwohnungen = new PO_WohneinheitenMietwohnungen();
		PO_WohneinheitenSozialwohnungen wohneinheitenSozialwohnungen = new PO_WohneinheitenSozialwohnungen();
		PO_WohnungenGewerblich wohnungenGewerblich = new PO_WohnungenGewerblich();
		PO_WohnungenFreiberuflich wohnungenFreiberuflich = new PO_WohnungenFreiberuflich();
		PO_NutzungseinheitenGewerbe nutzungseinheitenGewerbe = new PO_NutzungseinheitenGewerbe();
		
		Bruttogrundflaechen bruttogrundflaechen = new Bruttogrundflaechen();
		Nettogrundflachen nettogrundflachen = new Nettogrundflachen();
		
		
		if(measures == null) {
			measures = new ArrayList<>();
			
			measures.add(bezeichnungDesBauvorhabens);
			measures.add(artDerMassnahme);
			measures.add(artDesGebaeudes);
			measures.add(gebaeudeklasse);
			measures.add(sonderbau);
			measures.add(bauweise);
			measures.add(hoeheDerBaulichenAnlage);
			measures.add(anzahlVollgeschosse);
			measures.add(geschossflaeche);
			measures.add(baumasse);
			measures.add(verkaufsflaeche);
			measures.add(bebauteGrundstuecksflaeche);
			measures.add(nichtBebauteGrundstuecksflaeche);
			measures.add(versiegelteFlaeche);
			measures.add(spielUndFreizeitflaeche);
			measures.add(flaecheDerNebenanlagen);
			measures.add(flaecheDerGemeinschaftsanlagen);
			measures.add(stellplaetzeUndDerenZufahrten);
			measures.add(grundflaechenDerBaulichenAnlage);
			measures.add(geschossflaechenzahl);
			measures.add(grundflaechenzahl);
			measures.add(baumassenzahl);
			measures.add(po_GeplanteStellplaetze);
			measures.add(wohneinheitenGesamt);
			measures.add(wohneinheitenEigentumswohnungen);
			measures.add(wohneinheitenMietwohnungen);
			measures.add(wohneinheitenSozialwohnungen);
			measures.add(wohnungenGewerblich);
			measures.add(wohnungenFreiberuflich);
			measures.add(nutzungseinheitenGewerbe);
			measures.add(bruttogrundflaechen);
			measures.add(nettogrundflachen);
		}
		
		
		
		TreeItem<Object> top = new TreeItem<Object>("Nutzungsmaße");
		top.setExpanded(true);
		
		TreeItem<Object> allgemein = new TreeItem<Object>("Allgemeine Projektparameter");
		allgemein.setExpanded(true);
		top.getChildren().add(allgemein);
		
		
		allgemein.getChildren().addAll(
				new TreeItem<Object>(bezeichnungDesBauvorhabens),
				new TreeItem<Object>(artDerMassnahme), 
				new TreeItem<Object>(artDesGebaeudes), 
				new TreeItem<Object>(gebaeudeklasse), 
				new TreeItem<Object>(sonderbau),
				new TreeItem<Object>(bauweise)
		);
		
		
		TreeItem<Object> nutzungsmaße = new TreeItem<Object>("Nutzungsmaße");
		nutzungsmaße.setExpanded(true);
		top.getChildren().add(nutzungsmaße);
		
		nutzungsmaße.getChildren().addAll( 
				new TreeItem<Object>(hoeheDerBaulichenAnlage),
				new TreeItem<Object>(anzahlVollgeschosse),
				new TreeItem<Object>(grundflaechenDerBaulichenAnlage),
				new TreeItem<Object>(geschossflaeche),
				new TreeItem<Object>(baumasse),
				new TreeItem<Object>(verkaufsflaeche)
		);
		
		TreeItem<Object> grundstuecksflaechen = new TreeItem<Object>("Grundstücksflächen");
		grundstuecksflaechen.setExpanded(true);
		top.getChildren().add(grundstuecksflaechen);

		grundstuecksflaechen.getChildren().addAll( 
				new TreeItem<Object>(bebauteGrundstuecksflaeche),
				new TreeItem<Object>(nichtBebauteGrundstuecksflaeche),
				new TreeItem<Object>(versiegelteFlaeche),
				new TreeItem<Object>(spielUndFreizeitflaeche),
				new TreeItem<Object>(flaecheDerNebenanlagen),
				new TreeItem<Object>(flaecheDerGemeinschaftsanlagen),
				new TreeItem<Object>(stellplaetzeUndDerenZufahrten)
		);
		
		TreeItem<Object> staedtebaulicheKennzahlen = new TreeItem<Object>("Städtebauliche Kennzahlen");
		staedtebaulicheKennzahlen.setExpanded(true);
		top.getChildren().add(staedtebaulicheKennzahlen);
		
		staedtebaulicheKennzahlen.getChildren().addAll( 
				new TreeItem<Object>(geschossflaechenzahl),
				new TreeItem<Object>(grundflaechenzahl),
				new TreeItem<Object>(baumassenzahl)
		);
		
		TreeItem<Object> kennzahlen = new TreeItem<Object>("Kennzahlen");
		kennzahlen.setExpanded(true);
		top.getChildren().add(kennzahlen);
		
		kennzahlen.getChildren().addAll(
				new TreeItem<Object>(erforderlicheStellplaetze),
				new TreeItem<Object>(po_GeplanteStellplaetze)
		);
		
		TreeItem<Object> nutzungseinheiten = new TreeItem<Object>("Nutzungseinheiten");
		nutzungseinheiten.setExpanded(true);
		top.getChildren().add(nutzungseinheiten);
		
		nutzungseinheiten.getChildren().addAll( 
				new TreeItem<Object>(wohneinheitenGesamt),
				new TreeItem<Object>(wohneinheitenEigentumswohnungen),
				new TreeItem<Object>(wohneinheitenMietwohnungen),
				new TreeItem<Object>(wohneinheitenSozialwohnungen),
				new TreeItem<Object>(wohnungenGewerblich),
				new TreeItem<Object>(wohnungenFreiberuflich),
				new TreeItem<Object>(nutzungseinheitenGewerbe)
		);
		
		TreeItem<Object> din277Flaechen = new TreeItem<Object>("Flächen nach DIN 277");
		din277Flaechen.setExpanded(true);
		top.getChildren().add(din277Flaechen);
		
		din277Flaechen.getChildren().addAll(
				new TreeItem<Object>(bruttogrundflaechen),
				new TreeItem<Object>(nettogrundflachen)
		);
	
		
		return top;
	}

}
