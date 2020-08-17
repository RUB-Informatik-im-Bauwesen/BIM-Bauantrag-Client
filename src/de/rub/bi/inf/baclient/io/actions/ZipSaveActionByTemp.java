package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import de.rub.bi.inf.baclient.core.utils.XPlanungParser;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.xbau.io.CustomNamespacePrefixMapper;
import de.rub.bi.inf.xbau.io.XBauExporter;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ZipSaveActionByTemp implements ActionListener {

	private String exportPath = null;

	@Override
	public void actionPerformed(ActionEvent e) {

		final String sessionID = UUID.randomUUID().toString();
		CountDownLatch doneLatch = new CountDownLatch(1);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Zipped Project");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("ZIP-Project", "*.zip"));

				File selectedFile = fileChooser.showSaveDialog(null);

				if (selectedFile != null) {
					System.out.println("Zip Exported to: " + selectedFile.getAbsolutePath());
					exportPath = selectedFile.getAbsolutePath();
				}

				doneLatch.countDown();
			}
		});

		try {
			doneLatch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// Do nothing if filepath not entered
		if (exportPath == null) {
			return;
		}

		try {
			//Story BCF in temp and prepare for zip export 
			XViewer.getInstance().getViewerPanel().getXBCFCommentsView().exportSeparated(ResourceManager.getTempFolder().toString());
			createAndLocateXBAU(ResourceManager.getTempFolder().toString());
			
			
			ArrayList<File> files = retrieveFilesForFolder(ResourceManager.getTempFolder().toFile());
						
			// export all files to compressed zip folder
			exportAllFilesToZip(files, exportPath);

		} catch (IOException | XMLStreamException e1) {
			e1.printStackTrace();
		}

	}

	public ArrayList<File> retrieveFilesForFolder(final File folder) {
		ArrayList<File> result = new ArrayList<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				result.addAll(retrieveFilesForFolder(fileEntry));
			} else {
				System.out.println("Found File: " + fileEntry.getName());
				result.add(fileEntry);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param path
	 * @throws IOException 
	 * @throws XMLStreamException 
	 */
	private void exportAllFilesToZip(ArrayList<File> files, String path) throws IOException, XMLStreamException {

		FileOutputStream fos = new FileOutputStream(path);

		ZipOutputStream zipOut = new ZipOutputStream(fos);

		for (File fileToZip : files) {

			FileInputStream fis = new FileInputStream(fileToZip);

			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}

		zipOut.close();
		fos.close();
	}
	
	/**
	 * 
	 */
	private void createAndLocateXBAU(String path) {
		TreeItem rootItem = XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot();

		for (int i = 0; i < rootItem.getChildren().size(); i++) {
			Object value = ((TreeItem) rootItem.getChildren().get(i)).getValue();
			
			new XBauExporter().saveToFile(path + "/index.xbau", value);
		}
	}

}
