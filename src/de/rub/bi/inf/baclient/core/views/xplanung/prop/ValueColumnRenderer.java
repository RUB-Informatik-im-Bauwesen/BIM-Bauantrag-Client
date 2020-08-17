package de.rub.bi.inf.baclient.core.views.xplanung.prop;

import java.util.Collection;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.utils.ClassUtils;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.ReferenceType;

public class ValueColumnRenderer extends AbstractTreeTableColumnRenderer<Object> {
	
	public ValueColumnRenderer(TreeTableColumn<Object, String> column) {
		super(column);
	}

	@Override
	public String render(TreeItem<Object> treeItem) {
		
		Object val = treeItem.getValue();
		if(val instanceof JAXBElement<?>) {
			val = ((JAXBElement<?>) val).getValue();
		}
		
//		System.out.println(val);

		
		if(val instanceof String) {
			return (String) val;
		}else if (val instanceof Property) {
		
			Object propertyValue = ((Property) val).getValue();
			if(propertyValue instanceof JAXBElement<?>) {
				propertyValue = ((JAXBElement<?>) propertyValue).getValue();
			}
			
			if(Collection.class.isAssignableFrom(propertyValue.getClass())) {	
				Collection<?> collection = (Collection<?>) propertyValue;
				if (collection.size()>0 && !ClassUtils.isPrimitiveOrJavaLang(collection.iterator().next().getClass())) {
					return "[#"+collection.size()+"]";
				}else {							
					return propertyValue.toString();
				}
			}
			else if(propertyValue instanceof XMLGregorianCalendar) {
				XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) propertyValue;
				return gregorianCalendar.toXMLFormat();
			}
			else if(!ClassUtils.isPrimitiveOrJavaLang(propertyValue.getClass())) {
				return propertyValue.getClass().getSimpleName();
			}
			else {						
				return propertyValue.toString();
			}
			
		}
		
		if(val instanceof XMLGregorianCalendar) {
			XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) val;
			return gregorianCalendar.toXMLFormat();
		}
		else if (val instanceof ReferenceType) {
			ReferenceType referenceType = (ReferenceType) val;
			AbstractFeatureType featureType = detectFeature(treeItem);
			if(featureType != null) {
				XPlanungModel model = XPlanungModelContainer.getInstance().getModelOfFeature(featureType);
				String id = referenceType.getHref().substring(1);
				AbstractFeatureType refFeatureType = model.getFeatureById(id);
				if(refFeatureType!=null) {
					if(refFeatureType instanceof de.xplanung.xplangml._4._1.XPTextAbschnittType) {
						de.xplanung.xplangml._4._1.XPTextAbschnittType textAbschnittType = 
								(de.xplanung.xplangml._4._1.XPTextAbschnittType) refFeatureType;
						return textAbschnittType.getText();
					}
					else if(refFeatureType instanceof de.xplanung.xplangml._5._0.XPTextAbschnittType) {
						de.xplanung.xplangml._5._0.XPTextAbschnittType textAbschnittType = 
								(de.xplanung.xplangml._5._0.XPTextAbschnittType) refFeatureType;
						return textAbschnittType.getText();
						
					
					}else {
						return refFeatureType.toString();
					}				
				}else {
					return "Reference not found";
				}
			
			}else {
				return "Feature not found!";
			}
		}
		else if(ClassUtils.isPrimitiveOrJavaLang(val.getClass())) {
			return val.toString();
		}else {
			return val.getClass().getSimpleName();
		}
		
		
	}
	
	private AbstractFeatureType detectFeature(TreeItem<Object> treeItem) {
		if(treeItem.getParent() != null) {			
			TreeItem<Object> parentTreeItem = treeItem.getParent();
			if (parentTreeItem.getValue() instanceof AbstractFeatureType) {
				return (AbstractFeatureType) parentTreeItem.getValue();
			}else {
				return detectFeature(parentTreeItem);
			}
		}else {
			return null;
		}
	}

}
