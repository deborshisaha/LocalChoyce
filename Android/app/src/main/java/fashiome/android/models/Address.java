package fashiome.android.models;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.HashMap;

/**
 * Created by dsaha on 3/6/16.
 */
@ParseClassName("Address")
public class Address extends ParseObject implements Parcelable {

    private LatLng mPoint;

    public Address() {}

    public Address(LatLng point){
        setPoint(point);
    }

    public LatLng getPoint() {

        if (mPoint == null) {
            mPoint = new LatLng(getDouble("latitude"),getDouble("longitude"));
        }

        return mPoint;
    }

    public void setPoint(LatLng point) {
        this.mPoint = point;
        put("longitude", point.longitude);
        put("latitude", point.latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mPoint, 0);
    }

    protected Address(Parcel in) {
        this.mPoint = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };


    public double getLatitude() {
        return getPoint().latitude;
    }

    public double getLongitude() {
        return getPoint().longitude;
    }
}
