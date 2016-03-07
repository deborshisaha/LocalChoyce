package fashiome.android.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by dsaha on 3/5/16.
 */
@ParseClassName("Product")
public class Product extends ParseObject {

    private String productDescription;
    private String productName;
    private String productPrimaryImageCloudinaryPublicId;
    private float productRating;
    private String productSKU;
    private Address address;
    private User productPostedBy;
    private int numberOfReviews;
    private int numberOfViews;
    private int numberOfFavorites;
    private double price;
    private String currency;

    public String getProductPrimaryImageCloudinaryPublicId() {
        return productPrimaryImageCloudinaryPublicId;
    }

    public void setProductPrimaryImageCloudinaryPublicId(String productPrimaryImageCloudinaryPublicId) {
        this.productPrimaryImageCloudinaryPublicId = productPrimaryImageCloudinaryPublicId;
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
        return (User) getParseObject("user");
    }

    public void setProductPostedBy(User productPostedBy) {
        this.productPostedBy = productPostedBy;
        put("user", productPostedBy);
    }

    /**
     * Default constructor
     */
    public Product() {
        setProductName("Beach flipflops");
        setProductDescription("Greate Beach flipflops");
        setProductRating((float) 4.3);
        setProductSKU("asdwSA");
        setProductPrimaryImageCloudinaryPublicId(getProductSKU());
    }
}
