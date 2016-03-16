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

    public static final String PRODUCT_KEY = "fashiome.product";

    private String productDescription;
    private String productName;
    private String productSKU;
    private String currency;
    private List<String> photos;

    private Address address;
    private User productPostedBy;

    private double productRating;
    private int numberOfReviews;
    private int numberOfViews;
    private int numberOfFavorites;
    private double price;


    public String getProductPrimaryImageCloudinaryPublicId() {

        photos = getList("photos");

        if (photos == null || photos.size() == 0) {
            return "";
        }

        return this.getObjectId() + photos.get(0);
    }

    public Address getAddress() {

        if (this.address == null) {
            this.address = (Address) getParseObject("address");
        }

        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
        put("address", address);
    }

    public void setProductPostedBy(User user) {
        this.productPostedBy = user;
        put("productPostedBy", this.productPostedBy);
    }

    public User getProductPostedBy() {

        if (this.productPostedBy == null) {
            this.productPostedBy = (User) getParseObject("productPostedBy");
        }

        return this.productPostedBy;
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

    public String getProductDescription() {

        if (this.productDescription == null) {
            this.productDescription = getString("productDescription");
        }
        String string= this.productDescription;
        return string;
    }

    public void setProductDescription(String mProductDescription) {
        this.productDescription = mProductDescription;
        put("productDescription", mProductDescription);
    }

    public String getProductName() {

        if (this.productName == null) {
            this.productName = getString("productName");
        }
        String string= this.productName;
        return string;
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

//    public User getProductPostedBy() {
//        this.productPostedBy = (User) getParseUser("productPostedBy");
//        return this.productPostedBy;
//    }
//
//    public void setProductPostedBy(ParseUser productPostedBy) {
//        this.productPostedBy = (User) productPostedBy;
//        put("productPostedBy", productPostedBy);
//    }

    /**
     * Default constructor
     */
    public Product() {
        super();
    }

    public void setPhotos(ArrayList<String> photoCloudinaryPublicIdList) {

        photos = getList("photos");

        if (photos != null) {
            removeAll("photos", photos);
        }

        addAll("photos", photoCloudinaryPublicIdList);
    }

    public List<String> getPhotos() {
        return photos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeString(getProductDescription());
        dest.writeString(getProductName());
        dest.writeString(getProductSKU());
        dest.writeString(getCurrency());
        dest.writeStringList(getPhotos());
        dest.writeParcelable(getAddress(), 0);
        dest.writeParcelable(getProductPostedBy() , 0);
        dest.writeDouble(getProductRating());
        dest.writeInt(getNumberOfFavorites());
        dest.writeInt(getNumberOfViews());
        dest.writeInt(getNumberOfReviews());
        dest.writeDouble(getPrice());
    }

    protected Product(Parcel in) {
        this.setObjectId(in.readString());
        this.productDescription = in.readString();
        this.productName = in.readString();
        this.productSKU = in.readString();
        this.currency = in.readString();
        this.photos = in.createStringArrayList();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.productPostedBy = in.readParcelable(User.class.getClassLoader());
        this.productRating = in.readDouble();
        this.numberOfFavorites = in.readInt();
        this.numberOfViews = in.readInt();
        this.numberOfReviews = in.readInt();
        this.price = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getImageCloudinaryPublicId(int position) {

        if (this.photos.size() < position) {
            return null;
        }

        return this.getObjectId() + this.photos.get(position);
    }
}
