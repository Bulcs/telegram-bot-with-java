package Model;
public class Location {
	/** 
	 * @param locationName String
	 * @param locationDescription String
	 **/
    private String locationName;
    private String locationDescription;
    /**
     * Constructor Location
     * @param locationName location name
     * @param locationDescription location description
     * */
    public Location(String locationName, String locationDescription){
        this.locationName = locationName;
        this.locationDescription = locationDescription;
    }
    /** Method get the location name
     * @return locationName */
    public String getLocationName(){
        return this.locationName;
    }
    /** Method get the location description
     * @return locationDescription */
    public String getLocationDescription() {
        return this.locationDescription;
    }

}
