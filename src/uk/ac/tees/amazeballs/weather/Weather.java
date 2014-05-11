package uk.ac.tees.amazeballs.weather;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import org.json.JSONException;

import net.aksingh.java.api.owm.CurrentWeatherData;
import net.aksingh.java.api.owm.OpenWeatherMap;

public class Weather {

	//Registered AppID from OpenWeatherMap
	OpenWeatherMap owm = new OpenWeatherMap("ebe4cc284208eb70ca4be827307d6967");
	
	private GeoCoords coords = new GeoCoords();
	private float longitude;
	private float latitude;
	private Object weatherData;
	private String weatherType;
	
	public void getWeatherData() {
		
		owm = new OpenWeatherMap("ebe4cc284208eb70ca4be827307d6967");
		
		try {
			latitude = 54.60854f;
			longitude = -1.096573f;
		
			
			//longitude = (float) coords.getLongitude();
			//latitude = (float) coords.getLatitude();
			if(longitude != 0 && latitude != 0) {
				CurrentWeatherData cwd = owm.currentWeatherByCoordinates(latitude, longitude);
				cwd.getMainData_Object().getTemperature();
				boolean gotRainData = cwd.getRain_Object().hasRain3Hours();
				//weatherData = owm.currentWeatherByCoordinates(latitude, longitude);
				//setWeatherTile(weatherData);
			} else {
				//go to default weather tiles method..
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String setWeatherTile(Object weatherData) {
		if(weatherData != null) {
			//go through the object and look for cloudy or snow if not default clear
			weatherData.toString();
			
		}
		else {
			Random r = new Random();
			int randomInt = r.nextInt(3);
			
			switch (randomInt) {
				case 1: weatherType = "Clear";
				break;
				case 2: weatherType = "Ice";
				break;
				case 3: weatherType = "Windy";
				break;
			}
			}
		return weatherType;
	}
	//get the data
	//format it out for 3 options.. rain, snow or normal	
}
