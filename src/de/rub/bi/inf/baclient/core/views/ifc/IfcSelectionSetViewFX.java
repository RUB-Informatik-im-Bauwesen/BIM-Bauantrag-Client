package de.rub.bi.inf.baclient.core.views.ifc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import de.rub.bi.inf.baclient.core.utils.UIUtilities;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.coloring.ColorRule;
import de.rub.bi.inf.baclient.core.views.coloring.ColorSchema;
import de.rub.bi.inf.baclient.core.views.coloring.ColorSchemaManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import net.opengis.gml._3.AbstractFeatureType;

public class IfcSelectionSetViewFX extends JPanel {

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

				ArrayList<Object> selecteditems = new ArrayList<>();

				for (Object o : treeTableView.getSelectionModel().getSelectedCells()) {
					if (o instanceof TreeTablePosition<?, ?>) {
						TreeTablePosition tablePos = (TreeTablePosition) o;
						selecteditems.add(tablePos.getTreeItem().getValue());
					}
				}

				IfcSelectionSetPopupMenuFX popupMenu = null;
				if (selecteditems.size() == 1) {
					popupMenu = new IfcSelectionSetPopupMenuFX("Selection Set PopUp", selecteditems.get(0));
				} else {
					popupMenu = new IfcSelectionSetPopupMenuFX("Selection Set PopUp", selecteditems);
				}

