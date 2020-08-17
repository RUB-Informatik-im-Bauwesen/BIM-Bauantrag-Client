package de.rub.bi.inf.baclient.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBoolean;
import com.apstex.ifctoolbox.ifc.IfcContext;
import com.apstex.ifctoolbox.ifc.IfcElementQuantity;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcPhysicalQuantity;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertyEnumeratedValue;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcRelDefines;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcSimpleProperty;
import com.apstex.ifctoolbox.ifc.IfcValue;
import com.apstex.step.core.SET;

public class Util {

	public static class Ifc2x3 {

		public static Collection<IfcObject.Ifc2x3> getObjectsByProperty(ApplicationModelNode modelNode,
				Class<? extends IfcObject.Ifc2x3> queryClass, String propertySetName, String propertyName, Object propertyValue) {

			Collection<? extends IfcObject.Ifc2x3> objects = modelNode.getStepModel().getCollection(queryClass);
			Collection<IfcObject.Ifc2x3> filteredObjects = new ArrayList<>();

			for (IfcObject.Ifc2x3 obj2x3 : objects) {

				// Traverse PropertySets
				for (IfcRelDefines.Ifc2x3 isDefinedBy : obj2x3.getIsDefinedBy_Inverse()) {
					if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc2x3) {
						IfcRelDefinesByProperties.Ifc2x3 isDefinedBy2x3 = (IfcRelDefinesByProperties.Ifc2x3) isDefinedBy;

						if (isDefinedBy2x3.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc2x3) {
							IfcPropertySet.Ifc2x3 propertySet2x3 = (IfcPropertySet.Ifc2x3) isDefinedBy2x3
									.getRelatingPropertyDefinition();
							if (propertySet2x3.getName().getDecodedValue().equals(propertySetName)) {
								for (IfcProperty property : propertySet2x3.getHasProperties()) {
									if (property instanceof IfcSimpleProperty) {
										IfcSimpleProperty.Ifc2x3 simpleProperty = (IfcSimpleProperty.Ifc2x3) property;
										if (simpleProperty.getName().getDecodedValue().equals(propertyName)) {
											if (propertyValue==null)
												filteredObjects.add(obj2x3);
											else if(simpleProperty instanceof IfcPropertySingleValue.Ifc2x3) {
												IfcPropertySingleValue.Ifc2x3 propertySingleValue = 
														(IfcPropertySingleValue.Ifc2x3) simpleProperty;
												IfcValue ifcValue = propertySingleValue.getNominalValue();
												if (ifcValue instanceof IfcLabel.Ifc2x3) {
													IfcLabel.Ifc2x3 label = (IfcLabel.Ifc2x3) ifcValue;
													if (label.getDecodedValue().equals(propertyValue)) {
														filteredObjects.add(obj2x3);
													}
												} else if (ifcValue instanceof IfcBoolean.Ifc2x3){
													IfcBoolean.Ifc2x3 ifcBoolean = (IfcBoolean.Ifc2x3) ifcValue;
													if (propertyValue.equals(ifcBoolean.getValue()))
														filteredObjects.add(obj2x3);	
												}
											}else if (simpleProperty instanceof IfcPropertyEnumeratedValue.Ifc2x3){
												IfcPropertyEnumeratedValue.Ifc2x3 enumeratedValue = 
														(IfcPropertyEnumeratedValue.Ifc2x3) simpleProperty;
												IfcValue ifcValue = enumeratedValue.getEnumerationValues().get(0);
												if (ifcValue instanceof IfcLabel.Ifc2x3) {
													IfcLabel.Ifc2x3 label = (IfcLabel.Ifc2x3) ifcValue;
													if (label.getDecodedValue().equals(propertyValue)) {
														filteredObjects.add(obj2x3);
													}
												}
											}
										}
									}
								}

							}
						} // endif property set

					}
				} // foreach isDefined

			} // end for each obj

			return filteredObjects;
		}

