import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public abstract class EarthquakeMarker extends SimplePointMarker{
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	// The radius of the Earthquake marker
	protected float radius;
	
	protected String age;
	
	// abstract method implemented in subclasses
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		age = (String) properties.get("age");
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();	
		}
	
	// calls abstract method drawEarthquake and then checks age and draws X if needed
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
				
		// determine color of marker from depth
		colorDetermine(pg);
			
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
			
		//draw X over marker if within past hour
		if(age.contentEquals("Past Hour")){
			pg.line(x-10, y-10, x+10, y+10);
			pg.line(x+10, y-10, x-10, y+10);
		}
			
		// reset to previous styling
		pg.popStyle();
	}
		
	// determine color of marker from depth
	//Deep = red, intermediate = blue, shallow = yellow
	private void colorDetermine(PGraphics pg) {
		if(this.getDepth() < 70){
			pg.fill(255,255,0);
		}else if(this.getDepth() >= 70 && this.getDepth() < 300){
			pg.fill(0, 0, 255);
		}else if(this.getDepth() >= 300){
			pg.fill(255, 0, 0);
		}
	}
	
	//some getters and setters
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	
}
