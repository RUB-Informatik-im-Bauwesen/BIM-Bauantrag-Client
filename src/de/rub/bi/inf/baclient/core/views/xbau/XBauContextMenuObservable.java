package de.rub.bi.inf.baclient.core.views.xbau;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableRow;

public class XBauContextMenuObservable implements ObservableValue<XBauContextMenu>{

		private TreeTableRow<Object> row;
			
		public XBauContextMenuObservable(TreeTableRow<Object> row) {
			this.row = row;
		}
		
		@Override
		public void addListener(InvalidationListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeListener(InvalidationListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addListener(ChangeListener<? super XBauContextMenu> listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public XBauContextMenu getValue() {
			if(row.getItem()==null)
				return null;
		
			return new XBauContextMenu(row);
		}

		@Override
		public void removeListener(ChangeListener<? super XBauContextMenu> listener) {
			// TODO Auto-generated method stub
			
		}
		
	}

