package de.rub.bi.inf.baclient.core.views.ifc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.ifctoolbox.ifc.IfcApplication;
import com.apstex.ifctoolbox.ifc.IfcArbitraryClosedProfileDef;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement3D;
import com.apstex.ifctoolbox.ifc.IfcBuilding;
import com.apstex.ifctoolbox.ifc.IfcBuildingElementProxy;
import com.apstex.ifctoolbox.ifc.IfcCartesianPoint;
import com.apstex.ifctoolbox.ifc.IfcChangeActionEnum;
import com.apstex.ifctoolbox.ifc.IfcCurrencyEnum;
import com.apstex.ifctoolbox.ifc.IfcCurve;
import com.apstex.ifctoolbox.ifc.IfcDerivedUnit;
import com.apstex.ifctoolbox.ifc.IfcDerivedUnitElement;
import com.apstex.ifctoolbox.ifc.IfcDerivedUnitEnum;
import com.apstex.ifctoolbox.ifc.IfcDimensionCount;
import com.apstex.ifctoolbox.ifc.IfcDirection;
import com.apstex.ifctoolbox.ifc.IfcElementCompositionEnum;
import com.apstex.ifctoolbox.ifc.IfcExtrudedAreaSolid;
import com.apstex.ifctoolbox.ifc.IfcGeometricProjectionEnum;
import com.apstex.ifctoolbox.ifc.IfcGeometricRepresentationContext;
import com.apstex.ifctoolbox.ifc.IfcGeometricRepresentationSubContext;
import com.apstex.ifctoolbox.ifc.IfcGloballyUniqueId;
import com.apstex.ifctoolbox.ifc.IfcIdentifier;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcLocalPlacement;
import com.apstex.ifctoolbox.ifc.IfcMonetaryUnit;
import com.apstex.ifctoolbox.ifc.IfcNamedUnit;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcObjectPlacement;
import com.apstex.ifctoolbox.ifc.IfcOrganization;
import com.apstex.ifctoolbox.ifc.IfcOwnerHistory;
import com.apstex.ifctoolbox.ifc.IfcPerson;
import com.apstex.ifctoolbox.ifc.IfcPersonAndOrganization;
import com.apstex.ifctoolbox.ifc.IfcPolyline;
import com.apstex.ifctoolbox.ifc.IfcPositiveLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcProduct;
import com.apstex.ifctoolbox.ifc.IfcProductDefinitionShape;
import com.apstex.ifctoolbox.ifc.IfcProductRepresentation;
import com.apstex.ifctoolbox.ifc.IfcProfileDef;
import com.apstex.ifctoolbox.ifc.IfcProfileTypeEnum;
import com.apstex.ifctoolbox.ifc.IfcProject;
import com.apstex.ifctoolbox.ifc.IfcReal;
import com.apstex.ifctoolbox.ifc.IfcRelAggregates;
import com.apstex.ifctoolbox.ifc.IfcRelContainedInSpatialStructure;
import com.apstex.ifctoolbox.ifc.IfcRepresentation;
import com.apstex.ifctoolbox.ifc.IfcRepresentationContext;
import com.apstex.ifctoolbox.ifc.IfcRepresentationItem;
import com.apstex.ifctoolbox.ifc.IfcSIPrefix;
import com.apstex.ifctoolbox.ifc.IfcSIUnit;
import com.apstex.ifctoolbox.ifc.IfcSIUnitName;
import com.apstex.ifctoolbox.ifc.IfcShapeRepresentation;
import com.apstex.ifctoolbox.ifc.IfcSite;
import com.apstex.ifctoolbox.ifc.IfcSpatialElement;
import com.apstex.ifctoolbox.ifc.IfcSpatialStructureElement;
import com.apstex.ifctoolbox.ifc.IfcText;
import com.apstex.ifctoolbox.ifc.IfcTimeStamp;
import com.apstex.ifctoolbox.ifc.IfcUnit;
import com.apstex.ifctoolbox.ifc.IfcUnitAssignment;
import com.apstex.ifctoolbox.ifc.IfcUnitEnum;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.j3d.utils.geometry.NormalGenerator;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.javax.vecmath.Vector3f;
import com.apstex.step.core.INTEGER;
import com.apstex.step.core.LIST;
import com.apstex.step.core.SET;
import com.apstex.step.guidcompressor.GuidCompressor;

