package de.rub.bi.inf.baclient.core.views.extraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.core.views.xbau.actions.AddElementAction;
import de.rub.bi.inf.baclient.workflow.extraktion.ArtDerMassnahme;
import de.rub.bi.inf.baclient.workflow.extraktion.ArtDesGebaeudes;
import de.rub.bi.inf.baclient.workflow.extraktion.Bauweise;
import de.rub.bi.inf.baclient.workflow.extraktion.BezeichnungDesBauvorhabens;
import de.rub.bi.inf.baclient.workflow.extraktion.ExtraktionsVorgang;
import de.rub.bi.inf.baclient.workflow.extraktion.Gebaeudeklasse;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_AnzahlVollgeschosse;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Baumasse;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Baumassenzahl;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_BebauteGrundstuecksflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_FlaecheDerGemeinschaftsanlagen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_FlaecheDerNebenanlagen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_GeplanteStellplaetze;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Geschossflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Geschossflaechenzahl;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_GrundflaechenDerBaulichenAnlage;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Grundflaechenzahl;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_HoeheDerBaulichenAnlage;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_NichtBebauteGrundstuecksflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_NutzungseinheitenGewerbe;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_SpielUndFreizeitflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_StellplaetzeUndDerenZufahrten;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_Verkaufsflaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_VersiegelteFlaeche;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenEigentumswohnungen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenGesamt;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenMietwohnungen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohneinheitenSozialwohnungen;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohnungenFreiberuflich;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_WohnungenGewerblich;
import de.rub.bi.inf.baclient.workflow.extraktion.Sonderbau;
import de.rub.bi.inf.xbau.io.CodeListGenericode;
import de.rub.bi.inf.xbau.io.CodeListReader;
import de.rub.bi.inf.xbau.io.CodeListXSD;
import de.xleitstelle.xbau.schema._2._1.BaugenehmigungAntrag0200;
import de.xleitstelle.xbau.schema._2._1.BaulicheNutzungMass;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben;
import de.xleitstelle.xbau.schema._2._1.CodeBaulicheAnlagenGebaeude;
import de.xleitstelle.xbau.schema._2._1.Datenblatt;
import de.xleitstelle.xbau.schema._2._1.Grundstuecksflaechen;
import de.xleitstelle.xbau.schema._2._1.Kennzahlen;
import de.xleitstelle.xbau.schema._2._1.Nutzungseinheiten;
import de.xleitstelle.xbau.schema._2._1.StaedtebaulicheKennzahlen;
import de.xoev.schemata.code._1_0.Code;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben.Gegenstand;
import de.xleitstelle.xbau.schema._2._1.Bauvorhaben.Gegenstand.ArtDerBaulichenAnlage;
import de.xleitstelle.xbau.schema._2._1.CodeBaumassnahmeArt;
import de.xleitstelle.xbau.schema._2._1.CodeBauweise;
import de.xleitstelle.xbau.schema._2._1.CodeMboGebaeudeklasse;
import de.xleitstelle.xbau.schema._2._1.CodeSonderbauten;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.util.Pair;

public class DatenextraktionTransferMethods {

	Bauvorhaben bauvorhaben;
	private Gegenstand g;
	private BaulicheNutzungMass nm;
	
	private TreeItem rootItem;
//	private BaugenehmigungAntrag0200 antrag0200;

	public DatenextraktionTransferMethods() throws Exception {
		rootItem = XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().getRoot();
//		antrag0200 = (BaugenehmigungAntrag0200) rootItem.getValue();

		for (Object item : rootItem.getChildren()) {
			if (item instanceof TreeItem) {

				BaulicheNutzungMass temp = checkPolicy((TreeItem) item);
				if (temp != null) {
					nm = temp;
				}

			}

		}

	}

