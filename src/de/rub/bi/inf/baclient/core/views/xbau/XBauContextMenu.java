package de.rub.bi.inf.baclient.core.views.xbau;

import java.util.HashSet;
import java.util.Set;

import de.rub.bi.inf.baclient.core.views.xbau.actions.AddElementAction;
import de.rub.bi.inf.baclient.core.views.xbau.param.BigIntegerElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.BooleanAttribute;
import de.rub.bi.inf.baclient.core.views.xbau.param.BooleanElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.FloatElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.ListStringItemElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.ShortElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.StringAttribute;
import de.rub.bi.inf.baclient.core.views.xbau.param.StringElement;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableRow;

public class XBauContextMenu extends ContextMenu {
	
	private Object item;
	
	public XBauContextMenu(TreeTableRow<Object> row) {
		
		this.item = row.getItem();
		
        MenuItem menuItem = new MenuItem("Add");  
        menuItem.setOnAction(new AddElementAction(row));
        				                
        Set<Class<?>> excluded = new HashSet<>();
        excluded.add(StringAttribute.class);
        excluded.add(StringElement.class);
        excluded.add(BooleanAttribute.class);
        excluded.add(BooleanElement.class);
        excluded.add(ShortElement.class);
        excluded.add(FloatElement.class);
        excluded.add(ListStringItemElement.class);
        excluded.add(BigIntegerElement.class);
        
        //System.out.println(item.getClass());
        
        if(item.equals("Root") || !excluded.contains(item.getClass())) {
        	this.getItems().add(menuItem);
        }
               	
    	boolean choiceExists = BasicDocumentChoiceDialog.hasChoice(item.getClass());
		
        //EXPORT FOR ALL CLASSES
        if(choiceExists) {        	
        	this.getItems().add(menuItem);
        }
	}
}