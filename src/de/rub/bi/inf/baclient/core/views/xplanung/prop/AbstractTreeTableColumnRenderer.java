package de.rub.bi.inf.baclient.core.views.xplanung.prop;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;

public abstract class AbstractTreeTableColumnRenderer<T> {

	public AbstractTreeTableColumnRenderer(TreeTableColumn<T, String> column) {
		column.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<T,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<T, String> param) {
				return new SimpleStringProperty(
						render(param.getValue()));
			}
		});
	}
	
	
	public abstract String render(TreeItem<T> treeItem);

}
