package fashiome.android.v2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import fashiome.android.R;
import fashiome.android.models.User;
import fashiome.android.v2.classes.SearchCriteria;
import fashiome.android.v2.fragments.ConversationsFragment;
import fashiome.android.v2.fragments.DiscoverProductFragment;
import fashiome.android.v2.fragments.MoreFragment;
import fashiome.android.v2.fragments.ProductListFragment;
import fashiome.android.v2.fragments.SellerFragment;
import fashiome.android.v2.fragments.UserProfileFragment;

public class PanacheTabsAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private SearchCriteria sc;

    private final int[] ICONS = { R.drawable.ic_home, R.drawable.ic_seller,
            R.drawable.ic_conversations, R.drawable.more };

    private final int[] GUEST_ICONS = { R.drawable.ic_home, R.drawable.more };
    private LogOutCallback logOutCallback = null;

    public PanacheTabsAdapter(FragmentManager fragmentManager, SearchCriteria searchCriteria, LogOutCallback logOutCallback) {
        super(fragmentManager);
        this.sc = searchCriteria;
        this.logOutCallback = logOutCallback;
    }

    ///////
    @Override
    public int getCount() {
        if (ParseUser.getCurrentUser() != null) {
            return ICONS.length;
        } else {
            return GUEST_ICONS.length;
        }
    }

    @Override
    public Fragment getItem(int position) {

        if (ParseUser.getCurrentUser() != null) {
            return getLoggedInUserFragment(position);
        } else {
            return getGuestUserFragment(position);
        }

    }

    private Fragment getLoggedInUserFragment(int position) {

        if (position == 1) {
            return new SellerFragment();
        } else if (position == 2) {
            return new ConversationsFragment();
        } else if (position == 3) {
            return new MoreFragment(this.logOutCallback);
        }
        return new DiscoverProductFragment(this.sc);
    }

    private Fragment getGuestUserFragment(int position) {

        if (position == 1) {
            return new MoreFragment(null);
        }

        return new DiscoverProductFragment(this.sc);
    }

    @Override
    public int getPageIconResId(int position) {

        if (ParseUser.getCurrentUser() != null) {
            return ICONS[position];
        } else {
            return GUEST_ICONS[position];
        }
    }
}
