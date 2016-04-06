package fashiome.android.v2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.v2.fragments.ProductListFragment;
import fashiome.android.v2.fragments.SellerReviewFragment;

/**
 * Created by dsaha on 4/5/16.
 */
public class SellerInventoryTabsAdapter extends FragmentPagerAdapter {

    private String SELLER_INVENTORY_TITLES[] = new String[] { "Rented", "In stock" };

    public SellerInventoryTabsAdapter(FragmentManager fragmentManager ) {
        super(fragmentManager);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitles()[position];
    }

    @Override
    public int getCount() {
        return getTitles().length;
    }

    @Override
    public Fragment getItem(int position) {

        ProductListFragment productListFragment = ProductListFragment.newInstance();

        if (position == 1) {
            productListFragment.setProductParseQuery(getParseQueryForInStockItems());
        } else {
            productListFragment.setProductParseQuery(getParseQueryForProductsRented());
        }
        return productListFragment;
    }

    public ParseQuery<Product> getParseQueryForInStockItems() {
        ParseQuery<Product>  parseQueryForProductsRented = ParseQuery.getQuery(Product.class);
        parseQueryForProductsRented.setLimit(20);
        parseQueryForProductsRented.orderByDescending("createdAt");
        parseQueryForProductsRented.whereEqualTo("productPostedBy", ParseUser.getCurrentUser());
        parseQueryForProductsRented.include("productPostedBy");
        parseQueryForProductsRented.include("productBoughtBy");
        parseQueryForProductsRented.include("address");

        return parseQueryForProductsRented;
    }

    public ParseQuery<Product> getParseQueryForProductsRented() {
        ParseQuery<Product>  parseQueryForProductsRented = ParseQuery.getQuery(Product.class);
        parseQueryForProductsRented.setLimit(20);
        parseQueryForProductsRented.orderByDescending("createdAt");
        parseQueryForProductsRented.whereEqualTo("productPostedBy", ParseUser.getCurrentUser());
        parseQueryForProductsRented.include("productPostedBy");
        parseQueryForProductsRented.include("productBoughtBy");
        parseQueryForProductsRented.include("address");

        return parseQueryForProductsRented;
    }

    public String[] getTitles() {
        return SELLER_INVENTORY_TITLES;
    }
}
