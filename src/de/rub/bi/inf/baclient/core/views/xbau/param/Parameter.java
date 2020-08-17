package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import de.rub.bi.inf.baclient.core.views.xbau.actions.MethodObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;

public abstract class Parameter {

	protected boolean required;
	protected String name;
	protected PropertyDescriptor propertyDescriptor;
	protected Object instance;
	
	public Parameter(String name, PropertyDescriptor propertyDescriptor, Object instance) {
		this.name = name;
		this.propertyDescriptor = propertyDescriptor;
		this.instance = instance;
	}
	
	public String getName() {
		return name;
	}
	
    public Object getValue() {
    	
    	if(propertyDescriptor == null)
    		return null;
		
		Method method = propertyDescriptor.getReadMethod();
		if(propertyDescriptor.getPropertyType()==Boolean.class) {
			String methodName = "is"+name.substring(0, 1).toUpperCase()+
					name.substring(1, name.length());
			try {
				method = instance.getClass().getMethod(methodName);		
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Object value = null;
		
		try {
			value = method.invoke(instance);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;
	}
	
    
    public boolean isRequired() {
		return required;
	}
    
    public Class<?> getType(){
		return propertyDescriptor.getPropertyType();
	}
    
    public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}
    
    public Object getInstance() {
		return instance;
	}
    
    public Class<?> hasParametrizedListType(){

	    Type returnType = propertyDescriptor.getReadMethod().getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) returnType;
            Type[] argTypes = paramType.getActualTypeArguments();
            if (argTypes.length > 0) {
                try {
                	return ClassLoader.getSystemClassLoader().loadClass(argTypes[0].getTypeName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   
            }        
        }
        
        return null;
    }
    
    
    public abstract Node getEditor();
    

	public abstract void update();
    
	
    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}
    
    public void setInstance(Object instance) {
		this.instance = instance;
	}
    
    public void setRequired(boolean required) {
		this.required = required;
	}
    
    public void setValue(Object value) {
		
		Method method = propertyDescriptor.getWriteMethod();
		
		try {
			method.invoke(instance, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
}


