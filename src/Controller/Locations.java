package Controller;
import java.util.ArrayList;

import Exceptions.EmptyList;
import Exceptions.OffTheList;
import Model.Location;

public class Locations extends Controller{
	ArrayList<Location> locations;
	
	public Locations() {
		locations = new ArrayList<Location>();
	}
	
    /** Register a location.
     * @param locationName The location's  name.
     * @param locationDescription The location�s description.
     */
    public void register(String locationName, String locationDescription){
        Location default_location = new Location(locationName, locationDescription);
        locations.add(default_location);
    }
	
    /** Lists all LOCATIONS running through arraylist locations
     * @return locations */
	@Override
    public ArrayList<Location> list(){
    	return locations;
    }
	/** This method search a location by name
	 * @param searchName String - user searched name
	 * @return true - if the location name searched exist or return a Exception saying the location doesn't exist
	 * */
	@Override
	public boolean findByName(String searchName) throws OffTheList{
		for (Location local: locations) {
			if(local.getLocationName().toLowerCase().equals(searchName.toLowerCase())) {
				return true;
			}
		} 

		throw new OffTheList("A localização que você buscou não existe, tente novamente.");
	}
	/** This method verify if the arrayList of locations is empty
	 * throw for an exception
	 * */
	@Override
	public void sizeOfList() throws EmptyList {
		if(locations.size() == 0) {
			throw new EmptyList("Não há nenhuma localização cadastrada ainda!");
		}
	}


	
	



}
