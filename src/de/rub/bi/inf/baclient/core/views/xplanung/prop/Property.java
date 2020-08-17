package de.rub.bi.inf.baclient.core.views.xplanung.prop;

public class Property {
	
	String name;
	Object value;
	Class<?> _class;
	
	public String getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Class<?> get_class() {
		return _class;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void set_class(Class<?> _class) {
		this._class = _class;
	}
}
