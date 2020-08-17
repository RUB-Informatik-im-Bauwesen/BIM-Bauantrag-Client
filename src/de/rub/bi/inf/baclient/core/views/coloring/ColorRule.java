package de.rub.bi.inf.baclient.core.views.coloring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.apstex.ifctoolbox.ifc.IfcProduct;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertyEnumeratedValue;
import com.apstex.ifctoolbox.ifc.IfcPropertyListValue;
import com.apstex.ifctoolbox.ifc.IfcPropertyReferenceValue;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcPropertyTableValue;
import com.apstex.ifctoolbox.ifc.IfcRelDefines;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.views.coloring.CreateColorRuleFrame.OPERATOR;
import javafx.scene.paint.Color;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ColorRule")
public class ColorRule {

	@XmlElement(name = "Titel", required = true)
	private String titel = "";

	@XmlElement(name = "PropertySetName", required = false)
	private String propertySetName = null;

	@XmlElement(name = "PropertyName", required = false)
	private String propertyName = null;

	@XmlElement(name = "Value", required = false)
	private Object value = null;
	
	@XmlElement(name = "Operator", required = false)
	private String operator = null;

	@XmlElement(name = "ColorRed", required = true)
	private double colorRed = 0.0;

	@XmlElement(name = "ColorGreen", required = true)
	private double colorGreen = 0.0;

	@XmlElement(name = "ColorBlue", required = true)
	private double colorBlue = 0.0;

	@XmlElement(name = "ColorAlpha", required = true)
	private double colorAlpha = 0.0;

	public ColorRule() {
		// DO NOTHING
	}

	public ColorRule(String titel, String propertySetName, String propertyName, OPERATOR operator, Object value, Color color) {
		this.titel = titel;
		this.propertySetName = propertySetName;
		this.propertyName = propertyName;
		this.value = value;
		this.colorRed = color.getRed();
		this.colorGreen = color.getGreen();
		this.colorBlue = color.getBlue();
		this.colorAlpha = color.getOpacity();
		this.operator = operator.name();
	}

