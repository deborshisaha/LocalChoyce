package fashiome.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by dsaha on 3/6/16.
 */
@ParseClassName("_User")
public class User extends ParseUser implements Parcelable {

    public User() {}

    public String getFirstName() {

        if (this.firstName == null) {
            this.firstName = getString("firstName");
        }

        return this.firstName;
    }

    public void setFirstName(String aFirstName) {
        firstName = aFirstName;
        put("firstName", firstName);
    }

    public String getLastName() {

        if (this.lastName == null) {
            this.lastName = getString("lastName");
        }

        return this.lastName;
    }

    public void setLastName(String aLastName) {
        lastName = aLastName;
        put("lastName", lastName);
    }

    public String getProfilePictureURL() {
        return getString("profilePictureURL");
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
        put("profilePictureURL", profilePictureURL);
    }

    public double getRating() {
        return getDouble("rating");
    }

    public void setRating(double rating) {
        this.rating = rating;
        put("rating", rating);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        this.email = email;
        put("email", email);
    }

    public String getFullName() {

        if ( getFirstName() == null && getLastName() == null) {
            return null;
        }

        return getFirstName()+ " " +getLastName();
    }

    private String firstName;
    private String lastName;
    private String profilePictureURL;
    private double rating;
    private String email;

    /**
     * Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.profilePictureURL);
        dest.writeDouble(this.rating);
        dest.writeString(this.email);
    }

    protected User(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.profilePictureURL = in.readString();
        this.rating = in.readDouble();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
