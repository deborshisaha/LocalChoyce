package fashiome.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import fashiome.android.interfaces.ReviewInterface;

/**
 * Created by dsaha on 3/18/16.
 */

@ParseClassName("SellerReview")
public class SellerReview extends ParseObject implements Parcelable, ReviewInterface {

    private User seller;
    private User user;
    private String reviewText;
    private double rating;


    public User getSeller() {
        if (this.seller == null) {
            this.seller = (User)getParseObject("seller");
        }

        return this.seller;
    }

    public User getUser() {
        if (this.user == null) {
            this.user = (User)getParseObject("user");
        }

        return this.user;
    }

    public void setSeller(User seller) {
        this.seller = seller;
        put("seller", this.seller);
    }

    public void setUser (User user) {
        this.user = user;
        put("user", this.user);
    }

    public String getReviewText() {

        if (this.reviewText == null) {
            this.reviewText = getString("reviewText");
        }

        return this.reviewText;
    }

    public void setRating(double rating) {
        this.rating = rating;
        put("rating", this.rating);
    }

    public void setReviewText(String string) {
        this.reviewText = string;
        put("reviewText", this.reviewText);
    }

    public double getRating() {

        if (this.rating == 0) {
            this.rating = getDouble("rating");
        }

        return this.rating;
    }

    @Override
    public String getRelativeTime() {
        return null;
    }

    @Override
    public String getHeader() {
        return getUser().getUsername();
    }

    @Override
    public String getBody() {
        return getReviewText();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getSeller(), 0);
        dest.writeParcelable(getUser(), 0);
        dest.writeString(getReviewText());
        dest.writeDouble(getRating());
    }

    public SellerReview() {
    }

    protected SellerReview(Parcel in) {
        this.seller = in.readParcelable(User.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.reviewText = in.readString();
        this.rating = in.readDouble();
    }

    public static final Parcelable.Creator<SellerReview> CREATOR = new Parcelable.Creator<SellerReview>() {
        public SellerReview createFromParcel(Parcel source) {
            return new SellerReview(source);
        }

        public SellerReview[] newArray(int size) {
            return new SellerReview[size];
        }
    };

    public static void fetchSellerReview(User seller, FindCallback<SellerReview> sellerReviewsLoadedBlock) {
        ParseQuery<SellerReview> query = ParseQuery.getQuery(SellerReview.class);
        query.setLimit(50);
        query.setMaxCacheAge(60000*60);
        query.whereEqualTo("seller", seller);
        query.orderByDescending("createdAt");
        query.include("user");
        query.findInBackground(sellerReviewsLoadedBlock);
    }
}