		public static Object getElementQuantity(IfcObject.Ifc2x3 ifcObject, Class<?> quantityClass,
				String quantityName) {

			for (IfcRelDefines.Ifc2x3 isDefinedBy : ifcObject.getIsDefinedBy_Inverse()) {
				if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc2x3) {
					IfcRelDefinesByProperties.Ifc2x3 isDefinedBy2x3 = (IfcRelDefinesByProperties.Ifc2x3) isDefinedBy;

					if (isDefinedBy2x3.getRelatingPropertyDefinition() instanceof IfcElementQuantity.Ifc2x3) {

						IfcElementQuantity.Ifc2x3 elementQuantity2x3 = (IfcElementQuantity.Ifc2x3) isDefinedBy2x3
								.getRelatingPropertyDefinition();

						for (IfcPhysicalQuantity quantity : elementQuantity2x3.getQuantities()) {

							if (quantity.getClass().equals(quantityClass)
									&& quantity.getName().getDecodedValue().equals(quantityName)) {

								return quantity;

							}
						}
					} // endif quantity

				}
			} // foreach isDefined

			return null;

		}

		public static IfcPropertySingleValue.Ifc2x3 getElementProperty(IfcObject.Ifc2x3 ifcObject,
				String propertySetName, String propertyName) {

			for (IfcRelDefines.Ifc2x3 isDefinedBy : ifcObject.getIsDefinedBy_Inverse()) {
				if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc2x3) {
					IfcRelDefinesByProperties.Ifc2x3 isDefinedBy4 = (IfcRelDefinesByProperties.Ifc2x3) isDefinedBy;

					if (isDefinedBy4.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc2x3) {
						
						IfcPropertySet.Ifc2x3 propertySet4 = 
								(IfcPropertySet.Ifc2x3) isDefinedBy4.getRelatingPropertyDefinition();
						if(propertySet4.getName().getDecodedValue().equals(propertySetName)) {
							
							for (IfcProperty property : propertySet4.getHasProperties()) {
								if(property instanceof IfcPropertySingleValue.Ifc2x3) {
									IfcPropertySingleValue.Ifc2x3 simpleProperty = (IfcPropertySingleValue.Ifc2x3) property;
									
									if(simpleProperty.getName().getDecodedValue().equals(propertyName)) {
										return simpleProperty;
									}
								}
							}
						}
						

					} // endif propertyset

				}
			} // foreach isDefined

			return null;

		}
	}

	public static class Ifc4 {

		public static Collection<IfcObject.Ifc4> getObjectsByProperty(ApplicationModelNode modelNode,
				Class<? extends IfcObject.Ifc4> queryClass, String propertySetName, String propertyName, Object propertyValue) {

			Collection<? extends IfcObject.Ifc4> objects = modelNode.getStepModel().getCollection(queryClass);
			Collection<IfcObject.Ifc4> filteredObjects = new ArrayList<>();

			for (IfcObject.Ifc4 obj4 : objects) {

				for (IfcRelDefines isDefinedBy : obj4.getIsDefinedBy_Inverse()) {
					if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc4) {
						IfcRelDefinesByProperties.Ifc4 isDefinedBy4 = (IfcRelDefinesByProperties.Ifc4) isDefinedBy;

						if (isDefinedBy4.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc4) {
							IfcPropertySet.Ifc4 propertySet4 = (IfcPropertySet.Ifc4) isDefinedBy4
									.getRelatingPropertyDefinition();
							if (propertySet4.getName().getDecodedValue().equals(propertySetName)) {
								for (IfcProperty property : propertySet4.getHasProperties()) {
									if (property instanceof IfcSimpleProperty) {
										IfcSimpleProperty.Ifc4 simpleProperty = (IfcSimpleProperty.Ifc4) property;
										if (simpleProperty.getName().getDecodedValue().equals(propertyName)) {
											if (propertyValue==null)
												filteredObjects.add(obj4);
											else if(simpleProperty instanceof IfcPropertySingleValue.Ifc4) {
												IfcPropertySingleValue.Ifc4 propertySingleValue = 
														(IfcPropertySingleValue.Ifc4) simpleProperty;
												IfcValue ifcValue = propertySingleValue.getNominalValue();
												if (ifcValue instanceof IfcLabel.Ifc4) {
													IfcLabel.Ifc4 label = (IfcLabel.Ifc4) ifcValue;
													if (label.getDecodedValue().equals(propertyValue)) {
														filteredObjects.add(obj4);
													}
												} else if (ifcValue instanceof IfcBoolean.Ifc4){
													IfcBoolean.Ifc4 ifcBoolean = (IfcBoolean.Ifc4) ifcValue;
													if (propertyValue.equals(ifcBoolean.getValue()))
														filteredObjects.add(obj4);	
												}
											} else if (simpleProperty instanceof IfcPropertyEnumeratedValue.Ifc4){
												IfcPropertyEnumeratedValue.Ifc4 enumeratedValue = 
														(IfcPropertyEnumeratedValue.Ifc4) simpleProperty;
												IfcValue ifcValue = enumeratedValue.getEnumerationValues().get(0);
												if (ifcValue instanceof IfcLabel.Ifc4) {
													IfcLabel.Ifc4 label = (IfcLabel.Ifc4) ifcValue;
													if (label.getDecodedValue().equals(propertyValue)) {
														filteredObjects.add(obj4);
													}
												}
											}
										}
										
									}
								}

							}
						} // endif property set

					}
				} // foreach isDefined

			} // for obj

			return filteredObjects;
		}
		
		public static Collection<IfcObject.Ifc4> getObjectsByProperty(ApplicationModelNode modelNode,
				Class<? extends IfcObject.Ifc4> queryClass, String propertySetName, String propertyName, Set<Object> allowedPropertyValues) {

			Collection<? extends IfcObject.Ifc4> objects = modelNode.getStepModel().getCollection(queryClass);
			Collection<IfcObject.Ifc4> filteredObjects = new ArrayList<>();

			for (IfcObject.Ifc4 obj4 : objects) {

				for (IfcRelDefines isDefinedBy : obj4.getIsDefinedBy_Inverse()) {
					if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc4) {
						IfcRelDefinesByProperties.Ifc4 isDefinedBy4 = (IfcRelDefinesByProperties.Ifc4) isDefinedBy;

						if (isDefinedBy4.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc4) {
							IfcPropertySet.Ifc4 propertySet4 = (IfcPropertySet.Ifc4) isDefinedBy4
									.getRelatingPropertyDefinition();
							if (propertySet4.getName().getDecodedValue().equals(propertySetName)) {
								for (IfcProperty property : propertySet4.getHasProperties()) {
									if (property instanceof IfcSimpleProperty) {
										IfcSimpleProperty.Ifc4 simpleProperty = (IfcSimpleProperty.Ifc4) property;
										if (simpleProperty.getName().getDecodedValue().equals(propertyName)) {
											if (allowedPropertyValues==null)
												filteredObjects.add(obj4);
											else if(simpleProperty instanceof IfcPropertySingleValue.Ifc4) {
												IfcPropertySingleValue.Ifc4 propertySingleValue = 
														(IfcPropertySingleValue.Ifc4) simpleProperty;
												IfcValue ifcValue = propertySingleValue.getNominalValue();
												if (ifcValue instanceof IfcLabel.Ifc4) {
													IfcLabel.Ifc4 label = (IfcLabel.Ifc4) ifcValue;
													if (allowedPropertyValues.contains(label.getDecodedValue())
															) {
														filteredObjects.add(obj4);
													}
												} 
											} else if (simpleProperty instanceof IfcPropertyEnumeratedValue.Ifc4){
												IfcPropertyEnumeratedValue.Ifc4 enumeratedValue = 
														(IfcPropertyEnumeratedValue.Ifc4) simpleProperty;
												
												for(int i=0; i<enumeratedValue.getEnumerationValues().size(); i++) {
													IfcValue ifcValue = enumeratedValue.getEnumerationValues().get(i);
													if (ifcValue instanceof IfcLabel.Ifc4) {
														IfcLabel.Ifc4 label = (IfcLabel.Ifc4) ifcValue;
														if (allowedPropertyValues.contains(label.getDecodedValue())) {
															filteredObjects.add(obj4);
															break;
														}
													}
												}
												
						
											}
										}
										
									}
								}

							}
						} // endif property set

					}
				} // foreach isDefined

			} // for obj

			return filteredObjects;
		}

		public static Object getElementQuantity(IfcObject.Ifc4 ifcObject, Class<?> quantityClass,
				String quantityName) {

			for (IfcRelDefines.Ifc4 isDefinedBy : ifcObject.getIsDefinedBy_Inverse()) {
				if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc4) {
					IfcRelDefinesByProperties.Ifc4 isDefinedBy4 = (IfcRelDefinesByProperties.Ifc4) isDefinedBy;

					if (isDefinedBy4.getRelatingPropertyDefinition() instanceof IfcElementQuantity.Ifc4) {

						IfcElementQuantity.Ifc4 elementQuantity4 = (IfcElementQuantity.Ifc4) isDefinedBy4
								.getRelatingPropertyDefinition();
						
						System.out.println(elementQuantity4.getName().getDecodedValue());

						for (IfcPhysicalQuantity quantity : elementQuantity4.getQuantities()) {

							if (quantity.getClass().equals(quantityClass)
									&& quantity.getName().getDecodedValue().equals(quantityName)) {

								return quantity;

							}
						}
					} // endif quantity

				}
			} // foreach isDefined

			return null;

		}
		
		
		public static IfcSimpleProperty.Ifc4 getElementProperty(IfcObjectDefinition.Ifc4 ifcObjectDefinition,
				String propertySetName, String propertyName) {
			
			SET<? extends IfcRelDefines> relDefines = null;
			
			if(ifcObjectDefinition instanceof IfcObject.Ifc4) {
				relDefines = ((IfcObject.Ifc4)ifcObjectDefinition).getIsDefinedBy_Inverse();
			}else if (ifcObjectDefinition instanceof IfcContext.Ifc4) {
				relDefines = ((IfcContext.Ifc4)ifcObjectDefinition).getIsDefinedBy_Inverse();
			}else return null;

			for (IfcRelDefines isDefinedBy : relDefines) {
				if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc4) {
					IfcRelDefinesByProperties.Ifc4 isDefinedBy4 = (IfcRelDefinesByProperties.Ifc4) isDefinedBy;

					if (isDefinedBy4.getRelatingPropertyDefinition() instanceof IfcPropertySet.Ifc4) {
						
						IfcPropertySet.Ifc4 propertySet4 = 
								(IfcPropertySet.Ifc4) isDefinedBy4.getRelatingPropertyDefinition();
						if(propertySet4.getName().getDecodedValue().equals(propertySetName)) {
							
							for (IfcProperty property : propertySet4.getHasProperties()) {
								if(property instanceof IfcSimpleProperty.Ifc4) {
									IfcSimpleProperty.Ifc4 simpleProperty = (IfcSimpleProperty.Ifc4) property;
									
									if(simpleProperty.getName().getDecodedValue().equals(propertyName)) {
										return simpleProperty;
									}
								}
							}
						}
						

					} // endif propertyset

				}
			} // foreach isDefined

			return null;

		}
		
		public static IfcPhysicalQuantity.Ifc4 getElementQuantity(IfcObjectDefinition.Ifc4 ifcObjectDefinition,
				String quantitySetName, String quantityName) {
			
			SET<? extends IfcRelDefines> relDefines = null;
			
			if(ifcObjectDefinition instanceof IfcObject.Ifc4) {
				relDefines = ((IfcObject.Ifc4)ifcObjectDefinition).getIsDefinedBy_Inverse();
			}else if (ifcObjectDefinition instanceof IfcContext.Ifc4) {
				relDefines = ((IfcContext.Ifc4)ifcObjectDefinition).getIsDefinedBy_Inverse();
			}else return null;

			for (IfcRelDefines isDefinedBy : relDefines) {
				if (isDefinedBy instanceof IfcRelDefinesByProperties.Ifc4) {
					IfcRelDefinesByProperties.Ifc4 isDefinedBy4 = (IfcRelDefinesByProperties.Ifc4) isDefinedBy;

					if (isDefinedBy4.getRelatingPropertyDefinition() instanceof IfcElementQuantity) {
						
						IfcElementQuantity.Ifc4 elementQuantity4 = 
								(IfcElementQuantity.Ifc4) isDefinedBy4.getRelatingPropertyDefinition();
						if(elementQuantity4.getName().getDecodedValue().equals(quantitySetName)) {
							
							for (IfcPhysicalQuantity.Ifc4 quantity : elementQuantity4.getQuantities()) {
							
								if(quantity.getName().getDecodedValue().equals(quantityName)) {
									return quantity;
								}
								
							}
						}
						

					} // endif IfcElementQuantity

				}
			} // foreach isDefined

			return null;

		}
		
		
		
	
	}

}
