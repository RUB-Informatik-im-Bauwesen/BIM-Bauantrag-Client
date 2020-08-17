package de.rub.bi.inf.baclient.core.views.xbau.actions;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public class MethodObject {
	
	private PropertyDescriptor descriptor;
	private Object instance;
	private Object value;
	private Class<?> type;
	
	public MethodObject(String fieldName, Class<?> type) throws NoSuchMethodException, SecurityException, IntrospectionException {
		this.type = type;
		this.descriptor = new PropertyDescriptor(fieldName, this.getClass().getMethod("getValue"), this.getClass().getMethod("setValue", Object.class) );
		this.instance = this;
	}
	
	public MethodObject(PropertyDescriptor descriptor, Object instance) {
		this.type = descriptor.getPropertyType();
		this.descriptor = descriptor;
		this.instance = instance;
	}
	
	public Class<?> getType(){
		return type;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}
	
	public Object getInstance() {
		return instance;
	}

}
