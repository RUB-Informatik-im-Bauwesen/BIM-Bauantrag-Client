package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class BooleanElement extends Element {

	private CheckBox checkBox;
	
	public BooleanElement(String name, PropertyDescriptor propertyDescriptor, Object instance) {
		super(name, propertyDescriptor, instance);

		checkBox = new CheckBox();
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				setValue(newValue);
			}
		});
	}

	@Override
	public Node getEditor() {
		// TODO Auto-generated method stub
		return checkBox;
	}

	
	@Override
	public void update() {	
		//update checkbox by contained Value
		Object value = getValue();
		if(value instanceof Boolean) {
			checkBox.setSelected(((Boolean)value).booleanValue());
		}else {
			checkBox.setSelected(false);
		}
		
	}
}
