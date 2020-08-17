package de.rub.bi.inf.baclient.core.ifc;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class Geolocation {

	private static Geolocation instance = null;
	
    public static Geolocation getInstance() {
    	if (instance==null) {
    		instance = new Geolocation();
    	}
		return instance;
	}
    
    
    private MathTransform wgs84ToEpsg25832;
    
    private Geolocation() {
    	try {
    		CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
			CoordinateReferenceSystem epsg25832 = CRS.decode("EPSG:25832");
			wgs84ToEpsg25832 = CRS.findMathTransform(wgs84, epsg25832);
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
    
    public double [] transformWGS84ToEPSG25832(double... srcPoints) {
    	int srcOff=0, dstOff = 0;
		double[] dstPoints = new double[srcPoints.length];
		int numPoints = srcPoints.length/2;
		try {
			wgs84ToEpsg25832.transform(srcPoints, srcOff, dstPoints, dstOff, numPoints);
			
//			System.out.println(Arrays.toString(dstPoints));
			
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dstPoints;
    }
    
   
}
