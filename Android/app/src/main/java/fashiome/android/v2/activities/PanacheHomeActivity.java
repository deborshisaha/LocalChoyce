package fashiome.android.v2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.LogOutCallback;
import com.parse.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.v2.adapters.ItemsInterestedInAdapter;
import fashiome.android.v2.adapters.PanacheTabsAdapter;
import fashiome.android.v2.classes.SearchCriteria;

public class PanacheHomeActivity extends AppCompatActivity {

    @Bind(R.id.tabStripPanacheTabs)
    PagerSlidingTabStrip tabStripPanacheTabs;

    @Bind(R.id.vpPanacheTabs)
    ViewPager vpPanacheTabs;

    public static FragmentManager fragmentManager;
    private SearchCriteria searchCriteria;
    private PanacheTabsAdapter panacheTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        tabStripPanacheTabs.setShouldExpand(true);

        if (getIntent().getExtras() != null) {
            searchCriteria = (SearchCriteria) getIntent().getExtras().getParcelable(SearchCriteria.KEY);
        }

        panacheTabsAdapter = new PanacheTabsAdapter(getSupportFragmentManager(), searchCriteria, new LogOutCallback() {
            @Override
            public void done(ParseException e) {

                panacheTabsAdapter.notifyDataSetChanged();
                vpPanacheTabs.setCurrentItem(0, true);
                tabStripPanacheTabs.notifyDataSetChanged();
            }
        });

        // Setting adapter of Panache tabs
        vpPanacheTabs.setAdapter(panacheTabsAdapter);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
