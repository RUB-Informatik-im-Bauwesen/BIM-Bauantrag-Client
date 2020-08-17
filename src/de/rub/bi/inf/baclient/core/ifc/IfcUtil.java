package de.rub.bi.inf.baclient.core.ifc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

import com.apstex.ifctoolbox.ifc.IfcBoolean;
import com.apstex.ifctoolbox.ifc.IfcColourRgb;
import com.apstex.ifctoolbox.ifc.IfcComplexProperty;
import com.apstex.ifctoolbox.ifc.IfcCompoundPlaneAngleMeasure;
import com.apstex.ifctoolbox.ifc.IfcCurrencyEnum;
import com.apstex.ifctoolbox.ifc.IfcDerivedUnit;
import com.apstex.ifctoolbox.ifc.IfcDerivedUnitElement;
import com.apstex.ifctoolbox.ifc.IfcDerivedUnitEnum;
import com.apstex.ifctoolbox.ifc.IfcDimensionalExponents;
import com.apstex.ifctoolbox.ifc.IfcElementCompositionEnum;
import com.apstex.ifctoolbox.ifc.IfcGloballyUniqueId;
import com.apstex.ifctoolbox.ifc.IfcIdentifier;
import com.apstex.ifctoolbox.ifc.IfcInteger;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcMonetaryUnit;
import com.apstex.ifctoolbox.ifc.IfcNamedUnit;
import com.apstex.ifctoolbox.ifc.IfcNormalisedRatioMeasure;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcOwnerHistory;
import com.apstex.ifctoolbox.ifc.IfcPresentationLayerAssignment;
import com.apstex.ifctoolbox.ifc.IfcPresentationStyleAssignment;
import com.apstex.ifctoolbox.ifc.IfcPresentationStyleSelect;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;
import com.apstex.ifctoolbox.ifc.IfcPropertySetDefinition;
import com.apstex.ifctoolbox.ifc.IfcPropertySetDefinitionSelect;
import com.apstex.ifctoolbox.ifc.IfcPropertySingleValue;
import com.apstex.ifctoolbox.ifc.IfcRatioMeasure;
import com.apstex.ifctoolbox.ifc.IfcReal;
import com.apstex.ifctoolbox.ifc.IfcReflectanceMethodEnum;
import com.apstex.ifctoolbox.ifc.IfcRelAggregates;
import com.apstex.ifctoolbox.ifc.IfcRelDefinesByProperties;
import com.apstex.ifctoolbox.ifc.IfcRepresentationItem;
import com.apstex.ifctoolbox.ifc.IfcSIPrefix;
import com.apstex.ifctoolbox.ifc.IfcSIUnit;
import com.apstex.ifctoolbox.ifc.IfcSIUnitName;
import com.apstex.ifctoolbox.ifc.IfcSite;
import com.apstex.ifctoolbox.ifc.IfcStyleAssignmentSelect;
import com.apstex.ifctoolbox.ifc.IfcStyledItem;
import com.apstex.ifctoolbox.ifc.IfcSurfaceSide;
import com.apstex.ifctoolbox.ifc.IfcSurfaceStyle;
import com.apstex.ifctoolbox.ifc.IfcSurfaceStyleElementSelect;
import com.apstex.ifctoolbox.ifc.IfcSurfaceStyleRendering;
import com.apstex.ifctoolbox.ifc.IfcText;
import com.apstex.ifctoolbox.ifc.IfcUnit;
import com.apstex.ifctoolbox.ifc.IfcUnitAssignment;
import com.apstex.ifctoolbox.ifc.IfcUnitEnum;
import com.apstex.ifctoolbox.ifc.IfcValue;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.step.core.ClassInterface;
import com.apstex.step.core.INTEGER;
import com.apstex.step.core.SET;
import com.apstex.step.core.STRING;
import com.apstex.step.guidcompressor.GuidCompressor;
import com.apstex.step.header.File_Name;

import de.rub.bi.inf.baclient.core.utils.PropertyContainer;
import de.rub.bi.inf.baclient.core.utils.PropertyUtils;
import net.opengis.gml._3.AbstractFeatureType;

public class IfcUtil {

