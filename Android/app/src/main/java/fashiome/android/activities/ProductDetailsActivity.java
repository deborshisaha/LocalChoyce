package fashiome.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.utils.StringUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.ProductPagerAdapter;
import fashiome.android.fragments.ProductRentDetailsFragment;
import fashiome.android.managers.Push;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


public class ProductDetailsActivity extends AppCompatActivity implements ProductRentDetailsFragment.ProductRentDetailsDialogListener {

    private static String TAG= "ProductDetailsActivity";

    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private ViewPager mViewPager;
    private ProductPagerAdapter productPagerAdapter;
    private ShareActionProvider miShareAction;

    private boolean isLiked = false;
    private int totalAmount = 0;
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int
    private String URLString = null;
    private User user = null;
    private String mProductIDString = null;
    private Product mProduct = null;

    @Bind(R.id.tvProductTitle)
    TextView mProductTitle;

    @Bind(R.id.tvProductDescription)
    TextView mProductDescription;

    @Bind(R.id.tvSeller)
    TextView mSeller;

    @Bind(R.id.tvAddress)
    TextView mAddress;

    @Bind(R.id.rivSellerProfile)
    RoundedImageView mSellerProfile;

    @Bind(R.id.bRent)
    Button mRent;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mProduct = getIntent().getExtras().getParcelable("product");

        if (mProduct == null ) {
            mProductIDString = getIntent().getExtras().getString(Product.PRODUCT_ID);
        }  else {
            populateViewWithProduct(mProduct);
            return;
        }

        if (StringUtils.isEmpty(mProductIDString)) {
            return;
        }

        Log.d(TAG,"mProductIDString: "+mProductIDString);

        Product.fetchProductWithId(mProductIDString, new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                if (objects.size() == 1) {
                    mProduct = objects.get(0);
                    populateViewWithProduct(mProduct);
                }
            }
        });
    }

    private void populateViewWithProduct (Product product) {
        user = product.getProductPostedBy();

        if (user != null) {
            URLString = ImageURLGenerator.getInstance(this).URLForFBProfilePicture(user.getFacebookId(), Utils.getScreenWidthInDp(this));
        }

        if (URLString != null || URLString.length() > 0) {
            Glide.with(ProductDetailsActivity.this).load(URLString).into(mSellerProfile);
        }

        mProductTitle.setText(product.getProductName());
        mSeller.setText(product.getProductPostedBy().getUsername());
        mProductDescription.setText(String.valueOf(product.getProductDescription()));

        setViewPagerItemsWithAdapter(product);
        setUiPageViewController();

        if(ParseUser.getCurrentUser() != null) {
            parseCallForIsLiked(product);
        }

        getProductLocationAddress(product);
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

    private void setViewPagerItemsWithAdapter(Product product) {

        productPagerAdapter = new ProductPagerAdapter(ProductDetailsActivity.this, product);

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
            item.setIcon(R.drawable.ic_favorite);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {

            case R.id.menu_item_like:

                if(ParseUser.getCurrentUser() != null) {

                    if (isLiked) {
                        parseCallForRemoveLike(mProduct);
                    } else {
                        parseCallForAddLike(mProduct);
                    }
                } else {
                    startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
                }
                break;

            case R.id.menu_item_share:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parseCallForAddLike(final Product product){

        ParseObject addLike = new ParseObject("UserProduct");
        addLike.put("userId", ParseUser.getCurrentUser());
        addLike.put("productId", product);
        addLike.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    Log.i("info", "Liked successfully");
                    isLiked = true;
                    invalidateOptionsMenu();

                    try {
                        Push.userLikedProduct(product);
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }


                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void parseCallForRemoveLike(Product product){


        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserProduct");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.whereEqualTo("productId", product);
        Log.i("info", "user " + ParseUser.getCurrentUser().getObjectId());
        Log.i("info", "product  " + product.getObjectId());

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
                                        Log.i("info", "Removed like successfully");
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

    public void parseCallForIsLiked(Product product){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserProduct");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.whereEqualTo("productId", product);
        Log.i("info", "user " + ParseUser.getCurrentUser().getObjectId());
        Log.i("info", "product  " + product.getObjectId());

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> likeList, ParseException e) {
                if (e == null) {
                    Log.i("Found ", String.valueOf(likeList.size()));
                    if (likeList.size() > 0) {
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

        if(totalAmount <= 0) {
            showEditDialog(mProduct);
        } else {
            onScanPress(view);
        }
    }

    private void showEditDialog(Product product) {
        FragmentManager fm = getSupportFragmentManager();
        Log.i("info", "Price before: " + product.getPrice());
        ProductRentDetailsFragment rentDetailsFragment = ProductRentDetailsFragment.newInstance(product);
        rentDetailsFragment.context = this;
        rentDetailsFragment.listener = this;
        rentDetailsFragment.show(fm, "fragment_rent_details");
    }

    @Override
    public void onSavingRentDetails(int amount) {
        Log.i("info","callback received : total amount "+ String.valueOf(amount));
        totalAmount = amount;
        String displayAmount = "Pay $"+String.valueOf(totalAmount);
        mRent.setText(displayAmount);
    }

    /* this method is overridden to prevent the UP/BACK button_hollow from creating a new activity
instead of showing the old activity */
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
            Toast.makeText(ProductDetailsActivity.this, "Congratulations!! Payment done", Toast.LENGTH_LONG).show();

            // TODO - 1 this is currently not working. I want to send back the rented product back to the HomeActivity or
            // map activity but its a fragment ProductsRecyclerViewFragment

            Intent resultIntent  = new Intent();
            resultIntent.putExtra("product", mProduct);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        // else handle other activity results
    }

    public void getProductLocationAddress(Product product) {

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);

        try {

            List<Address> addresses = geocoder.getFromLocation(product.getAddress().getLatitude(),
                    product.getAddress().getLongitude(), 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                Log.i("I am at: ", strAddress.toString());
                if(strAddress.length()>0 && !strAddress.equals("")){
                    mAddress.setText(strAddress.toString());
                }

            }

            else
                Log.i("info","No location found..!");

        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }
}
