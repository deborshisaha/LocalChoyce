package fashiome.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by dsaha on 3/18/16.
 */

@ParseClassName("ProductReview")
public class ProductReview extends ParseObject implements Parcelable {

    private Product product;
    private User user;
    private String reviewText;
    private double rating;


    public Product getProduct() {
        if (this.product == null) {
            this.product = (Product)getParseObject("product");
        }

        return this.product;
    }

    public User getUser() {
        if (this.user == null) {
            this.user = (User)getParseObject("user");
        }

        return this.user;
    }

    public void setProduct(Product product) {
        this.product = product;
        put("product", this.product);
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getProduct(), 0);
        dest.writeParcelable(getUser(), 0);
        dest.writeString(getReviewText());
        dest.writeDouble(getRating());
    }

    public ProductReview() {
    }

    protected ProductReview(Parcel in) {
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.reviewText = in.readString();
        this.rating = in.readDouble();
    }

    public static final Parcelable.Creator<ProductReview> CREATOR = new Parcelable.Creator<ProductReview>() {
        public ProductReview createFromParcel(Parcel source) {
            return new ProductReview(source);
        }

        public ProductReview[] newArray(int size) {
            return new ProductReview[size];
        }
    };

    public static void fetchProductReview(User user, FindCallback<ProductReview> productReviewsLoadedBlock) {
        ParseQuery<ProductReview> query = ParseQuery.getQuery(ProductReview.class);
        query.setLimit(50);
        query.setMaxCacheAge(60000 * 60);
        query.whereEqualTo("user", user);
        query.orderByDescending("createdAt");
        query.include("user");
        query.findInBackground(productReviewsLoadedBlock);
    }
}