	private BaulicheNutzungMass checkPolicy(TreeItem rootItem) throws Exception {

		if (rootItem.getValue() instanceof BaugenehmigungAntrag0200) {
			BaugenehmigungAntrag0200 ba200 = (BaugenehmigungAntrag0200) rootItem.getValue();
			
			
			
			bauvorhaben = ba200.getBauvorhaben();
			if (bauvorhaben == null) {
				throw new Exception(
						"(Kein Bauvorhaben) Die Policy fuer die BaulichenNutzungsMasse wurden nicht erfuellt. Siehe Doc.");
			}

			g = bauvorhaben.getGegenstand();
			if (g == null) {
				throw new Exception(
						"(Kein Gegenstand) Die Policy fuer die BaulichenNutzungsMasse wurden nicht erfuellt. Siehe Doc.");
			}

			Datenblatt bdb = g.getBauvorhabenDatenblatt();
			if (bdb == null) {
				throw new Exception(
						"(Kein Datenblatt) Die Policy fuer die BaulichenNutzungsMasse wurden nicht erfuellt. Siehe Doc.");
			}

			if (bdb.getBauordnungsrechtlicheKlassifikation() == null) {
				throw new Exception(
						"(Kein BauordnungsrechtlicheKlassifikation) Die Policy fuer die BauordnungsrechtlicheKlassifikation wurden nicht erfuellt. Siehe Doc.");
			}

			if (bdb.getBauweise() == null) {
				throw new Exception(
						"(Kein Bauweise) Die Policy fuer die Bauweise wurden nicht erfuellt. Siehe Doc.");
			}
			
			List<BaulicheNutzungMass> bnList = bdb.getBaulicheNutzungMass();
			if (bnList == null) {
				throw new Exception(
						"(Kein List<BaulicheNutzungMass>) Die Policy fuer die BaulichenNutzungsMasse wurden nicht erfuellt. Siehe Doc.");
			}

			return bnList.get(0);

		}

		return null;
	}

