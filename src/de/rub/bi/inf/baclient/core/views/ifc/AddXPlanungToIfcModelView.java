package de.rub.bi.inf.baclient.core.views.ifc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement3D;
import com.apstex.ifctoolbox.ifc.IfcBoolean;
import com.apstex.ifctoolbox.ifc.IfcBuilding;
import com.apstex.ifctoolbox.ifc.IfcBuildingElement;
import com.apstex.ifctoolbox.ifc.IfcBuildingElementProxy;
import com.apstex.ifctoolbox.ifc.IfcCartesianPoint;
import com.apstex.ifctoolbox.ifc.IfcConnectedFaceSet;
import com.apstex.ifctoolbox.ifc.IfcElementCompositionEnum;
import com.apstex.ifctoolbox.ifc.IfcFace;
import com.apstex.ifctoolbox.ifc.IfcFaceBasedSurfaceModel;
import com.apstex.ifctoolbox.ifc.IfcFaceBound;
import com.apstex.ifctoolbox.ifc.IfcFaceOuterBound;
import com.apstex.ifctoolbox.ifc.IfcGloballyUniqueId;
import com.apstex.ifctoolbox.ifc.IfcLabel;
import com.apstex.ifctoolbox.ifc.IfcLengthMeasure;
import com.apstex.ifctoolbox.ifc.IfcLocalPlacement;
import com.apstex.ifctoolbox.ifc.IfcLoop;
import com.apstex.ifctoolbox.ifc.IfcObject;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcObjectPlacement;
import com.apstex.ifctoolbox.ifc.IfcPolyLoop;
import com.apstex.ifctoolbox.ifc.IfcProduct;
import com.apstex.ifctoolbox.ifc.IfcProductDefinitionShape;
import com.apstex.ifctoolbox.ifc.IfcProductRepresentation;
import com.apstex.ifctoolbox.ifc.IfcRelAggregates;
import com.apstex.ifctoolbox.ifc.IfcRelContainedInSpatialStructure;
import com.apstex.ifctoolbox.ifc.IfcRepresentation;
import com.apstex.ifctoolbox.ifc.IfcRepresentationItem;
import com.apstex.ifctoolbox.ifc.IfcShapeRepresentation;
import com.apstex.ifctoolbox.ifc.IfcSite;
import com.apstex.ifctoolbox.ifc.IfcSpatialElement;
import com.apstex.ifctoolbox.ifc.IfcSpatialStructureElement;
import com.apstex.ifctoolbox.ifc.IfcText;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.IndexedGeometryArray;
import com.apstex.javax.media.j3d.TriangleArray;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.step.core.LIST;
import com.apstex.step.core.SET;
import com.apstex.step.guidcompressor.GuidCompressor;

import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadLoader;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import de.rub.bi.inf.baclient.core.model.ChoiceProperty;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.utils.PropertyUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.PolygonType;

public class AddXPlanungToIfcModelView extends ChoiceDialog{
	
	public AddXPlanungToIfcModelView(ChoiceProperty<IfcModel> initAppNode, List<ChoiceProperty<IfcModel>> collection, AbstractFeatureType feature, double[] offset) {
		this(initAppNode, collection, Arrays.asList(feature), offset);
	}
	
	public AddXPlanungToIfcModelView(ChoiceProperty<IfcModel> initAppNode, List<ChoiceProperty<IfcModel>> collection, List<AbstractFeatureType> features, double[] offset) {
		super(initAppNode, collection);
		setTitle("Load XPlanung into IfcModel");
		setHeaderText("The selected XPlanung elements of the project tree will be added to the choosen IfcModel.");
		setContentText("Currently selected:");
		
		CheckBox box = new CheckBox();
		
		TextField modelTitel = new TextField();
		modelTitel.setPromptText("New Model");
		modelTitel.setText("New Model");
		
		Node s = getDialogPane().getContent();
		if(s instanceof GridPane) {
			((GridPane)s).setHgap(10);
			((GridPane)s).setVgap(10);
			((GridPane)s).setPadding(new Insets(20, 150, 10, 10));
			((GridPane)s).add(new Label("Titel of New Model:"), 0, 1);
			((GridPane)s).add(modelTitel, 1, 1);
			((GridPane)s).add(new Label("Use Complex Properties:"), 0, 2);
			((GridPane)s).add(box, 1, 2);
		}
		
		
		//Extend property map
		PropertyUtils.load(features);
		
		Optional<ChoiceProperty<IfcModel>> result = showAndWait();
		if (result.isPresent()){
			
			ChoiceProperty<IfcModel> choosen = result.get();
			this.addToIfc(choosen.getValue(), features, offset, box.isSelected());
			
			HashMap<String, Object> propertyMap = new HashMap<>();
			propertyMap.put("MODEL_NAME", modelTitel.getText());

			//Reload model
			for(ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
				if(node.getStepModel().equals(choosen.getValue())) {		
					propertyMap.put("MODEL_NAME", node.getModelName());
					Kernel.getApplicationModelRoot().removeNode(node);
				}
			}
			IfcLoadManager.getInstance().loadStepModel(choosen.getValue(), false, propertyMap);
			
		}
	}
	
