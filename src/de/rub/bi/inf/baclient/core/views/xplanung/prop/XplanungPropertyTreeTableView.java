package de.rub.bi.inf.baclient.core.views.xplanung.prop;

import java.awt.BorderLayout;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import de.rub.bi.inf.baclient.core.utils.ClassUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import net.opengis.gml._3.AbstractFeatureType;



public class XplanungPropertyTreeTableView extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Scene scene;
	private TreeTableView<Object> treeTableView;
	private JFXPanel jfxPanel;
	private VBox vBox;

	public XplanungPropertyTreeTableView() {
		
		//Needed for perserving the fx content to be exited whil docking, undocking etc.
		Platform.setImplicitExit(false); 
		
		this.setLayout(new BorderLayout());
		
		jfxPanel = new JFXPanel();
		this.add(jfxPanel, BorderLayout.CENTER);
			
        TreeTableColumn<Object,String> columnName = new TreeTableColumn<Object, String>("Name");
        columnName.setPrefWidth(150);
        new NameColumnRender(columnName);
        
        TreeTableColumn<Object,String> columnValue = new TreeTableColumn<Object, String>("Value");
        columnValue.setPrefWidth(150);
        new ValueColumnRenderer(columnValue); 
        
        
        TreeTableColumn<Object,String> columnInheritedFrom = new TreeTableColumn<>("Inherited From");
        columnInheritedFrom.setPrefWidth(150);
        new InheritedFromColumnRenderer(columnInheritedFrom);
//        
//		
		javafx.application.Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
					
				
			    treeTableView = new TreeTableView<>();
				treeTableView.getColumns().add(columnName);
		        treeTableView.getColumns().add(columnValue);
		        treeTableView.getColumns().add(columnInheritedFrom);
//		        
		        vBox = new VBox();
		        vBox.getChildren().setAll(treeTableView);
		        VBox.setVgrow(treeTableView, Priority.ALWAYS);
		 
			    scene  =  new Scene(vBox);
				
				jfxPanel.setScene(scene);
			}
		});

	
		
		
		
	}

	
	public void loadProperties(AbstractFeatureType featureType) {
		
		javafx.application.Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				TreeItem<Object> rootItem = new TreeItem<Object>(featureType);
				rootItem.setExpanded(true);
				resolveChildren(featureType, rootItem, 0);
				treeTableView.setRoot(rootItem);
				treeTableView.refresh();
			}
		});
		
	}
	
	
	private void resolveChildren(Object object, TreeItem<Object> parent, int depth) {
				
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		
		HashMap<String, PropertyDescriptor> propertyMap = new HashMap<>(propertyDescriptors.length);
		Arrays.stream(propertyDescriptors).forEach(p -> {
			propertyMap.put(p.getName().toLowerCase(), p);
		});
		
		ArrayList<Entry<String, Class>> set = new ArrayList<>();
		getPropertyByTypeHirachie(set, object.getClass());
		
		ArrayList<Object[]> dataList = new ArrayList<>();
//		Object[][] data = new Object[set.size()][3];
	
		for(Entry<String, Class> entry : set) {
			
//			Object[] listEntry = new Object[3];
			Property property = new Property();
			
			//skip rendering of these attributes
			if(
					entry.getKey().equals("position")
				||  entry.getKey().equals("boundedBy")
					
						)
				continue;
			
			//System.out.println(entry.getKey());
			
			property.setName(entry.getKey()); //Name
			try {
				String propertyName = entry.getKey();
				PropertyDescriptor propertyDescriptor = propertyMap.get(propertyName.toLowerCase());
				
				Method method = propertyDescriptor.getReadMethod();
				
				if(propertyDescriptor.getPropertyType()==Boolean.class) {
					String methodName = "is"+propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1, propertyName.length());
					try {
						method = object.getClass().getMethod(methodName);
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(method==null) {
					System.out.println(propertyDescriptor.getName()+" --> "+ method+" --> "+ propertyDescriptor.getPropertyType());
				}
				
				if (method != null) {
					Object value = method.invoke(object);
					if(value != null && (value instanceof JAXBElement<?>)) {
						JAXBElement<?> jaxbElement = (JAXBElement<?>) value;
						value = jaxbElement.getValue();
					}
					
					property.setValue(value);
					
					
				}else {
					System.out.println(propertyName);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			property.set_class(entry.getValue());
			
			//filter null or empty values
			if(property.getValue()==null) continue;
			if( (property.getValue()) instanceof Collection<?> && ((Collection<?>)property.getValue()).size()==0) continue;
			
//			System.out.println(listEntry[0]+": "+listEntry[1].getClass().getTypeName());
			
			
//			dataList.add(listEntry);
			
			TreeItem<Object> item = new TreeItem<>(property);
			item.setExpanded(depth<=1?true:false);
			parent.getChildren().add(item);
			
//			System.out.println(property.getValue().getClass());
			
			if(Collection.class.isAssignableFrom(property.getValue().getClass())) {
				Collection<?> collection = (Collection<?>) property.getValue();
				collection.forEach(c ->{
					TreeItem<Object> citem = new TreeItem<>(c);
					item.getChildren().add(citem);
					resolveChildren(c, citem, depth+1);
				});
			}else if(property.getValue() instanceof JAXBElement<?>) {
				JAXBElement<?> jaxbElement = (JAXBElement<?>) property.getValue();
				resolveChildren(jaxbElement.getValue(), item, depth+1);
			}
			else if(!ClassUtils.isPrimitiveOrJavaLang(property.getValue().getClass()) && depth <=10) {
					resolveChildren(property.getValue(), item, depth+1);
			}
		
		}

	}
	
	private void getPropertyByTypeHirachie(ArrayList<Entry<String, Class>> list, Class obj){
		
		if(!(obj == Object.class)) {
			
			for (Field field : obj.getDeclaredFields()) {
				XmlAttribute[] attributes = field.getDeclaredAnnotationsByType(XmlAttribute.class);
				for (XmlAttribute attribute : attributes) {
//					System.out.println(attribute.name());
					
					list.add(new Entry<String, Class>() {

						@Override
						public String getKey() {
							// TODO Auto-generated method stub
							return attribute.name();
						}

						@Override
						public Class getValue() {
							// TODO Auto-generated method stub
							return obj;
						}

						@Override
						public Class setValue(Class arg0) {
							// TODO Auto-generated method stub
							return null;
						}

						
					});
				}
			}
			
			
			
			
			XmlType annoType = (XmlType)obj.getDeclaredAnnotation(XmlType.class);
			if(annoType != null) {
				String[] arr = annoType.propOrder();

				for(String prop : arr) {
					if (prop == null || prop.equals(""))
						continue;
					
					list.add(new Entry<String, Class>() {

						@Override
						public String getKey() {
							// TODO Auto-generated method stub
							return prop;
						}

						@Override
						public Class getValue() {
							// TODO Auto-generated method stub
							return obj;
						}

						@Override
						public Class setValue(Class arg0) {
							// TODO Auto-generated method stub
							return null;
						}

						
					});
				}
			}
			
			getPropertyByTypeHirachie(list, obj.getSuperclass());
		}
	}

	
		
	
  
}
