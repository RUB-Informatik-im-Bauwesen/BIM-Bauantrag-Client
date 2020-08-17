package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xbau.BasicInstanceDocumentChoiceDialog;
import de.rub.bi.inf.xbau.io.XBauExporter;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class XBauSaveAction implements ActionListener {

	File selectedFile = null;
	CountDownLatch doneLatch;
	Object value = null;

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (check()) {
			
			value = null;
			doneLatch = new CountDownLatch(1);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					BasicInstanceDocumentChoiceDialog dialog = new BasicInstanceDocumentChoiceDialog();
		        	Optional<Object> result = dialog.showAndWait();
		        	
		        	if(result.isPresent()) {
		        		value = result.get();
		        	}

					doneLatch.countDown();
				}
			});

			try {
				doneLatch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(value != null) {
				selectedFile = null;
				
				doneLatch = new CountDownLatch(1);

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Speichern XBau Datei");
						fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XBau File", "*.xbau"));
						selectedFile = fileChooser.showSaveDialog(null);

						doneLatch.countDown();
					}
				});

				try {
					doneLatch.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (selectedFile != null) // if a file has been chosen
				{

					String filename = selectedFile.getAbsolutePath();
					System.out.println(filename);

					createAndLocateXBAU(selectedFile.getAbsolutePath(), value);

				}
			}
        		
		}

	}

	private boolean check() {
		TreeItem rootItem = XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot();

		if (rootItem.getChildren().isEmpty()) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert dialog = new Alert(AlertType.INFORMATION);
					dialog.setTitle("XBau Information");
					dialog.setHeaderText("Speichern kann nicht durchgeführt werden");
					dialog.setContentText("Keine XBau zum Speichern gefunden.");
					dialog.showAndWait();
				}
			});

			return false;
		}

		return true;
	}

	/**
	 * 
	 */
	private void createAndLocateXBAU(String path, Object value) {
		new XBauExporter().saveToFile(path, value);
	}
}