	public void transfer(ExtraktionsVorgang puefObject) {

		if (puefObject.getValue().isEmpty()) {
			System.err.println("Value for " + puefObject.getName() + " is empty.");
			return;
		}

		StaedtebaulicheKennzahlen staedtebaulicheKennzahlen = nm.getStaedtebaulicheKennzahlen().get(0);
		if(staedtebaulicheKennzahlen == null) {
			staedtebaulicheKennzahlen = new StaedtebaulicheKennzahlen();
			nm.getStaedtebaulicheKennzahlen().add(staedtebaulicheKennzahlen);
		}
		
		Kennzahlen kennzahlen = nm.getKennzahlen().get(0);
		if(kennzahlen == null) {
			kennzahlen = new Kennzahlen();
			nm.getKennzahlen().add(kennzahlen);
		}
		
		Grundstuecksflaechen grundstuecksflaechen = nm.getGrundstuecksflaechen();
		if (grundstuecksflaechen == null) {
			grundstuecksflaechen = new Grundstuecksflaechen();
			nm.setGrundstuecksflaechen(grundstuecksflaechen);
		}
		
		Nutzungseinheiten nutzungseinheit = nm.getNutzungseinheiten();
		if (nutzungseinheit == null) {
			nutzungseinheit = new Nutzungseinheiten();
			nm.setNutzungseinheiten(nutzungseinheit);
		}
		
		
//		CodeListMapper codeListMapper = new CodeListMapper();
		if (puefObject instanceof ArtDerMassnahme) {
			CodeBaumassnahmeArt code = g.getArtDerMassnahme();
			
			int token = ((ArtDerMassnahme)puefObject).getArtDerMassnahme();
			code.setCode(Integer.toString(token));
			
			CodeListXSD codeList = (CodeListXSD) CodeListReader.getInstance().getCodeList("Codelist.Baumassnahme");
			if(codeList!=null) {
				String wert = codeList.getWerte().get(token);
				if(wert != null) {
					code.setName(wert);
				}
			}
			

		} else if (puefObject instanceof ArtDesGebaeudes) {
			
			ArtDerBaulichenAnlage artDerBaulichenAnlage = g.getArtDerBaulichenAnlage();
			CodeBaulicheAnlagenGebaeude code = artDerBaulichenAnlage.getArtDesGebaeudes();
			
			int token = ((ArtDesGebaeudes)puefObject).getArtDesGebaeudes();
			code.setCode(Integer.toString(token));
			
			CodeListGenericode codeList = (CodeListGenericode) CodeListReader.getInstance().getCodeList("BMK.Bauwerkszuordnungskatalog");
			if(codeList!=null) {
				String wert = codeList.getBezeichnung().get(token);
				if(wert != null) {
					code.setName(wert);
				}
			}
			
			
		} else if (puefObject instanceof Bauweise) {

			CodeBauweise code = g.getBauvorhabenDatenblatt().getBauweise();
			
			int token = ((Bauweise)puefObject).getBauweise();
			code.setCode(Integer.toString(token));
			
			CodeListXSD codeList = (CodeListXSD) CodeListReader.getInstance().getCodeList("Codelist.Bauweise");
			if(codeList!=null) {
				String wert = codeList.getWerte().get(token);
				if(wert != null) {
					code.setName(wert);
				}
			}
			
		} else if (puefObject instanceof Gebaeudeklasse) {

			CodeMboGebaeudeklasse code = g.getBauvorhabenDatenblatt().getBauordnungsrechtlicheKlassifikation().getGebaeudeklasse();
			
			int token = ((Gebaeudeklasse)puefObject).getGebaeudeklasse();
			code.setCode(Integer.toString(token));
			
			CodeListXSD codeList = (CodeListXSD) CodeListReader.getInstance().getCodeList("Codelist.MboGebaeudeklasse");
			if(codeList!=null) {
				String wert = codeList.getWerte().get(token);
				if(wert != null) {
					code.setName(wert);
				}
			}
		} else if (puefObject instanceof Sonderbau) {

			List<CodeSonderbauten> codes = g.getBauvorhabenDatenblatt().getBauordnungsrechtlicheKlassifikation().getSonderbau();
			
			int token = ((Sonderbau)puefObject).getSonderbau();
			
			CodeSonderbauten code = new CodeSonderbauten();
			codes.add(code);
			code.setCode(Integer.toString(token));
			
			CodeListXSD codeList = (CodeListXSD) CodeListReader.getInstance().getCodeList("Codelist.Sonderbauten");
			if(codeList!=null) {
				String wert = codeList.getWerte().get(token);
				if(wert != null) {
					code.setName(wert);
				}
			}
		}
			
		if (puefObject instanceof PO_HoeheDerBaulichenAnlage) {
			nm.setHoeheDerBaulichenAnlage(new Float(((PO_HoeheDerBaulichenAnlage) puefObject).getHoeheDerBaulichenAnlage()));
		} else if (puefObject instanceof PO_AnzahlVollgeschosse) {
			nm.setAnzahlVollgeschosse(new BigInteger(Integer.toString(((PO_AnzahlVollgeschosse) puefObject).getAnz_Vollgeschosse())));
		} else if (puefObject instanceof PO_GrundflaechenDerBaulichenAnlage) {
			nm.setGrundflaechenDerBaulichenAnlage(new Float(((PO_GrundflaechenDerBaulichenAnlage) puefObject).getArea()));
		} else if (puefObject instanceof PO_Geschossflaeche) {
			nm.setGeschossflaeche(new Float(((PO_Geschossflaeche) puefObject).getGeschossflaeche()));
		} else if (puefObject instanceof PO_Baumasse) {
			nm.setBaumasse(new Float(((PO_Baumasse) puefObject).getBaumasse()));
		} else if (puefObject instanceof PO_Verkaufsflaeche) {
			// TODO
		} else if (puefObject instanceof PO_BebauteGrundstuecksflaeche) {
			grundstuecksflaechen
			.setBebauteGrundstuecksflaeche(new Float(((PO_BebauteGrundstuecksflaeche) puefObject).getArea()));
		} else if (puefObject instanceof PO_NichtBebauteGrundstuecksflaeche) {
			grundstuecksflaechen
			.setNichtBebauteGrundstuecksflaeche(new Float(((PO_NichtBebauteGrundstuecksflaeche) puefObject).getArea()));
		} else if (puefObject instanceof PO_VersiegelteFlaeche) {
			grundstuecksflaechen
			.setVersiegelteFlaeche((new Float(((PO_VersiegelteFlaeche)puefObject).getArea())));
		} else if (puefObject instanceof PO_SpielUndFreizeitflaeche) {
			grundstuecksflaechen
			.setSpielUndFreizeitflaeche((new Float(((PO_SpielUndFreizeitflaeche) puefObject).getArea())));
		} else if (puefObject instanceof PO_FlaecheDerNebenanlagen) {
			grundstuecksflaechen
			.setFlaecheDerNebenanlagen((new Float(((PO_FlaecheDerNebenanlagen) puefObject).getArea())));
		} else if (puefObject instanceof PO_FlaecheDerGemeinschaftsanlagen) {
			grundstuecksflaechen
			.setFlaecheDerGemeinschaftsanlagen((new Float(((PO_FlaecheDerGemeinschaftsanlagen) puefObject).getArea())));
		} else if (puefObject instanceof PO_StellplaetzeUndDerenZufahrten) {
			grundstuecksflaechen
			.setStellplaetzeUndDerenZufahrten((new Float(((PO_StellplaetzeUndDerenZufahrten) puefObject).getArea())));
		} else if (puefObject instanceof PO_VersiegelteFlaeche) {
			grundstuecksflaechen
			.setVersiegelteFlaeche((new Float(((PO_VersiegelteFlaeche) puefObject).getArea())));
		} else if (puefObject instanceof PO_GeplanteStellplaetze) {
			kennzahlen
			.setGeplanteStellplaetze((short)(((PO_GeplanteStellplaetze)puefObject).getFahrradStellplaetze() + ((PO_GeplanteStellplaetze)puefObject).getPkwStellplaetze()));
		} else if (puefObject instanceof PO_Geschossflaechenzahl) {
			staedtebaulicheKennzahlen
			.setGeschossflaechenzahl((new Float(((PO_Geschossflaechenzahl) puefObject).getRatio())));
		} else if (puefObject instanceof PO_Grundflaechenzahl) {
			staedtebaulicheKennzahlen
			.setGrundflaechenzahl((new Float(((PO_Grundflaechenzahl) puefObject).getRatio())));
		} else if (puefObject instanceof PO_Baumassenzahl) {
			staedtebaulicheKennzahlen
			.setBaumassenzahl((new Float(((PO_Baumassenzahl) puefObject).getRatio())));
		} else if (puefObject instanceof PO_WohneinheitenGesamt) {
			nutzungseinheit
			.setWohneinheitenGesamt((new BigInteger(new Integer(((PO_WohneinheitenGesamt)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof PO_WohneinheitenEigentumswohnungen) {
			nutzungseinheit
			.setWohneinheitenEigentumswohnungen((new BigInteger(new Integer(((PO_WohneinheitenEigentumswohnungen)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof PO_WohneinheitenMietwohnungen) {
			nutzungseinheit
			.setWohneinheitenMietwohnungen((new BigInteger(new Integer(((PO_WohneinheitenMietwohnungen)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof PO_WohneinheitenSozialwohnungen) {
			nutzungseinheit
			.setWohneinheitenSozialwohnungen((new BigInteger(new Integer(((PO_WohneinheitenSozialwohnungen)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof PO_WohnungenFreiberuflich) {
			nutzungseinheit
			.setWohnungenFreiberuflich((new BigInteger(new Integer(((PO_WohnungenFreiberuflich)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof PO_WohnungenGewerblich) {
			nutzungseinheit
			.setWohnungenGewerblich((new BigInteger(new Integer(((PO_WohnungenGewerblich)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof PO_NutzungseinheitenGewerbe) {
			nutzungseinheit
			.setNutzungseinheitenGewerbe((new BigInteger(new Integer(((PO_NutzungseinheitenGewerbe)puefObject).getNutzungseinheiten().keySet().size()).toString())));
		} else if (puefObject instanceof BezeichnungDesBauvorhabens) {
			bauvorhaben.setBezeichnungDesBauvorhabens(((BezeichnungDesBauvorhabens)puefObject).getValue());
		}
		
		
		
//		switch (puefObject.getName().toLowerCase()) {
//		case "anzahlvollgeschosse": nm.setAnzahlVollgeschosse(new BigInteger(puefObject.getValue())); break;
//		case "baumasse": nm.setBaumasse(new Float(puefObject.getValue()).floatValue()); break;
//		case "geschossflaeche": nm.setGeschossflaeche(new Float(puefObject.getValue()).floatValue()); break;
//		case "gundfleachenderbaulichenanlage": nm.setGrundflaechenDerBaulichenAnlage(new Float(puefObject.getValue()).floatValue()); break;
//		//case "grundstuecksflaechen": nm.setGrundstuecksflaechen( ? ); break;
//		case "hoehederbaulichenanlage": nm.setHoeheDerBaulichenAnlage(new Float(((PO_HoeheDerBaulichenAnlage)puefObject).getHoeheDerBaulichenAnlage())); break;
//		//case "nutzungseinheit": nm.setNutzungseinheiten( ? ); break;
//		case "verkaufsflaeche": nm.setVerkaufsflaeche(new Float(puefObject.getValue()).floatValue()); break;
//		default: System.err.println("Unbekantes Attribut uebertragen: " + puefObject.getName()); break;
//		}

		
		//TODO Recreate tree
		XViewer.getInstance().getViewerPanel().getXbauExplorer().getTreeTableView().refresh();

	}

}