	public static Double [] getSiteLocation(IfcSite site) {
		
		IfcCompoundPlaneAngleMeasure lon = site.getRefLongitude();
		IfcCompoundPlaneAngleMeasure lat = site.getRefLatitude();
		IfcLengthMeasure elev = site.getRefElevation();
		
		
		if (site instanceof IfcSite.Ifc2x3) {
			IfcCompoundPlaneAngleMeasure.Ifc2x3 lon2x3 = 
					(IfcCompoundPlaneAngleMeasure.Ifc2x3) lon;
		
			System.out.println(lon2x3);
	
			IfcCompoundPlaneAngleMeasure.Ifc2x3 lat2x3 = 
					(IfcCompoundPlaneAngleMeasure.Ifc2x3) lat;
		
//			System.out.println(toDecimalDatum(lat2x3));
			
			if(lon2x3 != null && lat2x3 !=null) {				
				return new Double[] {toDecimalDatum(lon2x3), toDecimalDatum(lat2x3)};
			}
			
		}else if(site instanceof IfcSite.Ifc4){
			
			IfcCompoundPlaneAngleMeasure.Ifc4 lon4 = 
					(IfcCompoundPlaneAngleMeasure.Ifc4) lon;
		
			System.out.println(lon4);
			
			IfcCompoundPlaneAngleMeasure.Ifc4 lat4 = 
					(IfcCompoundPlaneAngleMeasure.Ifc4) lat;
		
//			System.out.println(toDecimalDatum(lat2x3));
			
			if(lon4 != null && lat4 !=null) {				
				return new Double[] {toDecimalDatum(lon4), toDecimalDatum(lat4)};
			
		    }
		
		
		}
		
		return new Double[] {0.,0.};
		
	}
	
	
	private static double toDecimalDatum(IfcCompoundPlaneAngleMeasure.Ifc2x3 in) {
		
		System.out.println(in);
		
		double d = in.get(0).getValue();
		double m = in.get(1).getValue();
		double s = in.get(2).getValue();
		
		if (in.size()>3) {			
			double ms = in.get(3).getValue();
			s+=ms/1000000.;
		}
		
	    return d+(m/60.)+(s/3600.);
		
	}
	
	private static double toDecimalDatum(IfcCompoundPlaneAngleMeasure.Ifc4 in) {
		
		System.out.println(in);
		
		double d = in.get(0).getValue();
		double m = in.get(1).getValue();
		double s = in.get(2).getValue();
		
		if (in.size()>3) {			
			double ms = in.get(3).getValue();
			s+=ms/1000000.;
		}
		
	    return d+(m/60.)+(s/3600.);
		
	}
	
