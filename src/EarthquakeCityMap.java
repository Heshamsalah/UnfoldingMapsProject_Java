import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
//import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.marker.*;
import processing.core.*;
//Parsing library
import parsing.ParseFeed;

public class EarthquakeCityMap extends PApplet{
	private static final long serialVersionUID = 1L;
	private UnfoldingMap map;
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	public void setup(){
		size(950, 500, OPENGL); //size of the window
		AbstractMapProvider provider = new Google.GoogleMapProvider(); //polymorfism
		
		//"this" refers to this class instance
		//which is a PApplet instance
		map = new UnfoldingMap(this, 200, 50, 600, 450, provider);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<Marker> markers = new ArrayList<Marker>();
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		for(PointFeature eq: earthquakes){
			markers.add(new SimplePointMarker(eq.getLocation(), eq.getProperties()));
		}
		int yellow = color(255,255,0);
		int red = color(255,0,0);
		int blue = color(0,0,255);
		
		for(Marker mk: markers){
			if((float) mk.getProperty("magnitude") < 4.0){
				((SimplePointMarker) mk).setRadius(5);
				mk.setColor(blue);
			}
			else if((float) mk.getProperty("magnitude") >= 4.0 && (float) mk.getProperty("magnitude") < 5.0)
			{
				((SimplePointMarker) mk).setRadius(10);
				mk.setColor(yellow);
			}
			else{
				((SimplePointMarker) mk).setRadius(15);
				mk.setColor(red);
			}
		}	
		map.addMarkers(markers);
	}
	
	public void draw(){
		background(10);
		map.draw();
		addKey();
		//addKey();
	}
	
	public void addKey(){
		fill(255);
		rect(5, 50, 180, 100);
		textSize(12);
		
		fill(255, 0, 0); //red
		ellipse(30, 80, 15, 15);
		String s = "Magnitude > 5.0";
		text(s, 45, 70, 120, 90);  // Text wraps within text box
		
		fill(255,255,0); //yellow
		ellipse(30, 100, 10, 10);
		String x = "Magnitude 4.0-4.9";
		text(x, 45, 90, 120, 110);  // Text wraps within text box
		
		fill(0, 0, 255); //blue
		ellipse(30, 120, 5, 5);
		String e = "Magnitude < 4.0";
		text(e, 45, 110, 120, 130);  // Text wraps within text box
	}
	
	public void addKey(String s){
		fill(255);
		rect(5, 190, 180, 200);
		textSize(18);
		fill(100);
		text(s, 45, 190, 120, 180);
	}
}
