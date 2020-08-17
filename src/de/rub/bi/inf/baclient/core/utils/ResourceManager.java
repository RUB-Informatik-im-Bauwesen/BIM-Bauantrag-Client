package de.rub.bi.inf.baclient.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

public class ResourceManager {
	
	private static Path tempDirWithPrefix = null;
	
	public static Path createTempFolder(String path) throws IOException {
		tempDirWithPrefix = Files.createTempDirectory(path);
		return tempDirWithPrefix;
	}
	
	public static Path getTempFolder() throws IOException {
		if(tempDirWithPrefix == null) {
			return createTempFolder("temp");
		}
		return tempDirWithPrefix;
	}
	
	public static ImageIcon getIcon(String relativePath) {
		return new ImageIcon(ClassLoader.getSystemResource("resources/icon/"+relativePath));
	}
	
	public static InputStream getResource(String relativePath) {		
		return ClassLoader.getSystemResourceAsStream("resources/"+relativePath);
	}
}
