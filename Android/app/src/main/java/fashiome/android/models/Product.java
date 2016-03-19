package fashiome.android.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
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
    private String productSize;
    private String gender;
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
        put("productPostedBy", user);
    }

    public User getProductPostedBy() {

        if (this.productPostedBy == null) {
            this.productPostedBy = (User) getParseObject("productPostedBy");
        }

        return this.productPostedBy;
    }

    public int getNumberOfReviews() {

        if(this.numberOfReviews == 0) {
            this.numberOfReviews = getInt("numberOfReviews");
        }
        return this.numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
        put("numberOfReviews", numberOfReviews);
    }

    public int getNumberOfViews() {

        if(this.numberOfViews == 0){
            this.numberOfViews = getInt("numberOfViews");
        }
        return this.numberOfViews;
    }

    public void setNumberOfViews(int numberOfViews) {
        this.numberOfViews = numberOfViews;
        put("numberOfViews", numberOfViews);
    }

    public int getNumberOfFavorites() {

        if(this.numberOfFavorites == 0) {
            this.numberOfFavorites = getInt("numberOfFavorites");
        }
        return this.numberOfFavorites;
    }

    public void setNumberOfFavorites(int numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
        put("numberOfFavorites", numberOfFavorites);
    }

    public double getPrice() {

        if (this.price == 0) {
            this.price = getDouble("price");
        }

        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
        put("price", price);
    }

    public String getCurrency() {

        if (this.currency == null) {
            this.currency = getString("currency");
        }

        return this.currency;
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

    public String getProductSize() {

        if (this.productSize == null) {
            this.productSize = getString("productSize");
        }
        String string = this.productSize;
        return string;
    }

    public void setProductSize(String mProductSize) {
        this.productSize = mProductSize;
        put("productSize", mProductSize);
    }

    public String getGender() {

        if (this.gender == null) {
            this.gender = getString("gender");
        }
        String string = this.gender;
        return string;
    }

    public void setGender(String mGender) {
        this.gender = mGender;
        put("gender", mGender);
    }

    public double getProductRating() {

        if (this.productRating == 0) {
            this.productRating = getDouble("productRating");
        }
        return this.productRating;
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
        dest.writeString(getProductSize());
        dest.writeString(getGender());
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
        this.productSize = in.readString();
        this.gender = in.readString();
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

    public static void fetchProducts(Date date, FindCallback<Product> productsLoadedBlock){

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.setLimit(20);
        query.setMaxCacheAge(60000 * 60);
        query.orderByDescending("createdAt");
        if(date != null) {
            query.whereGreaterThan("createdAt", date);
        }
        query.include("productPostedBy");
        query.include("address");
        query.findInBackground(productsLoadedBlock);
    }

}
