package fashiome.android.v2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

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
    private DiscoverProductFragment.OnSearchDeactivationListener onSearchDeactivationListener = null;

    private DiscoverProductFragment discoverProductFragment;

    public PanacheTabsAdapter(FragmentManager fragmentManager, SearchCriteria searchCriteria, LogOutCallback logOutCallback, DiscoverProductFragment.OnSearchDeactivationListener sdl) {
        super(fragmentManager);
        this.sc = searchCriteria;
        this.logOutCallback = logOutCallback;
        this.onSearchDeactivationListener = sdl;
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

        if (discoverProductFragment == null) {
            discoverProductFragment = new DiscoverProductFragment(this.sc, this.onSearchDeactivationListener);
        }

        return discoverProductFragment;
    }

    private Fragment getGuestUserFragment(int position) {

        if (position == 1) {
            return new MoreFragment(null);
        }

        if (discoverProductFragment == null) {
            discoverProductFragment = new DiscoverProductFragment(this.sc, this.onSearchDeactivationListener);
        }

        return discoverProductFragment;
    }

    @Override
    public int getPageIconResId(int position) {

        if (ParseUser.getCurrentUser() != null) {
            return ICONS[position];
        } else {
            return GUEST_ICONS[position];
        }
    }

    public void notifyOfSearchActivation() {
        if (discoverProductFragment != null) {
            discoverProductFragment.activateSearch();
        }
    }
}
