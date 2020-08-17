package de.rub.bi.inf.baclient.core.model;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;

public class ChoiceProperty<T> {
	
	private T value;
	
	public ChoiceProperty() {
		// TODO Auto-generated constructor stub
	}
	
	public ChoiceProperty(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		if (value instanceof XPlanungModel) {
			return ((XPlanungModel) value).getModelName();
		} else if (value instanceof ApplicationModelNode) {
			return ((ApplicationModelNode) value).getModelName();
		}
		return super.toString();
	}

}
