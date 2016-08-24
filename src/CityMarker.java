import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public class CityMarker extends SimplePointMarker{
	
	public CityMarker(Location location) {
		super(location);
	}
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}
	
	
	//pg is the graphics object on which we call the graphics
	// methods.
	//method to draw marker on the map.
	//Parameters:
	//	pg - The PGraphics to draw on
	//	x - The x position in outer object coordinates.
	//	y - The y position in outer object coordinates.
	public void draw(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		// TODO: Add code to draw a triangle to represent the CityMarker
		pg.fill(200, 100, 150);
		pg.triangle(x, y-5, x+5, y+5, x-5, y+5);
		// Restore previous drawing style
		pg.popStyle();
	}
	
	
}
