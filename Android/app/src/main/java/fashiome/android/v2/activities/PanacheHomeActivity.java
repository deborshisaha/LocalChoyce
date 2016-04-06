package fashiome.android.v2.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.LogOutCallback;
import com.parse.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.v2.adapters.ItemsInterestedInAdapter;
import fashiome.android.v2.adapters.PanacheTabsAdapter;
import fashiome.android.v2.classes.SearchCriteria;
import fashiome.android.v2.fragments.DiscoverProductFragment;

public class PanacheHomeActivity extends AppCompatActivity implements DiscoverProductFragment.OnSearchDeactivationListener{

    @Bind(R.id.tabStripPanacheTabs)
    PagerSlidingTabStrip tabStripPanacheTabs;

    @Bind(R.id.vpPanacheTabs)
    ViewPager vpPanacheTabs;

    @Bind(R.id.searchFab)
    FloatingActionButton searchFab;

    public static FragmentManager fragmentManager;
    private SearchCriteria searchCriteria;
    private PanacheTabsAdapter panacheTabsAdapter;
    private boolean searchEnabled = false;
    private boolean hidden = false;

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
        }, this);

        View.OnClickListener searchOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activateSearch();
            }
        };

        // Setting on click listener
        searchFab.setOnClickListener(searchOnClickListener);

        // Setting adapter of Panache tabs
        vpPanacheTabs.setAdapter(panacheTabsAdapter);

        // Setting View pager
        tabStripPanacheTabs.setViewPager(vpPanacheTabs);

        tabStripPanacheTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

                if (position != 0) {
                    panacheTabsAdapter.notifyOfSearchDeactivation();
                    hideSearchFab();
                } else if (position == 0) {

                    if (searchEnabled) {
                        panacheTabsAdapter.notifyOfSearchActivation();
                        hideSearchFab();
                    } else {
                        showSearchFab();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void showSearchFab() {
        if (hidden == false) {return;}

        searchFab.animate().translationYBy(-200).setInterpolator(new AccelerateInterpolator(2)).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hidden = false;
            }
        }).setStartDelay(50).start();
    }

    private void hideSearchFab() {
        if (hidden == true) {return;}

        searchFab.animate().translationYBy(200).setInterpolator(new AccelerateInterpolator(2)).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hidden = true;
            }
        }).setStartDelay(50).start();
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

        if(panacheTabsAdapter != null) {
            panacheTabsAdapter.notifyDataSetChanged();
            vpPanacheTabs.setCurrentItem(3, true);
            tabStripPanacheTabs.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchDeactivation() {
        deActivateSearch();
    }

    private void activateSearch () {

        panacheTabsAdapter.notifyOfSearchActivation();
        hideSearchFab();
        searchEnabled = true;

    }

    @Override
    public void onMapViewActivation() {
        hideSearchFab();
    }

    private void deActivateSearch () {
        if (!searchEnabled) return;
        showSearchFab();
        searchEnabled = false;
    }
}
