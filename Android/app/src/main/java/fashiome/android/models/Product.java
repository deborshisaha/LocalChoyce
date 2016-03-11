package fashiome.android.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.util.Util;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Product")
public class Product extends ParseObject implements Parcelable {

    private String productDescription;
    private String productName;
    private String productPrimaryImageCloudinaryPublicId;
    private String productSKU;
    private String currency;

    private Address address;
    private User productPostedBy;

    private float productRating;
    private int numberOfReviews;
    private int numberOfViews;
    private int numberOfFavorites;
    private double price;


    public String getProductPrimaryImageCloudinaryPublicId() {

        List<String> arrOfPublicCloudinaryIds = getList("photos");

        if (arrOfPublicCloudinaryIds == null || arrOfPublicCloudinaryIds.size() == 0) {
            return "";
        }

        return this.getObjectId() + arrOfPublicCloudinaryIds.get(0);
    }

    public Address getAddress() {
        return (Address)getParseObject("address");
    }

    public void setAddress(Address address) {
        this.address = address;
        put("address", address);
    }

    public int getNumberOfReviews() {
        return getInt("numberOfReviews");
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
        put("numberOfReviews", numberOfReviews);
    }

    public int getNumberOfViews() {
        return getInt("numberOfViews");
    }

    public void setNumberOfViews(int numberOfViews) {
        this.numberOfViews = numberOfViews;
        put("numberOfViews", numberOfViews);
    }

    public int getNumberOfFavorites() {
        return getInt("numberOfFavorites");
    }

    public void setNumberOfFavorites(int numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
        put("numberOfFavorites", numberOfFavorites);
    }

    public double getPrice() {
        return getDouble("price");
    }

    public void setPrice(double price) {
        this.price = price;
        put("price", price);
    }

    public String getCurrency() {
        return getString("currency");
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        put("currency", currency);
    }

    public String getProductPrimaryImageURL() {
        return getString("productPrimaryImageCloudinaryPublicId");
    }

    public String getProductDescription() {
        return getString("productDescription");
    }

    public void setProductDescription(String mProductDescription) {
        this.productDescription = mProductDescription;
        put("productDescription", mProductDescription);
    }

    public String getProductName() {
        return getString("productName");
    }

    public void setProductName(String mProductName) {
        this.productName = mProductName;
        put("productName", mProductName);
    }

    public double getProductRating() {
        return getDouble("productRating");
    }

    public void setProductRating(float productRating) {
        this.productRating = productRating;
        put("productRating", productRating);
    }

    public String getProductSKU() {
        return getString("productSKU");
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
        put("productSKU", productSKU);
    }

    public User getProductPostedBy() {
        this.productPostedBy = (User) getParseUser("productPostedBy");
        return this.productPostedBy;
    }

    public void setProductPostedBy(ParseUser productPostedBy) {
        this.productPostedBy = (User) productPostedBy;
        put("productPostedBy", productPostedBy);
    }

    /**
     * Default constructor
     */
    public Product() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productDescription);
        dest.writeString(this.productName);
        dest.writeString(this.productPrimaryImageCloudinaryPublicId);
        dest.writeFloat(this.productRating);
        dest.writeString(this.productSKU);
        dest.writeParcelable(this.address, 0);
        dest.writeParcelable(this.productPostedBy, 0);
        dest.writeInt(this.numberOfReviews);
        dest.writeInt(this.numberOfViews);
        dest.writeInt(this.numberOfFavorites);
        dest.writeDouble(this.price);
        dest.writeString(this.currency);
    }

    protected Product(Parcel in) {
        this.productDescription = in.readString();
        this.productName = in.readString();
        this.productPrimaryImageCloudinaryPublicId = in.readString();
        this.productRating = in.readFloat();
        this.productSKU = in.readString();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.productPostedBy = in.readParcelable(User.class.getClassLoader());
        this.numberOfReviews = in.readInt();
        this.numberOfViews = in.readInt();
        this.numberOfFavorites = in.readInt();
        this.price = in.readDouble();
        this.currency = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setPhotos(ArrayList<String> photoCloudinaryPublicIdList) {

        List<String> photos = getList("photos");

        if (photos != null) {
            removeAll("photos", photos);
        }

        addAll("photos", photoCloudinaryPublicIdList);
    }
}
