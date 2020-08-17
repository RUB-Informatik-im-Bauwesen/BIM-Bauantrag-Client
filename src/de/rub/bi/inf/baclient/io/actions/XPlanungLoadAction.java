package de.rub.bi.inf.baclient.io.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.ResourceManager;
import de.rub.bi.inf.baclient.core.utils.XPlanungParser;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class XPlanungLoadAction implements ActionListener {

	File selectedFile = null;
	XPlanungModel model = null;
	CountDownLatch doneLatch;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		selectedFile = null;
		model = null;
		doneLatch = new CountDownLatch(1);
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open XPlanung File");
				fileChooser.getExtensionFilters().addAll(
				         new ExtensionFilter("XPlanung File", "*.gml"));
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
	    		//Copy File to temp Project
	    		System.out.println("File stored: " + ResourceManager.getTempFolder().toString() + "/" + selectedFile.getName());
				Files.copy(selectedFile.toPath(), new File(ResourceManager.getTempFolder().toString() + "/" + selectedFile.getName()).toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			model = XPlanungParser.getInstance().createModelFromFile(filename);
			XPlanungParser.getInstance().loadXPlanung(model);
			
			
		}
	}
	
}
