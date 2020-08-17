package de.rub.bi.inf.baclient.core.geometry;

import java.awt.Color;
import java.util.ArrayList;
import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.PointArray;
import com.apstex.javax.vecmath.Color3f;

public class XPlanungCadObjectJ3D extends CadObjectJ3D {
	
	 @Override
	public void setSelected(boolean arg0) {
		if(arg0) {
			this.colorize(Color.ORANGE);
		}else {
			resetColorization();
		}
		super.setSelected(arg0);
	}
	
	
	
	@Override
	public void colorize(Color paramColor) {
		if (paramColor == null) {
			resetColorization();
		} else {
			this.colorizeColor = paramColor;
			for (int i = 0; i < this.solidSwitch.numChildren(); i++) {
				if ((this.solidSwitch.getChild(i) instanceof MultiAppearanceShape3D)) {
					Color3f color = new Color3f(paramColor);
					ColoringAttributes coloringAttributes = new ColoringAttributes(color, ColoringAttributes.NICEST);
					((MultiAppearanceShape3D)this.solidSwitch.getChild(i)).getAppearance().setColoringAttributes(coloringAttributes);
				
					//Colorize PointArray if existing
					if(((MultiAppearanceShape3D)this.solidSwitch.getChild(i)).getGeometry() instanceof PointArray) {
						((PointArray)((MultiAppearanceShape3D)this.solidSwitch.getChild(i)).getGeometry()).setColor(0, color);
					}
				
				}
			}
		}
	}
	
	@Override
	public void resetColorization() {
		this.colorizeColor = null;
		for (int i = 0; i < this.solidSwitch.numChildren(); i++) {
			if ((this.solidSwitch.getChild(i) instanceof MultiAppearanceShape3D)) {
				MultiAppearanceShape3D shape = (MultiAppearanceShape3D)this.solidSwitch.getChild(i);
				shape.getAppearance().setColoringAttributes(shape.getOriginalAppearance().getColoringAttributes());
				
				//reverse colorization of PointArray
				Color3f color = new Color3f();
				shape.getOriginalAppearance().getColoringAttributes().getColor(color);
				
				if(shape.getGeometry() instanceof PointArray) {
					((PointArray)shape.getGeometry()).setColor(0, color);
				}
			}
		}
	}
	
	public void setLineThickness(int thickness){
		for (int i = 0; i < this.solidSwitch.numChildren(); i++) {
			if ((this.solidSwitch.getChild(i) instanceof MultiAppearanceShape3D)) {
				MultiAppearanceShape3D shape = (MultiAppearanceShape3D)this.solidSwitch.getChild(i);
				if(shape.getAppearance().getLineAttributes() != null) {					
					shape.getAppearance().getLineAttributes().setLineWidth((float)thickness);
				}
			}
		}
	}
	
	public void setPointThickness(int thickness){
		for (int i = 0; i < this.solidSwitch.numChildren(); i++) {
			if ((this.solidSwitch.getChild(i) instanceof MultiAppearanceShape3D)) {
				MultiAppearanceShape3D shape = (MultiAppearanceShape3D)this.solidSwitch.getChild(i);
				if(shape.getAppearance().getPointAttributes() != null) {					
					shape.getAppearance().getPointAttributes().setPointSize((float)thickness);
				}
			}
		}
	}
	
	public ArrayList<MultiAppearanceShape3D> getInnerShape3ds() {
		ArrayList<MultiAppearanceShape3D> innerShapes = new ArrayList<>();
		for (int i = 0; i < this.solidSwitch.numChildren(); i++) {
			if ((this.solidSwitch.getChild(i) instanceof MultiAppearanceShape3D)) {
				MultiAppearanceShape3D shape = (MultiAppearanceShape3D)this.solidSwitch.getChild(i);
				innerShapes.add(shape);
			}
		}
		return innerShapes;
	}
}
