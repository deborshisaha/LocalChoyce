package design.semicolon.modarenta.models;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by rakhe on 3/4/2016.
 */
@Parcel
public class Item {
    String mID;
    String mName;
    Float mCoordinates[];
    String mDesc;
    String mPrice;

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        mURL = URL;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    String mURL;
    public Item(){

    }
    public Item(String name, LatLng point) {
        mName = name;
        mPoint = point;
    }

    public static ArrayList<Item> loadItems()
    {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Sandals",new LatLng(37.371859,-122.021234)));
        items.add(new Item("coat",new LatLng(37.371715,-122.045628)));
        items.add(new Item("Gown",new LatLng(37.385015,-122.014943)));
        for (int i=0;i<40;i++){
            Item item = new Item();
            item.setPoint(getLocation(37.371859,-122.021234,20));
            item.setName(randomString(10));
            items.add(item);
        }
        return  items;
    }
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static LatLng getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
        System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );
        return new LatLng(foundLatitude,foundLongitude);
    }

    public Float[] getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(Float[] coordinates) {
        mCoordinates = coordinates;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public LatLng getPoint() {
        return mPoint;
    }

    public void setPoint(LatLng point) {
        mPoint = point;
    }

    LatLng mPoint;
    String mLocation;
}
