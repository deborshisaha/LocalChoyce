package fashiome.android.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.parse.ParseUser;

import fashiome.android.R;
import fashiome.android.fragments.SellerReviewFragment;
import fashiome.android.fragments.UserBoughtItemsFragment;
import fashiome.android.models.User;

public class UserProfilePagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[];
    public User user;
    private Context mContext;

    public UserProfilePagerAdapter(FragmentManager fm, User user, Context context) {
        super(fm);
        this.user = user;
        this.mContext = context;

        if(isOwner()){
            tabTitles = new String[] { mContext.getResources().getText(R.string.user_profile_my_products).toString(), mContext.getResources().getText(R.string.user_profile_my_reviews).toString() };
        } else {
            tabTitles = new String[] { mContext.getResources().getText(R.string.user_profile_products).toString(), mContext.getResources().getText(R.string.user_profile_seller_reviews).toString() };
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return UserBoughtItemsFragment.newInstance(user);

            case 1:
                return SellerReviewFragment.newInstance(user);

            default:
                return null;
        }
    }

    private boolean isOwner() {

        if (this.user == null) return false;

        return  ParseUser.getCurrentUser().getObjectId().equals(user.getObjectId());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}