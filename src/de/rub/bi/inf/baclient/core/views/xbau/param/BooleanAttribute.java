package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;

import de.rub.bi.inf.baclient.core.views.xbau.actions.MethodObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class BooleanAttribute extends Attribute {

	private CheckBox checkBox;
	
	public BooleanAttribute(String name, PropertyDescriptor propertyDescriptor, Object instance) {
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

}
