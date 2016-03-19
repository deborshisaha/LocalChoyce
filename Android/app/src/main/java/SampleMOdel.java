import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by rakhe on 3/19/2016.
 */
public class SampleMOdel implements Parcelable {
    List<String> strings;
    LatLng loc;

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(strings);
        dest.writeParcelable(loc, 0);
    }

    public SampleMOdel(Parcel in ){
        strings=in.createStringArrayList();
        loc=in.readParcelable(LatLng.class.getClassLoader());
    }
    public static final Creator<SampleMOdel> CREATOR = new Creator<SampleMOdel>() {
        public SampleMOdel createFromParcel(Parcel source) {
            return new SampleMOdel(source);
        }

        public SampleMOdel[] newArray(int size) {
            return new SampleMOdel[size];
        }
    };
}
