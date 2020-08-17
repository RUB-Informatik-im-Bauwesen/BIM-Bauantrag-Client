package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;

import de.rub.bi.inf.baclient.core.views.xbau.actions.MethodObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class StringElement extends Element {
	
	private final TextField textField;
	
	public StringElement(String name, PropertyDescriptor propertyDescriptor, Object instance) {
		super(name, propertyDescriptor, instance);	
		
		
		textField = new TextField("");
		
		textField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setValue(newValue);
			}
		});
	}
	
	
	
	@Override
	public Node getEditor() {
		// TODO Auto-generated method stub
		return textField;
	}

	@Override
	public void update() {	
		//update textfield by contained Value
		Object value = getValue();
		if(value instanceof String) {
			textField.setText((String)value);
		}else {
			textField.setText("");
		}
		
	}
}
