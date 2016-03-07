package fashiome.android.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by dsaha on 3/6/16.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    public User() {}

    public String getFirstName() {
        return getString("firstName");
    }

    public void setFirstName(String aFirstName) {
        firstName = aFirstName;
        put("firstName", firstName);
    }

    public String getLastName() {
        return getString("lastName");
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

    private String firstName;
    private String lastName;
    private String profilePictureURL;
    private double rating;
    private String email;
}
