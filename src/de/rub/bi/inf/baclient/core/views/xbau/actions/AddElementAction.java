package de.rub.bi.inf.baclient.core.views.xbau.actions;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import de.rub.bi.inf.baclient.core.views.bcf.AbweichungBeantragtView;
import de.rub.bi.inf.baclient.core.views.xbau.BasicDocumentChoiceDialog;
import de.rub.bi.inf.baclient.core.views.xbau.param.Attribute;
import de.rub.bi.inf.baclient.core.views.xbau.param.BigIntegerElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.BooleanAttribute;
import de.rub.bi.inf.baclient.core.views.xbau.param.BooleanElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.Element;
import de.rub.bi.inf.baclient.core.views.xbau.param.FloatElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.ListStringItemElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.Parameter;
import de.rub.bi.inf.baclient.core.views.xbau.param.ShortElement;
import de.rub.bi.inf.baclient.core.views.xbau.param.StringAttribute;
import de.rub.bi.inf.baclient.core.views.xbau.param.StringElement;
import de.rub.bi.inf.baclient.workflow.extraktion.ArtDesGebaeudes;
import de.xleitstelle.xbau.schema._2._1.AbweichungBeantragt;
import de.xleitstelle.xbau.schema._2._1.BaugenehmigungAntrag0200;
import de.xleitstelle.xbau.schema._2._1.BaulicheNutzungMass;
import de.xleitstelle.xbau.schema._2._1.BauordnungsrechtlicheKlassifikation;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben;
import de.xleitstelle.xbau.schema._2._1.CodeBaulicheAnlagenGebaeude;
import de.xleitstelle.xbau.schema._2._1.CodeBaumassnahmeArt;
import de.xleitstelle.xbau.schema._2._1.CodeBauweise;
import de.xleitstelle.xbau.schema._2._1.CodeMboGebaeudeklasse;
import de.xleitstelle.xbau.schema._2._1.CodeXBauNachrichten;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben.Gegenstand;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben.Gegenstand.ArtDerBaulichenAnlage;
import de.xleitstelle.xbau.schema._2._1.Datenblatt;
import de.xleitstelle.xbau.schema._2._1.Entwurfsverfasser.Bauvorlageberechtigung;
import de.xleitstelle.xbau.schema._2._1.Grundstuecksflaechen;
import de.xleitstelle.xbau.schema._2._1.IdentifikationNachricht;
import de.xleitstelle.xbau.schema._2._1.Kennzahlen;
import de.xleitstelle.xbau.schema._2._1.NachrichtenkopfPrivat2G;
import de.xleitstelle.xbau.schema._2._1.Nutzungseinheiten;
import de.xleitstelle.xbau.schema._2._1.StaedtebaulicheKennzahlen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;

public class AddElementAction implements EventHandler<ActionEvent> {

	private TreeTableRow<Object> row;

	public AddElementAction(TreeTableRow<Object> row) {
		this.row = row;
	}

	@Override
	public void handle(ActionEvent event) {

		if (row.getItem() == null)
			return;

		Class<?> cl = null;
		if (row.getItem().equals("Root")) {
			BasicDocumentChoiceDialog dialog = new BasicDocumentChoiceDialog();

			Optional<Class<?>> result = dialog.showAndWait();

			if (result.isPresent()) {
				cl = result.get();

				
				if(cl.equals(BaugenehmigungAntrag0200.class)) {
					
					createBaugenehmigungAntrag0200(row.getTreeItem());
		        	
				}else {
					createInstanceFromClass(cl, null);
				}

				
			}
		} else if (row.getItem() instanceof Element) {
			Element element = (Element) row.getItem();
			cl = element.getType();

			
			if (element.getValue() instanceof Collection<?>) {
				Collection collection = (Collection) element.getValue();
				cl = element.hasParametrizedListType();
				Object instance = createInstanceFromClass(cl, row.getTreeItem());
				// System.out.println("INSTANCE: " + instance.getClass().getSimpleName());
				
				if(instance != null) {					
					collection.add(instance);
				}
			} else {
				createInstanceFromClass(cl, row.getTreeItem());
			}

		}

		row.getTreeTableView().refresh();
	}


