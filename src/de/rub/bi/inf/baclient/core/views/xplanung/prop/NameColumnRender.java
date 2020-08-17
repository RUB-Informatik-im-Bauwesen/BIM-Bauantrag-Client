package de.rub.bi.inf.baclient.core.views.xplanung.prop;

import java.util.Collection;

import javax.xml.bind.JAXBElement;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

public class NameColumnRender extends AbstractTreeTableColumnRenderer<Object> {

	public NameColumnRender(TreeTableColumn<Object, String> column) {
		super(column);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String render(TreeItem<Object> treeItem) {
		// TODO Auto-generated method stub
		String outString = null;
		
		Object val = treeItem.getValue();
		if(val instanceof String) {
			outString = (String) val;
		}else if (val instanceof Property) {
			outString = ((Property) val).getName();
		}
		
		TreeItem<Object> parent = treeItem.getParent();
	    if(parent!=null) {	
	    	
	    	Object parentValue = parent.getValue();
	    	if (parent.getValue() instanceof Property) {
	    		parentValue = ((Property) parent.getValue()).getValue();
	    	}
	    	
	    	if(Collection.class.isAssignableFrom(parentValue.getClass())) 
	    	{
	    		int index = parent.getChildren().indexOf(treeItem);
	    
	    		String nameString = "("+index+")";
	    		
	    		if(val instanceof JAXBElement<?>) {
	    			JAXBElement<?> jaxbElement = (JAXBElement<?>) val;
	    			nameString+=" "+jaxbElement.getName().getLocalPart();
		    	}
	    		
	    		outString = nameString;
	    	}
	    		
	    }
	    			
	    
	    if(outString==null) {
	    	outString = val.getClass().getSimpleName();
	    }
	    
		return outString;
	}

}
