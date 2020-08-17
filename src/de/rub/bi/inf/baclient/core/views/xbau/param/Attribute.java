package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;

import de.rub.bi.inf.baclient.core.views.xbau.actions.MethodObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class Attribute extends Parameter {

	public Attribute(String name, PropertyDescriptor propertyDescriptor, Object instance) {
		super(name, propertyDescriptor, instance);
	}

	@Override
	public Node getEditor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		//DO NOTHING HERE
	}

}
