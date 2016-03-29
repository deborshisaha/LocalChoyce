package fashiome.android.v2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import fashiome.android.R;
import fashiome.android.v2.fragments.ConversationsFragment;
import fashiome.android.v2.fragments.ProductListFragment;
import fashiome.android.v2.fragments.SellerFragment;

public class PanacheTabsAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {


    private final int[] ICONS = { R.drawable.ic_home, R.drawable.ic_seller,
            R.drawable.ic_conversations, R.drawable.ic_profile_filled };

    public PanacheTabsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    ///////
    @Override
    public int getCount() {
        return ICONS.length;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1) {
            return new SellerFragment();
        } else if (position == 2) {
            return new ConversationsFragment();
        }
        return new ProductListFragment();
    }

    @Override
    public int getPageIconResId(int position) {
        return ICONS[position];
    }
}