				try {
					popupMenu.inititializeContent();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				popupMenu.show(e.getComponent(), e.getX(), e.getY());

			}
		}
	};

	/**
	 * 
	 */
	private static final long serialVersionUID = -8065414764986390375L;

	private JPanel toolChainTop = null;
	private JPanel toolChainBottom = null;
	private JFXPanel content = null;
	private JCheckBox enableRemoveMultiple = null;

	private TreeTableView treeTableView = null;
	private ArrayList<SelectionSet> selectionSets;

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

	private static IfcSelectionSetViewFX self = null;

	public static IfcSelectionSetViewFX getInstance() {
		if (self == null) {
			self = new IfcSelectionSetViewFX();
		}
		return self;
	}

	private IfcSelectionSetViewFX() {

		this.setLayout(new BorderLayout());

		this.toolChainTop = new JPanel();
		this.toolChainTop.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.toolChainTop.setLayout(new FlowLayout(FlowLayout.RIGHT));
		

		this.toolChainBottom = new JPanel();
		this.toolChainBottom.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.toolChainBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));

		content = new JFXPanel();

		this.add(this.toolChainTop, BorderLayout.NORTH);
		this.add(this.toolChainBottom, BorderLayout.SOUTH);
		this.add(content);

		try {

			this.initToolChain();
			this.initContentChain();

		} catch (IOException e) {
			e.printStackTrace();
		}

		selectionSets = new ArrayList<SelectionSet>();

		content.addMouseListener(new PopUpMouseHandler());
		content.setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	private void initToolChain() throws IOException {

		java.awt.Image image = ResourceManager.getIcon("del.gif").getImage(); // transform it
		java.awt.Image newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way


		JButton removeButton = UIUtilities.createButton("Remove Selection Set", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javafx.application.Platform.runLater(new Runnable() {

					private SelectionSet retrieveSelectionSet(TreeItem<Object> item) {
						TreeItem<Object> parent = item.getParent();
						if(parent.getValue() instanceof SelectionSet) {
							return (SelectionSet)parent.getValue();
						}else {
							return retrieveSelectionSet(parent);
						}
					}
					
					private boolean removeFromTable(TreeItem<Object> item) {
						
						if(item.getValue() instanceof ArrayList) {
							retrieveSelectionSet(item).getSelection().removeAll((ArrayList<?>)item.getValue());
						}
						
						if(item.getValue() instanceof ClassInterface) {
							retrieveSelectionSet(item).getSelection().remove((ClassInterface)item.getValue());
						}
						
						boolean result = item.getParent().getChildren().remove(item);
						return result;
					}

					@Override
					public void run() {

						ArrayList<Object> obsList = new ArrayList<Object>(treeTableView.getSelectionModel().getSelectedItems());
						
						for (Object item : obsList) {
							if (item instanceof TreeItem) {
								TreeItem<Object> treeItem = (TreeItem<Object>) item;
								removeFromTable(treeItem);
							}
						}
						
						treeTableView.getSelectionModel().clearSelection();
					}
				});
			}
		}, new ImageIcon(newimg));
		
		enableRemoveMultiple = new JCheckBox("Mehrfachauswahl erlauben");
		enableRemoveMultiple.setSelected(false);
		enableRemoveMultiple.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				removeButton.setEnabled(!enableRemoveMultiple.isSelected());
				if(!enableRemoveMultiple.isSelected()) {					
					treeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				}else {
					treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				}
				treeTableView.getSelectionModel().clearSelection();
				
			}
		});
		removeButton.setEnabled(!enableRemoveMultiple.isSelected());
		
		toolChainTop.add(enableRemoveMultiple);
		toolChainBottom.add(removeButton);

		
		image = ResourceManager.getIcon("add.gif").getImage(); // transform it
		newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

		toolChainBottom.add(UIUtilities.createButton("Add Selection Set", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						TextInputDialog dialog = new TextInputDialog("SelectionSetExample");
						dialog.setTitle("Erstelle Selektion Set");
						dialog.setHeaderText("Bitte Titel des Selektion Sets eingeben");
						dialog.setContentText("Titel Eingeben:");
						dialog.setWidth(250);

						// Traditional way to get the response value.
						Optional<String> result = dialog.showAndWait();
						if (result.isPresent()) {
							SelectionSet selectionSet = new SelectionSet(result.get(), new ArrayList<ClassInterface>());

							for (ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {

								for (Object selectedObject : node.getSelectionModel().getSelectedObjects()) {
									if (selectedObject instanceof ClassInterface) {
										selectionSet.getSelection().add((ClassInterface) selectedObject);
									}
								}

							}

							self.addSelectionSet(selectionSet);
						}
					}

				});

			}
		}, new ImageIcon(newimg)));
		
		
		image = ResourceManager.getIcon("color.png").getImage(); // transform it
		newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

		toolChainTop.add(UIUtilities.createButton("Edit Color Schema", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						
						ColorSchemaManager.getInstance().setVisible(true);
						
					}

				});

			}
		}, new ImageIcon(newimg)));
		
		
		image = ResourceManager.getIcon("uncolor.png").getImage(); // transform it
		newimg = image.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

		toolChainTop.add(UIUtilities.createButton("Reset Color Schema", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						
						for(SelectionSet selectionSet : selectionSets) {
							selectionSet.resetColorSchema();
						}
					}

				});

			}
		}, new ImageIcon(newimg)));

	}

	private void initContentChain() {

		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {

				treeTableView = new TreeTableView<>();
				treeTableView.setEditable(true);
				treeTableView.setRoot(new TreeItem<Object>("ROOT"));
				treeTableView.setShowRoot(false);

				if(!enableRemoveMultiple.isSelected()) {					
					treeTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				}else {
					treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				}
				
				// MAIN EXPLORER COLUMN
				TreeTableColumn<Object, Object> column = new TreeTableColumn<>("Grouped Items");
				column.setCellValueFactory(
						(TreeTableColumn.CellDataFeatures<Object, Object> param) -> new ReadOnlyObjectWrapper(
								param.getValue().getValue()));

				column.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

					final Image icon = new Rectangle(12, 12, javafx.scene.paint.Color.CORNFLOWERBLUE).snapshot(null,
							null);

					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {
							ImageView graphic = new ImageView(icon);
							graphic.setPreserveRatio(true);

							if (item instanceof SelectionSet) {
								Image image = new Image(ResourceManager.getResource("icon/folder.png"), 16, 16, false,
										true);
								graphic = new ImageView(image);

								setText(((SelectionSet) item).getName());
							}

							if (item instanceof List<?>) {
								// Image image = new Image(ResourceManager.getResource("icon/folder.png"), 16,
								// 16, false, true);
								// graphic = new ImageView(image);

								String typeName = ((List<?>) item).get(0).getClass().getSimpleName();
								if (((List<?>) item).get(0) instanceof ClassInterface) {
									typeName = ((ClassInterface) ((List<?>) item).get(0)).getClassName();
								}
								setText(typeName);
							}

							if (item instanceof ClassInterface) {
								// Image image = new Image(ResourceManager.getResource("icon/folder.png"), 16,
								// 16, false, true);
								// graphic = new ImageView(image);
								setText(((ClassInterface) item).getClassName() + "_(#"
										+ ((ClassInterface) item).getStepLineNumber() + ")");
							}

							setGraphic(graphic);
						} else {
							setText(null);
							setGraphic(null);
						}
					}
				});

				column.setPrefWidth(250);

				treeTableView.getColumns().add(column);

				treeTableView.setOnMouseClicked(selectionEvent);

				// COLOR COLUMN
				TreeTableColumn<Object, Object> colorColumn = new TreeTableColumn<Object, Object>("Color");
				colorColumn.setPrefWidth(125);

				colorColumn.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<Object, Object>, ObservableValue<Object>>() {

							@Override
							public ObservableValue<Object> call(CellDataFeatures<Object, Object> param) {

								return new ReadOnlyObjectWrapper<Object>(param.getValue().getValue());

							}
						});

				colorColumn
						.setCellFactory(new Callback<TreeTableColumn<Object, Object>, TreeTableCell<Object, Object>>() {

							@Override
							public TreeTableCell<Object, Object> call(TreeTableColumn<Object, Object> p) {

								TreeTableCell<Object, Object> cell = new TreeTableCell<Object, Object>() {
									protected void updateItem(Object item, boolean empty) {

										setGraphic(null);

//										if (item instanceof ClassInterface) {
//											Pane pane = new Pane();
//											pane.setStyle("-fx-background-color: " + String.format("#%02x%02x%02x",
//													Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue())
//													+ ";");
//											pane.setPrefSize(25, 10);
//
//											setGraphic(pane);
//										}
										
										if (item instanceof SelectionSet) {
											
											ComboBox<ColorSchema> comboSchemas = ((SelectionSet)item).getColorSchemaCombobox();
											setGraphic(comboSchemas);

										}

									};

								};
								return cell;

							}
						});

				treeTableView.getColumns().add(colorColumn);

				// Wrap to jfxpanel
				VBox vBox = new VBox();
				vBox.getChildren().setAll(treeTableView);
				VBox.setVgrow(treeTableView, Priority.ALWAYS);

				javafx.scene.Scene scene = new javafx.scene.Scene(vBox);

				content.setScene(scene);

			}
		});
	}

	public void refresh() {
		this.treeTableView.refresh();
		this.updateUI();
	}

	public void addSelectionSet(SelectionSet selection) {
		this.selectionSets.add(selection);

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				TreeItem<Object> root = treeTableView.getRoot();

				TreeItem<Object> selectionSetItem = new TreeItem<Object>(selection);
				root.getChildren().add(selectionSetItem);

				HashMap<String, ArrayList<ClassInterface>> sortedItems = new HashMap<String, ArrayList<ClassInterface>>();
				for (ClassInterface item : selection.getSelection()) {

					ArrayList<ClassInterface> selectList = sortedItems.get(item.getClassName());
					if (selectList == null) {
						selectList = new ArrayList<ClassInterface>();
						sortedItems.put(item.getClassName(), selectList);
					}
					selectList.add(item);
				}

				for (String listKey : sortedItems.keySet()) {
					ArrayList<ClassInterface> selectList = sortedItems.get(listKey);
					selectList.sort(new Comparator<ClassInterface>() {

						@Override
						public int compare(ClassInterface o1, ClassInterface o2) {

							return (o1.getClassName() + "_(#" + o1.getStepLineNumber() + ")")
									.compareTo(o2.getClassName() + "_(#" + o2.getStepLineNumber() + ")");
						}
					});

					TreeItem<Object> listTreeItem = new TreeItem<Object>(selectList);
					selectionSetItem.getChildren().add(listTreeItem);

					for (ClassInterface ifcItem : selectList) {
						TreeItem<Object> ifcTreeItem = new TreeItem<Object>(ifcItem);
						listTreeItem.getChildren().add(ifcTreeItem);
					}
				}

			}
		});
	}

}
