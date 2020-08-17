package de.rub.bi.inf.baclient.core.views.pruefungen;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableRow;

public class ModellpruefungMenuObservable implements ObservableValue<ModellpruefungContextMenu>{

	private TreeTableRow<Object> row;
	
	public ModellpruefungMenuObservable(TreeTableRow<Object> row) {
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
	public void addListener(ChangeListener<? super ModellpruefungContextMenu> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ModellpruefungContextMenu getValue() {
		if(row.getItem()==null)
			return null;
		return new ModellpruefungContextMenu(row);
	}

	@Override
	public void removeListener(ChangeListener<? super ModellpruefungContextMenu> listener) {
		// TODO Auto-generated method stub
		
	}

}
