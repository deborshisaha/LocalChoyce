package fashiome.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.ProductPagerAdapter;
import fashiome.android.models.Product;


public class ProductDetailsActivity extends AppCompatActivity {

    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private ViewPager mViewPager;
    private ProductPagerAdapter productPagerAdapter;
    private ShareActionProvider miShareAction;
    private Product mProduct;
    private boolean isLiked = false;

    GoogleMap googleMap;

    @Bind(R.id.tvProductTitle) TextView mProductTitle;
    @Bind(R.id.tvValue) TextView mProductValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mProduct = getIntent().getExtras().getParcelable(Product.PRODUCT_KEY);


        // Get Intent for a product here
        int numberOfRatings = 5;
        LinearLayout mLL = (LinearLayout)findViewById(R.id.llRatingBar);
        for(int i = 0;i<numberOfRatings;i++){

            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.ic_star_filled);
            android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(40,40);
            iv.setLayoutParams(layoutParams);
            mLL.addView(iv);
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
            }
        });

        mProductTitle.setText(mProduct.getProductName());
        mProductValue.setText(String.valueOf(mProduct.getPrice()));

        setViewPagerItemsWithAdapter();
        setUiPageViewController();
        parseCallForIsLiked();

    }

    private void setUiPageViewController() {
        dotsLayout = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        dotsCount = productPagerAdapter.getCount();
        if (dotsCount > 0 ) {
            dots = new TextView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(30);
                dots[i].setTextColor(Color.DKGRAY);
                dotsLayout.addView(dots[i]);
            }

            dots[0].setTextColor(Color.WHITE);
        }
    }

    private void setViewPagerItemsWithAdapter() {

        productPagerAdapter = new ProductPagerAdapter(ProductDetailsActivity.this, mProduct);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(productPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setTextColor(Color.DKGRAY);
                }
                dots[position].setTextColor(Color.WHITE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_product_details_activity, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        //shareIntent.putExtra(Intent.EXTRA_TEXT, wvView.getUrl());

        miShareAction.setShareIntent(shareIntent);

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.menu_item_like);

        if(isLiked){
            item.setIcon(R.drawable.ic_like);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {

            case R.id.menu_item_like:

                if(isLiked){
                    parseCallForRemoveLike();
                } else {
                    parseCallForAddLike();
                }
                break;

            case R.id.menu_item_share:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parseCallForAddLike(){

        ParseObject addLike = new ParseObject("UserProduct");
        addLike.put("userId", ParseUser.getCurrentUser());
        addLike.put("productId", mProduct);
        addLike.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.i("info", "Liked successfully");
                    isLiked = true;
                    invalidateOptionsMenu();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseCallForRemoveLike(){


        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserProduct");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.whereEqualTo("productId", mProduct);
        Log.i("info", "user " + ParseUser.getCurrentUser().getObjectId());
        Log.i("info", "product  " + mProduct.getObjectId());

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> deleteList, ParseException e) {
                if (e == null) {
                    Log.i("Found row to delete", String.valueOf(deleteList.size()));
                    if (deleteList.size() > 0) {
                        for (ParseObject del : deleteList) {
                            del.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("info","Removed like successfully");
                                        isLiked = false;
                                        invalidateOptionsMenu();
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseCallForIsLiked(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserProduct");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.whereEqualTo("productId", mProduct);
        Log.i("info", "user " + ParseUser.getCurrentUser().getObjectId());
        Log.i("info","product  "+mProduct.getObjectId());

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> likeList, ParseException e) {
                if (e == null) {
                    Log.i("Found ", String.valueOf(likeList.size()));
                    if(likeList.size()>0){
                        isLiked = true;
                        invalidateOptionsMenu();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void processPayment(View view) {
        //Intent i = new Intent(ProductDetailsActivity.this, PaymentActivity.class);
        Intent i = new Intent(ProductDetailsActivity.this, BookingDetailsActivity.class);
        startActivity(i);
    }

    /* this method is overridden to prevent the UP/BACK button_hollow from creating a new activity
instead of showing the old activity */
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

}
