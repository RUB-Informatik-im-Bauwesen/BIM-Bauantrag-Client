package de.rub.bi.inf.baclient.core.utils;

import java.util.ArrayList;
import java.util.Collection;

public class PropertyContainer {
	
	private ArrayList<PropertyContainer> contained;
	
	private String name;
	private String usageName;
	private String description;
	private Object value;
	
	public PropertyContainer(String name, String usageName, String description, Object value) {
		this.name = name;
		this.usageName = usageName;
		this.description = description;
		this.value = value;
		this.contained = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getUsageName() {
		return usageName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void addContainedProperty(PropertyContainer propContained) {
		this.contained.add(propContained);
	}

	public void addAllContainedProperty(Collection<PropertyContainer> propContained) {
		this.contained.addAll(propContained);
	}

	
	public void removeContainedProperty(PropertyContainer propContained) {
		this.contained.remove(propContained);
	}
	
	public ArrayList<PropertyContainer> getContainedProperties(){
		return contained;
	}
}
