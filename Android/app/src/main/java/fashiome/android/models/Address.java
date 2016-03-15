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

//    public String getFeatureName() {
//        if (mFeatureName == null){
//
//        }
//        return mFeatureName;
//    }
//
//    public void setFeatureName(String mFeatureName) {
//        this.mFeatureName = mFeatureName;
//        put("featureName", mFeatureName);
//    }
//
//    public String getAdminArea() {
//        return mAdminArea;
//    }
//
//    public void setAdminArea(String mAdminArea) {
//        this.mAdminArea = mAdminArea;
//        put("adminArea", mAdminArea);
//    }
//
//    public String getSubAdminArea() {
//        return mSubAdminArea;
//    }
//
//    public void setSubAdminArea(String mSubAdminArea) {
//        this.mSubAdminArea = mSubAdminArea;
//        put("subAdminArea", mSubAdminArea);
//    }
//
//    public String getLocality() {
//        return mLocality;
//    }
//
//    public void setLocality(String mLocality) {
//        this.mLocality = mLocality;
//        put("locality", mLocality);
//    }
//
//    public String getSubLocality() {
//        return mSubLocality;
//    }
//
//    public void setSubLocality(String mSubLocality) {
//        this.mSubLocality = mSubLocality;
//        put("subLocality", mSubLocality);
//    }
//
//    public String getThoroughfare() {
//        return mThoroughfare;
//    }
//
//    public void setThoroughfare(String mThoroughfare) {
//        this.mThoroughfare = mThoroughfare;
//        put("thoroughFare", mThoroughfare);
//    }
//
//    public String getSubThoroughfare() {
//        return mSubThoroughfare;
//    }
//
//    public void setSubThoroughfare(String mSubThoroughfare) {
//        this.mSubThoroughfare = mSubThoroughfare;
//        put("subThoroughfare", mSubThoroughfare);
//    }
//
//    public String getPostalCode() {
//        return mPostalCode;
//    }
//
//    public void setPostalCode(String mPostalCode) {
//        this.mPostalCode = mPostalCode;
//        put("postalCode", mPostalCode);
//    }
//
//    public String getCountryCode() {
//        return mCountryCode;
//    }
//
//    public void setCountryCode(String mCountryCode) {
//        this.mCountryCode = mCountryCode;
//        put("countryCode", mCountryCode);
//    }
//
//    public String getCountryName() {
//        return mCountryName;
//    }
//
//    public void setCountryName(String mCountryName) {
//        this.mCountryName = mCountryName;
//        put("countryName", mCountryName);
//    }
//
//    public double getLatitude() {
//        return mLatitude;
//    }
//
//    public void setLatitude(double mLatitude) {
//        this.mLatitude = mLatitude;
//        put("latitude", mLatitude);
//    }
//
//    public double getLongitude() {
//        return mLongitude;
//    }
//
//    public void setLongitude(double mLongitude) {
//        this.mLongitude = mLongitude;
//        put("longitude", mLongitude);
//    }
//
//    public LatLng getPoint() {
//        return mPoint;
//    }
//
//    public void setPoint(LatLng point) {
//        mPoint = point;
//        mLatitude = point.latitude;
//        mLongitude = point.longitude;
//        mHasLatitude = true;
//        mHasLongitude = true;
//    }


//    private String mFeatureName;
//    private String mAdminArea;
//    private String mSubAdminArea;
//    private String mLocality;
//    private String mSubLocality;
//    private String mThoroughfare;
//    private String mSubThoroughfare;
//    private String mPostalCode;
//    private String mCountryCode;
//    private String mCountryName;
//    private double mLatitude;
//    private double mLongitude;
//    private boolean mHasLatitude = false;
//    private boolean mHasLongitude = false;


//    public boolean isHasLatitude() {
//        return mHasLatitude;
//    }
//
//    public boolean isHasLongitude() {
//        return mHasLongitude;
//    }

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
