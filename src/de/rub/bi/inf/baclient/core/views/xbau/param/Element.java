package de.rub.bi.inf.baclient.core.views.xbau.param;

import java.beans.PropertyDescriptor;

import de.rub.bi.inf.baclient.core.views.xbau.actions.MethodObject;
import javafx.scene.Node;

public class Element extends Parameter{
	
	
	public Element(String name, PropertyDescriptor propertyDescriptor, Object instance) {
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
