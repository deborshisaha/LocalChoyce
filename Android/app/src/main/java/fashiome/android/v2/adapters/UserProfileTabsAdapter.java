package fashiome.android.v2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.v2.fragments.ProductListFragment;
import fashiome.android.v2.fragments.SellerReviewFragment;

public class UserProfileTabsAdapter extends FragmentPagerAdapter {

    private User profileForUser = null;

    private String CURRENT_USER_PROFILE_TITLES[] = new String[] { "Likes", "Reviews" };
    private String USER_PROFILE_TITLES[] = new String[] { "Marketplace", "Reviews" };

    private final int[] USER_PROFILE_ICONS = { R.drawable.ic_home, R.drawable.ic_conversations };
    private final int[] CURRENT_USER_PROFILE_ICONS = { R.drawable.ic_seller, R.drawable.ic_profile_filled };

    private ParseQuery<Product> parseQueryForUserLikes;

    public UserProfileTabsAdapter(FragmentManager fragmentManager, User user ) {
        super(fragmentManager);
        this.profileForUser = user;
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

        if (position == 1) {
            // Seller reviews
            return SellerReviewFragment.newInstance(profileForUser);
        }

        if (position == 0) {
            ProductListFragment productListFragment = new ProductListFragment();
            if (profileForUser.isCurrentUser()) {
                // return my likes
                productListFragment.setProductParseQuery(getParseQueryForUsersProductsInMarketPlace());
            } else {
                // return my
                productListFragment.setProductParseQuery(getParseQueryForUsersProductsInMarketPlace());
                return productListFragment;
            }
            return productListFragment;
        }

        return null;
    }

    //@Override
    public int getPageIconResId(int position) {
        return getIcons()[position];
    }

    private int[] getIcons () {
        if (profileForUser.isCurrentUser()) {
            return CURRENT_USER_PROFILE_ICONS;
        }

        return USER_PROFILE_ICONS;
    }

    public ParseQuery<Product> getParseQueryForUserLikes() {
        ParseQuery<Product>  parseQueryForUserLikes = ParseQuery.getQuery(Product.class);
        return parseQueryForUserLikes;
    }

    public ParseQuery<Product> getParseQueryForUsersProductsInMarketPlace() {
        ParseQuery<Product>  parseQueryForUsersProductsInMarketPlace = ParseQuery.getQuery(Product.class);
        parseQueryForUsersProductsInMarketPlace.setLimit(20);
        parseQueryForUsersProductsInMarketPlace.orderByDescending("createdAt");
        parseQueryForUsersProductsInMarketPlace.whereEqualTo("productPostedBy", profileForUser);
        parseQueryForUsersProductsInMarketPlace.include("productPostedBy");
        parseQueryForUsersProductsInMarketPlace.include("address");

        return parseQueryForUsersProductsInMarketPlace;
    }

    public String[] getTitles() {

        if (ParseUser.getCurrentUser()!= null) {
            return CURRENT_USER_PROFILE_TITLES;
        }

        return USER_PROFILE_TITLES;
    }
}