	public static IfcModel createEmptyModel(IfcSchema schema) {
		
		boolean schemaFlag = schema.equals(IfcSchema.IFC2X3);

		IfcModel model = new IfcModel();
		File_Name fName = new File_Name();
		fName.setname(schemaFlag ? new STRING("New IFC2x3 Model", true) : new STRING("New IFC4 Model", true));
		model.setFile_Name(fName);
		
		IfcOwnerHistory history = schemaFlag ? new IfcOwnerHistory.Ifc2x3.Instance() : new IfcOwnerHistory.Ifc4.Instance();
		//history.setCreationDate(new IfcTimeStamp.Ifc4(Calendar.getInstance().getTime().getDate()));
		model.addObject(history);
		
		IfcUnitAssignment unitAssignment = IfcUtil.createUnits(model, schemaFlag);
		
		IfcProject project = schemaFlag ? new IfcProject.Ifc2x3.Instance() : new IfcProject.Ifc4.Instance();
		project.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) :
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true));
		project.setOwnerHistory(history);
		project.setName(schemaFlag ? new IfcLabel.Ifc2x3("IFC Basic Project", true) : new IfcLabel.Ifc4("IFC Basic Project", true));
		String description = "A default model created as base for some Project.";
		project.setDescription(schemaFlag ? new IfcText.Ifc2x3(description, true) : new IfcText.Ifc4(description, true));
		project.setUnitsInContext(unitAssignment);
		model.addObject(project);
		
		IfcSite site = schemaFlag ? new IfcSite.Ifc2x3.Instance() : new IfcSite.Ifc4.Instance();
		site.setCompositionType(
				schemaFlag ? 
					new IfcElementCompositionEnum.Ifc2x3(
						IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.ELEMENT
					) : 
					new IfcElementCompositionEnum.Ifc4(
						IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.ELEMENT
					)
		);
		site.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) : 
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true));
		site.setName(schemaFlag ? new IfcLabel.Ifc2x3("XPlanungModel", true) : new IfcLabel.Ifc4("XPlanungModel", true));
		model.addObject(site);
		
		SET<IfcObjectDefinition> related = new SET<>();
		related.add((IfcObjectDefinition)site);
		
		IfcRelAggregates relAggregates = schemaFlag ? new IfcRelAggregates.Ifc2x3.Instance() : new IfcRelAggregates.Ifc4.Instance();
		relAggregates.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) : 
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true));
		relAggregates.setOwnerHistory(history);
		relAggregates.setRelatingObject((IfcObjectDefinition)project);
		relAggregates.setRelatedObjects(related);
		model.addObject(relAggregates);
		
		return model;
	}
	
	/**
	 * Add color for a specific shape.
	 * 
	 * @param Color to be added  
	 * @param ifcModel model to add into 
	 * @param appearance containing the information of the Coloring
	 * @param surfaceModels the surface objects where to add the new defined representation
	 * @param schema flag (true = IFC2x3, false = Ifc4) 
	 * 
	 */
	public static void addColorToModel(Color3f color3f, IfcModel ifcModel, SET<IfcRepresentationItem> surfaceModels, double transparence, boolean schemaFlag){
		IfcColourRgb colour = schemaFlag ? new IfcColourRgb.Ifc2x3.Instance() : new IfcColourRgb.Ifc4.Instance();
		colour.setRed(schemaFlag? new IfcNormalisedRatioMeasure.Ifc2x3(color3f.getX()):new IfcNormalisedRatioMeasure.Ifc4(color3f.getX()));
		colour.setGreen(schemaFlag ? new IfcNormalisedRatioMeasure.Ifc2x3(color3f.getY()) : new IfcNormalisedRatioMeasure.Ifc4(color3f.getY()));		
		colour.setBlue(schemaFlag ? new IfcNormalisedRatioMeasure.Ifc2x3(color3f.getZ()) : new IfcNormalisedRatioMeasure.Ifc4(color3f.getZ()));
		colour.setName(schemaFlag ? new IfcLabel.Ifc2x3("Colorization", true) :  new IfcLabel.Ifc4("Colorization", true));
		ifcModel.addObject(colour);

		IfcSurfaceStyleRendering rendering = schemaFlag ? new IfcSurfaceStyleRendering.Ifc2x3.Instance() :  new IfcSurfaceStyleRendering.Ifc4.Instance();
		rendering.setSurfaceColour(colour);
		rendering.setTransparency(schemaFlag ? new IfcNormalisedRatioMeasure.Ifc2x3(transparence) :  new IfcNormalisedRatioMeasure.Ifc4(transparence));
		rendering.setReflectanceMethod(schemaFlag ? new IfcReflectanceMethodEnum.Ifc2x3("NOTDEFINED") : new IfcReflectanceMethodEnum.Ifc4("NOTDEFINED"));
		ifcModel.addObject(rendering);

		HashSet<IfcSurfaceStyleElementSelect> ifcSurfaceStyleElementSelects = new HashSet<IfcSurfaceStyleElementSelect>();
		ifcSurfaceStyleElementSelects.add((IfcSurfaceStyleElementSelect) rendering);
		
		IfcSurfaceStyle style = schemaFlag ? new IfcSurfaceStyle.Ifc2x3.Instance() : new IfcSurfaceStyle.Ifc4.Instance();
		
		style.setName(schemaFlag ? new IfcLabel.Ifc2x3("Surface Style", true) : new IfcLabel.Ifc4("Surface Style", true));
		style.setSide(schemaFlag ? new IfcSurfaceSide.Ifc2x3("BOTH") : new IfcSurfaceSide.Ifc4("BOTH"));
		style.setStyles(new SET<IfcSurfaceStyleElementSelect>(ifcSurfaceStyleElementSelects));
		ifcModel.addObject(style);
		
		HashSet<IfcPresentationStyleSelect> presenSet = new HashSet<IfcPresentationStyleSelect>();
		presenSet.add((IfcPresentationStyleSelect) style);
		
		IfcPresentationStyleAssignment assignment = 
			schemaFlag ? 
				new IfcPresentationStyleAssignment.Ifc2x3.Instance(new SET<IfcPresentationStyleSelect>(presenSet)) : 
					new IfcPresentationStyleAssignment.Ifc4.Instance(new SET<IfcPresentationStyleSelect>(presenSet));
		ifcModel.addObject(assignment);
		
		if(schemaFlag) {
			HashSet<IfcPresentationStyleAssignment> assignSet = new HashSet<IfcPresentationStyleAssignment>();
			assignSet.add(assignment);
			
			for(IfcRepresentationItem it : surfaceModels){
				IfcStyledItem item = new IfcStyledItem.Ifc2x3.Instance(it, new SET<IfcPresentationStyleAssignment>(assignSet), null);
				ifcModel.addObject(item);
			}	
		}else {
			HashSet<IfcStyleAssignmentSelect> assignSet = new HashSet<IfcStyleAssignmentSelect>();
			assignSet.add((IfcStyleAssignmentSelect)assignment);
			
			for(IfcRepresentationItem it : surfaceModels){
				IfcStyledItem item = new IfcStyledItem.Ifc4.Instance(it, new SET<IfcStyleAssignmentSelect>(assignSet), null);
				ifcModel.addObject(item);
			}
		}
	}
	
	public static IfcValue convertValueToIfc(Object value, boolean schemaFlag) {
		
		if(value instanceof IfcValue) {
			return (IfcValue)value;
		}
		
		if(value instanceof Number) {
			if(value instanceof Integer) {
				return schemaFlag ? new IfcInteger.Ifc2x3(((Number)value).intValue()) : new IfcInteger.Ifc4(((Number)value).intValue());
			}else if(value instanceof BigInteger) {
				return schemaFlag ? new IfcInteger.Ifc2x3(((Number)value).intValue()) : new IfcInteger.Ifc4(((Number)value).intValue());
			}else if(value instanceof Double) {
				return schemaFlag ? new IfcReal.Ifc2x3(((Number)value).doubleValue()) : new IfcReal.Ifc4(((Number)value).doubleValue());
			}else if(value instanceof Float) {
				return schemaFlag ? new IfcReal.Ifc2x3(((Number)value).floatValue()) : new IfcReal.Ifc4(((Number)value).floatValue());
			}else if(value instanceof Byte) {
				return schemaFlag ? new IfcInteger.Ifc2x3(((Number)value).byteValue()) : new IfcInteger.Ifc4(((Number)value).byteValue());
			}else if(value instanceof Long) {
				return schemaFlag ? new IfcInteger.Ifc2x3((int)((Number)value).longValue()) : new IfcInteger.Ifc4((int)((Number)value).longValue());
			}else if(value instanceof Short) {
				return schemaFlag ? new IfcInteger.Ifc2x3((int)((Number)value).shortValue()) : new IfcInteger.Ifc4((int)((Number)value).shortValue());
			}else {	
				return schemaFlag ? new IfcReal.Ifc2x3(((Number)value).doubleValue()) : new IfcReal.Ifc4(((Number)value).doubleValue());
			}
		}
		
		if(value instanceof String) {
			return schemaFlag ? new IfcText.Ifc2x3((String)value, true) : new IfcText.Ifc4((String)value, true);
		}
		
		if(value instanceof Boolean) {
			return schemaFlag ? new IfcBoolean.Ifc2x3(((Boolean)value).booleanValue()) : new IfcBoolean.Ifc4(((Boolean)value).booleanValue());
		}
		
		return schemaFlag ? new IfcLabel.Ifc2x3(value.toString(), true) : new IfcLabel.Ifc4(value.toString(), true);
	}
	
	public static void addFeaturePropertiesToModel(IfcModel model, IfcObject obj, AbstractFeatureType feature, boolean complexProperties) {
		boolean schemaFlag = !model.getFile_SchemaString().equals("IFC4");
		
		IfcPropertySetDefinition propertySetDefinition = null;
		
		if(PropertyUtils.getIfcProperty(feature.getId() + (schemaFlag ? "_IFC2x3" : "_IFC4")) != null) {
			propertySetDefinition = PropertyUtils.getIfcProperty(feature.getId() + (schemaFlag ? "_IFC2x3" : "_IFC4"));
		}
		
		if(propertySetDefinition == null || !model.containsObject(propertySetDefinition)){
			SET<IfcProperty> properties = new SET<>();
			for(PropertyContainer prop : PropertyUtils.getPropertyInfos(feature.getId())) {
				
				//TODO option to append properties only as IfcPropertySingleValue
				if(complexProperties) {					
					properties.add(addPropertiesAsComplex(model, prop, schemaFlag));
				}else {
					addPropertiesAsSimple(model, "", prop, properties, schemaFlag);
				}
				
			}
			
			//addDefaultPropertiesAsSimple(model, "", new PropertyContainer("ExtendToStructure", "ExtendToStructure", "", new Boolean(false)), properties, schemaFlag);
			//addDefaultPropertiesAsSimple(model, "", new PropertyContainer("LoadBearing", "LoadBearing", "", new Boolean(true)), properties, schemaFlag);
			//addDefaultPropertiesAsSimple(model, "", new PropertyContainer("IsExternal", "IsExternal", "", new Boolean(true)), properties, schemaFlag);
			//addDefaultPropertiesAsSimple(model, "", new PropertyContainer("Reference", "Reference", "", schemaFlag ? new IfcIdentifier.Ifc2x3("EXTRUSION", true) : new IfcIdentifier.Ifc2x3("EXTRUSION", true)), properties, schemaFlag);
			
			IfcPropertySet propSet = schemaFlag ? new IfcPropertySet.Ifc2x3.Instance() : new IfcPropertySet.Ifc4.Instance();
			propSet.setGlobalId(
					schemaFlag ? 
							new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) : 
								new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true));
			propSet.setOwnerHistory(model.getIfcProject().getOwnerHistory());
			propSet.setHasProperties(properties);
			model.addObject(propSet);
			
			propertySetDefinition = (IfcPropertySetDefinition)propSet;
			PropertyUtils.setIfcProperty(feature.getId() + (schemaFlag ? "_IFC2x3" : "_IFC4"), propertySetDefinition);
		}
		
		IfcRelDefinesByProperties definesByProperties = schemaFlag ? new IfcRelDefinesByProperties.Ifc2x3.Instance(
				new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true),
				model.getIfcProject().getOwnerHistory(),
				new IfcLabel.Ifc2x3("Property_Relation", true),
				new IfcText.Ifc2x3("", true),
				new SET<IfcObject>(Arrays.asList(obj)), 
				propertySetDefinition
		) : new IfcRelDefinesByProperties.Ifc4.Instance(
				new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true),
				model.getIfcProject().getOwnerHistory(),
				new IfcLabel.Ifc4("Property_Relation", true),
				new IfcText.Ifc4("", true),
				new SET<IfcObjectDefinition>(Arrays.asList((IfcObjectDefinition)obj)), 
				(IfcPropertySetDefinitionSelect)propertySetDefinition
		);
		model.addObject(definesByProperties);
		
	}

	
	private static IfcProperty addPropertiesAsComplex(IfcModel model, PropertyContainer container, boolean schemaFlag) {
		
		//System.out.println(container.getContainedProperties().size());
		
		if(!container.getContainedProperties().isEmpty()) {
			
			SET<IfcProperty> ifcProperties = new SET<>();
			for(int i = 0; i < container.getContainedProperties().size(); i++) {	
				ifcProperties.add((IfcProperty)addPropertiesAsComplex(model, container.getContainedProperties().get(i), schemaFlag));
			}
			
			IfcComplexProperty complexProperty = schemaFlag ? new IfcComplexProperty.Ifc2x3.Instance() : new IfcComplexProperty.Ifc4.Instance();
			complexProperty.setName(schemaFlag ? new IfcIdentifier.Ifc2x3(container.getName(), true) : new IfcIdentifier.Ifc4(container.getName(), true));
			complexProperty.setDescription(schemaFlag ? new IfcText.Ifc2x3(container.getDescription(), true) : new IfcText.Ifc4(container.getDescription(), true));
			complexProperty.setHasProperties(ifcProperties);
			complexProperty.setUsageName(schemaFlag ? new IfcIdentifier.Ifc2x3(container.getUsageName(), true) : new IfcIdentifier.Ifc4(container.getUsageName(), true));
			model.addObject(complexProperty);
			
			return (IfcProperty)complexProperty;
						
		}else {
			
			IfcPropertySingleValue singleValue = schemaFlag ? new IfcPropertySingleValue.Ifc2x3.Instance() : new IfcPropertySingleValue.Ifc4.Instance();
			singleValue.setName(schemaFlag ? new IfcIdentifier.Ifc2x3(container.getName(), true) : new IfcIdentifier.Ifc4(container.getName(), true));
			singleValue.setNominalValue(IfcUtil.convertValueToIfc(container.getValue(), schemaFlag));
			singleValue.setDescription(schemaFlag ? new IfcText.Ifc2x3(container.getDescription(), true) : new IfcText.Ifc4(container.getDescription(), true));
			model.addObject(singleValue);
			
			return (IfcProperty)singleValue;
		}
	}

	private static void addPropertiesAsSimple(IfcModel model, String propName, PropertyContainer container, SET<IfcProperty> outputs, boolean schemaFlag) {
		
		if(!container.getContainedProperties().isEmpty()) {
			for(PropertyContainer inner : container.getContainedProperties()) {
				addPropertiesAsSimple(model, propName + container.getName() + ".", inner, outputs, schemaFlag);
			}
		}else {
			IfcPropertySingleValue singleValue = schemaFlag ? new IfcPropertySingleValue.Ifc2x3.Instance() : new IfcPropertySingleValue.Ifc4.Instance();
			singleValue.setName(schemaFlag ? new IfcIdentifier.Ifc2x3(propName + container.getName(), true) : new IfcIdentifier.Ifc4(propName + container.getName(), true));
			singleValue.setNominalValue(IfcUtil.convertValueToIfc(container.getValue(), schemaFlag));
			singleValue.setDescription(schemaFlag ? new IfcText.Ifc2x3(container.getDescription(), true) : new IfcText.Ifc4(container.getDescription(), true));
			model.addObject(singleValue);
		
			outputs.add((IfcProperty) singleValue);
		}	
	}
	
	private static void addDefaultPropertiesAsSimple(IfcModel model, String propName, PropertyContainer container, SET<IfcProperty> outputs, boolean schemaFlag) {
		
		if(!container.getContainedProperties().isEmpty()) {
			for(PropertyContainer inner : container.getContainedProperties()) {
				addPropertiesAsSimple(model, propName + container.getName() + ".", inner, outputs, schemaFlag);
			}
		}else {
			IfcPropertySingleValue singleValue = schemaFlag ? new IfcPropertySingleValue.Ifc2x3.Instance() : new IfcPropertySingleValue.Ifc4.Instance();
			singleValue.setName(schemaFlag ? new IfcIdentifier.Ifc2x3(propName + container.getName(), true) : new IfcIdentifier.Ifc4(propName + container.getName(), true));
			singleValue.setNominalValue(IfcUtil.convertValueToIfc(container.getValue(), schemaFlag));
			singleValue.setDescription(schemaFlag ? new IfcText.Ifc2x3(container.getDescription(), true) : new IfcText.Ifc4(container.getDescription(), true));
			model.addObject(singleValue);
		
			outputs.add((IfcProperty) singleValue);
		}	
	}

	
	public static IfcUnitAssignment createUnits(IfcModel model, boolean schemaFlag) {
		IfcSIUnit unitA = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitA.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitA.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.LENGTHUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.LENGTHUNIT)); 
		unitA.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.METRE) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.METRE));
		//unitA.setPrefix(schemaFlag ? new IfcSIPrefix.Ifc2x3(IfcSIPrefix.Ifc2x3.IfcSIPrefix_internal.DECI) : new IfcSIPrefix.Ifc4(IfcSIPrefix.Ifc4.IfcSIPrefix_internal.DECI));
		model.addObject(unitA);
		
		IfcSIUnit unitG = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitG.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitG.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.AREAUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.AREAUNIT)); 
		unitG.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.SQUARE_METRE) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.SQUARE_METRE));
		model.addObject(unitG);

		IfcSIUnit unitH = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitH.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitH.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.VOLUMEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.VOLUMEUNIT)); 
		unitH.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.CUBIC_METRE) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.CUBIC_METRE));
		model.addObject(unitH);

		IfcSIUnit unitJ = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitH.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitJ.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.MASSUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.MASSUNIT)); 
		unitJ.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.GRAM) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.GRAM));
		unitJ.setPrefix(schemaFlag ? new IfcSIPrefix.Ifc2x3(IfcSIPrefix.Ifc2x3.IfcSIPrefix_internal.KILO) : new IfcSIPrefix.Ifc4(IfcSIPrefix.Ifc4.IfcSIPrefix_internal.KILO));
		model.addObject(unitJ);
		
		IfcSIUnit unitB = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitB.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitB.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.TIMEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.TIMEUNIT)); 
		unitB.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.SECOND) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.SECOND));
		//unitB.setPrefix(schemaFlag ? new IfcSIPrefix.Ifc2x3(IfcSIPrefix.Ifc2x3.IfcSIPrefix_internal.NANO) : new IfcSIPrefix.Ifc4(IfcSIPrefix.Ifc4.IfcSIPrefix_internal.NANO));
		model.addObject(unitB);
		
		IfcSIUnit unitC = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitC.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitC.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.FREQUENCYUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.FREQUENCYUNIT)); 
		unitC.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.HERTZ) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.HERTZ));
		model.addObject(unitC);
		
		IfcSIUnit unitD = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitD.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitD.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.THERMODYNAMICTEMPERATUREUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.THERMODYNAMICTEMPERATUREUNIT)); 
		unitD.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.DEGREE_CELSIUS) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.DEGREE_CELSIUS));
		model.addObject(unitD);
	
		IfcSIUnit unitK = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		//unitD.setDimensions(schemaFlag ? new IfcDimensionalExponents.Ifc2x3.Instance() : new IfcDimensionalExponents.Ifc4.Instance());
		unitK.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.THERMODYNAMICTEMPERATUREUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.THERMODYNAMICTEMPERATUREUNIT)); 
		unitK.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.KELVIN) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.KELVIN));
		model.addObject(unitK);
	
			
		IfcDerivedUnitElement unitDerivedElementA = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementA.setUnit((IfcNamedUnit)unitJ);
		unitDerivedElementA.setExponent(new INTEGER(1));
		model.addObject(unitDerivedElementA);
		
		IfcDerivedUnitElement unitDerivedElementB = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementB.setUnit((IfcNamedUnit)unitD);
		unitDerivedElementB.setExponent(new INTEGER(-1));
		model.addObject(unitDerivedElementB);
		
		IfcDerivedUnitElement unitDerivedElementC = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementC.setUnit((IfcNamedUnit)unitB);
		unitDerivedElementC.setExponent(new INTEGER(-3));
		model.addObject(unitDerivedElementC);
		
		IfcDerivedUnit derivedUnitA = schemaFlag ? new IfcDerivedUnit.Ifc2x3.Instance() : new IfcDerivedUnit.Ifc4.Instance();
		derivedUnitA.addElements(unitDerivedElementA);
		derivedUnitA.addElements(unitDerivedElementB);
		derivedUnitA.addElements(unitDerivedElementC);
		derivedUnitA.setUnitType(
			schemaFlag ?  
				new IfcDerivedUnitEnum.Ifc2x3(IfcDerivedUnitEnum.Ifc2x3.IfcDerivedUnitEnum_internal.THERMALTRANSMITTANCEUNIT) : 
					new IfcDerivedUnitEnum.Ifc4(IfcDerivedUnitEnum.Ifc4.IfcDerivedUnitEnum_internal.THERMALTRANSMITTANCEUNIT));
		model.addObject(derivedUnitA);
		
		
		IfcDerivedUnitElement unitDerivedElementD = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementD.setUnit((IfcNamedUnit)unitA);
		unitDerivedElementD.setExponent(new INTEGER(3));
		model.addObject(unitDerivedElementD);
		
		IfcDerivedUnitElement unitDerivedElementE = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementE.setUnit((IfcNamedUnit)unitB);
		unitDerivedElementE.setExponent(new INTEGER(-1));
		model.addObject(unitDerivedElementE);
		
		IfcDerivedUnit derivedUnitB = schemaFlag ? new IfcDerivedUnit.Ifc2x3.Instance() : new IfcDerivedUnit.Ifc4.Instance();
		derivedUnitB.addElements(unitDerivedElementD);
		derivedUnitB.addElements(unitDerivedElementE);
		derivedUnitB.setUnitType(
			schemaFlag ?  
				new IfcDerivedUnitEnum.Ifc2x3(IfcDerivedUnitEnum.Ifc2x3.IfcDerivedUnitEnum_internal.VOLUMETRICFLOWRATEUNIT) : 
					new IfcDerivedUnitEnum.Ifc4(IfcDerivedUnitEnum.Ifc4.IfcDerivedUnitEnum_internal.VOLUMETRICFLOWRATEUNIT));
		model.addObject(derivedUnitB);
		
		
		
		
		IfcSIUnit unitL = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitL.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.ELECTRICCURRENTUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.ELECTRICCURRENTUNIT)); 
		unitL.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.AMPERE) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.AMPERE));
		model.addObject(unitL);
	
		IfcSIUnit unitM = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitM.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.ELECTRICVOLTAGEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.ELECTRICVOLTAGEUNIT)); 
		unitM.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.VOLT) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.VOLT));
		model.addObject(unitM);
	
		IfcSIUnit unitN = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitN.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.POWERUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.POWERUNIT)); 
		unitN.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.WATT) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.WATT));
		model.addObject(unitN);
	
		IfcSIUnit unitO = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitO.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.FORCEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.FORCEUNIT)); 
		unitO.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.NEWTON) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.NEWTON));
		unitO.setPrefix(schemaFlag ? new IfcSIPrefix.Ifc2x3(IfcSIPrefix.Ifc2x3.IfcSIPrefix_internal.KILO) : new IfcSIPrefix.Ifc4(IfcSIPrefix.Ifc4.IfcSIPrefix_internal.KILO));
		model.addObject(unitO);
	
		IfcSIUnit unitP = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitP.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.ILLUMINANCEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.ILLUMINANCEUNIT)); 
		unitP.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.LUX) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.LUX));
		model.addObject(unitP);
	
		IfcSIUnit unitQ = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitQ.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.LUMINOUSFLUXUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.LUMINOUSFLUXUNIT)); 
		unitQ.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.LUMEN) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.LUMEN));
		model.addObject(unitQ);
	
		IfcSIUnit unitR = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitR.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.LUMINOUSINTENSITYUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.LUMINOUSINTENSITYUNIT)); 
		unitR.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.CANDELA) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.CANDELA));
		model.addObject(unitR);
		
		
		IfcDerivedUnitElement unitDerivedElementF = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementF.setUnit((IfcNamedUnit)unitJ);
		unitDerivedElementF.setExponent(new INTEGER(-1));
		model.addObject(unitDerivedElementF);
		
		IfcDerivedUnitElement unitDerivedElementG = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementG.setUnit((IfcNamedUnit)unitA);
		unitDerivedElementG.setExponent(new INTEGER(-2));
		model.addObject(unitDerivedElementG);
		
		IfcDerivedUnitElement unitDerivedElementH = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementH.setUnit((IfcNamedUnit)unitB);
		unitDerivedElementH.setExponent(new INTEGER(3));
		model.addObject(unitDerivedElementH);
		
		IfcDerivedUnitElement unitDerivedElementI = schemaFlag ? new IfcDerivedUnitElement.Ifc2x3.Instance() : new IfcDerivedUnitElement.Ifc4.Instance();
		unitDerivedElementI.setUnit((IfcNamedUnit)unitQ);
		unitDerivedElementI.setExponent(new INTEGER(1));
		model.addObject(unitDerivedElementI);
	
		IfcDerivedUnit derivedUnitC = schemaFlag ? new IfcDerivedUnit.Ifc2x3.Instance() : new IfcDerivedUnit.Ifc4.Instance();
		derivedUnitC.addElements(unitDerivedElementF);
		derivedUnitC.addElements(unitDerivedElementG);
		derivedUnitC.addElements(unitDerivedElementH);
		derivedUnitC.addElements(unitDerivedElementI);
		derivedUnitC.setUserDefinedType(schemaFlag ? new IfcLabel.Ifc2x3("Luminous Efficacy", true) : new IfcLabel.Ifc4("Luminous Efficacy", true));
		derivedUnitC.setUnitType(
			schemaFlag ?  
				new IfcDerivedUnitEnum.Ifc2x3(IfcDerivedUnitEnum.Ifc2x3.IfcDerivedUnitEnum_internal.USERDEFINED) : 
					new IfcDerivedUnitEnum.Ifc4(IfcDerivedUnitEnum.Ifc4.IfcDerivedUnitEnum_internal.USERDEFINED));
		model.addObject(derivedUnitC);
		
		
		IfcMonetaryUnit monetaryUnit = 
				schemaFlag ? 
						new IfcMonetaryUnit.Ifc2x3.Instance(new IfcCurrencyEnum.Ifc2x3(IfcCurrencyEnum.Ifc2x3.IfcCurrencyEnum_internal.EUR)) :
							new IfcMonetaryUnit.Ifc4.Instance(new IfcLabel.Ifc4("EUR", true));
		model.addObject(monetaryUnit);
		
		IfcSIUnit unitS = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitS.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.PRESSUREUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.PRESSUREUNIT)); 
		unitS.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.PASCAL) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.PASCAL));
		model.addObject(unitS);
		
		IfcSIUnit unitE = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitE.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.ILLUMINANCEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.ILLUMINANCEUNIT)); 
		unitE.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.LUX) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.LUX));
		model.addObject(unitE);
	
		IfcSIUnit unitI = schemaFlag ? new IfcSIUnit.Ifc2x3.Instance() : new IfcSIUnit.Ifc4.Instance();
		unitI.setUnitType(schemaFlag ? new IfcUnitEnum.Ifc2x3(IfcUnitEnum.Ifc2x3.IfcUnitEnum_internal.PLANEANGLEUNIT) : new IfcUnitEnum.Ifc4(IfcUnitEnum.Ifc4.IfcUnitEnum_internal.PLANEANGLEUNIT)); 
		unitI.setName(schemaFlag ? new IfcSIUnitName.Ifc2x3(IfcSIUnitName.Ifc2x3.IfcSIUnitName_internal.RADIAN) : new IfcSIUnitName.Ifc4(IfcSIUnitName.Ifc4.IfcSIUnitName_internal.RADIAN));
		model.addObject(unitI);
		
		SET<IfcUnit> units = new SET<>();
		units.add((IfcUnit)unitA);
		units.add((IfcUnit)unitB);
		units.add((IfcUnit)unitC);
		units.add((IfcUnit)unitD);
		units.add((IfcUnit)unitE);
		units.add((IfcUnit)unitG);
		units.add((IfcUnit)unitH);
		units.add((IfcUnit)unitI);
		units.add((IfcUnit)unitJ);
		//units.add((IfcUnit)unitK);
		units.add((IfcUnit)unitL);
		units.add((IfcUnit)unitM);
		units.add((IfcUnit)unitN);
		units.add((IfcUnit)unitO);
		units.add((IfcUnit)unitP);
		units.add((IfcUnit)unitQ);
		units.add((IfcUnit)unitR);
		units.add((IfcUnit)unitS);
		units.add((IfcUnit)derivedUnitA);
		units.add((IfcUnit)derivedUnitB);
		//units.add((IfcUnit)derivedUnitC);

		IfcUnitAssignment unitAssignment = schemaFlag ? new IfcUnitAssignment.Ifc2x3.Instance(units) : new IfcUnitAssignment.Ifc4.Instance(units);
		model.addObject(unitAssignment);
		
		return unitAssignment;
	}
}
