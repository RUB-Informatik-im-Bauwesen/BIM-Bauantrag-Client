package de.rub.bi.inf.baclient.workflow.extraktion;

import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcCountMeasure;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.Util;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc2x3;
import de.rub.bi.inf.baclient.core.utils.Util.Ifc4;

public class PO_ErforderlicheStellplaetze extends ExtraktionsVorgang {
	
	
	public PO_ErforderlicheStellplaetze() {
		this.name = "Erforderliche Stellplätze";
	}

	@Override
	public void perform(ApplicationModelNode applicationModelNode, XPlanungModel xPlanModel) {
		
		if(applicationModelNode != null) {
			
			
		} //endif applicationModelNode

	}

}
