//package uk.ac.tees.amazeballs.weather;
//
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//import android.location.Address;
//import android.location.Geocoder;
//import android.app.Activity;
//import android.widget.Toast;
//
//public class GeoLocation extends Activity {	
//	
//	private GeoCoords coOrdinates = new GeoCoords();
//	private String postCode;
//	//convert latitude and longitude to an address
//	public void getMyLocationAddress() {
//        
//        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
//        
//        try {
//               
//              //Place your latitude and longitude
//              List<Address> addresses = geocoder.getFromLocation(coOrdinates.getLongitude(), coOrdinates.getLatitude(), 1);
//              
//              if(addresses != null) {
//               
//            	  //get the first address as may be multiple
//                  Address fetchedAddress = addresses.get(0);
//                  //get the post code of that address
//                  postCode = fetchedAddress.getPostalCode();
//              }
//          
//        } 
//        catch (IOException e) {
//                 // TODO Auto-generated catch block
//                 e.printStackTrace();
//                 Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
//        }
//    }
//	
//	public String getPostCode() {
//		getMyLocationAddress();
//		return postCode;
//	}
//
//}