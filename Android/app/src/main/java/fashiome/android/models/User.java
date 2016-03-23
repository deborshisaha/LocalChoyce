package fashiome.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsaha on 3/6/16.
 */
@ParseClassName("_User")
public class User extends ParseUser implements Parcelable {

    public static final String USER_KEY = "fashiome.user.key" ;
    private String facebookId;

    public User() {}

    public String getFacebookId() {
        if (this.facebookId == null) {
            this.facebookId = getString("facebookId");
        }

        return this.facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        put("facebookId", facebookId);
    }

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

    public String getPhoneNumber() {
        if (this.phoneNumber == null) {
            this.phoneNumber = getString("phoneNumber");
        }
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        put("phoneNumber", phoneNumber);
    }

    public String getProfilePictureURL() {
        if (this.profilePictureURL == null) {
            this.profilePictureURL = getString("profilePictureUrl");
        }
        return this.profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
        put("profilePictureURL", profilePictureURL);
    }


    public int getRating() {

        this.rating = getInt("rating");
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        put("rating", rating);
    }

    public String getEmail() {
        if(this.email == null) {
            this.email = getString("email");
        }
        return this.email;
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

    public String getUsername() {
        if(this.username == null) {
            this.username = getString("username");
        }
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
        put("username", username);
    }

    private String firstName;
    private String lastName;
    private String profilePictureURL;
    private int rating;
    private String email;
    private String username;
    //private String skypeId;
    private String phoneNumber;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeString(getFacebookId());
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.profilePictureURL);
        dest.writeString(this.phoneNumber);
        dest.writeInt(this.rating);
        dest.writeString(getEmail());
        dest.writeString(getUsername());
    }

    protected User(Parcel in) {
        this.setObjectId(in.readString());
        this.facebookId = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.profilePictureURL = in.readString();
        this.phoneNumber = in.readString();
        this.rating = in.readInt();
        this.email = in.readString();
        this.username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
