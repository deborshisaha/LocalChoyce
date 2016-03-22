package fashiome.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.ProductPagerAdapter;
import fashiome.android.fragments.ProductRentDetailsFragment;
import fashiome.android.managers.Push;
import fashiome.android.models.Conversation;
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

    User currentUser;
    ProgressDialog pd;

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

    @Bind(R.id.iBtnMessage)
    ImageView mImageViewMessage;

    @Bind(R.id.tvNumberOfTimesFavorited)
    TextView mTimesLiked;

    @Bind(R.id.tvNumberOfTimesRented)
    TextView mTimesRented;

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

        Log.d(TAG, "mProductIDString: " + mProductIDString);

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

    private void populateViewWithProduct (final Product product) {
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
        mImageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ChatRoomActivity.class);
                intent.putExtra(Conversation.CONVERSATION_IDENTIFIER, Conversation.getIdentifierFromUserId(product.getProductPostedBy().getObjectId(), ParseUser.getCurrentUser().getObjectId()));
                startActivity(intent);
            }
        });
        mTimesLiked.setText(String.valueOf(new Random().nextInt(1000) + 10));
        mTimesRented.setText(String.valueOf(new Random().nextInt(100) + 10));

        int numberOfUserRatings = product.getProductPostedBy().getRating();
        int numberOfProductRatings = product.getProductRating();
        Log.i("info"," user ratings "+String.valueOf(numberOfUserRatings));
        Log.i("info"," product ratings "+String.valueOf(numberOfProductRatings));

        LinearLayout mLL1 = (LinearLayout)findViewById(R.id.llRatingBar1);
        int i;
        int temp = new Random().nextInt(4) + 1;
        for(i = 0;i<temp;i++){

            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.ic_star_filled);
            android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(48,48);
            iv.setLayoutParams(layoutParams);
            mLL1.addView(iv);
        }

        if(i < 5){
            for( ;i<5;i++){
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.ic_star_empty);
                android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(48,48);
                iv.setLayoutParams(layoutParams);
                mLL1.addView(iv);
            }

        }

        LinearLayout mLL2 = (LinearLayout)findViewById(R.id.llRatingBar2);
        temp = new Random().nextInt(4) + 1;
        for(i = 0;i<temp;i++){

            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.ic_star_filled);
            android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(48,48);
            iv.setLayoutParams(layoutParams);
            mLL2.addView(iv);
        }

        if(i < 5){
            for( ;i<5;i++){
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.ic_star_empty);
                android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(48,48);
                iv.setLayoutParams(layoutParams);
                mLL2.addView(iv);
            }

        }

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
                    Intent intent = new Intent(ProductDetailsActivity.this, IntroAndLoginActivity.class);
                    intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                    startActivity(intent);
                }
                break;

            case R.id.menu_item_share:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parseCallForAddLike(final Product product){

        final ProgressDialog pd = new ProgressDialog(ProductDetailsActivity.this);
        pd.isIndeterminate();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        ParseObject addLike = new ParseObject("UserProduct");
        addLike.put("userId", ParseUser.getCurrentUser());
        addLike.put("productId", product);
        addLike.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                pd.dismiss();

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

        final ProgressDialog pd = new ProgressDialog(ProductDetailsActivity.this);
        pd.isIndeterminate();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

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
                                    pd.dismiss();
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
            if(ParseUser.getCurrentUser()!= null) {
                onScanPress(view);
            } else {
                Intent intent = new Intent(ProductDetailsActivity.this, IntroAndLoginActivity.class);
                intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                startActivityForResult(intent, 300);
            }
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
            //Toast.makeText(ProductDetailsActivity.this, "Congratulations!! Payment done", Toast.LENGTH_LONG).show();

            // TODO - 1 this is currently not working. I want to send back the rented product back to the HomeActivity or
            // map activity but its a fragment ProductsRecyclerViewFragment
            // add the bought item to the order table
            pd = new ProgressDialog(ProductDetailsActivity.this);
            pd.setMessage("Completing your purchase ...");
            pd.isIndeterminate();
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

            getUserAndSaveProduct();

        }
        if(requestCode == 300){
            if (ParseUser.getCurrentUser() != null) {
                //Intent intent = new Intent(ProductDetailsActivity.this, ProductFormActivity.class);
                //startActivityForResult(intent, 100);
                processPayment(null);
            }

        }
        // else handle other activity results
    }

    public void saveBoughtProductDetailsToParse (final User user){

        ParseQuery<Product> query = ParseQuery.getQuery("Product");
        query.setLimit(20);
        query.include("productPostedBy");
        query.include("productBoughtBy");
        query.include("address");

        query.getInBackground(mProduct.getObjectId(), new GetCallback<Product>() {
            public void done(final Product product, ParseException e) {
                if (e == null) {

                    product.put("productBoughtBy", user);
                    product.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i("info", "Successfully updated the bought item");
                            pd.setMessage("Purchase done!");
                            pd.dismiss();

                            try {
                                Push.userRentedProduct(product);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            showPurchaseCompleteDialog();

                            //finish();
                        }

                    });
                }
            }
        });
    }

    public void getProductLocationAddress(Product product) {

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);


        //if(product.getAddress() != null) {
            try {

                Log.i("info", "Latitude : " + product.getAddress().getLatitude());
                Log.i("info", "Longitude : " + product.getAddress().getLongitude());

                List<Address> addresses = geocoder.getFromLocation(product.getAddress().getLatitude(),
                        product.getAddress().getLongitude(), 1);

                if (addresses != null && addresses.size() > 0) {

                    Address fetchedAddress = addresses.get(0);
                    StringBuilder strAddress = new StringBuilder();

                    for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                    }

                    Log.i("I am at: ", strAddress.toString());
                    if (strAddress.length() > 0 && !strAddress.equals("")) {
                        mAddress.setText(strAddress.toString());
                    }

                } else
                    Log.i("info", "No location found..!");

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
            }
        //}
    }

    public void getUserAndSaveProduct(){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.i("info", " user found");
                    //ParseUser u = objects.get(0);
                    currentUser = (User) objects.get(0);
                    saveBoughtProductDetailsToParse(currentUser);
                    // The query was successful.
                } else {
                    currentUser = null;
                    Toast.makeText(ProductDetailsActivity.this, "Sorry couldn't complete the purchase", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    finish();
                    // Something went wrong.
                }
            }
        });
    }

    public void showPurchaseCompleteDialog() {

        String message = "Congratulations!";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailsActivity.this);

        alertDialogBuilder
                .setTitle(message)
                .setCancelable(true)
                .setIcon(R.drawable.ic_purchase_complete)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void sellerProfileClicked(View view) {
        Intent i = new Intent(ProductDetailsActivity.this, UserDetailsActivity.class);
        i.putExtra("objectId", mProduct.getProductPostedBy().getObjectId());
        startActivity(i);

    }
}
