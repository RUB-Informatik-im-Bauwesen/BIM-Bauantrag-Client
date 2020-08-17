package de.rub.bi.inf.baclient.core.views.xbau;

import java.awt.BorderLayout;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;
import javax.xml.bind.annotation.XmlRootElement;

import de.rub.bi.inf.baclient.core.views.xbau.param.Element;
import de.rub.bi.inf.baclient.core.views.xbau.param.ListStringItemElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.Parameter;
import de.rub.bi.inf.baclient.core.views.xbau.rendering.Renderer;
import de.rub.bi.inf.baclient.core.views.xbau.rendering.Renderers;
import de.xleitstelle.xbau.schema._2._1.Text;
import de.xoev.schemata.code._1_0.Code;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;



public class XbauExplorer extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Scene scene;
	private TreeTableView<Object> treeTableView;
	private JFXPanel jfxPanel;
	private VBox vBox;

	public XbauExplorer() {
		
		//Needed for perserving the fx content to be exited whil docking, undocking etc.
		Platform.setImplicitExit(false); 
		
		this.setLayout(new BorderLayout());
		
		jfxPanel = new JFXPanel();
		this.add(jfxPanel, BorderLayout.CENTER);
			
        TreeTableColumn<Object,String> columnName = new TreeTableColumn<Object, String>("Element");
        columnName.setPrefWidth(250);
        columnName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Object,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Object, String> param) {
				// TODO Auto-generated method stub
				
				XmlRootElement xmlRootElement = param.getValue().getValue().getClass().getDeclaredAnnotation(XmlRootElement.class);
				if(xmlRootElement != null) {
					return new SimpleStringProperty(xmlRootElement.name());
				}
				
				TreeItem<Object> treeItem = param.getValue();
				TreeItem<Object> parentItem = treeItem.getParent();
				if(parentItem!=null) {			
					if(parentItem.getValue() instanceof Element) {
						Element parentElement = (Element) parentItem.getValue();
						
						Class<?> cl = parentElement.getType();
						if(cl.isAssignableFrom(List.class)) {
							
							int index = parentItem.getChildren().indexOf(treeItem);
							return new SimpleStringProperty("("+index+")");
						}
					}
						
				}
				
				
	
				if(param.getValue().getValue() instanceof Parameter) {
					Parameter parameter = (Parameter) param.getValue().getValue();
					
					/*
					if(parameter.getName().equals("produktversion")) {
						System.out.println(parameter.getName());
						System.out.println(parameter.getValue());
						System.out.println(parameter.getInstance());
						System.out.println(parameter.getClass());
						System.out.println(parameter.getType());
						System.out.println(param.getValue());
					}*/
					
					return new SimpleStringProperty(parameter.getName());
				}
				
				return new SimpleStringProperty(param.getValue().getValue().toString());
			}
			
			
		});
        
        
        TreeTableColumn<Object,Object> columnValue = new TreeTableColumn<Object, Object>("Value");
        columnValue.setPrefWidth(150);
        columnValue.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Object,Object>, ObservableValue<Object>>() {
			
			@Override
			public ObservableValue<Object> call(CellDataFeatures<Object, Object> param) {
				
					
				if(param.getValue().getValue() instanceof Parameter) {
					Parameter attribute = (Parameter) param.getValue().getValue();
					
					if (attribute.getValue() != null) {
						Renderer ren = Renderers.getInstance().forClass(attribute.getValue().getClass());
						if(ren != null) {
							return new SimpleObjectProperty<>(ren.render(attribute.getValue()));
						}else if (attribute.getValue() instanceof String) {
							return new SimpleObjectProperty<>(attribute.getValue().toString());
						}else if (attribute.getValue() instanceof BigInteger) {
							return new SimpleObjectProperty<>(((BigInteger)attribute.getValue()).longValue());
						}else if (attribute.getValue() instanceof Short) {
							return new SimpleObjectProperty<>(((Short)attribute.getValue()).shortValue());
						}else if (attribute.getValue() instanceof Float) {
							return new SimpleObjectProperty<>(((Float)attribute.getValue()).floatValue());
						}else if (attribute.getValue() instanceof Boolean) {
							return new SimpleObjectProperty<>(((Boolean)attribute.getValue()).booleanValue());
						}else if (attribute.getValue() instanceof Text) {
							return new SimpleObjectProperty<>(((Text)attribute.getValue()).getTextabsatz().toString());
						}else if(attribute.getValue() instanceof Collection<?>) {
							Collection c = (Collection) attribute.getValue();
							return new SimpleObjectProperty<>("["+c.size()+"]");
						}else if(attribute.getValue() instanceof Code) {
							ren = Renderers.getInstance().forClass(Code.class);
							return new SimpleObjectProperty<>(ren.render(attribute.getValue()));
						}
						else {						
							return new SimpleObjectProperty<>(
									Integer.toHexString(attribute.getValue().hashCode())
										+"@"+attribute.getValue().getClass().getSimpleName());
						}
					}else {
						return new SimpleObjectProperty<>("<leer>");
					}
				}
				
				
				
				return new SimpleObjectProperty<>("");
			}
		});
      
        
        columnValue.setCellFactory(column -> {
        	        	
            return new TreeTableCell<Object, Object>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                   
                    
                    if (item == null || empty) {
                      setText(null);
                      setGraphic(null);
                    }else {
                    	TreeItem<Object> treeItem = getTreeTableView().getTreeItem(getIndex());
                    	if(treeItem!=null) {                    	
                                      	
                        	if(treeItem.getValue() instanceof Parameter) {
                        		Parameter parameter = (Parameter) treeItem.getValue();
                    			if(parameter.isRequired() && 
                    					(parameter.getValue()==null || parameter.getValue().equals(""))) {
                    		        
                    				getTreeTableRow().setStyle("-fx-background-color:rgba(255,106,0,0.25)");
                    				                  				
                    			}
                    		}
                    		
    						setText(item.toString());
    						setGraphic(null);
                        }else {
                        	setBackground(null);
                        }
					}
               
                }
                
                @Override
                public void startEdit() {
                	 int index = getIndex();
                     if (index < 0 || index >= getTreeTableView().getExpandedItemCount()) {
                         return ;
                     }

                     super.startEdit();
                     setText(null);
                     
                     Object item = getTreeTableView().getTreeItem(index).getValue();
               
                     
                     
                     if(item instanceof Parameter) {
                    	 
                    	 Node n = ((Parameter) item).getEditor();
                    	 
                    	 if(n instanceof TextField) {
                    		 if("".equals(((TextField)n).getText()) && ((Parameter) item).getValue() != null) {                    			 
                    			 ((TextField)n).setText(((Parameter) item).getValue().toString());
                    		 }
                    	 }
                     	 
                    	 setGraphic(n);
                     }
                     
                    
                }
                
                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    Object item = getItem();
                    setText(item == null ? null : item.toString());
                    setGraphic(null);
                    getTreeTableView().refresh();
                }
                
                
            };
        });
        columnValue.setEditable(true);
