package Model;
public class Location {
    private String locationName;
    private String locationDescription;

    public Location(String locationName, String locationDescription){
        this.locationName = locationName;
        this.locationDescription = locationDescription;
    }

    public String getLocationName(){
        return this.locationName;
    }

    public String getLocationDescription() {
        return this.locationDescription;
    }

}
