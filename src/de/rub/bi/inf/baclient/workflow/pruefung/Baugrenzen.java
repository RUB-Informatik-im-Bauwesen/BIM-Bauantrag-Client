package de.rub.bi.inf.baclient.workflow.pruefung;

import java.awt.Color;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.apstex.gm3d.bool4.BooleanModeller4;
import com.apstex.gm3d.bool6.BooleanModeller6;
import com.apstex.gm3d.bool6.BooleanModeller6a;
import com.apstex.gm3d.core.Solid;
import com.apstex.gm3d.core.TriFace;
import com.apstex.gm3d.core.TriSolid;
import com.apstex.gm3d.util.PointInShape3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.ifc.controller.IfcLoadManager;
import com.apstex.ifctoolbox.ifc.IfcApplication;
import com.apstex.ifctoolbox.ifc.IfcArbitraryClosedProfileDef;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement;
import com.apstex.ifctoolbox.ifc.IfcAxis2Placement3D;
import com.apstex.ifctoolbox.ifc.IfcBuilding;
import com.apstex.ifctoolbox.ifc.IfcBuildingElementProxy;
import com.apstex.ifctoolbox.ifc.IfcCartesianPoint;
import com.apstex.ifctoolbox.ifc.IfcChangeActionEnum;
import com.apstex.ifctoolbox.ifc.IfcCurve;
import com.apstex.ifctoolbox.ifc.IfcDimensionCount;
import com.apstex.ifctoolbox.ifc.IfcDirection;
import com.apstex.ifctoolbox.ifc.IfcElement;
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
import com.apstex.ifctoolbox.ifc.IfcShapeRepresentation;
import com.apstex.ifctoolbox.ifc.IfcSite;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.ifctoolbox.ifc.IfcSpatialElement;
import com.apstex.ifctoolbox.ifc.IfcSpatialStructureElement;
import com.apstex.ifctoolbox.ifc.IfcText;
import com.apstex.ifctoolbox.ifc.IfcTimeStamp;
import com.apstex.ifctoolbox.ifc.IfcUnitAssignment;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.j3d.utils.geometry.NormalGenerator;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.IndexedTriangleArray;
import com.apstex.javax.media.j3d.Shape3D;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.media.j3d.TriangleArray;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3d;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.javax.vecmath.Vector3f;
import com.apstex.loader3d.core.CadTreeNode;
import com.apstex.loader3d.ifc.IfcLoader;
import com.apstex.loader3d.ifc.IfcUnitConverter;
import com.apstex.step.core.LIST;
import com.apstex.step.core.SET;
import com.apstex.step.guidcompressor.GuidCompressor;

import de.rub.bi.inf.baclient.core.geometry.CustomCadObjectJ3D;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadLoader;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.utils.GeometryUtil;
import de.rub.bi.inf.baclient.core.utils.IfcSpaceUtil;
import de.rub.bi.inf.baclient.core.utils.PropertyUtils;
import de.rub.bi.inf.baclient.core.views.XViewer;
import de.rub.bi.inf.baclient.workflow.extraktion.PO_HoeheDerBaulichenAnlage;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.AbstractRingPropertyType;
import net.opengis.gml._3.AbstractRingType;
import net.opengis.gml._3.PolygonType;
import net.opengis.gml._3.ReferenceType;

public class Baugrenzen extends Pruefvorgang {
	
	public Baugrenzen() {
		this.name="Baugrenzen";
	}

