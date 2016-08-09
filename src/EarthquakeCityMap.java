import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.marker.*;
import processing.core.*;
//Parsing library
import parsing.ParseFeed;

public class EarthquakeCityMap extends PApplet{
	private static final long serialVersionUID = 1L;
	private UnfoldingMap map;
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	public void setup(){
		size(950, 500, OPENGL);
		AbstractMapProvider provider = new Google.GoogleMapProvider(); //polymorfism
		//"this" refers to this class instance
		//which is a PApplet instance
		map = new UnfoldingMap(this, 200, 50, 700, 500, provider);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
		List<Marker> markers = new ArrayList<Marker>();
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		Location valloc = new Location(-38.14f, -73.03f);
		Feature valEq = new PointFeature(valloc);
		valEq.addProperty("title", "Valdivia, Chile");
		valEq.addProperty("magnitude", "9.5");
		valEq.addProperty("date", "May 22, 1960");
		valEq.addProperty("year", "1960");
		Marker valMk = new SimplePointMarker(valloc, valEq.getProperties());
		map.addMarker(valMk);
	}
	
	public void draw(){
		background(10);
		map.draw();
		//addkey();
	}
}
