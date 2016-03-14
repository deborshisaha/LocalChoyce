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

    public Address() {
//        setPostalCode("95136");
//        setFeatureName("Facebook HQ");
//        setLatitude(72.98);
//        setLongitude(122.31);
//        setLocality("");
//        setAdminArea("San Jose");
//        setCountryName("USA");
//        setSubAdminArea("Santa Clara County");
//        setSubLocality("Santa Clara");
//        setThoroughfare("");
//        setSubThoroughfare("");
    }

    public String getFeatureName() {
        return mFeatureName;
    }

    public void setFeatureName(String mFeatureName) {
        this.mFeatureName = mFeatureName;
        put("featureName", mFeatureName);
    }

    public String getAdminArea() {
        return mAdminArea;
    }

    public void setAdminArea(String mAdminArea) {
        this.mAdminArea = mAdminArea;
        put("adminArea", mAdminArea);
    }

    public String getSubAdminArea() {
        return mSubAdminArea;
    }

    public void setSubAdminArea(String mSubAdminArea) {
        this.mSubAdminArea = mSubAdminArea;
        put("subAdminArea", mSubAdminArea);
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String mLocality) {
        this.mLocality = mLocality;
        put("locality", mLocality);
    }

    public String getSubLocality() {
        return mSubLocality;
    }

    public void setSubLocality(String mSubLocality) {
        this.mSubLocality = mSubLocality;
        put("subLocality", mSubLocality);
    }

    public String getThoroughfare() {
        return mThoroughfare;
    }

    public void setThoroughfare(String mThoroughfare) {
        this.mThoroughfare = mThoroughfare;
        put("thoroughFare", mThoroughfare);
    }

    public String getSubThoroughfare() {
        return mSubThoroughfare;
    }

    public void setSubThoroughfare(String mSubThoroughfare) {
        this.mSubThoroughfare = mSubThoroughfare;
        put("subThoroughfare", mSubThoroughfare);
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(String mPostalCode) {
        this.mPostalCode = mPostalCode;
        put("postalCode", mPostalCode);
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
        put("countryCode", mCountryCode);
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String mCountryName) {
        this.mCountryName = mCountryName;
        put("countryName", mCountryName);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
        put("latitude", mLatitude);
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
        put("longitude", mLongitude);
    }

    public LatLng getPoint() {
        return mPoint;
    }

    public void setPoint(LatLng point) {
        mPoint = point;
        mLatitude = point.latitude;
        mLongitude = point.longitude;
        mHasLatitude = true;
        mHasLongitude = true;
    }

    public Address(LatLng point){
        setPoint(point);
    }

    private String mFeatureName;
    private String mAdminArea;
    private String mSubAdminArea;
    private String mLocality;
    private String mSubLocality;
    private String mThoroughfare;
    private String mSubThoroughfare;
    private String mPostalCode;
    private String mCountryCode;
    private String mCountryName;
    private double mLatitude;
    private double mLongitude;
    private boolean mHasLatitude = false;
    private boolean mHasLongitude = false;
    private LatLng mPoint;

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isHasLatitude() {
        return mHasLatitude;
    }

    public boolean isHasLongitude() {
        return mHasLongitude;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFeatureName);
        dest.writeString(this.mAdminArea);
        dest.writeString(this.mSubAdminArea);
        dest.writeString(this.mLocality);
        dest.writeString(this.mSubLocality);
        dest.writeString(this.mThoroughfare);
        dest.writeString(this.mSubThoroughfare);
        dest.writeString(this.mPostalCode);
        dest.writeString(this.mCountryCode);
        dest.writeString(this.mCountryName);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
        dest.writeByte(mHasLatitude ? (byte) 1 : (byte) 0);
        dest.writeByte(mHasLongitude ? (byte) 1 : (byte) 0);
    }

    protected Address(Parcel in) {
        this.mFeatureName = in.readString();
        this.mAdminArea = in.readString();
        this.mSubAdminArea = in.readString();
        this.mLocality = in.readString();
        this.mSubLocality = in.readString();
        this.mThoroughfare = in.readString();
        this.mSubThoroughfare = in.readString();
        this.mPostalCode = in.readString();
        this.mCountryCode = in.readString();
        this.mCountryName = in.readString();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mHasLatitude = in.readByte() != 0;
        this.mHasLongitude = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
