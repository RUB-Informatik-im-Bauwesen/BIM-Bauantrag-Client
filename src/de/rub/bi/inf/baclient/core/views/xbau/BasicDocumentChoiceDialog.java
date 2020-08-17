package de.rub.bi.inf.baclient.core.views.xbau;

import java.util.ArrayList;

import de.xleitstelle.xbau.schema._2._1.*;
import javafx.scene.control.ChoiceDialog;

public class BasicDocumentChoiceDialog extends ChoiceDialog<Class<?>> {
	
	public BasicDocumentChoiceDialog() {	
		super(null, generateChoices());
		
	}
	
	public static boolean hasChoice(Class cls) {
		return generateChoices().contains(cls);
	}
	
	private static ArrayList<Class<?>> generateChoices(){
		
		ArrayList<Class<?>> choices = new ArrayList<>();
	
		choices.add(BaugenehmigungAntrag0200.class);
		choices.add(BaugenehmigungFormellePruefung0201.class);
		choices.add(BaugenehmigungAntragGeaendert0202.class);
		choices.add(BaugenehmigungAnhoerung0203.class);
		choices.add(BaugenehmigungStellungnahme0204.class);
		choices.add(BaugenehmigungBescheid0205.class);
		choices.add(BaugenehmigungGebuehrenbescheid0206.class);
	
		return choices;
	}
	
}
