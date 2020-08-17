package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class ListStringItemElement extends Element {
	
	private final TextField textField;
	private List<String> collectionRef;
	private int index;
	
	public ListStringItemElement(String name, int ind, List<String> collection) {
		super(name, null, null);	
	
		index = ind;
		collectionRef = collection;
		textField = new TextField("");
		
		textField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setValue(newValue);
			}
		});
	}
	
	@Override
	public void setValue(Object value) {
		collectionRef.set(index, (String)value);
	}
	
	@Override
	public Object getValue() {
		return collectionRef.get(index);
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