	@Override
	public void pruefe(ApplicationModelNode ifcModel, AbstractFeatureType bpBereich) {
		
		this.modelNode = ifcModel;
		
		if (bpBereich==null)
			return;
	
		
		
		Object planinhaltObj = invokeMethodByName("getPlaninhalt", bpBereich);
		List<ReferenceType> planinhalt=(List<ReferenceType>) planinhaltObj;
		
	
		if(planinhalt==null)
			return;
		
		ArrayList<AbstractFeatureType> uebgfs = new ArrayList<>();
		
		planinhalt.forEach(ref -> {
			
			AbstractFeatureType featureType = this.xPlanungModel.getFeatureById(ref.getHref().substring(1));
			
			if (featureType.getClass().getName().contains("BPUeberbaubareGrundstuecksFlaecheType")) {
				
				Integer level = (Integer) invokeMethodByName("getEbene", featureType);

				if (level >= 0) {
					uebgfs.add(featureType);
				}
				
				
			}
	
		});
		
		
		Shape union = getUnionShape(uebgfs);
		
		
		System.out.println(union);
		
		
		Collection<IfcElement> allElements = ifcModel.getStepModel().getCollection(IfcElement.class);
		Collection<IfcElement> elementsToSelect = new ArrayList<IfcElement>();
		
		//---Visualisation for verifcations---BEGIN
		ArrayList<ArrayList<Point3f>> polylines = GeometryUtil.extract3DPolylineBoundary(union);				
		CadObjectJ3D ergebnisflaeche = new CustomCadObjectJ3D();
		
		PO_HoeheDerBaulichenAnlage hoeheDerBaulichenAnlage = new PO_HoeheDerBaulichenAnlage();
		hoeheDerBaulichenAnlage.perform(ifcModel, null);
		float hoehe = (float) (hoeheDerBaulichenAnlage.getHoeheDerBaulichenAnlage()-hoeheDerBaulichenAnlage.getTiefeDerBaulichenAnlage());
	
		
		polylines.forEach(pl -> {
			Point3f[] polyline = new Point3f[pl.size()];
			pl.toArray(polyline);
			
			IndexedTriangleArray geoArray = (IndexedTriangleArray) GeometryUtil.createPolygon(polyline, true);			
			
		
			TriSolid testSpace = (TriSolid) createExtrusion(pl, hoeheDerBaulichenAnlage.getTiefeDerBaulichenAnlage(), hoehe);
			
			Transform3D transform3d = this.modelNode.getCadObjectModel().getRootBranchGroup().getTransform();
			
			IfcUnitConverter unitConverter = new IfcUnitConverter((IfcModel) ifcModel.getStepModel());
			
//			PointInShape3D.EPSILON = 0.01;
			
			
			allElements.forEach(elem ->{
				CadTreeNode n1 = IfcLoader.interpretIfcProduct((IfcProduct) elem, unitConverter);
				
				if (n1.getShapes().size()>0) {
					
					TriSolid elementSolid = (TriSolid) n1.getShapes().iterator().next();
								
					boolean oneElemPointOutside = false;
					List<TriFace> faces = elementSolid.getFaces();
					for(TriFace triFace : faces) {
						for (Point3d point : triFace.getVertices()) {
							
							Point3d ptest = new Point3d(point);
							transform3d.transform(ptest);
							
						
							
							try {
								if (PointInShape3D.pointInShape3D(ptest, testSpace) == PointInShape3D.OUTSIDE) {
									oneElemPointOutside = true;
									break;
								}	
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						
						}
						if(oneElemPointOutside)
							break;
					}
					
					
					boolean oneTestSpacePointInside = false;
					faces = testSpace.getFaces();
					for(TriFace triFace : faces) {
						for (Point3d point : triFace.getVertices()) {
							
							Point3d ptest = new Point3d(point);
							transform3d.transform(ptest);
							
							int pointInShape = -1;
							
							try {
								PointInShape3D.pointInShape3D(ptest, elementSolid);	
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if ( pointInShape == PointInShape3D.INSIDE) {
//								if ( pointInShape == PointInShape3D.INSIDE || pointInShape == PointInShape3D.ON_BOUNDARY) {
									oneTestSpacePointInside = true;
									break;
								}
							
						}
						if(oneTestSpacePointInside)
							break;
					}

					
					if(oneElemPointOutside || oneTestSpacePointInside) { //outside
						elementsToSelect.add(elem);	
					}
					
		
				
				}
				
				
			});
		
		
			
		});
		
		modelNode.getSelectionModel().select(null, elementsToSelect);
		
		this.comment=elementsToSelect.size()+" Elemente außerhalb der Grundstücksgrenzen";

	}
	
	
	private Solid createExtrusion(ArrayList<Point3f> polyline, double baseHeight, float height) {
		

		boolean schemaFlag = false;
				
		IfcModel model = new IfcModel();
		
		IfcOrganization organization =  schemaFlag ? new IfcOrganization.Ifc2x3.Instance() : new IfcOrganization.Ifc4.Instance(); 
		organization.setDescription(schemaFlag ? new IfcText.Ifc2x3("", true) : new IfcText.Ifc4("", true));
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
		
//		LIST<IfcLengthMeasure> positionMeasures = new LIST<>();
//		positionMeasures.add(new IfcLengthMeasure.Ifc4(0));
//		positionMeasures.add(new IfcLengthMeasure.Ifc4(0));
//		positionMeasures.add(new IfcLengthMeasure.Ifc4(0));
//		
//		IfcCartesianPoint positionPoint = new IfcCartesianPoint.Ifc4.Instance(positionMeasures);
//		model.addObject(positionPoint);
//		
//		IfcAxis2Placement3D defaultPlacement = new IfcAxis2Placement3D.Ifc4.Instance();
//		defaultPlacement.setLocation(positionPoint);
//		model.addObject(defaultPlacement);
		
		Vector3f normalDirection = new Vector3f(0f, 0f, 1f);
		
		IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef.Ifc4.Instance();
		arbitraryClosedProfileDef.setProfileType(
					new IfcProfileTypeEnum.Ifc4(IfcProfileTypeEnum.Ifc4.IfcProfileTypeEnum_internal.CURVE)
		);
		
		IfcPolyline curve = new IfcPolyline.Ifc4.Instance();
		ArrayList<IfcCartesianPoint> pointList = new ArrayList<>();
	
		for(Point3f point : polyline) {
			
			LIST<IfcLengthMeasure> lengthMeasures = new LIST<>();
			lengthMeasures.add(new IfcLengthMeasure.Ifc4(point.getX()));
			lengthMeasures.add(new IfcLengthMeasure.Ifc4(point.getY()));
			lengthMeasures.add(new IfcLengthMeasure.Ifc4(point.getZ()));
			
			IfcCartesianPoint cartesianPoint = new IfcCartesianPoint.Ifc4.Instance(lengthMeasures);
			model.addObject(cartesianPoint);
			
			pointList.add(cartesianPoint);
		}
		
		curve.addAllPoints(pointList);
		model.addObject(curve);
		
		
		arbitraryClosedProfileDef.setOuterCurve((IfcCurve) curve);
		model.addObject(arbitraryClosedProfileDef);
		
		LIST<IfcReal> ifcReals = new LIST<>();
		ifcReals.add(new IfcReal.Ifc4(normalDirection.getX()));
		ifcReals.add(new IfcReal.Ifc4(normalDirection.getY()));
		ifcReals.add(new IfcReal.Ifc4(normalDirection.getZ()));
		
		IfcDirection direction =  new IfcDirection.Ifc4.Instance(ifcReals);
		model.addObject(direction);
		
		IfcExtrudedAreaSolid extrudedAreaSolid = new IfcExtrudedAreaSolid.Ifc4.Instance();
		extrudedAreaSolid.setSweptArea((IfcProfileDef)arbitraryClosedProfileDef);
		extrudedAreaSolid.setExtrudedDirection(direction);
		extrudedAreaSolid.setPosition(defaultPlacement);
		
		extrudedAreaSolid.setDepth(new IfcPositiveLengthMeasure.Ifc4(height));
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
		
		
		LIST<IfcLengthMeasure> proxyPositionMeasures = new LIST<>();
		proxyPositionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		proxyPositionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(0) : new IfcLengthMeasure.Ifc4(0));
		proxyPositionMeasures.add(schemaFlag ? new IfcLengthMeasure.Ifc2x3(baseHeight) : new IfcLengthMeasure.Ifc4(baseHeight));
		
		IfcCartesianPoint proxyPositionPoint = schemaFlag ? new IfcCartesianPoint.Ifc2x3.Instance(proxyPositionMeasures) : new IfcCartesianPoint.Ifc4.Instance(proxyPositionMeasures);
		model.addObject(proxyPositionPoint);
		
		IfcAxis2Placement3D proxyPlacement = schemaFlag ? new IfcAxis2Placement3D.Ifc2x3.Instance() : new IfcAxis2Placement3D.Ifc4.Instance();
		proxyPlacement.setLocation(proxyPositionPoint);
		model.addObject(proxyPlacement);
		
		IfcLocalPlacement proxyLocalPlacement = schemaFlag ? new IfcLocalPlacement.Ifc2x3.Instance() : new IfcLocalPlacement.Ifc4.Instance();
		proxyLocalPlacement.setRelativePlacement((IfcAxis2Placement) proxyPlacement);
		model.addObject(proxyLocalPlacement);
		
		
		IfcBuildingElementProxy proxy = schemaFlag ? new IfcBuildingElementProxy.Ifc2x3.Instance() : new IfcBuildingElementProxy.Ifc4.Instance();
		proxy.setGlobalId(
				schemaFlag ? 
					new IfcGloballyUniqueId.Ifc2x3(GuidCompressor.getNewIfcGloballyUniqueId(), false) :
						new IfcGloballyUniqueId.Ifc4(GuidCompressor.getNewIfcGloballyUniqueId(), false)
		);
		proxy.setName(schemaFlag ? new IfcLabel.Ifc2x3("TestSpace", false) : new IfcLabel.Ifc4("TestSpace", false));
		proxy.setOwnerHistory(history);
		proxy.setRepresentation((IfcProductRepresentation)productRepresentation);
		proxy.setObjectPlacement((IfcObjectPlacement)proxyLocalPlacement);
		model.addObject(proxy);

		
		products.add((IfcProduct)proxy);
	
		
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
		
		
		
		//Custom Code
//		IfcModel model = new IfcModel();
		IfcUnitConverter unitConverter = new IfcUnitConverter(model);
//		IfcBuildingElementProxy space1 = new IfcBuildingElementProxy.Ifc4.Instance();
	
		
//		LIST<IfcLengthMeasure> positionMeasures = new LIST<>();
//		positionMeasures.add(new IfcLengthMeasure.Ifc4(0));
//		positionMeasures.add(new IfcLengthMeasure.Ifc4(0));
//		positionMeasures.add(new IfcLengthMeasure.Ifc4(0));
//		
//		IfcCartesianPoint positionPoint = new IfcCartesianPoint.Ifc4.Instance(positionMeasures);
//		model.addObject(positionPoint);
//		
//		IfcAxis2Placement3D defaultPlacement = new IfcAxis2Placement3D.Ifc4.Instance();
//		defaultPlacement.setLocation(positionPoint);
//		model.addObject(defaultPlacement);
//		
//		Vector3f normalDirection = new Vector3f(0f, 0f, 1f);
//		
//		IfcArbitraryClosedProfileDef arbitraryClosedProfileDef = new IfcArbitraryClosedProfileDef.Ifc4.Instance();
//		arbitraryClosedProfileDef.setProfileType(
//					new IfcProfileTypeEnum.Ifc4(IfcProfileTypeEnum.Ifc4.IfcProfileTypeEnum_internal.CURVE)
//		);
//		
//		IfcPolyline curve = new IfcPolyline.Ifc4.Instance();
//		ArrayList<IfcCartesianPoint> pointList = new ArrayList<>();
//	
//		for(Point3f point : polyline) {
//			
//			LIST<IfcLengthMeasure> lengthMeasures = new LIST<>();
//			lengthMeasures.add(new IfcLengthMeasure.Ifc4(point.getX()));
//			lengthMeasures.add(new IfcLengthMeasure.Ifc4(point.getY()));
//			lengthMeasures.add(new IfcLengthMeasure.Ifc4(point.getZ()));
//			
//			IfcCartesianPoint cartesianPoint = new IfcCartesianPoint.Ifc4.Instance(lengthMeasures);
//			model.addObject(cartesianPoint);
//			
//			pointList.add(cartesianPoint);
//		}
//		
//		curve.addAllPoints(pointList);
//		model.addObject(curve);
//		
//		
//		arbitraryClosedProfileDef.setOuterCurve((IfcCurve) curve);
//		model.addObject(arbitraryClosedProfileDef);
//		
//		LIST<IfcReal> ifcReals = new LIST<>();
//		ifcReals.add(new IfcReal.Ifc4(normalDirection.getX()));
//		ifcReals.add(new IfcReal.Ifc4(normalDirection.getY()));
//		ifcReals.add(new IfcReal.Ifc4(normalDirection.getZ()));
//		
//		IfcDirection direction =  new IfcDirection.Ifc4.Instance(ifcReals);
//		model.addObject(direction);
//		
//		IfcExtrudedAreaSolid extrudedAreaSolid = new IfcExtrudedAreaSolid.Ifc4.Instance();
//		extrudedAreaSolid.setSweptArea((IfcProfileDef)arbitraryClosedProfileDef);
//		extrudedAreaSolid.setExtrudedDirection(direction);
//		extrudedAreaSolid.setPosition(defaultPlacement);
//		
//		extrudedAreaSolid.setDepth(new IfcPositiveLengthMeasure.Ifc4(20));
//		model.addObject(extrudedAreaSolid);

//		SET<IfcRepresentationItem> representations = new SET<>();
//		representations.add((IfcRepresentationItem)extrudedAreaSolid);
//		
//		IfcShapeRepresentation shapeRepresentation = new IfcShapeRepresentation.Ifc4.Instance();
//		shapeRepresentation.setItems(representations);
//		
//		model.addObject(shapeRepresentation);
//		
//		LIST<IfcRepresentation> ifcRepresentations = new LIST<>();
//		ifcRepresentations.add((IfcRepresentation)shapeRepresentation);
//
//		IfcProductDefinitionShape productRepresentation = new IfcProductDefinitionShape.Ifc4.Instance();
//		productRepresentation.setRepresentations(ifcRepresentations);
//		model.addObject(productRepresentation);
		
		proxy.setRepresentation((IfcProductRepresentation) productRepresentation);		
		CadTreeNode n1 = IfcLoader.interpretIfcProduct((IfcProduct) proxy, unitConverter);
		Solid s1 = n1.getShapes().iterator().next();
		
		
		
		
		
		
		
		
		IfcLoadManager.getInstance().loadStepModel(model, false);
		
	
		
		return s1;
		
	}
	
	
	
	Shape getUnionShape(ArrayList<AbstractFeatureType> features) {

		ArrayList<Polygon> polygons = new ArrayList<>();

		features.forEach(flaeche -> {
			XPlanungCadObjectJ3D cadObj = this.xPlanungModel.getCadObjOfFeature(flaeche);

			cadObj.getSolidShapes().forEach(solidShape -> {

				if (solidShape.getGeometry() instanceof TriangleArray) {
					TriangleArray triangleArray = (TriangleArray) solidShape.getGeometry();

					Point3f[] coords = new Point3f[triangleArray.getVertexCount()];
					for (int i = 0; i < coords.length; i++) {
						coords[i] = new Point3f();
					}
					triangleArray.getCoordinates(0, coords);
//				    		
					for (int i = 0; i < coords.length - 2; i += 3) {
						Point3f[] triangleCoords = new Point3f[3];
						triangleCoords[0] = coords[0 + i];
						triangleCoords[1] = coords[1 + i];
						triangleCoords[2] = coords[2 + i];

						polygons.add(GeometryUtil.create2DPolygonFX(triangleCoords));
					}

				} else {
					try {
						throw new Exception(
								"Geometry not considered of type: " + solidShape.getGeometry().getClass().getName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}); // for each feature

		Shape union = GeometryUtil.union_2D(polygons);

		return union;
		
	}
	
	
	Object invokeMethodByName(String methodName, Object obj, Object... args) {
		
		Method method = null;
		
		try {
			method = obj.getClass().getMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if (method == null)
			return null;
		
		
		try {
			Object result = method.invoke(obj, args);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return method;
	}

}
