package de.rub.bi.inf.baclient.core.views.xbau.rendering;

import java.util.HashMap;

import de.xleitstelle.xbau.schema._2._1.NameNatuerlichePerson;
import de.xoev.schemata.code._1_0.Code;

public class Renderers {

	private HashMap<Class<?>, Renderer> renderers = new HashMap<>(); 
	
	private static Renderers instance;
	
	public static Renderers getInstance() {
		if(instance==null) {
			instance=new Renderers();
		}
		
		return instance;
	}
	
	private Renderers() {
		renderers.put(NameNatuerlichePerson.class, new NameNatuerlichePersonRenderer());
		renderers.put(Code.class, new CodeListRender());
	}
	
	
	public Renderer forClass(Class<?> cl) {
		return renderers.get(cl);
	}
	
}