//        new ValueColumnRenderer(columnValue); 
        
       
        
        TreeTableColumn<Object, String> columnType = new TreeTableColumn<>("Type");
        columnType.setPrefWidth(150); 
        columnType.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Object,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Object, String> param) {
				
				Object item = param.getValue().getValue();
				if(item instanceof Parameter) {
					
					if(((Parameter)item).getPropertyDescriptor() == null) {
						return new SimpleStringProperty(item.toString());
					}
					
					Class<?> cl = ((Parameter)item).getType();
					if(cl.isAssignableFrom(List.class)) {
						Class<?> listClass = ((Parameter) item).hasParametrizedListType();
						if(listClass != null) {							
							return new SimpleStringProperty(cl.getSimpleName()+"<"+listClass.getSimpleName()+">");
						}
					}
					return new SimpleStringProperty(((Parameter)item).getType().getSimpleName());
				}
				
				
				return null;
			}
		});
        
        
        
		javafx.application.Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
					
				
			    treeTableView = new TreeTableView<>();
				treeTableView.getColumns().add(columnName);
		        treeTableView.getColumns().add(columnValue);
		        treeTableView.getColumns().add(columnType);
		        
		        treeTableView.expandedItemCountProperty().addListener(new ChangeListener<Object>() {

					@Override
					public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
						treeTableView.refresh();
						
					}
				});

		        
		        treeTableView.comparatorProperty().addListener(new InvalidationListener() {
					
					@Override
					public void invalidated(Observable observable) {
						//System.out.println("Comparator Property invalidated");
						treeTableView.refresh();
						
					}
				});		        
		        

		        
		        treeTableView.setRowFactory(new Callback<TreeTableView<Object>, TreeTableRow<Object>>() {

					@Override
					public TreeTableRow<Object> call(TreeTableView<Object> param) {
						
						
						final TreeTableRow<Object> row = new TreeTableRow<>();  
		                             
		                row.contextMenuProperty().bind(new XBauContextMenuObservable(row));
		               
		 
		                return row;
					}
				});
		        
		        treeTableView.setEditable(true);
//		        
		        vBox = new VBox();
		        vBox.getChildren().setAll(treeTableView);
		        VBox.setVgrow(treeTableView, Priority.ALWAYS);
		 
			    scene = new Scene(vBox);
				
				jfxPanel.setScene(scene);
				
				
				TreeItem<Object> rootItem = new TreeItem<Object>("Root");
				treeTableView.setRoot(rootItem);
				treeTableView.refresh();
			}
		});
		
	}

	public TreeTableView<Object> getTreeTableView() {
		return treeTableView;
	}
  
}
