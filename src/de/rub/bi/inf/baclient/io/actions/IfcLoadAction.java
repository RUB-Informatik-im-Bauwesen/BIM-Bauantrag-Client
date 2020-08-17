package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import com.apstex.gui.core.controller.LoadManagerListener;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelRoot;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.loader3d.core.NotSupportedException;

import de.rub.bi.inf.baclient.core.ifc.CustomIfcLoaderManager;
import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class IfcLoadAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				 FileChooser fileChooser = new FileChooser();
				 fileChooser.setTitle("Open Resource File");
				 fileChooser.getExtensionFilters().addAll(
				         new ExtensionFilter("IFC-Files", "*.ifc"));
				 File selectedFile = fileChooser.showOpenDialog(null);
				 if (selectedFile != null) {
				    System.out.println(selectedFile.getAbsolutePath());
				    
				    try {
				    	
				    	try {
				    		//Copy File to temp Project
				    		File tempTile = new File(ResourceManager.getTempFolder().toString() + "/" + selectedFile.getName());
							Files.copy(selectedFile.toPath(), tempTile.toPath());
							System.out.println("File stored: " + tempTile.getAbsolutePath());
				    	
							HashMap<String, Object> eigenschaften = new HashMap<>();
							eigenschaften.put("TEMP_FILE_NAME", tempTile.getName());
							eigenschaften.put("TEMP_FILE_PATH", tempTile.getAbsolutePath());
					    	
					    	CustomIfcLoaderManager.getInstance().loadFile(selectedFile, false, eigenschaften);

				    	} catch (IOException e) {
							e.printStackTrace();
						}

				    }catch (NotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				
			}
		});
		

	}
}