import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadLoader;
import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.PropertyUtils;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.AbstractRingPropertyType;
import net.opengis.gml._3.AbstractRingType;
import net.opengis.gml._3.PolygonType;

public class IfcExtrusionModelCreator extends Dialog<Boolean>{

	private XPlanungModel xplanungModel;
	private ArrayList<AbstractFeatureType> features = null;
	
	public IfcExtrusionModelCreator(AbstractFeatureType feature, XPlanungModel model) {
		this.features = new ArrayList<>();
		this.features.add(feature);
		
		this.xplanungModel = model;
		init();
	}
	
	public IfcExtrusionModelCreator(ArrayList<AbstractFeatureType> features, XPlanungModel model) {
		this.features = features;
		this.xplanungModel = model;
		init();
	}

	private void init() {
		setTitle("Extrusion Model Creator");
		
		// Set the button types.
		ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
		
		// Fields for user interaction
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		ChoiceBox<IfcSchema> ifcChoise = new ChoiceBox<>();
		ifcChoise.getItems().add(IfcSchema.IFC2X3);
		ifcChoise.getItems().add(IfcSchema.IFC4);
		ifcChoise.getSelectionModel().select(IfcSchema.IFC2X3);

		TextField basementHeight = new TextField();
		basementHeight.setPromptText("8.7");
		basementHeight.setText("8.7");
		
		TextField storageHeight = new TextField();
		storageHeight.setPromptText("3.0");
		storageHeight.setText("3.0");
		
		CheckBox box = new CheckBox("Use Complex Properties");

		
		grid.add(new Label("Use Schema:"), 0, 0);
		grid.add(ifcChoise, 1, 0);
		grid.add(new Label("Basement Height:"), 0, 1);
		grid.add(basementHeight, 1, 1);
		grid.add(new Label("Storage Height:"), 0, 2);
		grid.add(storageHeight, 1, 2);
		grid.add(box, 1, 3);

		getDialogPane().setContent(grid);
		
		// Convert the result to a username-password-pair when the login button is clicked.
		setResultConverter(dialogButton -> {
		    if (dialogButton == createButtonType) {
		    	return new Boolean(true);
		    }
	    	return new Boolean(false);
		});
		
		
		Optional<Boolean> result = showAndWait();
		if (result.isPresent()){
			if(result.get().booleanValue()) {
				IfcSchema schema = (IfcSchema)ifcChoise.getSelectionModel().getSelectedItem();
				
				
		    	float valA = 0.0f;
		    	float valB = 3.0f;
		    	
		    	try {
		    		if(basementHeight.getText() != null) {		    		
			    		valA = new Float(basementHeight.getText()).floatValue();
			    	}
			    	
			    	if(storageHeight.getText() != null) {		    		
			    		valB = new Float(storageHeight.getText()).floatValue();
			    	}
				} catch (Exception e) {
					System.err.println("Input musst be a number! Using default values for basement and storage height.");
				}
		    	
				createModel(schema, valA, valB, box.isSelected());

				System.out.println("=== Extrusion model created ===");

			}
		}

	}
	
	
	public void createModel(IfcSchema schema, float basementHeight, float storageHeight, boolean complexProperties) {
		
		boolean schemaFlag = schema.equals(IfcSchema.IFC2X3);
		
		//Extend property map
		PropertyUtils.load(features);
		
		IfcModel model = new IfcModel();
		
		IfcOrganization organization =  schemaFlag ? new IfcOrganization.Ifc2x3.Instance() : new IfcOrganization.Ifc4.Instance(); 
		organization.setDescription(schemaFlag ? new IfcText.Ifc2x3("SFB - RUB BI INF", true) : new IfcText.Ifc4("RUB BI INF", true));
		model.addObject(organization);		
		
		IfcPerson person = schemaFlag ? new IfcPerson.Ifc2x3.Instance() : new IfcPerson.Ifc4.Instance();
		model.addObject(person);
		
		IfcPersonAndOrganization personAndOrganization = schemaFlag ? new IfcPersonAndOrganization.Ifc2x3.Instance() : new IfcPersonAndOrganization.Ifc4.Instance();
		personAndOrganization.setThePerson(person);
		personAndOrganization.setTheOrganization(organization);
		model.addObject(personAndOrganization);
		
		IfcApplication application = schemaFlag ? new IfcApplication.Ifc2x3.Instance() : new IfcApplication.Ifc4.Instance();
		application.setApplicationDeveloper(organization);
		application.setApplicationFullName(schemaFlag ? new IfcLabel.Ifc2x3("XViewer Extrusion-Model Exporter", true) : new IfcLabel.Ifc4("XViewer Extrusion-Model Exporter", true));
		application.setVersion(schemaFlag ? new IfcLabel.Ifc2x3("IN DEV v0.3", true) : new IfcLabel.Ifc4("IN DEV v0.3", true));
		application.setApplicationIdentifier(schemaFlag ? new IfcIdentifier.Ifc2x3("XViewer", true) : new IfcIdentifier.Ifc4("XViewer", true));
		model.addObject(application);
		
		IfcOwnerHistory history = schemaFlag ? new IfcOwnerHistory.Ifc2x3.Instance() : new IfcOwnerHistory.Ifc4.Instance();
		history.setOwningApplication(application);
		history.setOwningUser(personAndOrganization);
		history.setChangeAction(
			schemaFlag ? 
				new IfcChangeActionEnum.Ifc2x3(IfcChangeActionEnum.Ifc2x3.IfcChangeActionEnum_internal.NOCHANGE) : 
					new IfcChangeActionEnum.Ifc4(IfcChangeActionEnum.Ifc4.IfcChangeActionEnum_internal.NOCHANGE));
		history.setCreationDate(schemaFlag ? new IfcTimeStamp.Ifc2x3() : new IfcTimeStamp.Ifc4());
		model.addObject(history);
		
		
		// ==== UNIT DEFINITIONS ====
		IfcUnitAssignment unitAssignment = IfcUtil.createUnits(model, schemaFlag);
		// ==========================
				
		// ==== DEFAULT PLACEMENT ===
		LIST<IfcLengthMeasure> positionMeasures = new LIST<>();
		positionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		positionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		positionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		
		IfcCartesianPoint positionPoint = schemaFlag ? new IfcCartesianPoint.Ifc2x3.Instance(positionMeasures) : new IfcCartesianPoint.Ifc4.Instance(positionMeasures);
		model.addObject(positionPoint);
		
		IfcAxis2Placement3D defaultPlacement = schemaFlag ? new IfcAxis2Placement3D.Ifc2x3.Instance() : new IfcAxis2Placement3D.Ifc4.Instance();
		defaultPlacement.setLocation(positionPoint);
		model.addObject(defaultPlacement);
		
		IfcLocalPlacement defaultLocalPlacement = schemaFlag ? new IfcLocalPlacement.Ifc2x3.Instance() : new IfcLocalPlacement.Ifc4.Instance();
		defaultLocalPlacement.setRelativePlacement((IfcAxis2Placement) defaultPlacement);
		model.addObject(defaultLocalPlacement);
		// ==========================
		
		
		// ==== GEOMETRIE CONTEXT ===
		IfcDirection trueNorthDir = schemaFlag ? new IfcDirection.Ifc2x3.Instance() : new IfcDirection.Ifc4.Instance();
		trueNorthDir.addDirectionRatios(schemaFlag ? new IfcReal.Ifc2x3(0.0) : new IfcReal.Ifc4(0.0));
		trueNorthDir.addDirectionRatios(schemaFlag ? new IfcReal.Ifc2x3(1.0) : new IfcReal.Ifc4(1.0));
		model.addObject(trueNorthDir);
		
		IfcGeometricRepresentationContext context = schemaFlag ? new IfcGeometricRepresentationContext.Ifc2x3.Instance() : new IfcGeometricRepresentationContext.Ifc4.Instance();
		context.setWorldCoordinateSystem((IfcAxis2Placement)defaultPlacement);
		context.setCoordinateSpaceDimension(schemaFlag ? new IfcDimensionCount.Ifc2x3(3) : new IfcDimensionCount.Ifc4(3));
		context.setContextType(schemaFlag ? new IfcLabel.Ifc2x3("Model", true) : new IfcLabel.Ifc4("Model", true));
		context.setPrecision(schemaFlag ? new IfcReal.Ifc2x3(0.00001) : new IfcReal.Ifc4(0.00001));
		context.setTrueNorth(trueNorthDir);
		model.addObject(context);
		
		IfcGeometricRepresentationSubContext subContextA = schemaFlag ? new IfcGeometricRepresentationSubContext.Ifc2x3.Instance() : new IfcGeometricRepresentationSubContext.Ifc4.Instance();
		subContextA.setParentContext(context);
		subContextA.setContextType(schemaFlag ? new IfcLabel.Ifc2x3("Model", true) : new IfcLabel.Ifc4("Model", true));
		subContextA.setContextIdentifier(schemaFlag ? new IfcLabel.Ifc2x3("Axis", true) : new IfcLabel.Ifc4("Axis", true));
		subContextA.setTargetView(
			schemaFlag ? 
				new IfcGeometricProjectionEnum.Ifc2x3(IfcGeometricProjectionEnum.Ifc2x3.IfcGeometricProjectionEnum_internal.GRAPH_VIEW) : 
					new IfcGeometricProjectionEnum.Ifc4(IfcGeometricProjectionEnum.Ifc4.IfcGeometricProjectionEnum_internal.GRAPH_VIEW));
		model.addObject(subContextA);
		
		IfcGeometricRepresentationSubContext subContextB = schemaFlag ? new IfcGeometricRepresentationSubContext.Ifc2x3.Instance() : new IfcGeometricRepresentationSubContext.Ifc4.Instance();
		subContextB.setParentContext(context);
		subContextB.setContextType(schemaFlag ? new IfcLabel.Ifc2x3("Model", true) : new IfcLabel.Ifc4("Model", true));
		subContextB.setContextIdentifier(schemaFlag ? new IfcLabel.Ifc2x3("Body", true) : new IfcLabel.Ifc4("Body", true));
		subContextB.setTargetView(
			schemaFlag ? 
				new IfcGeometricProjectionEnum.Ifc2x3(IfcGeometricProjectionEnum.Ifc2x3.IfcGeometricProjectionEnum_internal.MODEL_VIEW) : 
					new IfcGeometricProjectionEnum.Ifc4(IfcGeometricProjectionEnum.Ifc4.IfcGeometricProjectionEnum_internal.MODEL_VIEW));
		model.addObject(subContextB);
		
		IfcGeometricRepresentationSubContext subContextC = schemaFlag ? new IfcGeometricRepresentationSubContext.Ifc2x3.Instance() : new IfcGeometricRepresentationSubContext.Ifc4.Instance();
		subContextC.setParentContext(context);
		subContextC.setContextType(schemaFlag ? new IfcLabel.Ifc2x3("Model", true) : new IfcLabel.Ifc4("Model", true));
		subContextC.setContextIdentifier(schemaFlag ? new IfcLabel.Ifc2x3("Box", true) : new IfcLabel.Ifc4("Box", true));
		subContextC.setTargetView(
			schemaFlag ? 
				new IfcGeometricProjectionEnum.Ifc2x3(IfcGeometricProjectionEnum.Ifc2x3.IfcGeometricProjectionEnum_internal.MODEL_VIEW) : 
					new IfcGeometricProjectionEnum.Ifc4(IfcGeometricProjectionEnum.Ifc4.IfcGeometricProjectionEnum_internal.MODEL_VIEW));
		model.addObject(subContextC);
		
		IfcGeometricRepresentationSubContext subContextD = schemaFlag ? new IfcGeometricRepresentationSubContext.Ifc2x3.Instance() : new IfcGeometricRepresentationSubContext.Ifc4.Instance();
		subContextD.setParentContext(context);
		subContextD.setContextType(schemaFlag ? new IfcLabel.Ifc2x3("Model", true) : new IfcLabel.Ifc4("Model", true));
		subContextD.setContextIdentifier(schemaFlag ? new IfcLabel.Ifc2x3("FootPrint", true) : new IfcLabel.Ifc4("FootPrint", true));
		subContextD.setTargetView(
			schemaFlag ? 
				new IfcGeometricProjectionEnum.Ifc2x3(IfcGeometricProjectionEnum.Ifc2x3.IfcGeometricProjectionEnum_internal.MODEL_VIEW) : 
					new IfcGeometricProjectionEnum.Ifc4(IfcGeometricProjectionEnum.Ifc4.IfcGeometricProjectionEnum_internal.MODEL_VIEW));
		model.addObject(subContextD);
		
		SET<IfcRepresentationContext> contexts = new SET<>();
		contexts.add((IfcRepresentationContext)context);
		// ==========================
		
		// ===== CORE HIERARCHY =====
		IfcProject project = schemaFlag ? new IfcProject.Ifc2x3.Instance() : new IfcProject.Ifc4.Instance();
		project.setGlobalId(
				schemaFlag ? 
					new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
						new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
		);
		project.setRepresentationContexts(contexts);
		project.setOwnerHistory(history);
		project.setName(schemaFlag ? new IfcLabel.Ifc2x3("IFC Extrusion Model", false) : new IfcLabel.Ifc4("IFC Extrusion Model", false));
		project.setLongName(schemaFlag ? new IfcLabel.Ifc2x3("Project Longname", false) : new IfcLabel.Ifc4("Project Longname", false));
		project.setDescription(
				schemaFlag ? 
					new IfcText.Ifc2x3("A model created by using XPlanung PolygonType faces for extrusion.", false) : 
						new IfcText.Ifc4("A model created by using XPlanung PolygonType faces for extrusion.", false));
		project.setUnitsInContext(unitAssignment);
		project.setPhase(schemaFlag ? new IfcLabel.Ifc2x3("Project Status", false) : new IfcLabel.Ifc4("Project Status", false));
		model.addObject(project);

		IfcSite site = schemaFlag ? new IfcSite.Ifc2x3.Instance() : new IfcSite.Ifc4.Instance();
		site.setOwnerHistory(history);
		site.setCompositionType(
				schemaFlag ? 
					new IfcElementCompositionEnum.Ifc2x3(IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.ELEMENT) :
						new IfcElementCompositionEnum.Ifc4(IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.ELEMENT)
		);
		site.setGlobalId(
				schemaFlag ? 
					new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
						new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
		);
		site.setName(schemaFlag ? new IfcLabel.Ifc2x3("Site of Extrusion Geometry", false) : new IfcLabel.Ifc4("Site of Extrusion Geometry", false));
		//site.setObjectPlacement((IfcObjectPlacement)defaultLocalPlacement);
		model.addObject(site);
		
		SET<IfcObjectDefinition> related = new SET<>();
		related.add((IfcObjectDefinition)site);
		
		IfcRelAggregates relAggregates = schemaFlag ? new IfcRelAggregates.Ifc2x3.Instance() : new IfcRelAggregates.Ifc4.Instance();
		relAggregates.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
		);
		relAggregates.setOwnerHistory(history);
		relAggregates.setRelatingObject((IfcObjectDefinition)project);
		relAggregates.setRelatedObjects(related);
		model.addObject(relAggregates);
		
		
		IfcBuilding building = schemaFlag ? new IfcBuilding.Ifc2x3.Instance() : new IfcBuilding.Ifc4.Instance();
		building.setCompositionType(
				schemaFlag ? 
					new IfcElementCompositionEnum.Ifc2x3(IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.ELEMENT) :
						new IfcElementCompositionEnum.Ifc4(IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.ELEMENT)
		);
		building.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
		);
		building.setObjectPlacement((IfcObjectPlacement)defaultLocalPlacement);
		model.addObject(building);
		
		SET<IfcObjectDefinition> buildingProducts = new SET<>();
		buildingProducts.add((IfcObjectDefinition)building);

		IfcRelAggregates relAggregates2 = schemaFlag ? new IfcRelAggregates.Ifc2x3.Instance() : new IfcRelAggregates.Ifc4.Instance();
		relAggregates2.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
		);
		relAggregates2.setOwnerHistory(history);
		relAggregates2.setRelatingObject((IfcObjectDefinition)site);
		relAggregates2.setRelatedObjects(buildingProducts);
		model.addObject(relAggregates2);
		// ==========================
		
		// ==== BUIDING PROXY ELEMENTS ====
		SET<IfcProduct> products = new SET<>();
		for(AbstractFeatureType feature : this.features) {
		
			BeanInfo beanInfo = null;
			try {
				beanInfo = Introspector.getBeanInfo(feature.getClass());
			} catch (IntrospectionException e1) {
				e1.printStackTrace();
			}
			
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			
			HashMap<String, PropertyDescriptor> propertyMap = new HashMap<>(propertyDescriptors.length);
			Arrays.stream(propertyDescriptors).forEach(p -> {
				propertyMap.put(p.getName().toLowerCase(), p);
			});
			
			//retrive hight if it is deffined
			PropertyDescriptor propDiscH = propertyMap.get("hoehenangabe");
			Double val = findExtrusionValue(feature, propDiscH);

			if(val.isNaN()) {
				PropertyDescriptor propDiscZ = propertyMap.get("z");
				val = findExtrusionValue(feature, propDiscZ) * storageHeight;			
			}
			
			if(!val.isNaN()) {
			
				val += basementHeight;
				
				List<MultiAppearanceShape3D> shape3ds = XPlanungModel.getCadObjOfFeature(feature).getSolidShapes();
				
				Transform3D transform3d = new Transform3D();
				Vector3f normalDirection = new Vector3f(0f, 0f, 1f);
				
				double[] offset = xplanungModel.getLocalTranslation();
				
				for(MultiAppearanceShape3D shape3d : shape3ds) {
					
					GeometryInfo info = new GeometryInfo(((GeometryArray)shape3d.getGeometry()));
					info.indexify(true);
					info.compact();
					
					NormalGenerator normalGenerator = new NormalGenerator();
					normalGenerator.generateNormals(info);
					
					shape3d.getLocalToVworld(transform3d);
					info.getGeometryArray().getNormal(0, normalDirection);

					HashSet<Point3f> pList = new HashSet<>(Arrays.asList(info.getCoordinates()));
					Point3f[] arrayPoints = new Point3f[pList.size()];
					pList.toArray(arrayPoints);
					
					if(arrayPoints.length > 3) {
						normalDirection = GeometryUtil.calculatePlaneNormal(arrayPoints[0], arrayPoints[1], arrayPoints[arrayPoints.length-1]);
					}
					
				}

				AbstractGeometryType geometryType = GeometryUtil.hasGeometry(feature);
				
				if(geometryType instanceof PolygonType) {
					PolygonType polygonType = (PolygonType)geometryType;
					
					if (polygonType.getExterior() != null) {
						AbstractRingPropertyType arpType = polygonType.getExterior();
						AbstractRingType exteriorRing = arpType.getAbstractRing().getValue(); //exterior ring

						ArrayList<Integer> stripCountList = new ArrayList<>();
						Collection<Point3d> coordinateList = new ArrayList<>();
						XPlanungCadLoader.ringTypeToPointList(coordinateList, stripCountList, exteriorRing); //fills list with information

						IfcPolyline polyline = schemaFlag ? new IfcPolyline.Ifc2x3.Instance() : new IfcPolyline.Ifc4.Instance();
						ArrayList<IfcCartesianPoint> pointList = new ArrayList<>();
						
						
						for(Point3d point : coordinateList) {
							
							LIST<IfcLengthMeasure> lengthMeasures = new LIST<>();
							lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getX()+offset[0]) : new IfcLengthMeasure.Ifc4(point.getX()+offset[0]));
							lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getY()+offset[1]) : new IfcLengthMeasure.Ifc4(point.getY()+offset[1]));
							lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getZ()) : new IfcLengthMeasure.Ifc4(point.getZ()));
							
							IfcCartesianPoint cartesianPoint = schemaFlag ? new IfcCartesianPoint.Ifc2x3.Instance(lengthMeasures) : new IfcCartesianPoint.Ifc4.Instance(lengthMeasures);
							model.addObject(cartesianPoint);
							
							pointList.add(cartesianPoint);
						}
						
						polyline.addAllPoints(pointList);
						model.addObject(polyline);
		
						IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = schemaFlag ? new IfcArbitraryClosedProfileDef.Ifc2x3.Instance() : new IfcArbitraryClosedProfileDef.Ifc4.Instance();
						arbitraryClosedProfileDef.setProfileType(
							schemaFlag ? 
								new IfcProfileTypeEnum.Ifc2x3(IfcProfileTypeEnum.Ifc2x3.IfcProfileTypeEnum_internal.CURVE) :
									new IfcProfileTypeEnum.Ifc4(IfcProfileTypeEnum.Ifc4.IfcProfileTypeEnum_internal.CURVE)
						);
						arbitraryClosedProfileDef.setOuterCurve((IfcCurve)polyline);
						model.addObject(arbitraryClosedProfileDef);
						
						LIST<IfcReal> ifcReals = new LIST<>();
						ifcReals.add(schemaFlag ? new IfcReal.Ifc2x3(normalDirection.getX()) : new IfcReal.Ifc4(normalDirection.getX()));
						ifcReals.add(schemaFlag ? new IfcReal.Ifc2x3(normalDirection.getY()) : new IfcReal.Ifc4(normalDirection.getY()));
						ifcReals.add(schemaFlag ? new IfcReal.Ifc2x3(normalDirection.getZ()) : new IfcReal.Ifc4(normalDirection.getZ()));
						
						IfcDirection direction = schemaFlag ? new IfcDirection.Ifc2x3.Instance(ifcReals) : new IfcDirection.Ifc4.Instance(ifcReals);
						model.addObject(direction);
						
						IfcExtrudedAreaSolid extrudedAreaSolid = schemaFlag ? new IfcExtrudedAreaSolid.Ifc2x3.Instance() : new IfcExtrudedAreaSolid.Ifc4.Instance();
						extrudedAreaSolid.setSweptArea((IfcProfileDef)arbitraryClosedProfileDef);
						extrudedAreaSolid.setExtrudedDirection(direction);
						extrudedAreaSolid.setPosition(defaultPlacement);
						
						// calculate position, also for basement
						IfcAxis2Placement3D placement3d = schemaFlag ? new IfcAxis2Placement3D.Ifc2x3.Instance() : new IfcAxis2Placement3D.Ifc4.Instance(); 
						model.addObject(placement3d);
						
						if(true) {

							float basementX = -1 * normalDirection.getX() * basementHeight;
							float basementY = -1 * normalDirection.getY() * basementHeight;
							float basementZ = -1 * normalDirection.getZ() * basementHeight;
							
							LIST<IfcLengthMeasure> lengthMeasures = new LIST<>();
							lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(basementX) : new IfcLengthMeasure.Ifc4(basementX));
							lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(basementY) : new IfcLengthMeasure.Ifc4(basementY));
							lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(basementZ) : new IfcLengthMeasure.Ifc4(basementZ));

							IfcCartesianPoint basementTranslate = schemaFlag ? new IfcCartesianPoint.Ifc2x3.Instance(lengthMeasures) : new IfcCartesianPoint.Ifc4.Instance(lengthMeasures);
							model.addObject(basementTranslate);

							placement3d.setLocation(basementTranslate);
							
							extrudedAreaSolid.setPosition(placement3d);
						}

						extrudedAreaSolid.setDepth(schemaFlag ? new IfcPositiveLengthMeasure.Ifc2x3(val.doubleValue()) : new IfcPositiveLengthMeasure.Ifc4(val.doubleValue()));;
						model.addObject(extrudedAreaSolid);

						SET<IfcRepresentationItem> representations = new SET<>();
						representations.add((IfcRepresentationItem)extrudedAreaSolid);
												
						//change appearance
						IfcUtil.addColorToModel(new Color3f(0.55f, 0.65f, 0.95f), model, representations, 0.15, schemaFlag);
						
						IfcShapeRepresentation shapeRepresentation = schemaFlag ? new IfcShapeRepresentation.Ifc2x3.Instance() : new IfcShapeRepresentation.Ifc4.Instance();
						shapeRepresentation.setItems(representations);
						shapeRepresentation.setContextOfItems((IfcRepresentationContext)subContextB);
						
						// ==== !!!IMPORTENT FOR DESITE MD IMPORT!!! ====
						/*
						 * Must be set and defined in this specific way! Otherwise it will cause missing hierarchy entries.
						 */
						shapeRepresentation.setRepresentationIdentifier(schemaFlag ? new IfcLabel.Ifc2x3("Body", false) : new IfcLabel.Ifc4("Body", false));
						shapeRepresentation.setRepresentationType(schemaFlag ? new IfcLabel.Ifc2x3("SweptSolid", false) : new IfcLabel.Ifc4("SweptSolid", false));
						// ============================================== 
						
						model.addObject(shapeRepresentation);
					
						LIST<IfcRepresentation> ifcRepresentations = new LIST<>();
						ifcRepresentations.add((IfcRepresentation)shapeRepresentation);

						IfcProductDefinitionShape productRepresentation = schemaFlag ? new IfcProductDefinitionShape.Ifc2x3.Instance() : new IfcProductDefinitionShape.Ifc4.Instance();
						productRepresentation.setRepresentations(ifcRepresentations);
						model.addObject(productRepresentation);
						
						IfcBuildingElementProxy proxy = schemaFlag ? new IfcBuildingElementProxy.Ifc2x3.Instance() : new IfcBuildingElementProxy.Ifc4.Instance();
						proxy.setGlobalId(
								schemaFlag ? 
									new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
										new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
						);
						proxy.setName(schemaFlag ? new IfcLabel.Ifc2x3(feature.getId(), false) : new IfcLabel.Ifc4(feature.getId(), false));
						proxy.setOwnerHistory(history);
						proxy.setRepresentation((IfcProductRepresentation)productRepresentation);
						proxy.setObjectPlacement((IfcObjectPlacement)defaultLocalPlacement);
						model.addObject(proxy);
						
						//Add Propeties to model
						IfcUtil.addFeaturePropertiesToModel(model, (IfcObject)proxy, feature, complexProperties);
						
						products.add((IfcProduct)proxy);
					}
				}
			}
		}
		
		IfcRelContainedInSpatialStructure containedInSpatialStructureA = 
			schemaFlag ? 
				new IfcRelContainedInSpatialStructure.Ifc2x3.Instance(
					new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false),
					history, 
					null,
					null,
					products,
					(IfcSpatialStructureElement)building
				) : 
				new IfcRelContainedInSpatialStructure.Ifc4.Instance(
					new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false),
					history, 
					null,
					null,
					products,
					(IfcSpatialElement)building	
		);		
		model.addObject(containedInSpatialStructureA);
		// ==========================
		
		// ======= LOAD MODEL =======
		//Set parameter for creating the applicationmodelnode		
		HashMap<String, Object> pMap = new HashMap<>();
		pMap.put("MODEL_NAME", "Extrusion Model");
		
		IfcLoadManager.getInstance().loadStepModel(model, false, pMap);
		// ==========================
	}
		
	private Double findExtrusionValue(AbstractFeatureType type, String propName) {
		Object returnVal = null;
		if (propName != null && propName.length() >= 1) {
			String methodName = "get" + propName.substring(0, 1).toUpperCase();
			if (propName.length() >= 2) {
				methodName += propName.substring(1, propName.length());
			}

			Method m = null;
			try {
				m = type.getClass().getMethod(methodName);
				returnVal = m.invoke(type);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// e.printStackTrace();
			}

		}
		
		
		if(returnVal instanceof ArrayList) {
			ArrayList list = (ArrayList)returnVal;
			if(list.size() == 1) {
				Object o = list.get(0);
				if(o instanceof de.xplanung.xplangml._4._1.XPHoehenangabePropertyType) {
					return ((de.xplanung.xplangml._4._1.XPHoehenangabePropertyType)o).getXPHoehenangabe().getH().getValue();
				}
				
				if(o instanceof de.xplanung.xplangml._5._0.XPHoehenangabePropertyType) {
					return ((de.xplanung.xplangml._5._0.XPHoehenangabePropertyType)o).getXPHoehenangabe().getH().getValue();
				}
			}
			
		}
		
		return Double.NaN;
	}
	
	private Double findExtrusionValue(AbstractFeatureType type, PropertyDescriptor propDisc) {
		Object returnVal = null;
		if (propDisc != null) {
			Method m = propDisc.getReadMethod();
			try {
				returnVal = m.invoke(type);
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// e.printStackTrace();
			}

		}
		
		if(returnVal instanceof ArrayList) {
			ArrayList list = (ArrayList)returnVal;
			if(list.size() == 1) {
				Object o = list.get(0);
				if(o instanceof de.xplanung.xplangml._4._1.XPHoehenangabePropertyType) {
					return ((de.xplanung.xplangml._4._1.XPHoehenangabePropertyType)o).getXPHoehenangabe().getH().getValue();
				}
				
				if(o instanceof de.xplanung.xplangml._5._0.XPHoehenangabePropertyType) {
					return ((de.xplanung.xplangml._5._0.XPHoehenangabePropertyType)o).getXPHoehenangabe().getH().getValue();
				}
			}
			
		}
		
		if(returnVal instanceof Number) {
			return ((Number)returnVal).doubleValue();
		}
		
		return Double.NaN;
	}
}
