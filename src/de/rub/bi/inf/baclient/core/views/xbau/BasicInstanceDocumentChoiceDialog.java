package de.rub.bi.inf.baclient.core.views.xbau;

import java.util.ArrayList;

import de.rub.bi.inf.baclient.core.views.XViewer;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TreeItem;

public class BasicInstanceDocumentChoiceDialog extends ChoiceDialog<Object> {
	
	public BasicInstanceDocumentChoiceDialog() {	
		super(null, generateChoices());
		this.setTitle("Antrag Auswahl.");
		this.setHeaderText("Einen existierenden Antrag auswählen und fortsetzen.");
	}
	
	public static boolean hasChoice(Object obj) {
		return generateChoices().contains(obj);
	}
	
	private static ArrayList<Object> generateChoices(){
		ArrayList<Object> choices = new ArrayList<>();
	
		TreeItem rootItem = XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot();

		for (int i = 0; i < rootItem.getChildren().size(); i++) {
			Object value = ((TreeItem) rootItem.getChildren().get(i)).getValue();
			
			choices.add(value);
		}
		
		return choices;
	}
	
}
