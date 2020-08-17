package de.rub.bi.inf.baclient.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.apstex.ifctoolbox.ifc.IfcPropertySetDefinition;

import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import net.opengis.gml._3.AbstractFeatureType;

public class PropertyUtils {
	
	private static HashMap<String, List<PropertyContainer>> propertyMap = new HashMap<>();
	private static HashMap<String, IfcPropertySetDefinition> ifcPropertyMap = new HashMap<>();
	
	public static void load(List<?> objs) {
		propertyMap.putAll(createPropertyMap(objs));
	}
	
	public static HashMap<String, List<PropertyContainer>> createPropertyMap(List<?> objs){
		HashMap<String, List<PropertyContainer>> propertyMap = new HashMap<>();
		
		for(Object obj : objs) {			
			List<PropertyContainer> propertyList = createPropertyList(obj);
			
			if(obj instanceof AbstractFeatureType) {				
				propertyMap.put(((AbstractFeatureType)obj).getId(), propertyList);
			}else {
				propertyMap.put(obj.toString(), propertyList);	
			}
		}
		
		return propertyMap;
	}
	
	private static List<PropertyContainer> createPropertyList(Object obj){
		
		List<PropertyContainer> pList = new ArrayList<>();
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			
			Arrays.stream(propertyDescriptors).forEach(p -> {

				Method method = p.getReadMethod();
				Object value = null;
				
				try {
					if (method != null) {
						value = method.invoke(obj);
						if(value != null && (value instanceof JAXBElement<?>)) {
							JAXBElement<?> jaxbElement = (JAXBElement<?>) value;
							value = jaxbElement.getValue();
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				
				
				boolean propertyCheck = 
						p.getName().equalsIgnoreCase("position") | 
						p.getName().equalsIgnoreCase("abstracktGeometry") |
						p.getName().equalsIgnoreCase("exterior") | 
						p.getName().equalsIgnoreCase("boundedBy") | 
						p.getName().equalsIgnoreCase("envelope");
				if(value != null && !propertyCheck) {
					if(value instanceof ArrayList ? !((ArrayList)value).isEmpty() : true) {						
						pList.add(createPropertyList(p, value));
					}
				}

			});

		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		
		return pList;
	}
	
	private static PropertyContainer createPropertyList(PropertyDescriptor p, Object obj){
		
		PropertyContainer prop = new PropertyContainer(p.getName(), p.getDisplayName(), p.getShortDescription(), obj);
		
		if(obj instanceof ArrayList) {
			if(!((ArrayList)obj).isEmpty()) {

				for(int i = 0; i < ((ArrayList)obj).size(); i++) {
					PropertyContainer arrProp = new PropertyContainer("[" + i + "]", "", "index " + i + " of content from " + p.getName(), ((ArrayList)obj).get(i));
					prop.addContainedProperty(arrProp);
					if(!ClassUtils.isPrimitiveOrJavaLang(((ArrayList)obj).get(i).getClass())) {
						arrProp.addAllContainedProperty(createPropertyList(((ArrayList)obj).get(i)));
					}
				}
			}
		}else if(!ClassUtils.isPrimitiveOrJavaLang(obj.getClass())){			
			prop.addAllContainedProperty(createPropertyList(obj));
		}
		
		return prop;
		
	}
		
	
	public static void setIfcProperty(String id, IfcPropertySetDefinition prop) {
		ifcPropertyMap.put(id, prop);
	}
	
	public static IfcPropertySetDefinition getIfcProperty(String id) {
		return ifcPropertyMap.get(id);
	}
	
	public static List<PropertyContainer> getPropertyInfos(String id){
		return propertyMap.get(id);
	}
}
