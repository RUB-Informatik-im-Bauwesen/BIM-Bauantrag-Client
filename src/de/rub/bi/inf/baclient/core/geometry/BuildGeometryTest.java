package de.rub.bi.inf.baclient.core.geometry;

import java.util.ArrayList;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.views.view3d.Ifc3DViewJ3D;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.j3d.utils.geometry.Sphere;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.PolygonAttributes;
import com.apstex.javax.media.j3d.QuadArray;
import com.apstex.javax.media.j3d.Shape3D;
import com.apstex.javax.media.j3d.TransparencyAttributes;
import com.apstex.javax.media.j3d.TriangleArray;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Point3f;

public class BuildGeometryTest {
	
	public static ArrayList<CadObject> create(Ifc3DViewJ3D view) {

		PolygonAttributes p = new PolygonAttributes();
		Appearance planeAppearance = new Appearance();
		planeAppearance.setPolygonAttributes(p);
		Color3f planeColor = new Color3f(1.0f, 1.0f, 1.0f); // This makes it white.
		ColoringAttributes planeCA = new ColoringAttributes(planeColor, 1);
		planeAppearance.setColoringAttributes(planeCA);
		planeAppearance.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		planeAppearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);

		
		QuadArray plane = new QuadArray(4, QuadArray.COORDINATES); // This makes the plane.
		plane.setCoordinate(0, new Point3f(-5f, -5f, 5f)); // You specify your own cornerpoints.
		plane.setCoordinate(1, new Point3f(5f, -5f, 5f));
		plane.setCoordinate(2, new Point3f(5f, 5f, 5f));
		plane.setCoordinate(3, new Point3f(-5f, 5f, 5f));

		PolygonAttributes p2 = new PolygonAttributes();
		Appearance planeAppearance2 = new Appearance();
		planeAppearance2.setPolygonAttributes(p2);
		Color3f planeColor2 = new Color3f(1.0f, 0.5f, 0.5f); // This makes it white.
		ColoringAttributes planeCA2 = new ColoringAttributes(planeColor2, 1);
		TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.6f);
		planeAppearance2.setTransparencyAttributes(transparencyAttributes);
		planeAppearance2.setColoringAttributes(planeCA2);
		
		planeAppearance2.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		planeAppearance2.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		
		
		TriangleArray triangle = new TriangleArray(3, TriangleArray.COORDINATES);
		triangle.setCoordinate(0, new Point3f(-5f, -5f, 10f));
		triangle.setCoordinate(1, new Point3f(5f, -5f, 10f));
		triangle.setCoordinate(2, new Point3f(-5f, 5f, 10f));

		Shape3D shapeA = new Shape3D(plane, planeAppearance);
		shapeA.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_COLLISION_BOUNDS_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_BOUNDS_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_LOCALE_READ);
		shapeA.setCapability(Shape3D.ALLOW_COLLIDABLE_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
		shapeA.setCapability(Shape3D.ALLOW_PICKABLE_READ);
		shapeA.setCapability(Shape3D.ENABLE_PICK_REPORTING);
		shapeA.setCapability(Shape3D.ALLOW_PARENT_READ);
		shapeA.setName("PlaneA");
		
		Shape3D shapeB = new Shape3D(triangle, planeAppearance2);
		shapeB.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_COLLISION_BOUNDS_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_BOUNDS_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_LOCALE_READ);
		shapeB.setCapability(Shape3D.ALLOW_COLLIDABLE_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_PICKABLE_WRITE);
		shapeB.setCapability(Shape3D.ALLOW_PICKABLE_READ);
		shapeB.setCapability(Shape3D.ENABLE_PICK_REPORTING);
		shapeB.setCapability(Shape3D.ALLOW_PARENT_READ);
		shapeB.setName("TriableA");
		
		Sphere s = new Sphere(3); // Create a sphere geometry
		s.getAppearance().setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_TEXGEN_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		s.getAppearance().setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
		s.setName("SphereA");
		
		// Build a CadObject
		//BranchGroup group = new BranchGroup(); // Content branch.

		CadObjectJ3D objectJ3DA = new CadObjectJ3D();
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_BOUNDS_WRITE);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_CHILDREN_EXTEND);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_CHILDREN_WRITE);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_COLLIDABLE_WRITE);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_COLLISION_BOUNDS_WRITE);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_DETACH);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_LOCAL_TO_VWORLD_READ);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_PICKABLE_READ);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_PICKABLE_WRITE);
		objectJ3DA.setCapability(CadObjectJ3D.ALLOW_PARENT_READ);
		objectJ3DA.setName("CadObjectA");
		
		
		CadObjectJ3D objectJ3DB = new CadObjectJ3D();
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_BOUNDS_WRITE);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_CHILDREN_EXTEND);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_CHILDREN_WRITE);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_COLLIDABLE_WRITE);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_COLLISION_BOUNDS_WRITE);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_DETACH);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_LOCAL_TO_VWORLD_READ);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_PICKABLE_READ);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_PICKABLE_WRITE);
		objectJ3DB.setCapability(CadObjectJ3D.ALLOW_PARENT_READ);
		objectJ3DB.setName("CadObjectB");
		
		//objectJ3D.addChild(group); // add a group of content to cad context

		//=== !! setUserData necessary for selection listener !! ======
		objectJ3DA.addChild(s); // add geometry primitiv to cad context
		s.getShape().getAppearance().setMaterial(null);
		s.getShape().setUserData(objectJ3DA);
		
		objectJ3DA.addChild(shapeA); // Add plane to content branch.
		shapeA.setUserData(objectJ3DA);
		
		objectJ3DB.addChild(shapeB); // Add tirangle to content branch.
		shapeB.setUserData(objectJ3DB);
		//=============================================================
	
		ArrayList<CadObject> cadList = new ArrayList<>();
		cadList.add(objectJ3DA);
		cadList.add(objectJ3DB);
		
		return cadList;
	}

}
