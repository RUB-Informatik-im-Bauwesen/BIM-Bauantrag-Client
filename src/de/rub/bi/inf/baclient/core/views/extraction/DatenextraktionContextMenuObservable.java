package de.rub.bi.inf.baclient.core.views.extraction;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableRow;

public class DatenextraktionContextMenuObservable implements ObservableValue<DatenextraktionMaßeContextMenu>{

	private TreeTableRow<Object> row;
	
	public DatenextraktionContextMenuObservable(TreeTableRow<Object> row) {
		this.row = row;
	}
	
	
	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ChangeListener<? super DatenextraktionMaßeContextMenu> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatenextraktionMaßeContextMenu getValue() {
		if(row.getItem()==null)
			return null;
		return new DatenextraktionMaßeContextMenu(row);
	}

	@Override
	public void removeListener(ChangeListener<? super DatenextraktionMaßeContextMenu> listener) {
		// TODO Auto-generated method stub
		
	}

}
