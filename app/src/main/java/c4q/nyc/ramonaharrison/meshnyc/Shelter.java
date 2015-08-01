package c4q.nyc.ramonaharrison.meshnyc;

/**
 * Created by July on 8/1/15.
 */
public class Shelter {
    private String city;
    private String address;
    private double latitude;
    private double longitude;
    private String postal;

    public Shelter(String city, String address, double latitude, double longitude, String postal){
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postal = postal;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }
}
