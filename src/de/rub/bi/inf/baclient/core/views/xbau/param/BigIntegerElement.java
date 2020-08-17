package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;
import java.math.BigInteger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class BigIntegerElement extends Element {
	
	private final TextField textField;
	
	public BigIntegerElement(String name, PropertyDescriptor propertyDescriptor, Object instance) {
		super(name, propertyDescriptor, instance);	
	
		textField = new TextField("");
		
		textField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			
				try {
					
					BigInteger bigIntVal = new BigInteger(newValue);
					setValue(bigIntVal);
					
				} catch (NumberFormatException e) {
					System.err.println("Wrong format: Please enter a valid Long-Type input value for BigInteger-Type.");
					System.err.println(e.getMessage());
					//textField.setText(oldValue);
				}
				
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
		
		//update text by contained Value
		String currentVal = "";
		
		Object value = getValue();
		if(value instanceof BigInteger) {
			currentVal = new Long(
					((BigInteger)value).longValue()
			).toString();
		}
		
		textField.setText(currentVal);
		
	}

}
