package uk.ac.tees.amazeballs.weather;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GeoCoords implements LocationListener {
	
	private double longitude;
	private double latitude;
	
    public void onLocationChanged(Location location) {
    	longitude = location.getLongitude();
        latitude = location.getLatitude(); 
    }

    public double getLongitude() {
    	return longitude;
    }
    
    public double getLatitude() {
    	return latitude;
    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//add method here for default tile selection from weather..
		
	}
    
}
