package de.rub.bi.inf.baclient.core.views.bcf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import com.apstex.bcf.core.BcfComment;
import com.apstex.bcf.core.BcfModel;
import com.apstex.bcf.core.BcfZipParser;
import com.apstex.bcf.core.BcfZipWriter;
import com.apstex.gui.bcf.kernel.BcfKernel;
import com.apstex.gui.bcf.views.commentview.CommentsView;

import de.rub.bi.inf.baclient.core.utils.ResourceManager;

public class XBCFCommentsView extends CommentsView{
	
	private HashMap<String, BcfComment> commentMap;
	
	public XBCFCommentsView() {
		commentMap = new HashMap<>();
	}
	
	@Override
	public void commentsAdded(BcfComment[] comments) {
		super.commentsAdded(comments);
		
		for(BcfComment comment : comments) {
			String key = "(" + comment.getMarkup().getTopic().getTitle() + ")_" +comment.getCommentId();
			commentMap.put(key, comment);
			System.out.println("Created BCF Comment: " + key);
		}
	}
	
	public Collection<String> getBCFKeySet() {
		return commentMap.keySet();
	}
	
	public String[] getBCFKeys() {
		Set<String> keySet = commentMap.keySet();
		
		String[] items = new String[keySet.size()];
		keySet.toArray(items);
		
		return items;
	}
	
	public BcfComment getBCF(String key) {
		return commentMap.get(key);
	}
	
	public ArrayList<File> exportSeparated(String path) throws XMLStreamException, IOException {
		ArrayList<File> files = new ArrayList<>();

		BcfModel bcfModel = new BcfModel();
		
		for(String bcfKey : commentMap.keySet()) {
			BcfComment comment = commentMap.get(bcfKey);
			bcfModel.addComment(comment);
		}
		
		File f = new File(path + "/Abweichungen.bcfzip");
		f.delete();
		
		if(!bcfModel.isEmpty()) {
			BcfZipWriter bcfZipWriter = new BcfZipWriter();
			bcfZipWriter.writeBcfZipFile(new File(path + "/Abweichungen.bcfzip"), bcfModel);	
			
			System.out.println("File stored: " + path + "/Abweichungen.bcfzip");
			
			files.add(new File(path + "/Abweichungen.bcfzip"));
		}
		
		
		return files;
	}
	
	public void importFromFile(File file) {
		
		BcfZipParser parser = new BcfZipParser();
		try {
			parser.readBcfZipFile(file, BcfKernel.getBcfModel()); //need to be BcfKernel.getBcfModel()
		} catch (IOException | XMLStreamException | FactoryConfigurationError | ParseException e) {
			e.printStackTrace();
		}
		
	}
	
}
