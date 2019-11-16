package Controller;
import java.util.ArrayList;

import Model.Location;

public class Locations extends Controller{
	ArrayList<Location> locations;
	
	public Locations() {
		locations = new ArrayList<Location>();
	}
	
    /** Register a location.
     * @param locationName The location's  name.
     * @param locationDescription The location’s description.
     */
    public void register(String locationName, String locationDescription){
        Location default_location = new Location(locationName, locationDescription);
        locations.add(default_location);
    }
	
    /** Lists all LOCATIONS running through arraylist locations */
	@Override
    public ArrayList<Location> list(){
    	return locations;
    }


	
	



}
