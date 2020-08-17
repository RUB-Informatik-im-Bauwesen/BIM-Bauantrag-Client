package de.rub.bi.inf.baclient.core.views.xbau.rendering;

import de.xleitstelle.xbau.schema._2._1.NameNatuerlichePerson;

public class NameNatuerlichePersonRenderer implements Renderer {

	
	
	@Override
	public String render(Object o) {
		NameNatuerlichePerson name = (NameNatuerlichePerson) o;
		String out="";
		
		if(name.getFamilienname() != null) {
			out+=name.getFamilienname().getName()+",";
		}

		if(name.getVorname() != null) {			
			out+=name.getVorname().getName()+"";
		}
		
		out+=name.getTitel(); 
		return out;
	}

}