	public static void createBaugenehmigungAntrag0200(TreeItem<Object> root) {	
		BaugenehmigungAntrag0200 antrag0200 = new BaugenehmigungAntrag0200();
		 
		NachrichtenkopfPrivat2G nachrichtenkopf = new NachrichtenkopfPrivat2G();
		antrag0200.setNachrichtenkopfPrivat2G(nachrichtenkopf);
		nachrichtenkopf.setIdentifikationNachricht(new IdentifikationNachricht());
		nachrichtenkopf.getIdentifikationNachricht().setNachrichtenUUID(UUID.randomUUID().toString());
		CodeXBauNachrichten codeXBauNachrichten = new CodeXBauNachrichten();
		codeXBauNachrichten.setCode("0200");
		codeXBauNachrichten.setName("baugenehmigung.antrag.0200");
		nachrichtenkopf.getIdentifikationNachricht().setNachrichtentyp(codeXBauNachrichten);
	
		XMLGregorianCalendar now = null;
		try {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
	        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (now != null) {
        	nachrichtenkopf.getIdentifikationNachricht().setErstellungszeitpunkt(now);
        }
		
		
		
		Bauvorhaben bauvorhaben = new Bauvorhaben();
		antrag0200.setBauvorhaben(bauvorhaben);
		
		antrag0200.getBaulastErklaerung();
		antrag0200.getAngefragteErleichterung();
		antrag0200.getBaulastErklaerung();
		
		Gegenstand gegenstand = new Gegenstand();
		bauvorhaben.setGegenstand(gegenstand);
		
		ArtDerBaulichenAnlage artDerBaulichenAnlage = new ArtDerBaulichenAnlage();
		ArtDesGebaeudes artDesGebaeudes = new ArtDesGebaeudes();
		artDerBaulichenAnlage.setArtDesGebaeudes(new CodeBaulicheAnlagenGebaeude());
		Datenblatt datenblatt = new Datenblatt();
		
		gegenstand.setArtDerMassnahme(new CodeBaumassnahmeArt());
		gegenstand.setArtDerBaulichenAnlage(artDerBaulichenAnlage);
		gegenstand.setArtDerBaulichenAnlage(artDerBaulichenAnlage);
		gegenstand.setBauvorhabenDatenblatt(datenblatt);
		
		BauordnungsrechtlicheKlassifikation bauordnungsrechtlicheKlassifikation = new BauordnungsrechtlicheKlassifikation();
		bauordnungsrechtlicheKlassifikation.setGebaeudeklasse(new CodeMboGebaeudeklasse());
		datenblatt.setBauordnungsrechtlicheKlassifikation(bauordnungsrechtlicheKlassifikation);
	
		datenblatt.setBauweise(new CodeBauweise());
		
		BaulicheNutzungMass baulicheNutzungMass = new BaulicheNutzungMass();
		datenblatt.getBaulicheNutzungMass().add(baulicheNutzungMass);
		Grundstuecksflaechen grundstuecksflaechen = new Grundstuecksflaechen();
		StaedtebaulicheKennzahlen staedtebaulicheKennzahlen = new StaedtebaulicheKennzahlen();
		Kennzahlen kennzahlen = new Kennzahlen();
		Nutzungseinheiten nutzungseinheiten = new Nutzungseinheiten();
		baulicheNutzungMass.setGrundstuecksflaechen(grundstuecksflaechen);
		baulicheNutzungMass.getStaedtebaulicheKennzahlen().add(staedtebaulicheKennzahlen);
		baulicheNutzungMass.getKennzahlen().add(kennzahlen);
		baulicheNutzungMass.setNutzungseinheiten(nutzungseinheiten);
		

		Object o = antrag0200;

		TreeItem<Object> newTreeItem = new TreeItem<>(o);
//    	
		root.getChildren().add(newTreeItem);
		root.setExpanded(true);

		AddElementAction.resolveParameters(o, newTreeItem);
		newTreeItem.setExpanded(true);
        
	}
	
	private Object createInstanceFromClass(Class<?> cl, TreeItem<Object> instanceTreeItem) {
		try {
			
			Object instance = null;
			
			//spezifische typen abfangen
			if(cl.equals(short.class) || cl.equals(Short.class)) {
				instance = new Short("0");
			}else if(cl.equals(float.class) || cl.equals(Float.class)) {
				instance = new Float("0");
			}else if(cl.equals(AbweichungBeantragt.class)) {
				AbweichungBeantragtView abweichungBeantragtView = new AbweichungBeantragtView();
				instance = abweichungBeantragtView.toAbweichungBeantragt();
			}else {
				instance = cl.newInstance();
			}
			
			if(instance == null) {
				return null;
			}

			if (instanceTreeItem == null) {
				instanceTreeItem = new TreeItem<>(instance);
				row.getTreeItem().getChildren().add(instanceTreeItem);
				row.getTreeItem().setExpanded(true); // expand parent
			}

			if (instanceTreeItem.getValue() instanceof Element) {
				// System.out.println(instance.getClass().getSimpleName());

				Element element = (Element) instanceTreeItem.getValue();

				if (element.getValue() instanceof Collection<?>) {
					
					List<String> li = (List<String>)element.getValue();
					
					if(instance instanceof String) {
						TreeItem<Object> listEntryTreeItem = new TreeItem<Object>(
								new ListStringItemElement("[" + li.size() + "]", li.size(), li)
						);
						instanceTreeItem.getChildren().add(listEntryTreeItem);
						instanceTreeItem = listEntryTreeItem;
					}else {
						TreeItem<Object> listEntryTreeItem = new TreeItem<Object>(instance);
						instanceTreeItem.getChildren().add(listEntryTreeItem);
						instanceTreeItem = listEntryTreeItem;
					}
				}

				if (element.getValue() == null || !(element.getValue() instanceof Collection<?>))
					element.setValue(instance);
				// System.out.println(element.getName());
			}

			// System.out.println(instance + "-" + instanceTreeItem);
			if(!(instance instanceof String)) {				
				resolveParameters(instance, instanceTreeItem);
			}

			instanceTreeItem.setExpanded(true);
			row.getTreeTableView().refresh();

			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static void resolveParameters(Object instance, TreeItem<Object> instanceTreeItem) {

		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(instance.getClass());
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		HashMap<String, PropertyDescriptor> propertyMap = new HashMap<>(propertyDescriptors.length);
		Arrays.stream(propertyDescriptors).forEach(p -> {
			propertyMap.put(p.getName(), p);
		});

		ArrayList<Parameter> parameterList = new ArrayList<>();
		// ArrayList<Entry<String, Class>> set = new ArrayList<>();
		getParameters(parameterList, instance.getClass());

		for (Parameter parameter : parameterList) {

			parameter.setInstance(instance);
			parameter.setPropertyDescriptor(propertyMap.get(parameter.getName()));
			parameter.update();

			TreeItem<Object> parameterTreeItem = new TreeItem<Object>(parameter);

			instanceTreeItem.getChildren().add(parameterTreeItem);

			Set<Class<?>> excluded = new HashSet<>();
			excluded.add(StringAttribute.class);
			excluded.add(StringElement.class);
			excluded.add(BooleanAttribute.class);
			excluded.add(BooleanElement.class);
			excluded.add(BigIntegerElement.class);
			excluded.add(ShortElement.class);
			excluded.add(FloatElement.class);
			excluded.add(ListStringItemElement.class);

			// already contains a value
			if (parameter.getPropertyDescriptor() != null && parameter.getValue() != null
					&& !excluded.contains(parameterTreeItem.getValue().getClass())) {

				if (parameter.getValue() instanceof Collection<?>) {

					Collection<?> collection = (Collection<?>) parameter.getValue();

					/*
					if (collection.size() > 0) {
						System.out.println(parameter.getValue());
					}
					*/

					int objIndex = 0;
					for (Object obj : collection) {
					//for (int objIndex = 0; objIndex < collection.size(); objIndex++) {
						//System.out.println("A: " + obj.getClass().getSimpleName());
						if(obj instanceof String) {
							TreeItem<Object> listEntryTreeItem = new TreeItem<Object>(
									new ListStringItemElement("[" + objIndex + "]", objIndex, (List<String>)collection)
							);
							parameterTreeItem.getChildren().add(listEntryTreeItem);
							objIndex++;
						}else {							
							TreeItem<Object> listEntryTreeItem = new TreeItem<Object>(obj);
							parameterTreeItem.getChildren().add(listEntryTreeItem);
							
							resolveParameters(obj, listEntryTreeItem);
						}

					}

				} else {
					//System.out.println("B: " + parameter.getValue().getClass().getSimpleName());
					resolveParameters(parameter.getValue(), parameterTreeItem);
				}
			}
		}
	}

	private static void getParameters(ArrayList<Parameter> parameters, Class<?> obj) {
		if (!(obj == Object.class)) {
			for (Field field : obj.getDeclaredFields()) {

				XmlAttribute xmlAttribute = field.getDeclaredAnnotation(XmlAttribute.class);

				// System.out.println(field.getName()+", "+field.getType().getSimpleName()+",
				// "+xmlAttribute);

				if (xmlAttribute != null) {
					Attribute attribute = null;
					if (field.getType().equals(String.class)) {
						attribute = new StringAttribute(xmlAttribute.name(), null, null);
					} else if (field.getType().equals(Boolean.class)) {
						attribute = new BooleanAttribute(xmlAttribute.name(), null, null);
					} else {
						//System.out.println("NOTHING: " + xmlAttribute.name());
						attribute = new Attribute(xmlAttribute.name(), null, null);
					}

					attribute.setRequired(xmlAttribute.required());
					parameters.add(attribute);
					continue;
				}
				
				/*
				if(obj.equals(String.class)) {
					Attribute attribute = new StringAttribute("Text", null, null);
					attribute.setRequired(true);
					parameters.add(attribute);
					continue;
				}*/

				XmlElement xmlElement = field.getDeclaredAnnotation(XmlElement.class);
				Element element = null;
				if (field.getType().equals(String.class)) {
					element = new StringElement(field.getName(), null, null);
				} else if (field.getType().equals(Boolean.class)) {
					element = new BooleanElement(field.getName(), null, null);
				} else if (field.getType().equals(BigInteger.class)) {
					element = new BigIntegerElement(field.getName(), null, null);
				} else if (field.getType().equals(short.class) || field.getType().equals(Short.class)) {
					element = new ShortElement(field.getName(), null, null);
				} else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
					element = new FloatElement(field.getName(), null, null);
				} else {
					//System.out.println("E NOTHING: " + field.getName() + "_" + field.getType() + "_" + obj.getSimpleName());
					element = new Element(field.getName(), null, null);
				}

				if (xmlElement != null) {
					element.setRequired(xmlElement.required());
				}
				parameters.add(element);

			}

			getParameters(parameters, obj.getSuperclass());

		}

	}

}
