package fashiome.android.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

import fashiome.android.R;
import fashiome.android.fragments.UserBoughtItemsFragment;
import fashiome.android.fragments.UserFollowerListFragment;
import fashiome.android.fragments.UserMediaCollectionFragment;
import fashiome.android.fragments.UserMessagesFragment;
import fashiome.android.models.User;

public class UserProfilePagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private String tabTitles[];

    private int tabIcons[] ;

    //private Context context;
    private boolean isOwner;

    public User user;

    public UserProfilePagerAdapter(FragmentManager fm, User user, boolean flag) {
        super(fm);
        //this.context = context;
        this.user = user;
        this.isOwner = flag;
        if(isOwner){
            tabTitles = new String[] { "Items bought", "followers", "messages", "collections" };
            tabIcons = new int[] {R.drawable.ic_shopped, R.drawable.ic_user_followers,
                    R.drawable.ic_user_messages, R.drawable.ic_other_collections};
        } else {
            tabTitles = new String[] {"followers", "collections" };
            tabIcons = new int[] {R.drawable.ic_user_followers, R.drawable.ic_other_collections};
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        if(isOwner) {
            switch (position) {

                case 0:
                    return UserBoughtItemsFragment.newInstance(user);
                //break;

                case 1:
                    return UserFollowerListFragment.newInstance(user);
                //break;

                case 2:
                    return UserMessagesFragment.newInstance(user);
                //break;

                case 3:
                    return UserMediaCollectionFragment.newInstance(user);
                //break;

                default:
                    return null;
                //break;
            }
        } else {
            switch (position) {

                case 0:
                    return UserFollowerListFragment.newInstance(user);
                //break;

                case 1:
                    return UserMediaCollectionFragment.newInstance(user);
                //break;

                default:
                    return null;
                //break;
            }
        }
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];

/*
        switch (position) {
            case 0:
                return tabTitles[0];

            case 1:
                return tabTitles[1];

            case 2:
                return tabTitles[2];

            case 3:
                return tabTitles[3];

            default:
                return "";
        }
*/
    }
}