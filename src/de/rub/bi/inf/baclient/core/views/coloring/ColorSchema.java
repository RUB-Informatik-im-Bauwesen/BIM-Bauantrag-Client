package de.rub.bi.inf.baclient.core.views.coloring;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.apstex.step.core.ClassInterface;

import javafx.scene.paint.Color;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ColorSchema")
public class ColorSchema {

	@XmlElement(name = "Titel", required = true)
	private String titel = null;

	@XmlElement(name = "Rules", required = true)
	private ArrayList<ColorRule> rules = null;

	public ColorSchema() {
		// DO NOTHING
	}

	public ColorSchema(String titel) {
		this.titel = titel;
		this.rules = new ArrayList<ColorRule>();
	}

	public void setRules(ArrayList<ColorRule> rules) {
		this.rules = rules;
	}

	public Color applyRules(ClassInterface ifcObj) {
		for (ColorRule rule : rules) {
			if (rule.isValid(ifcObj)) {
				return rule.getColor();
			}
		}
		return null;
	}

	public void addRule(ColorRule rule) {
		this.rules.add(rule);
	}

	public ArrayList<ColorRule> getRules() {
		return rules;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String toString() {
		return titel + "_(Rules: " + rules.size() + ")";
	}

}
