package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xbau.BasicDocumentChoiceDialog;
import de.rub.bi.inf.baclient.core.views.xbau.actions.AddElementAction;
import de.rub.bi.inf.xbau.io.XBauImporter;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XBauLoadAction implements ActionListener {

	File selectedFile = null;
	CountDownLatch doneLatch;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		selectedFile = null;
		doneLatch = new CountDownLatch(1);
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Laden XBau Datei");
				fileChooser.getExtensionFilters().addAll(
				         new ExtensionFilter("XBau File", "*.xbau"));
				selectedFile = fileChooser.showOpenDialog(null);
				doneLatch.countDown();
			}
		});
		
		try {
			doneLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (selectedFile!=null) // if a file has been chosen
		{
		
			String filename = selectedFile.getAbsolutePath();
			System.out.println(filename);
			
			try {
				//initiate import
				importXBAU();
			} catch (IOException | JAXBException | XMLStreamException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 * @throws JAXBException
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private void importXBAU() throws JAXBException, XMLStreamException, IOException {
		//Copy File to temp Project
		Files.copy(selectedFile.toPath(), new File(ResourceManager.getTempFolder().toString() + "/" + selectedFile.getName()).toPath());
		System.out.println("File stored: " + ResourceManager.getTempFolder().toString() + "/" + selectedFile.getName());

		//Start opening process
		System.out.println("Opening File: " + ResourceManager.getTempFolder().toString() + "/" + selectedFile.getName());
	
		Object o = new XBauImporter().readFromFile(selectedFile.getAbsolutePath());

		// if(o instanceof BaugenehmigungAntrag0200){
		if (BasicDocumentChoiceDialog.hasChoice(o.getClass())) {

			TreeItem<Object> newTreeItem = new TreeItem<>(o);

			TreeItem rootItem = XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot();

			rootItem.getChildren().add(newTreeItem);
			rootItem.setExpanded(true);

			AddElementAction.resolveParameters(o, newTreeItem);
			newTreeItem.setExpanded(true);

		}
	}
	
}
