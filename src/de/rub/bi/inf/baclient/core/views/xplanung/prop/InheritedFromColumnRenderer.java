package de.rub.bi.inf.baclient.core.views.xplanung.prop;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

public class InheritedFromColumnRenderer extends AbstractTreeTableColumnRenderer<Object> {

	public InheritedFromColumnRenderer(TreeTableColumn<Object, String> column) {
		super(column);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String render(TreeItem<Object> treeItem) {
		Object val =treeItem.getValue();
		if(val instanceof String) {
			return (String) val;
		}else if (val instanceof Property) {
			return ((Property) val).get_class().getSimpleName();
		}
		
		return val.getClass().getSimpleName();
	
	}

}
