package de.rub.bi.inf.baclient.core.views.xplanung.browseFX;

import de.rub.bi.inf.baclient.core.actions.XPlanungActionUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeTableCell;

public class VisibilityCheckboxCellFactory extends TreeTableCell<Object, Boolean>{
    private CheckBox checkBox;

    public VisibilityCheckboxCellFactory() {
        checkBox = new CheckBox();
        
        checkBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
            	boolean c = checkBox.isSelected();
                
                if(getTreeTableRow().getItem() != null) {
                	if(c) {
                		XPlanungActionUtils.setVisible(getTreeTableRow().getItem());
                	}else {
                		XPlanungActionUtils.setInvisible(getTreeTableRow().getItem());
                	}
    			}
            }
        });
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
    	if (empty) {
            setText(null);
            setGraphic(null);
        }else{
            checkBox.setSelected(item);
            setText(null);
            setGraphic(checkBox);
        }
    }
}