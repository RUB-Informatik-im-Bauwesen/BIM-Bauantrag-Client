package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModelListener;
import com.apstex.loader3d.ifc.IfcLoader;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.ifc.CustomIfcLoaderManager;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import de.rub.bi.inf.baclient.core.utils.XPlanungParser;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xbau.BasicDocumentChoiceDialog;
import de.rub.bi.inf.baclient.core.views.xbau.actions.AddElementAction;
import de.rub.bi.inf.xbau.io.CustomNamespacePrefixMapper;
import de.rub.bi.inf.xbau.io.XBauImporter;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ZipLoadAction implements ActionListener {

	private ConcurrentHashMap<String, File> fileMap = new ConcurrentHashMap<>();
	private String exportPath = null;

	@Override
	public void actionPerformed(ActionEvent e) {

		final String sessionID = UUID.randomUUID().toString();
		CountDownLatch doneLatch = new CountDownLatch(1);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Load Zipped Project");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("ZIP-Project", "*.zip"));

				File selectedFile = fileChooser.showOpenDialog(null);

				if (selectedFile != null) {
					System.out.println("Zip Import of: " + selectedFile.getAbsolutePath());
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

		// export all files to compressed zip folder
		importAllFilesFromZip(exportPath);
	}

	/**
	 * 
	 * @param path
	 * @throws JAXBException
	 */
	private void importAllFilesFromZip(String path) {
		try {
			ZipFile zipFile = new ZipFile(path);

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			
			HashMap<String, InputStream> ifcStreams = new HashMap<String, InputStream>();
			HashMap<String, InputStream> xBauStreams = new HashMap<String, InputStream>();
			HashMap<String, InputStream> xPlanungtreams = new HashMap<String, InputStream>();
			HashMap<String, InputStream> bcfStreams = new HashMap<String, InputStream>();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream stream = zipFile.getInputStream(entry);

				System.out.println("Reading File: " + entry.getName());

				// XBAU IMPORT
				if (entry.getName().toLowerCase().endsWith(".xbau")) {
					xBauStreams.put(entry.getName(), stream);
				}
				// IFC IMPORT
				if (entry.getName().toLowerCase().endsWith(".ifc")) {
					ifcStreams.put(entry.getName(), stream);
				}

				// GML IMPORT
				if (entry.getName().toLowerCase().endsWith(".gml")) {
					xPlanungtreams.put(entry.getName(), stream);
				}

				// BCFZIP IMPORT
				if (entry.getName().toLowerCase().endsWith(".bcfzip")) {
					bcfStreams.put(entry.getName(), stream);
				}
			}
			
			for(String xBauKey : xBauStreams.keySet()) {
				try {
					importXBAU(xBauKey, xBauStreams.get(xBauKey));
				} catch (JAXBException | XMLStreamException e) {
					e.printStackTrace();
				}
			}

			for(String xPlanKey : xPlanungtreams.keySet()) {
				importXPLANUNG(xPlanKey, xPlanungtreams.get(xPlanKey));
			}
			
			for(String ifcKey : ifcStreams.keySet()) {
				importIFC(ifcKey, ifcStreams.get(ifcKey));
			}
			
			for(String bcfKey : bcfStreams.keySet()) {
				importBCF(bcfKey, bcfStreams.get(bcfKey));
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * @param stream
	 * @throws JAXBException
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private void importXBAU(String name, InputStream stream) throws JAXBException, XMLStreamException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = stream.read(buf)) >= 0)
			baos.write(buf, 0, n);
		byte[] content = baos.toByteArray();

		InputStream is1 = new ByteArrayInputStream(content);
		byte[] buffer = new byte[is1.available()];
		is1.read(buffer);

		File targetFile = new File(ResourceManager.getTempFolder().toString() + "/" + name);
		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);
		outStream.close();

		System.out.println("Opening File: " + ResourceManager.getTempFolder().toString() + "/" + name);

		Object o = new XBauImporter().readFromFile(targetFile.getAbsolutePath());

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

	/**
	 * 
	 * @param stream
	 * @throws IOException
	 * @throws JAXBException
	 */
	private void importIFC(String name, InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = stream.read(buf)) >= 0)
			baos.write(buf, 0, n);
		byte[] content = baos.toByteArray();

		InputStream is1 = new ByteArrayInputStream(content);
		byte[] buffer = new byte[is1.available()];
		is1.read(buffer);

		File targetFile = new File(ResourceManager.getTempFolder().toString() + "/" + name);
		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);
		outStream.close();

		System.out.println("Opening File: " + ResourceManager.getTempFolder().toString() + "/" + name);
		InputStream is2 = new ByteArrayInputStream(content);


		HashMap<String, Object> eigenschaften = new HashMap<>();
		eigenschaften.put("TEMP_FILE_NAME", targetFile.getName());
		eigenschaften.put("TEMP_FILE_PATH", targetFile.getAbsolutePath());
		
    	CustomIfcLoaderManager.getInstance().loadStepInputStream(is2, true, eigenschaften);
		//IfcLoadManager.getInstance().loadStepInputStream(is2); //Ohne geolocalization
	}

	/**
	 * 
	 * @param stream
	 * @throws IOException
	 * @throws JAXBException
	 */
	private void importXPLANUNG(String name, InputStream stream) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = stream.read(buf)) >= 0)
			baos.write(buf, 0, n);
		byte[] content = baos.toByteArray();

		InputStream is1 = new ByteArrayInputStream(content);
		byte[] buffer = new byte[is1.available()];
		is1.read(buffer);

		File targetFile = new File(ResourceManager.getTempFolder().toString() + "/" + name);
		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);
		outStream.close();

		System.out.println("Opening File: " + ResourceManager.getTempFolder().toString() + "/" + name);
		InputStream is2 = new ByteArrayInputStream(content);

		XPlanungModel model = XPlanungParser.getInstance().createModelFromFile(name, is2);
		XPlanungParser.getInstance().loadXPlanung(model);
	}

	/**
	 * 
	 * @param stream
	 * @throws IOException 
	 * @throws JAXBException
	 */
	private void importBCF(String name, InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = stream.read(buf)) >= 0)
		    baos.write(buf, 0, n);
		byte[] content = baos.toByteArray();

		InputStream is1 = new ByteArrayInputStream(content);
		byte[] buffer = new byte[is1.available()];
		is1.read(buffer);
			
		File targetFile = new File(ResourceManager.getTempFolder().toString() + "/" + name);
		OutputStream outStream = new FileOutputStream(targetFile);
		outStream.write(buffer);
		outStream.close();
		
		System.out.println("Opening File: " + ResourceManager.getTempFolder().toString() + "/" + name);
	
		XViewer.getInstance().getViewerPanel().getXBCFCommentsView().importFromFile(
				targetFile
		);
	}
}
