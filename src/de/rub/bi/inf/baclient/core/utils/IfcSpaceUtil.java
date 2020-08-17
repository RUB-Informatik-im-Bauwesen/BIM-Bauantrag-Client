package de.rub.bi.inf.baclient.core.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.j3d.model.cadobjectmodel.MultiAppearanceShape3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.ifctoolbox.ifc.IfcSpace;
import com.apstex.j3d.utils.geometry.GeometryInfo;
import com.apstex.javax.media.j3d.GeometryArray;
import com.apstex.javax.media.j3d.IndexedGeometryArray;
import com.apstex.javax.vecmath.Point3f;
import com.apstex.javax.vecmath.Vector3f;

public class IfcSpaceUtil {
	
	
	public static ArrayList<int[]> getContourTriangles(Point3f[] points, int[] triangles, float lowerBoundry){
	    	ArrayList<int[]> conturTriangles = new ArrayList<>();
			for(int t = 0; t < triangles.length - 2; t += 3) {
				
				//System.out.println("Triangle: " + triangles[t] + ", " + triangles[t + 1] + ", " + triangles[t + 2]);
				//System.out.println("Triangle Points: " + points[triangles[t]].toString() + ", " + points[triangles[t + 1]].toString() + ", " + points[triangles[t + 2]].toString());
				
				Point3f pA = points[triangles[t]];
				Point3f pB = points[triangles[t + 1]];
				Point3f pC = points[triangles[t + 2]];
				
				Vector3f v1 = new Vector3f(pB);
				v1.sub(pA);
				Vector3f v2 = new Vector3f(pC);
				v2.sub(pA);
				Vector3f cross = new Vector3f();
				cross.cross(v1, v2);
				Vector3f zUnit = new Vector3f(0, 0, 1);
				
				
				if(
						(pA.getZ() == lowerBoundry || pB.getZ() == lowerBoundry || pC.getZ() == lowerBoundry) //mind. 1 Punkt in unterer Fläche
						
						&& Math.abs(zUnit.dot(cross)) > 0.00001 //wenn nicht orthogonal zu z-Vector (nicht in einer vertikalen Ebene)
						
						) {
					
					
//					System.out.println(zUnit.dot(cross));
					
					int[] conTri = new int[3];
					conTri[0] = triangles[t];
					conTri[1] = triangles[t + 1];
					conTri[2] = triangles[t + 2];
					
					conturTriangles.add(conTri);
				}
				
			}
			return conturTriangles;
	    }

	
	 public static ArrayList<int[]> getEdges(ArrayList<int[]> conturTriangles) {
	    	
	    	HashMap<String, Integer> edgeMap = new HashMap<>();
	    	for(int[] triangle : conturTriangles) {
	    		String keyA = "E[" + triangle[0] + "," + triangle[1] + "]";
	    		String keyAi = "E[" + triangle[1] + "," + triangle[0] + "]";
	        	
	    		String keyB = "E[" + triangle[1] + "," + triangle[2] + "]";
	    		String keyBi = "E[" + triangle[2] + "," + triangle[1] + "]";
	        	
	    		String keyC = "E[" + triangle[2] + "," + triangle[0] + "]";
	    		String keyCi = "E[" + triangle[0] + "," + triangle[2] + "]";
	        	
	    		if(!edgeMap.containsKey(keyA)) {
	    			edgeMap.put(keyA, 0);
	    		}
	    		if(!edgeMap.containsKey(keyAi)) {
	    			edgeMap.put(keyAi, 0);
	    		}
	    		if(!edgeMap.containsKey(keyB)) {
	    			edgeMap.put(keyB, 0);
	    		}
	    		if(!edgeMap.containsKey(keyBi)) {
	    			edgeMap.put(keyBi, 0);
	    		}
	    		if(!edgeMap.containsKey(keyC)) {
	    			edgeMap.put(keyC, 0);
	    		}
	    		if(!edgeMap.containsKey(keyCi)) {
	    			edgeMap.put(keyCi, 0);
	    		}
	    		
	    		edgeMap.put(keyA, edgeMap.get(keyA) + 1);
	    		edgeMap.put(keyAi, edgeMap.get(keyAi) + 1);
	    		edgeMap.put(keyB, edgeMap.get(keyB) + 1);
	    		edgeMap.put(keyBi, edgeMap.get(keyBi) + 1);
	    		edgeMap.put(keyC, edgeMap.get(keyC) + 1);
	    		edgeMap.put(keyCi, edgeMap.get(keyCi) + 1);
	    		
	    	}
	    	
	    	ArrayList<int[]> edgeList = new ArrayList<>();
	    	for(int[] triangle : conturTriangles) {
	    		String keyA = "E[" + triangle[0] + "," + triangle[1] + "]";
	    		String keyB = "E[" + triangle[1] + "," + triangle[2] + "]";
	    		String keyC = "E[" + triangle[2] + "," + triangle[0] + "]";
	    	
	    		if(edgeMap.get(keyA) == 1) {
	    			int[] edge = new int[2];
	    			edge[0] = triangle[0];
	    			edge[1] = triangle[1];
	    			
	    			edgeList.add(edge);
	    		}
	    		
	    		if(edgeMap.get(keyB) == 1) {
	    			int[] edge = new int[2];
	    			edge[0] = triangle[1];
	    			edge[1] = triangle[2];
	    			
	    			edgeList.add(edge);
	    		}
	    		
	    		if(edgeMap.get(keyC) == 1) {
	    			int[] edge = new int[2];
	    			edge[0] = triangle[2];
	    			edge[1] = triangle[0];
	    			
	    			edgeList.add(edge);
	    		}
	    	}
	    	
	    	
	    	
	    	
	    	//Sort edges
			ArrayList<int[]> sortedEdges = new ArrayList<>();
			
			int[] firstEdge = edgeList.get(0);
			sortedEdges.add(firstEdge);
			int[] lastEdge = edgeList.get(0);
			while(lastEdge[1]!=firstEdge[0]) {
				for(int[] edge : edgeList) {
					if(lastEdge[1] == edge[0]) {
						lastEdge = edge;
						sortedEdges.add(lastEdge);
						break;
					}
				}
			}
			
			
			return sortedEdges;
	    }
	 
	 
	 public static Point3f[] getContourPoints(ApplicationModelNode modelNode, IfcSpace ifcSpace , boolean ignoreZ) {
		 IfcSpace.Ifc4 space4 = (IfcSpace.Ifc4) ifcSpace;
			
			CadObject cadObject = modelNode.getCadObjectModel().getCadObject(space4);
			if (!(cadObject instanceof CadObjectJ3D)) return null;
			
			CadObjectJ3D cadObjectJ3D = (CadObjectJ3D) cadObject;
			
			
			MultiAppearanceShape3D mShape = cadObjectJ3D.getSolidShapes().get(0);

			Float lowerBoundry = new Float(Float.MAX_VALUE);
			Float upperBoundry = new Float(Float.MIN_VALUE);
			Point3f[] points = null;
			
			Point3f[] coordinates = null;
    		
    		if(mShape.getGeometry() instanceof GeometryArray) {
    			GeometryInfo geometryInfo = new GeometryInfo((GeometryArray)mShape.getGeometry());
    			IndexedGeometryArray indexedGeoArr = geometryInfo.getIndexedGeometryArray(true);
    			
    			points = geometryInfo.getCoordinates();
    			
    			//calculate lower bounds
    			for(Point3f point : points) {
    				if(point.getZ() < lowerBoundry) {
    					lowerBoundry = point.getZ();
    				}
    				if(point.getZ() > upperBoundry) {
    					upperBoundry = point.getZ();
    				}
    			}
    			//System.out.println("LowerBoundry is " + lowerBoundry.toString());
    			
    			int[] triangles = new int[indexedGeoArr.getValidIndexCount()];
    			indexedGeoArr.getCoordinateIndices(0, triangles);
    			
    			ArrayList<int[]> conturTriangles = IfcSpaceUtil.getContourTriangles(points, triangles, lowerBoundry);
    			ArrayList<int[]> contourEdges = IfcSpaceUtil.getEdges(conturTriangles);
    			

    			
    			coordinates = new Point3f[contourEdges.size()];
    			for (int i=0; i< coordinates.length; i++) {
    				if(ignoreZ) {
    					Point3f p = points[contourEdges.get(i)[0]];  
    					coordinates[i]=new Point3f(p.x, p.y, 0f);
    				}else {    					
    					coordinates[i]=points[contourEdges.get(i)[0]];    			
    				}
    			}

    		}
	
			
			return coordinates;
			
		
	 }

}
