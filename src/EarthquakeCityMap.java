import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
//import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.marker.*;
import processing.core.*;
//Parsing library
import parsing.ParseFeed;

public class EarthquakeCityMap extends PApplet{
	private static final long serialVersionUID = 1L;
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
		
	// The map
	private UnfoldingMap map;
		
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	public void setup(){
		size(900, 700, OPENGL); //size of the window
		AbstractMapProvider provider = new Google.GoogleMapProvider(); //polymorfism
		
		//"this" refers to this class instance
		//which is a PApplet instance
		map = new UnfoldingMap(this, 200, 50, 650, 600, provider);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//Reading in earthquake data and geometric properties
	    
		//load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes){
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }
		
	    //printQuakes();
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	}
	
	public void draw(){
		background(10);
		map.draw();
		addMapKey();
	}
	
	//helper method to draw key in GUI
	private void addMapKey() {	
		int x = 50;
		
		fill(255, 250, 240);
		rect(25, 50, 150, 300);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", x, 75);
		
		fill(color(200, 100, 150));
		triangle(x, 125-5, x+5, 125+5, x-5, 125+5);
		
		fill(color(255, 255, 255));
		ellipse(x, 150, 20, 20);
		rect(x-10, 175-10, 20, 20);

		fill(0, 0, 0);
		text("City Marker", 75, 125);
		text("Land Quake", 75, 150);
		text("Ocean Quake", 75, 175);
		text("Size ~ Magnitude", 50, 200);
		
		fill(color(255, 255, 0));
		ellipse(x, 250, 15, 15);
		fill(color(0, 0, 255));
		ellipse(x, 275, 15, 15);
		fill(color(255, 0, 0));
		ellipse(x, 300, 15, 15);
		fill(color(255, 255, 255));
		ellipse(x, 325, 15, 15);
		line(x-10, 325-10, x+10, 325+10);
		line(x+10, 325-10, x-10, 325+10);
		
		fill(0, 0, 0);
		text("Shallow", 75, 250);
		text("Intermediate", 75, 275);
		text("Deep", 75, 300);
		text("Past Hour", 75, 325);
	}
	
	
	//Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.
	private boolean isLand(PointFeature earthquake) {
		//loop over all countries to check if location is in any of them
		for(Marker country : countryMarkers){
			if(isInCountry(earthquake, country)){
				return true;
			}
		}
		// not inside any country
		return false;
	}
	

	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		//getting location of feature
		Location checkLoc = earthquake.getLocation();

		//some countries represented it as MultiMarker
		//looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
			//looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				//checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					//return if is inside one
					return true;
				}
			}
		}
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}
		
	//prints countries with number of earthquakes
	private void printQuakes() 
	{
		int quakeCounter = 0, oceanCounter = 0; 
		for(Marker c : countryMarkers){
			
			for(Marker q : quakeMarkers){
				if(q.getProperty("country") != null){
					if(c.getProperty("name") == q.getProperty("country")){
						quakeCounter++;
					}
				}
			}
			if(quakeCounter > 0){
				System.out.println(c.getProperty("name") + ": 	" + quakeCounter);
				quakeCounter = 0;
			}
		}
		for(Marker q : quakeMarkers){
			if(q.getProperty("country") == null){
				oceanCounter++;
			}
		}
		if(oceanCounter > 0){
			System.out.println("Ocean:    " + oceanCounter);
		}		
	}
}