	private void addToIfc(IfcModel model, List<AbstractFeatureType> features, double[] offset, boolean complexProperties) {
		for(AbstractFeatureType feature : features) {				
			addToIfc(model, feature, offset, complexProperties);
		}
	}
	
	private String getLevel(AbstractFeatureType aType) {
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(aType.getClass());
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		
		HashMap<String, PropertyDescriptor> propertyMap = new HashMap<>(propertyDescriptors.length);
		Arrays.stream(propertyDescriptors).forEach(p -> {
			propertyMap.put(p.getName().toLowerCase(), p);
		});
		
		PropertyDescriptor ebene = propertyMap.get("ebene");
		Object iVal = null;
		
		if(ebene != null) {
			try {
				iVal = ebene.getReadMethod().invoke(aType);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		if(iVal != null) {	
			return "Ebene: " + iVal.toString();
		}
		
		return "Ebene: NoLevel";
	}
	
	private void addToIfc(IfcModel model, AbstractFeatureType feature, double[] offset, boolean complexProperties) {
		boolean schemaFlag = !model.getFile_SchemaString().equals("IFC4");
		
		IfcSite parentSite = null;
		
		IfcBuilding xplanungSite = null;
		IfcBuilding site = null;
		
		Collection<IfcSite> sites = model.getCollection(IfcSite.class);
		for(IfcSite s : sites) {
			parentSite = s;
			break;
		}
		
		String xPlanungGroupName = getLevel(feature);
		
		Collection<IfcBuilding> buildings = model.getCollection(IfcBuilding.class);
		for(IfcBuilding s : buildings) {
			
			if(schemaFlag ?
					s.getCompositionType().getValue().equals(IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.COMPLEX) :
						s.getCompositionType().getValue().equals(IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.COMPLEX)) {
				
				String name = s.getName().getDecodedValue();
				if(name != null) {
					if(name.equals(xPlanungGroupName)) {	
						xplanungSite = s;
					}					
				}
			}
			
			if(schemaFlag ?
					s.getCompositionType().getValue().equals(IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.PARTIAL) :
						s.getCompositionType().getValue().equals(IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.PARTIAL)) {
				if(s.getName().getDecodedValue().equals(feature.getClass().getSimpleName())) {
					site = s;
				}
			}
		}
		
		if(parentSite == null) {
			System.err.println("Model does not contain parent site! XPlanung counld not be appended.");
			return;
		}
		
		if(xplanungSite == null) {
			xplanungSite = schemaFlag ? new IfcBuilding.Ifc2x3.Instance() : new IfcBuilding.Ifc4.Instance();
			xplanungSite.setCompositionType(
					schemaFlag ? new IfcElementCompositionEnum.Ifc2x3(
							IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.COMPLEX
					) : 
					new IfcElementCompositionEnum.Ifc4(
							IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.COMPLEX
					)
			);
			xplanungSite.setName(schemaFlag ? new IfcLabel.Ifc2x3(xPlanungGroupName, true) : new IfcLabel.Ifc4(xPlanungGroupName, true));
			xplanungSite.setGlobalId(
					schemaFlag ? 
							new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) :
								new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true)
			);
			model.addObject(xplanungSite);
		
			SET<IfcObjectDefinition> related = new SET<>();
			related.add((IfcObjectDefinition)xplanungSite);
			
			IfcRelAggregates relAggregates = schemaFlag ? new IfcRelAggregates.Ifc2x3.Instance() : new IfcRelAggregates.Ifc4.Instance();
			relAggregates.setGlobalId(
					schemaFlag ? 
							new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) :
								new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true)
			);
			relAggregates.setOwnerHistory(model.getIfcProject().getOwnerHistory());
			relAggregates.setRelatingObject((IfcObjectDefinition)parentSite);
			relAggregates.setRelatedObjects(related);
			model.addObject(relAggregates);
		}
		
		if(site == null) {
			site = schemaFlag ? new IfcBuilding.Ifc2x3.Instance() : new IfcBuilding.Ifc4.Instance();
			site.setCompositionType(
					schemaFlag ? new IfcElementCompositionEnum.Ifc2x3(
							IfcElementCompositionEnum.Ifc2x3.IfcElementCompositionEnum_internal.PARTIAL
					) : 
					new IfcElementCompositionEnum.Ifc4(
							IfcElementCompositionEnum.Ifc4.IfcElementCompositionEnum_internal.PARTIAL
					)
			);
			site.setGlobalId(
					schemaFlag ? 
							new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) :
								new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true)
			);
			site.setName(schemaFlag ? new IfcLabel.Ifc2x3(feature.getClass().getSimpleName(), true) : new IfcLabel.Ifc4(feature.getClass().getSimpleName(), true));
			model.addObject(site);
		
			SET<IfcObjectDefinition> related = new SET<>();
			related.add((IfcObjectDefinition)site);
			
			IfcRelAggregates relAggregates = schemaFlag ? new IfcRelAggregates.Ifc2x3.Instance() : new IfcRelAggregates.Ifc4.Instance();
			relAggregates.setGlobalId(
					schemaFlag ? 
							new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) :
								new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true)
			);
			relAggregates.setOwnerHistory(model.getIfcProject().getOwnerHistory());
			relAggregates.setRelatingObject((IfcObjectDefinition)xplanungSite);
			relAggregates.setRelatedObjects(related);
			model.addObject(relAggregates);
		}
		
		LIST<IfcLengthMeasure> positionMeasures = new LIST<>();
		positionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		positionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		positionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		
		IfcCartesianPoint positionPoint = schemaFlag ? new IfcCartesianPoint.Ifc2x3.Instance(positionMeasures) : new IfcCartesianPoint.Ifc4.Instance(positionMeasures);
		model.addObject(positionPoint);

		IfcAxis2Placement3D defaultAxis = schemaFlag ? new IfcAxis2Placement3D.Ifc2x3.Instance() : new IfcAxis2Placement3D.Ifc4.Instance();
		defaultAxis.setLocation(positionPoint);
		model.addObject(defaultAxis);
		
		IfcLocalPlacement defaultPlacement = schemaFlag ? new IfcLocalPlacement.Ifc2x3.Instance() : new IfcLocalPlacement.Ifc4.Instance();
		defaultPlacement.setRelativePlacement((IfcAxis2Placement)defaultAxis);
		model.addObject(defaultPlacement);
		
		SET<IfcProduct> products = new SET<>();		
		SET<IfcObjectDefinition> productObjDef = new SET<>();		
		
		//Create model object
		IfcBuildingElement proxy = schemaFlag ? new IfcBuildingElementProxy.Ifc2x3.Instance() : new IfcBuildingElementProxy.Ifc4.Instance();
		proxy.setGlobalId(
				schemaFlag ? 
						new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true) :
							new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true)
		);
		proxy.setName(schemaFlag ? new IfcLabel.Ifc2x3(feature.getId(), true) : new IfcLabel.Ifc4(feature.getId(), true));
		proxy.setOwnerHistory(model.getIfcProject().getOwnerHistory());
		proxy.setObjectPlacement((IfcObjectPlacement)defaultPlacement);
		proxy.setObjectType(schemaFlag ? new IfcLabel.Ifc2x3(feature.getClass().getSimpleName(), true) : new IfcLabel.Ifc4(feature.getClass().getSimpleName(), true));					
		model.addObject(proxy);
		
		//Add Propeties to model
		IfcUtil.addFeaturePropertiesToModel(model, (IfcObject)proxy, feature, complexProperties);
		
		products.add((IfcProduct)proxy);
		productObjDef.add((IfcObjectDefinition)proxy);
		
		
		AbstractGeometryType geometryType = GeometryUtil.hasGeometry(feature);
		if(geometryType instanceof PolygonType) {
			
			XPlanungCadObjectJ3D obj3d = XPlanungModel.getCadObjOfFeature(feature);
			MultiAppearanceShape3D shape3d = obj3d.getInnerShape3ds().get(0);
			
			if(shape3d.getGeometry() instanceof TriangleArray) {
				TriangleArray triArr = (TriangleArray)shape3d.getGeometry();
				
				GeometryInfo geometryInfo = new GeometryInfo(triArr);

				geometryInfo.convertToIndexedTriangles();
				IndexedGeometryArray ga = geometryInfo.getIndexedGeometryArray(true);
				
				int[] triangles = new int[ga.getValidIndexCount()];
				ga.getCoordinateIndices(0, triangles);
				
				
				SET<IfcFace> faces = new SET<>();
				for(int trianlgeIndex = 0; trianlgeIndex < triangles.length; trianlgeIndex += 3) {
					//IfcPolyline polyline = schemaFlag ? new IfcPolyline.Ifc2x3.Instance() : new IfcPolyline.Ifc4.Instance();
					
					int[] triangle = new int[3];
					triangle[0] = triangles[trianlgeIndex];
					triangle[1] = triangles[trianlgeIndex+1];
					triangle[2] = triangles[trianlgeIndex+2];
					
					LIST<IfcCartesianPoint> pointList = new LIST<>();
					
					for(int index : triangle) {
						
						Point3f point = new Point3f();
						ga.getCoordinate(index, point);
						
						LIST<IfcLengthMeasure> lengthMeasures = new LIST<>();
						lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getX()) : new IfcLengthMeasure.Ifc4(point.getX()));
						lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getY()) : new IfcLengthMeasure.Ifc4(point.getY()));
						
						//add to code for offset 
						//lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getX()+offset[0]) : new IfcLengthMeasure.Ifc4(point.getX()+offset[0]));
						//lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getY()+offset[1]) : new IfcLengthMeasure.Ifc4(point.getY()+offset[1]));
						
						lengthMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(point.getZ()) : new IfcLengthMeasure.Ifc4(point.getZ()));
						
						IfcCartesianPoint cartesianPoint = schemaFlag ? new IfcCartesianPoint.Ifc2x3.Instance(lengthMeasures) : new IfcCartesianPoint.Ifc4.Instance(lengthMeasures);
						model.addObject(cartesianPoint);
						
						pointList.add(cartesianPoint);
					}
					
					//polyline.addAllPoints(pointList);
					//model.addObject(polyline);
					
					//Create geometry representation
					IfcPolyLoop loop = schemaFlag ? new IfcPolyLoop.Ifc2x3.Instance(pointList) : new IfcPolyLoop.Ifc4.Instance(pointList);
					model.addObject(loop);
					
					IfcFaceOuterBound outerBound = schemaFlag ? new IfcFaceOuterBound.Ifc2x3.Instance((IfcLoop)loop, new IfcBoolean.Ifc2x3(true)) : new IfcFaceOuterBound.Ifc4.Instance((IfcLoop)loop, new IfcBoolean.Ifc4(true));
					model.addObject(outerBound);

					SET<IfcFaceBound> bounds = new SET<>();
					bounds.add((IfcFaceBound)outerBound);
				
					IfcFace face = schemaFlag ? new IfcFace.Ifc2x3.Instance(bounds) : new IfcFace.Ifc4.Instance(bounds);
					model.addObject(face);

					faces.add(face);
				}
				
				IfcConnectedFaceSet faceSet = schemaFlag ? new IfcConnectedFaceSet.Ifc2x3.Instance(faces) : new IfcConnectedFaceSet.Ifc4.Instance(faces);
				model.addObject(faceSet);
				
				SET<IfcConnectedFaceSet> faceSets = new SET<>();
				faceSets.add(faceSet);
				
				IfcFaceBasedSurfaceModel surfaceModel = schemaFlag ? new IfcFaceBasedSurfaceModel.Ifc2x3.Instance(faceSets) : new IfcFaceBasedSurfaceModel.Ifc4.Instance(faceSets);
				model.addObject(surfaceModel);
				
				SET<IfcRepresentationItem> representations = new SET<>();
				representations.add((IfcRepresentationItem)surfaceModel);
				
				//change appearance
				Color3f color3f = XPlanungCadLoader.getColorForType(feature.getClass().getSimpleName());
				IfcUtil.addColorToModel(color3f, model, representations, 0.0, schemaFlag);
				
				IfcShapeRepresentation shapeRepresentation = schemaFlag ? new IfcShapeRepresentation.Ifc2x3.Instance() : new IfcShapeRepresentation.Ifc4.Instance();
				shapeRepresentation.setItems(representations);
				
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

				IfcProductRepresentation productRepresentation = schemaFlag ? new IfcProductDefinitionShape.Ifc2x3.Instance() : new IfcProductDefinitionShape.Ifc4.Instance();
				productRepresentation.setRepresentations(ifcRepresentations);
				model.addObject(productRepresentation);
				
				//register representation in proxy
				proxy.setRepresentation(productRepresentation);
				
			}	
		}
		
		IfcRelContainedInSpatialStructure containedInSpatialStructure = schemaFlag ? 
			new IfcRelContainedInSpatialStructure.Ifc2x3.Instance(
				new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), true),
				model.getIfcProject().getOwnerHistory(), 
				new IfcLabel.Ifc2x3("XPlanung_To_IfcBuilding", true),
				new IfcText.Ifc2x3("Connection between the XPlanung feature models and IFC structures", true),
				products,
				(IfcSpatialStructureElement)site
			) : new IfcRelContainedInSpatialStructure.Ifc4.Instance(
				new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), true),
				model.getIfcProject().getOwnerHistory(), 
				new IfcLabel.Ifc4("XPlanung_To_IfcBuilding", true),
				new IfcText.Ifc4("Connection between the XPlanung feature models and IFC structures", true),
				products,
				(IfcSpatialElement)site
			);
		model.addObject(containedInSpatialStructure);			
		
	}	
}
