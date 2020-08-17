package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;

import de.rub.bi.inf.baclient.core.views.xbau.actions.MethodObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class StringAttribute extends Attribute{

	private final TextField textField;
	
	public StringAttribute(String name, PropertyDescriptor propertyDescriptor, Object instance) {
		super(name, propertyDescriptor, instance);	
		
		textField = new TextField("");
		
		   
		SimpleStringProperty stringProperty = new SimpleStringProperty("");
		stringProperty.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setValue(newValue);
			}

		});
		
		textField.textProperty().bindBidirectional(stringProperty);
	}
	
	
	@Override
	public Node getEditor() {
		// TODO Auto-generated method stub
		return textField;
	}

}
