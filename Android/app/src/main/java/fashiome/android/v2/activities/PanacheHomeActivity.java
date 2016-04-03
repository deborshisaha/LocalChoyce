package fashiome.android.v2.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.v2.adapters.PanacheTabsAdapter;

public class PanacheHomeActivity extends AppCompatActivity {

    @Bind(R.id.tabStripPanacheTabs)
    PagerSlidingTabStrip tabStripPanacheTabs;

    @Bind(R.id.vpPanacheTabs)
    ViewPager vpPanacheTabs;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        tabStripPanacheTabs.setShouldExpand(true);

        // Setting adapter of Panache tabs
        vpPanacheTabs.setAdapter(new PanacheTabsAdapter(getSupportFragmentManager()));

        // Setting View pager
        tabStripPanacheTabs.setViewPager(vpPanacheTabs);

        tabStripPanacheTabs.setTranslationY(-tabStripPanacheTabs.getHeight());
        tabStripPanacheTabs.animate().translationY(0).setDuration(1500).setStartDelay(1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
