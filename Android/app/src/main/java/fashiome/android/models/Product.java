package fashiome.android.models;

/**
 * Created by dsaha on 3/5/16.
 */
public class Product {

    private String mProductImageURL;
    private String mProductDescription;
    private String mProductTitle;

    public Product(String mProductImageURL, String mProductDescription, String mProductTitle) {
        this.mProductImageURL = mProductImageURL;
        this.mProductDescription = mProductDescription;
        this.mProductTitle = mProductTitle;
    }

    public Product() {}

    public Product(String title, String description) {
        super();
        this.mProductDescription = description;
        this.mProductTitle = title;
    }

    public String getProductImageURL() {
        return mProductImageURL;
    }

    public String getProductDescription() {
        return mProductDescription;
    }

    public String getProductTitle() {
        return mProductTitle;
    }
}
