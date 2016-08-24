import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

public class LandQuakeMarker extends EarthquakeMarker{

	public LandQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = true;
	}

	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		float r = this.radius;
		pg.ellipse(x, y, r*2, r*2);
	}
	
	// Get the country the earthquake is in
	public String getCountry() {
		return (String) getProperty("country");
	}

}
