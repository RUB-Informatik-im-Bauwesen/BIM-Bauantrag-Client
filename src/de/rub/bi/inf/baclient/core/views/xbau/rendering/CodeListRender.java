package de.rub.bi.inf.baclient.core.views.xbau.rendering;

import de.xoev.schemata.code._1_0.Code;

public class CodeListRender implements Renderer {

	@Override
	public String render(Object o) {
		Code code = (Code) o;
		
		String retVal = "";
		retVal+=code.getCode()!=null?code.getCode():"";
		retVal+=" | ";
		retVal+=code.getName()!=null?code.getName():"";
		
		return retVal;
	}

}