	public boolean isValid(ClassInterface ifcObj) {

		if (ifcObj == null) {
			return false;
		}
		
		if (propertySetName == null) {
			return true;
		}

		if (ifcObj instanceof IfcProduct.Ifc2x3) {

			IfcProduct.Ifc2x3 product = (IfcProduct.Ifc2x3) ifcObj;

			if(product.getIsDefinedBy_Inverse() == null) {
				return false;	
			}

			for (IfcRelDefines.Ifc2x3 relDefines : product.getIsDefinedBy_Inverse()) {
				if (relDefines instanceof IfcRelDefinesByProperties.Ifc2x3) {
					IfcRelDefinesByProperties.Ifc2x3 relDefinesByProperties = (IfcRelDefinesByProperties.Ifc2x3) relDefines;

					if (relDefinesByProperties.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc2x3) {
						IfcPropertySet.Ifc2x3 propertySet = (IfcPropertySet.Ifc2x3) relDefinesByProperties
								.getRelatingPropertyDefinition();

						if (propertySet.getName().getDecodedValue().toLowerCase().equals(propertySetName)) {
							for (IfcProperty.Ifc2x3 prop : propertySet.getHasProperties()) {

								if (propertyName == null) {
									return true;
								}

								if (prop.getName().getDecodedValue().toLowerCase().equals(propertyName)) {

									OPERATOR currentOperator = OPERATOR.valueOf(operator);
									String ifcValue = null;
									
									if(prop instanceof IfcPropertySingleValue.Ifc2x3) {
										IfcPropertySingleValue.Ifc2x3 propValue = (IfcPropertySingleValue.Ifc2x3) prop;
										if(propValue.getNominalValue() != null) {											
											ifcValue = propValue.getNominalValue().toString();
										}
									}else if(prop instanceof IfcPropertyEnumeratedValue.Ifc2x3) {
										IfcPropertyEnumeratedValue.Ifc2x3 propValue = (IfcPropertyEnumeratedValue.Ifc2x3) prop;
										if(propValue.getEnumerationValues() != null) {											
											ifcValue = propValue.getEnumerationValues().toString();
											
											if(currentOperator.equals(OPERATOR.GLEICH) || currentOperator.equals(OPERATOR.ENTHALTEN)) {
												currentOperator = OPERATOR.ENTHALTEN;
											}else {
												currentOperator = OPERATOR.NICHT_ENTHALTEN;
											}
										}
									}else if(prop instanceof IfcPropertyListValue.Ifc2x3) {
										IfcPropertyListValue.Ifc2x3 propValue = (IfcPropertyListValue.Ifc2x3) prop;
										if(propValue.getListValues() != null) {											
											ifcValue = propValue.getListValues().toString();
											
											if(currentOperator.equals(OPERATOR.GLEICH) || currentOperator.equals(OPERATOR.ENTHALTEN)) {
												currentOperator = OPERATOR.ENTHALTEN;
											}else {
												currentOperator = OPERATOR.NICHT_ENTHALTEN;
											}
										}
									}else if(prop instanceof IfcPropertyReferenceValue.Ifc2x3) {
										//TODO
									}else if(prop instanceof IfcPropertyTableValue.Ifc2x3) {
										//TODO
									}
									
									if(operator == null) {
										return true;
									}
									
									if (value == null) {
										return true;
									}
									
									if(ifcValue != null) {
										if(checkValues(value.toString(), ifcValue, currentOperator)) {
											return true;
										}
									}
								}
							}
						}

					}
				}
			}
		}

		if (ifcObj instanceof IfcProduct.Ifc4) {

			IfcProduct.Ifc4 product = (IfcProduct.Ifc4) ifcObj;

			if(product.getIsDefinedBy_Inverse() == null) {
				return false;	
			}
			
			for (IfcRelDefines.Ifc4 relDefines : product.getIsDefinedBy_Inverse()) {
				if (relDefines instanceof IfcRelDefinesByProperties.Ifc4) {

					IfcRelDefinesByProperties.Ifc4 relDefinesByProperties = (IfcRelDefinesByProperties.Ifc4) relDefines;

					if (relDefinesByProperties.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc4) {
						IfcPropertySet.Ifc4 propertySet = (IfcPropertySet.Ifc4) relDefinesByProperties
								.getRelatingPropertyDefinition();
						
						if (propertySet.getName().getDecodedValue().toLowerCase().equals(propertySetName.toLowerCase())) {
							for (IfcProperty.Ifc4 prop : propertySet.getHasProperties()) {
								
								if (propertyName == null) {
									return true;
								}

								if (prop.getName() == null) {
									continue;
								}
								
								if (prop.getName().getDecodedValue().toLowerCase().equals(propertyName.toLowerCase())) {
									
									OPERATOR currentOperator = OPERATOR.valueOf(operator);
									String ifcValue = null;
									
									if(prop instanceof IfcPropertySingleValue.Ifc4) {
										IfcPropertySingleValue.Ifc4 propValue = (IfcPropertySingleValue.Ifc4) prop;
										if(propValue.getNominalValue() != null) {											
											ifcValue = propValue.getNominalValue().toString();
										}
									}else if(prop instanceof IfcPropertyEnumeratedValue.Ifc4) {
										IfcPropertyEnumeratedValue.Ifc4 propValue = (IfcPropertyEnumeratedValue.Ifc4) prop;
										if(propValue.getEnumerationValues() != null) {											
											ifcValue = propValue.getEnumerationValues().toString();

											if(currentOperator.equals(OPERATOR.GLEICH) || currentOperator.equals(OPERATOR.ENTHALTEN)) {
												currentOperator = OPERATOR.ENTHALTEN;
											}else {
												currentOperator = OPERATOR.NICHT_ENTHALTEN;
											}
										}
									}else if(prop instanceof IfcPropertyListValue.Ifc4) {
										IfcPropertyListValue.Ifc4 propValue = (IfcPropertyListValue.Ifc4) prop;
										if(propValue.getListValues() != null) {											
											ifcValue = propValue.getListValues().toString();
											
											if(currentOperator.equals(OPERATOR.GLEICH) || currentOperator.equals(OPERATOR.ENTHALTEN)) {
												currentOperator = OPERATOR.ENTHALTEN;
											}else {
												currentOperator = OPERATOR.NICHT_ENTHALTEN;
											}
										}
									}else if(prop instanceof IfcPropertyReferenceValue.Ifc4) {
										//TODO
									}else if(prop instanceof IfcPropertyTableValue.Ifc4) {
										//TODO
									}
									
									if(operator == null) {
										return true;
									}
									
									if (value == null) {
										return true;
									}
									
									if(ifcValue != null) {
										if(checkValues(value.toString(), ifcValue, currentOperator)) {
											return true;
										}
									}
									
								}
							}
						}

					}
				}
			}
		}

		return false;
	}

