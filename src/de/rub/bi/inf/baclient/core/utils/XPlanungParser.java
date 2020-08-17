package de.rub.bi.inf.baclient.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;

import de.rub.bi.inf.baclient.core.actions.CadSelectionColorizeMouseListener;
import de.rub.bi.inf.baclient.core.actions.XPlanungSelectionModel;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadLoader;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xplanung.browseFX.XPlanungProjectExplorerViewFX;
import net.opengis.gml._3.AbstractFeatureCollectionType;
import net.opengis.gml._3.FeaturePropertyType;

public class XPlanungParser {
	
	private final static String TESTFILE = "resources/xplanung/BP2070.gml";

	private Listener unmarshallListener;
	private static XPlanungParser self;
	
	private XPlanungParser(Listener listener) {
		this.unmarshallListener = listener;
	}
	
	public static XPlanungParser getInstance() {
		if(self == null) {
			self = new XPlanungParser(new Listener() {
				
				@Override
				public void afterUnmarshal(Object target, Object parent) {
					super.afterUnmarshal(target, parent);
				}
				
			});
		}
		return self;
	}
	
	public XPlanungModel createModel() {
		// try connecting XPlanung-JAXB-Binding project
		XPlanungModel unmarshalled_objectJ3D = null;
		try {
			unmarshalled_objectJ3D = unmarshall(TESTFILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unmarshalled_objectJ3D;
	}
	
	// === "real application" === ("Load File...")
	public XPlanungModel createModelFromFile(String filename) {
		// try connecting XPlanung-JAXB-Binding project
		XPlanungModel unmarshalled_objectJ3D = null;
		try {
			if (filename != null) {				
				unmarshalled_objectJ3D = unmarshall(filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unmarshalled_objectJ3D;
	}
	
	public XPlanungModel createModelFromFile(InputStream stream) {
		return createModelFromFile("xplan.gml", stream);
	}

	public XPlanungModel createModelFromFile(String modelname, InputStream stream) {
		// try connecting XPlanung-JAXB-Binding project
		XPlanungModel unmarshalled_objectJ3D = null;
		try {
			if (stream != null) {				
				unmarshalled_objectJ3D = unmarshall(modelname, stream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unmarshalled_objectJ3D;
	}
	
	private XPlanungModel unmarshall(String filename) throws Exception {
		
		System.out.println("Unmarshalling started.");

		InputStream fis = filename.equals(TESTFILE)? 
				ClassLoader.getSystemClassLoader().getResourceAsStream(filename)
				:new FileInputStream(filename);
	   // XMLInputFactory.newInstance().createXMLEventReader(fis);
		
	    JAXBContext context = null; 

	    String versionInfo = "none";
	    
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance(); 
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(fis);
		while (xmlStreamReader.hasNext()) { 
		   
		   switch (xmlStreamReader.getEventType()) {
		   case XMLStreamConstants.START_ELEMENT:
				if(xmlStreamReader.getLocalName().contains("XPlanAuszug")){
					for ( int i = 0; i < xmlStreamReader.getAttributeCount(); i++ ) {
						
					    if(xmlStreamReader.getAttributeLocalName(i).equals("schemaLocation")) {
					    	String schemaLocation = xmlStreamReader.getAttributeValue(i);
					    	if (schemaLocation.contains("/5/1")){
					    		context = JAXBContext.newInstance("de.xplanung.xplangml._5._1");
					    		System.out.println("Using de.xplanung.xplangml._5._1");
					    		versionInfo = "de.xplanung.xplangml._5._1";
					    	}
					    	else
					    	if (schemaLocation.contains("/5/")){
					    		context = JAXBContext.newInstance("de.xplanung.xplangml._5._0");
					    		System.out.println("Using de.xplanung.xplangml._5._0");
					    		versionInfo = "Using de.xplanung.xplangml._5._0";
					    	}
					    	else if (schemaLocation.contains("/4/")) {
					    		context = JAXBContext.newInstance("de.xplanung.xplangml._4._1");
					    		System.out.println("Using de.xplanung.xplangml._4._1");
					    		versionInfo = "Using de.xplanung.xplangml._4._1";
					    	}
					    }
						
					}

				}
				break;

			default:
				break;
			}
		   
		   xmlStreamReader.next();
		}
		
		
		if(context == null)
			throw new Exception("Cannot determine the Version of XPlanung File! Please ensure a valid schemaLocation attribute");
		

		Unmarshaller um = context.createUnmarshaller();
		
		if(unmarshallListener!=null)
			um.setListener(unmarshallListener);

		fis = filename.equals(TESTFILE)? 
				ClassLoader.getSystemClassLoader().getResourceAsStream(filename)
				:new FileInputStream(filename);
				
		XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(fis);
        Object o = um.unmarshal(reader);

		JAXBElement<AbstractFeatureCollectionType> root = null;
		if (o instanceof JAXBElement<?>) {
			root = (JAXBElement<AbstractFeatureCollectionType>) o;
		} else {
			throw new Exception("Root Object is not castable to JAXBElement<XPlanAuszugType>");
		}
		AbstractFeatureCollectionType xpaType = root.getValue();

		// process content tree
		List<FeaturePropertyType> members = xpaType.getFeatureMember();
		

		//Create model container
		XPlanungModel model = new XPlanungModel(
				Paths.get(filename).getFileName().toString(),
				root.getValue()
		);
		
		model.setVersionInfo(versionInfo);
		
		
		System.out.println("Unmarshalling executed successfully.");
		return model;
	}
	
	private XPlanungModel unmarshall(InputStream stream) throws Exception {
		return unmarshall("xplan.gml", stream);
	}
	
	private XPlanungModel unmarshall(String modelName, InputStream stream) throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while ((n = stream.read(buf)) >= 0)
		    baos.write(buf, 0, n);
		byte[] content = baos.toByteArray();
		
		InputStream is1 = new ByteArrayInputStream(content);
		
		System.out.println("Unmarshalling started.");
	   // XMLInputFactory.newInstance().createXMLEventReader(fis);
		
	    JAXBContext context = null; 

	    String versionInfo = "none";
	    
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance(); 
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(is1);
		while (xmlStreamReader.hasNext()) { 
		   
		   switch (xmlStreamReader.getEventType()) {
		   case XMLStreamConstants.START_ELEMENT:
				if(xmlStreamReader.getLocalName().contains("XPlanAuszug")){
					for ( int i = 0; i < xmlStreamReader.getAttributeCount(); i++ ) {
						
					    if(xmlStreamReader.getAttributeLocalName(i).equals("schemaLocation")) {
					    	String schemaLocation = xmlStreamReader.getAttributeValue(i);
					    	if (schemaLocation.contains("/5/1")){
					    		context = JAXBContext.newInstance("de.xplanung.xplangml._5._1");
					    		System.out.println("Using de.xplanung.xplangml._5._1");
					    		versionInfo = "de.xplanung.xplangml._5._1";
					    	}
				    		else if (schemaLocation.contains("/5/0")){
					    		context = JAXBContext.newInstance("de.xplanung.xplangml._5._0");
					    		System.out.println("Using de.xplanung.xplangml._5._0");
					    		versionInfo = "de.xplanung.xplangml._5._0";
					    	}
					    	else if (schemaLocation.contains("/4/")) {
					    		context = JAXBContext.newInstance("de.xplanung.xplangml._4._1");
					    		System.out.println("Using de.xplanung.xplangml._4._1");
					    		versionInfo = "de.xplanung.xplangml._4._1";
					    	}
					    }
					    
					}

				}
				break;

			default:
				break;
			}
		   
		   xmlStreamReader.next();
		}
		
		
		if(context == null)
			throw new Exception("Cannot determine the Version of XPlanung File! Please ensure a valid schemaLocation attribute");
		

		Unmarshaller um = context.createUnmarshaller();
		
		if(unmarshallListener!=null)
			um.setListener(unmarshallListener);

		InputStream is2 = new ByteArrayInputStream(content);
				
		XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(is2);
        Object o = um.unmarshal(reader);

		JAXBElement<AbstractFeatureCollectionType> root = null;
		if (o instanceof JAXBElement<?>) {
			root = (JAXBElement<AbstractFeatureCollectionType>) o;
		} else {
			throw new Exception("Root Object is not castable to JAXBElement<XPlanAuszugType>");
		}
		AbstractFeatureCollectionType xpaType = root.getValue();

		// process content tree
		List<FeaturePropertyType> members = xpaType.getFeatureMember();
		

		//Create model container
		XPlanungModel model = new XPlanungModel(
				modelName,
				root.getValue()
		);
		
		model.setVersionInfo(versionInfo);
		
		System.out.println("Unmarshalling executed successfully.");
		return model;
	}
	
	private boolean marshall(String filename, XPlanungModel model) throws Exception {
		
		//System.out.println("marshall for XPlanung not jet implemented (see de.rub.bi.inf.baclient.core.utils.XPlanungParser)");
		System.out.println("marshalling started.");
		
		Object value = model.getFeatureCollectionType();

		JAXBContext context;
		try {
			context = JAXBContext.newInstance("de.xplanung.xplangml._4._1");

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			OutputStream outputStream = new FileOutputStream(filename);
			// OutputStream outputStream = new FileOutputStream("testing" + i + ".xml");
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLEventWriter xmlEventWriter = outputFactory.createXMLEventWriter(outputStream);

			marshaller.marshal(value, xmlEventWriter);

		} catch (JAXBException | FileNotFoundException | XMLStreamException e2) {
			e2.printStackTrace();
			return false;
		}

		System.out.println("marshalling executed successfully.");		
		return true;
	}
	
	public boolean writeFileFromModel(String filename, XPlanungModel model) {
		boolean flag = false;
		try {
			if (filename != null) {				
				flag = marshall(filename, model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	

	public void loadXPlanung(XPlanungModel model) {

		Ifc3DViewJ3D view3d = XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d();

		// ===== ADD SELECTION LISTENER ========
		// Create and register listener to the viewer
		XPlanungCadLoader cadLoader = new XPlanungCadLoader();
		cadLoader.setSelectionModel(new XPlanungSelectionModel(view3d));
		CadSelectionColorizeMouseListener selectionMouseListener2 = new CadSelectionColorizeMouseListener(
				view3d, model);					
		
		cadLoader.getSelectionModel().addListener(selectionMouseListener2);

		//interpret features and create cad geometry data, that will be stored into model
		cadLoader.interpretGeometry(model);
		cadLoader.loadIntoViewer(model, XViewer.getInstance().getViewerPanel().getModelviewer().getWidget3d());

		
		XPlanungModelContainer.getInstance().getModels().add(model);

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				//XPlanungProjectExplorerView explorerView = XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerView();
				//explorerView.refresh();
				
				XPlanungProjectExplorerViewFX explorerViewFX = XViewer.getInstance().getViewerPanel().getxPlanungProjectExplorerViewFX();
				explorerViewFX.rebuild();
				explorerViewFX.refresh();
			}
		});
	}

}
