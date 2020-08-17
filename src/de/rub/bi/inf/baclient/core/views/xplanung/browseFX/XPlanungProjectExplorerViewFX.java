package de.rub.bi.inf.baclient.core.views.xplanung.browseFX;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.rub.bi.inf.baclient.core.actions.XPlanungActionUtils;
import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import de.rub.bi.inf.baclient.core.utils.UIUtilities;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xplanung.XPlanungRepresentationSettingsView;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.CurveType;
import net.opengis.gml._3.LineStringType;
import net.opengis.gml._3.PointType;
import net.opengis.gml._3.PolygonType;

public class XPlanungProjectExplorerViewFX extends JPanel{
	
	
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
				
				for(Object o : treeTableView.getSelectionModel().getSelectedCells()) {
					if(o instanceof TreeTablePosition<?, ?>) {
						TreeTablePosition tablePos = (TreeTablePosition)o;
						selecteditems.add(tablePos.getTreeItem().getValue());
					}
				}
				
				XPlanungProjectExplorerPopupMenuFX popupMenu = new XPlanungProjectExplorerPopupMenuFX("Project Edit", selecteditems);
				
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
	
	private JPanel toolChain = null;
	private JFXPanel content = null;

	private TreeTableView treeTableView = null;
	
	private EventHandler selectionEvent = new EventHandler<Event>() {

		@Override
		public void handle(Event event) {
			TreeTableView tableView = (TreeTableView)event.getSource();
			
			Object obj = tableView.getSelectionModel().getSelectedItem();
		
			if(obj instanceof TreeItem) {
				Object itemValue = ((TreeItem)obj).getValue();
				
				if(itemValue instanceof AbstractFeatureType) {
					XViewer.getInstance().getViewerPanel()
						.getXplanungPropertyTreeTableView().loadProperties((AbstractFeatureType)itemValue);
				}
			}
		
		}
	};
	
	
	private XPlanungProjectExplorerViewFX() {

		this.setLayout(new BorderLayout());
		
		this.toolChain = new JPanel();
		this.toolChain.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.toolChain.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		content = new JFXPanel();
		
		this.add(this.toolChain, BorderLayout.NORTH);
		this.add(content);
		
		
		try {

			this.initToolChain();
			this.initContentChain();

		} catch (IOException e) {
			e.printStackTrace();
		}
	
		content.addMouseListener(new PopUpMouseHandler());
		content.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	private static XPlanungProjectExplorerViewFX self = null;
	
	public static XPlanungProjectExplorerViewFX getInstance() {
		if(self == null) {
			self = new XPlanungProjectExplorerViewFX();
		}
		return self;
	}
	
	private void initToolChain() throws IOException {

		toolChain.add(UIUtilities.createButton("Representation Settings", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new XPlanungRepresentationSettingsView();
			}
		}, ResourceManager.getIcon("preferences.gif")));
		
	}

	
	private void initContentChain() {
		
		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
		        
		    	treeTableView = new TreeTableView<>();
		    	treeTableView.setEditable(true);
		    	
		    	
		    	//MAIN EXPLORER COLUMN
			    TreeTableColumn<Object, Object> column = new TreeTableColumn<>("Project Explorer");
			    column.setCellValueFactory(
			            (TreeTableColumn.CellDataFeatures<Object, Object> param) -> 
			            new ReadOnlyObjectWrapper(param.getValue().getValue())
			    );
			    
			    column.setCellFactory(ttc -> new TreeTableCell<Object, Object>() {

			    	final Image icon = new Rectangle(12, 12, javafx.scene.paint.Color.CORNFLOWERBLUE).snapshot(null, null);

		            //private final ImageView graphic = new ImageView(icon);

		            @Override
		            protected void updateItem(Object item, boolean empty) {
		                super.updateItem(item, empty);
		                
		                if(item != null && !empty) {
		                	ImageView graphic = new ImageView(icon);
		                	graphic.setPreserveRatio(true);

		                	if(item instanceof AbstractFeatureType) {
		                		AbstractGeometryType geometryType = GeometryUtil.hasGeometry((AbstractFeatureType)item);
								if(geometryType instanceof PolygonType) {
									Image image = new Image(ResourceManager.getResource("icon/face.png"), 16, 16, false, true);
									graphic = new ImageView(image);
								}else if(geometryType instanceof LineStringType) {
									Image image = new Image(ResourceManager.getResource("icon/line.png"), 16, 16, false, true);
									graphic = new ImageView(image);
								} else if(geometryType instanceof PointType) {
									Image image = new Image(ResourceManager.getResource("icon/point.png"), 16, 16, false, true);
									graphic = new ImageView(image);
								} else if(geometryType instanceof CurveType){
									Image image = new Image(ResourceManager.getResource("icon/curve.png"), 16, 16, false, true);
									graphic = new ImageView(image);
								}
		                	}
		                	
		                	setText(XPlanungActionUtils.getRepresentionString(item));
			                setGraphic(graphic);
		                }else {
		                    setText(null);
			                setGraphic(null);
		                }
		            }
		        });
			    
			    column.setPrefWidth(400);
			    
			    treeTableView.getColumns().add(column);
			    
			    treeTableView.setOnMouseClicked(selectionEvent);


		    	//VISIBILITY COLUMN
			    TreeTableColumn<Object, Boolean> visibleColumn = new TreeTableColumn<Object, Boolean>(
						"Visibility");
			    visibleColumn.setPrefWidth(75);

			    visibleColumn.setCellValueFactory(
			            (TreeTableColumn.CellDataFeatures<Object, Boolean> param) -> 
			            new ReadOnlyBooleanWrapper(XPlanungActionUtils.isVisible(param.getValue().getValue()))
			    );
				
			    visibleColumn.setCellFactory(new Callback<TreeTableColumn<Object,Boolean>,TreeTableCell<Object,Boolean>>() {
		            @Override
		            public TreeTableCell<Object,Boolean> call( TreeTableColumn<Object,Boolean> p ) {
	
		            	VisibilityCheckboxCellFactory cell = new VisibilityCheckboxCellFactory();
		            	cell.setAlignment(Pos.CENTER);
		                return cell;
		                
		            }
		            
		        });
				
			    treeTableView.getColumns().add(visibleColumn);
		    	

		    	//TRANSPARENCY COLUMN
			    TreeTableColumn<Object, Boolean> transparencyColumn = new TreeTableColumn<Object, Boolean>(
						"Transparency");
			    transparencyColumn.setPrefWidth(75);

			    transparencyColumn.setCellValueFactory(
			            (TreeTableColumn.CellDataFeatures<Object, Boolean> param) -> 
			            new ReadOnlyBooleanWrapper(
			            		XPlanungActionUtils.isTransparent(param.getValue().getValue()) && 
			            		XPlanungActionUtils.isVisible(param.getValue().getValue()))
			    );
				
			    transparencyColumn.setCellFactory(new Callback<TreeTableColumn<Object,Boolean>,TreeTableCell<Object,Boolean>>() {
		            @Override
		            public TreeTableCell<Object,Boolean> call( TreeTableColumn<Object,Boolean> p ) {
	
		            	TransparencyCheckboxCellFactory cell = new TransparencyCheckboxCellFactory();
		            	cell.setAlignment(Pos.CENTER);
		                return cell;
		                
		            }
		            
		        });
				
			    treeTableView.getColumns().add(transparencyColumn);
		    	
			    
			    //Wrap to jfxpanel
			    VBox vBox = new VBox();
		        vBox.getChildren().setAll(treeTableView);
		        VBox.setVgrow(treeTableView, Priority.ALWAYS);
		 
		        javafx.scene.Scene scene = new javafx.scene.Scene(vBox);
		
		        content.setScene(scene);
		        
		        treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		    	
		    }
		});
	}
	
	public void refresh() {
		this.treeTableView.refresh();
		this.updateUI();
	}
	
	public void rebuild() {
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	TreeItem root = ProjectTreeBuilderFX.createTree();
		    	treeTableView.setRoot(root);
		    }
		});
		refresh();
	}
	
}
