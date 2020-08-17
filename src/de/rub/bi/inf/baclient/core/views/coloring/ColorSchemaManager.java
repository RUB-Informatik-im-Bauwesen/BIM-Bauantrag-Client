package de.rub.bi.inf.baclient.core.views.coloring;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ColorSchemaManager extends JFrame {

	/**
	 * Open the Popup menu if right mouse button has been clicked.
	 * 
	 * @author Marcel Stepien
	 *
	 */
	public class PopUpMouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {

				ArrayList<TreeItem> selecteditems = new ArrayList<>();

				for (Object o : treeTableView.getSelectionModel().getSelectedCells()) {
					if (o instanceof TreeTablePosition<?, ?>) {
						TreeTablePosition tablePos = (TreeTablePosition) o;
						selecteditems.add(tablePos.getTreeItem());
					}
				}

				ColorSchemaPopupMenuFX popupMenu = null;
				if (selecteditems.size() == 1) {
					popupMenu = new ColorSchemaPopupMenuFX(self, "Selection Set PopUp", selecteditems.get(0));

					try {
						popupMenu.inititializeContent();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	};

	private TreeTableView treeTableView = null;
	private static ColorSchemaManager self;

	private ArrayList<ComboBox<ColorSchema>> comboboxes = null;
	private ArrayList<ColorSchema> schemas = null;

	public static ColorSchemaManager getInstance() {
		if (self == null) {
			self = new ColorSchemaManager();
		}
		return self;
	}

	private ColorSchemaManager() {

		setTitle("Farbschema-Manager");
		setSize(1100, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		comboboxes = new ArrayList<ComboBox<ColorSchema>>();
		schemas = new ArrayList<ColorSchema>();

		JPanel centerContent = new JPanel();
		centerContent.setLayout(new BorderLayout());

		this.initMenuBar();
		this.initContentPanel(centerContent);

		JScrollPane scrollPane = new JScrollPane(centerContent);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.self = this;
	}

	private void initMenuBar() {
		JMenuBar menubar = new JMenuBar();

		JMenu menu = new JMenu("File");
		menubar.add(menu);

		JMenuItem item = new JMenuItem("Import");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {


				CountDownLatch doneLatch = new CountDownLatch(1);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Farbschema Importieren");
						fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Farschema-XML", "*.xml"));

						File selectedFile = fileChooser.showOpenDialog(null);

						if (selectedFile != null) {
							try {
								ColorSchema schema = unmarshallSchema(selectedFile);
								
								schemas.add(schema);
								
								TreeItem<Object> treeItem = new TreeItem<Object>(schema);
								treeTableView.getRoot().getChildren().add(treeItem);
								
								for(ColorRule rule : schema.getRules()) {

									TreeItem<Object> ruleTreeItem = new TreeItem<Object>(rule);
									treeItem.getChildren().add(ruleTreeItem);
									
								}

								notifyComboboxes();
								
								System.out.println("Farbschema Importiert: " + selectedFile.getPath());
							} catch (Exception exp) {
								exp.printStackTrace();
							}	
						}
						
						doneLatch.countDown();
					}
				});

				try {
					doneLatch.await();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}


			}
		});

		JMenuItem item2 = new JMenuItem("Export");
		item2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				CountDownLatch doneLatch = new CountDownLatch(1);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						
						if(schemas.size() == 0) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setContentText("Es sind keine Farbschema zum Export vorhanden!");
							alert.showAndWait();
						}else {
							ChoiceDialog<ColorSchema> choiceDialog = new ChoiceDialog<ColorSchema>(schemas.get(0), schemas);
							choiceDialog.setHeaderText("Farbschema Auswahl");
							choiceDialog.setContentText("Waehle eine Farbschema fuer den Export aus.");
							Optional<ColorSchema> option = choiceDialog.showAndWait();
							
							if(option.get() != null) {
								FileChooser fileChooser = new FileChooser();
								fileChooser.setTitle("Farbschema Exportieren");
								fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Farschema-XML", "*.xml"));

								File selectedFile = fileChooser.showSaveDialog(null);

								if (selectedFile != null) {
									try {
										marshallSchema(selectedFile, option.get());
										System.out.println("Farbschema Exportiert nach: " + selectedFile.getPath());
									} catch (Exception exp) {
										exp.printStackTrace();
									}	
								}

							}
							
						}
						
						doneLatch.countDown();
					}
				});

				try {
					doneLatch.await();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		});

		menu.add(item);
		menu.add(item2);

		this.setJMenuBar(menubar);
	}

	private void initContentPanel(JPanel contentPanel) {

		JFXPanel centerPanel = new JFXPanel();
		centerPanel.setBorder(new EmptyBorder(1, 0, 1, 0));
		contentPanel.add(centerPanel, BorderLayout.CENTER);

		centerPanel.addMouseListener(new PopUpMouseHandler());

		initContentChain(centerPanel);

		JFXPanel bottomPanel = new JFXPanel();
		// topPanel.setMinimumSize(new Dimension(0, 150));
		bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		contentPanel.add(bottomPanel, BorderLayout.SOUTH);

		Button buttonNew = new Button("Neues Schema");
		buttonNew.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent event) {

				TextInputDialog dialog = new TextInputDialog("Farbschema erstellen");
				dialog.setTitle("Erstelle neues Farbschema");
				dialog.setHeaderText("Bitte Titel des Farbschemas eingeben");
				dialog.setContentText("Titel Eingeben:");
				dialog.setWidth(250);

				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					ColorSchema schema = new ColorSchema(result.get());

					schemas.add(schema);

					TreeItem<Object> treeItem = new TreeItem<Object>(schema);
					treeTableView.getRoot().getChildren().add(treeItem);

					notifyComboboxes();
				}

			}
		});

		Button buttonEdit = new Button("Anwenden");
		buttonEdit.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent event) {
				dispose();
			}
		});

		final HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setMinHeight(45);
		hbox.getChildren().addAll(buttonNew, new Label(" | "), // spacer 1
				buttonEdit, new Label("   ") // spacer 2
		);

		final Scene scene = new Scene(hbox);
		bottomPanel.setScene(scene);
	}

	private void initContentChain(JFXPanel content) {

		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {

				treeTableView = new TreeTableView<>();
				treeTableView.setEditable(false);
				treeTableView.setShowRoot(false);
				treeTableView.setRoot(new TreeItem<Object>("ROOT"));

				// COLUMN: Schema und Farbe
				// ====================================================================
				TreeTableColumn<Object, Object> column = new TreeTableColumn<>("Farbschema und Filter");

				column.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				column.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {

							if (item instanceof ColorSchema) {
								setText(((ColorSchema) item).getTitel());
							}
							if (item instanceof ColorRule) {
								setText(((ColorRule) item).getTitel());
							}

						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				column.setPrefWidth(400);
				// column.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(column);

				// COLUMN: Wert
				// ====================================================================
				TreeTableColumn<Object, Object> columnPreview = new TreeTableColumn<Object, Object>("Vorschau");
				columnPreview.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				columnPreview.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {

							if (item instanceof ColorRule) {
								Pane pane = new Pane();
								pane.setStyle("-fx-background-color: " + String.format("#%02x%02x%02x",
										(int) (((ColorRule) item).getColor().getRed() * 255),
										(int) (((ColorRule) item).getColor().getGreen() * 255),
										(int) (((ColorRule) item).getColor().getBlue()) * 255) + ";");
								pane.setPrefSize(25, 10);

								setGraphic(pane);
							}

						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				columnPreview.setPrefWidth(175);
				columnPreview.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(columnPreview);

				// COLUMN: PropetySet
				// ====================================================================
				TreeTableColumn<Object, Object> propertySetColumn = new TreeTableColumn<Object, Object>("PropertySet");

				propertySetColumn.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				propertySetColumn.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {

							if (item instanceof ColorRule) {
								if (((ColorRule) item).getPropertySetName() != null) {
									setText(((ColorRule) item).getPropertySetName());
								}

							}

						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				propertySetColumn.setPrefWidth(120);
				// columnComment.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(propertySetColumn);

				// COLUMN: Property
				// ====================================================================
				TreeTableColumn<Object, Object> propertyColumn = new TreeTableColumn<Object, Object>("Property");

				propertyColumn.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				propertyColumn.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {

							if (item instanceof ColorRule) {
								if (((ColorRule) item).getPropertyName() != null) {
									setText(((ColorRule) item).getPropertyName());
								}
							}

						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				propertyColumn.setPrefWidth(120);
				// columnComment.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(propertyColumn);

				// COLUMN: OPERATOR
				// ====================================================================
				TreeTableColumn<Object, Object> operatorColumn = new TreeTableColumn<Object, Object>("Operator");

				operatorColumn.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				operatorColumn.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {

							if (item instanceof ColorRule) {
								if (((ColorRule) item).getOperator() != null) {
									setText(((ColorRule) item).getOperator());
								}
							}

						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				operatorColumn.setPrefWidth(120);
				// columnComment.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(operatorColumn);
				
				// COLUMN: Value
				// ====================================================================
				TreeTableColumn<Object, Object> valueColumn = new TreeTableColumn<Object, Object>("Wert");

				valueColumn.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				valueColumn.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {
							if (item instanceof ColorRule) {
								if (((ColorRule) item).getValue() != null) {
									setText(((ColorRule) item).getValue().toString());
								}
							}
						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				valueColumn.setPrefWidth(120);
				// columnComment.setStyle("-fx-alignment: center;");

				treeTableView.getColumns().add(valueColumn);
				
				
				// Wrap to jfxpanel
				VBox vBox = new VBox();
				vBox.getChildren().setAll(treeTableView);
				VBox.setVgrow(treeTableView, Priority.ALWAYS);

				javafx.scene.Scene scene = new javafx.scene.Scene(vBox);

				content.setScene(scene);

				treeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			}
		});
	}

	public void registerComboBox(ComboBox<ColorSchema> combobox) {
		comboboxes.add(combobox);
		notifyComboboxes();
	}

	public void refresh() {
		treeTableView.refresh();
	}

	@Override
	public void dispose() {
		this.setVisible(false);
	}

	public void notifyComboboxes() {
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				for (ComboBox<ColorSchema> combo : comboboxes) {
					ColorSchema selectedItem = combo.getSelectionModel().getSelectedItem();

					combo.setItems(FXCollections.observableArrayList(schemas));

					if (schemas.contains(selectedItem)) {
						combo.getSelectionModel().select(selectedItem);
					}
				}
			}
			
		});
	}

	public void marshallSchema(File exportTo, ColorSchema schema) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ColorSchema.class);
		Marshaller jaxbMarshaller = context.createMarshaller();
		jaxbMarshaller.marshal(schema, exportTo);
	}

	public ColorSchema unmarshallSchema(File file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ColorSchema.class);
		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
		Object obj = jaxbUnmarshaller.unmarshal(file);

		ColorSchema schema = null;
		if (obj instanceof ColorSchema) {
			schema = (ColorSchema) obj;
		}

		return schema;
	}
	
	public boolean removeColorSchema(ColorSchema schema) {
		ArrayList<TreeItem<?>> items = new ArrayList<TreeItem<?>>();
		for(Object treeItem : treeTableView.getRoot().getChildren()) {
			if(treeItem instanceof TreeItem<?>) {
				if(((TreeItem<?>)treeItem).getValue() instanceof ColorSchema) {
					if(((ColorSchema)((TreeItem<?>)treeItem).getValue()).equals(schema)) {
						items.add((TreeItem<?>)treeItem);
					}
				}
			}
		}
		
		for(TreeItem<?> treeitem : items) {
			treeTableView.getRoot().getChildren().remove(treeitem);
		}
		boolean flag = schemas.remove(schema);
		
		ColorSchemaManager.getInstance().notifyComboboxes();
		
		return flag;
	}
}