	public boolean checkValues(String val1, String val2, OPERATOR operator) {
		switch(operator) {
		case GLEICH: return val1.toLowerCase().equals(val2.toLowerCase());
		case ENTHALTEN: return val2.toLowerCase().contains(val1.toLowerCase());
		case NICHT_ENTHALTEN: return !val2.toLowerCase().contains(val1.toLowerCase());
		case GROESSER: return checkBigger_AsNumericValues(val1, val2);
		case KLEINER: return checkSmaler_AsNumericValues(val1, val2);
		case NICHT_GLEICH: return !val1.toLowerCase().equals(val2.toLowerCase());
		default: break;
		}
		
		return false;
	}
	
	private boolean checkBigger_AsNumericValues(String val1, String val2) {
		try {
		
			Double dVal1 = new Double(val1.replace(",", "."));
			Double dVal2 = new Double(val2.replace(",", "."));
			
			return dVal1.doubleValue() <= dVal2.doubleValue(); 
			
		}catch (Exception e) {
			System.err.println(val1 + " or " + val2 + " could not be cast to number.");
		}
		
		return false;
	}
	
	private boolean checkSmaler_AsNumericValues(String val1, String val2) {

		try {
			
			Double dVal1 = new Double(val1.replace(",", "."));
			Double dVal2 = new Double(val2.replace(",", "."));
			
			return dVal1.doubleValue() >= dVal2.doubleValue(); 
			
		}catch (Exception e) {
			System.err.println(val1 + " or " + val2 + " could not be cast to number.");
		}
		
		return false;
	}
	
	public String getPropertySetName() {
		return propertySetName;
	}

	public void setPropertySetName(String propertySetName) {
		this.propertySetName = propertySetName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Color getColor() {
		return new Color(colorRed, colorGreen, colorBlue, colorAlpha);
	}

	public void setColor(Color color) {
		this.colorRed = color.getRed();
		this.colorGreen = color.getGreen();
		this.colorBlue = color.getBlue();
		this.colorAlpha = color.getOpacity();
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public double getColorRed() {
		return colorRed;
	}

	public void setColorRed(double colorRed) {
		this.colorRed = colorRed;
	}

	public double getColorGreen() {
		return colorGreen;
	}

	public void setColorGreen(double colorGreen) {
		this.colorGreen = colorGreen;
	}

	public double getColorBlue() {
		return colorBlue;
	}

	public void setColorBlue(double colorBlue) {
		this.colorBlue = colorBlue;
	}

	public double getColorAlpha() {
		return colorAlpha;
	}

	public void setColorAlpha(double colorAlpha) {
		this.colorAlpha = colorAlpha;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public boolean isEmpty() {
		return this.titel == null;
	}
}
