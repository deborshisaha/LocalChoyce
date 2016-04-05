package fashiome.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import fashiome.android.R;
import fashiome.android.fragments.ProductsRecyclerViewFragment;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.utils.Constants;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;
import fashiome.android.v2.activities.ConversationActivity;
import fashiome.android.v2.activities.IntroAndLoginActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RoundedImageView riv;
    TextView t1;
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView t2;
    Button fb;
    ProductsRecyclerViewFragment mProductsFragment;
    MenuItem searchItem;
    SearchView searchView;
    boolean isAllProducts = true;

    final int FROM_FAB_TO_LOGIN = 300;

    final int FROM_FAB_TO_PRODUCT_FORM = 100;

    final int FROM_HEADER_TO_LOGIN = 400;

    final int FROM_DRAWER_MESSAGES_TO_LOGIN = 500;

    final int FROM_DRAWER_PROFILE_TO_LOGIN = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*
        final RevealFAB revealFAB = (RevealFAB) findViewById(R.id.fab1);

        revealFAB.setOnClickListener(new RevealFAB.OnClickListener() {
            @Override
            public void onClick(RevealFAB button, View v) {
                Log.i("info","main  fab clicked");
                Intent intent = null;

                if (ParseUser.getCurrentUser() == null) {
                    intent = new Intent(MainActivity.this, IntroAndLoginActivity.class);
                    intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                    //startActivityForResult(intent, FROM_FAB_TO_LOGIN);
                } else {
                    intent = new Intent(MainActivity.this, ProductFormActivity.class);
                    //startActivityForResult(intent, FROM_FAB_TO_PRODUCT_FORM);
                }
                revealFAB.setIntent(intent);

                button.startActivityWithAnimation();
            }
        });
*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG,"main  fab clicked");
                Intent intent = null;

                if (ParseUser.getCurrentUser() == null) {
                    intent = new Intent(MainActivity.this, IntroAndLoginActivity.class);
                    intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                    startActivityForResult(intent, FROM_FAB_TO_LOGIN);
                } else {
                    intent = new Intent(MainActivity.this, ProductFormActivity.class);
                    startActivityForResult(intent, FROM_FAB_TO_PRODUCT_FORM);
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);

                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProductsFragment = (ProductsRecyclerViewFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentProducts);


        //setupHeader();
    }

    public void setupHeader(){
        if(ParseUser.getCurrentUser() != null){
            fb.setVisibility(View.INVISIBLE);
            User u = (User)ParseUser.getCurrentUser();
            String profileUrl = ParseUser.getCurrentUser().get("profilePictureUrl").toString();
            Log.i("header", "url " + profileUrl);
            Log.i("header", "url " + u.getProfilePictureURL());
            String URLString = ImageURLGenerator.getInstance(this).URLForFBProfilePicture(u.getFacebookId(), Utils.getScreenWidthInDp(this));
            Log.i("header", "url " + URLString);

            Glide.with(this).load(URLString).into(riv);
            t1.setText(u.getUsername());
            t2.setText(u.getEmail());
            riv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                    intent.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home_activity, menu);

        searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG, "query " + query);

                getProductsWithSearchTerm(query);
                return true;
                //return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.i(TAG, "query " + newText);

                if ((newText.length() == 0) && (!isAllProducts)) {
                    //searchView.setQueryHint("Search ...");
                    mProductsFragment.getAllProductsFromParse(Constants.NEW_SEARCH_OPERATION, null);
                    //ideally should be set true in the getall callback success
                    isAllProducts = true;
                    return true;
                }
                //getProductsWithSearchTerm(newText);

                //return true;
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
//            return true;
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_activity, menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.map_launcher) {
            Log.i(TAG,"Map launcher clicked");
            launchMap();
        //    return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void launchMap() {
        Intent i = new Intent(MainActivity.this,MapFullScreenActivity.class);
        i.putParcelableArrayListExtra("products", mProductsFragment.getProductsAdapter().getAll());
        startActivity(i);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_style) {

            // Handle the camera action
        } else if (id == R.id.nav_profile) {

            if(ParseUser.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                intent.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());
                startActivity(intent);
            } else {

                Intent intent = new Intent(MainActivity.this, IntroAndLoginActivity.class);
                intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                startActivityForResult(intent, FROM_DRAWER_PROFILE_TO_LOGIN);
            }

        } else if (id == R.id.nav_messages) {

            if(ParseUser.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                intent.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());
                startActivity(intent);
            } else {

                Intent intent = new Intent(MainActivity.this, IntroAndLoginActivity.class);
                intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                startActivityForResult(intent, FROM_DRAWER_MESSAGES_TO_LOGIN);
            }

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loginToFacebook(View view) {
        Intent intent;
        intent = new Intent(MainActivity.this, IntroAndLoginActivity.class);
        intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
        startActivityForResult(intent, FROM_HEADER_TO_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == FROM_HEADER_TO_LOGIN) {
            t1 = (TextView) findViewById(R.id.tvBags);
            t2 = (TextView) findViewById(R.id.tvCoats);
            fb = (Button) findViewById(R.id.btnAction);
            riv = (RoundedImageView) findViewById(R.id.rivImg);


            //get the user's profile logo
            User u = (User) ParseUser.getCurrentUser();
            String profileUrl = ParseUser.getCurrentUser().get("profilePictureUrl").toString();
            Log.i(TAG, "url " + profileUrl);
            Log.i(TAG, "url " + u.getProfilePictureURL());
            String URLString = ImageURLGenerator.getInstance(this).URLForFBProfilePicture(u.getFacebookId(), Utils.getScreenWidthInDp(this));
            Log.i(TAG, "url " + URLString);
            Glide.with(MainActivity.this).load(URLString).into(riv);
            t1.setText(u.getUsername());
            t2.setText(u.getEmail());
            fb.setVisibility(View.INVISIBLE);
        }

        if(requestCode == FROM_FAB_TO_LOGIN){
            if (ParseUser.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, ProductFormActivity.class);
                startActivityForResult(intent, 100);
            }

        }

        if(requestCode == FROM_FAB_TO_PRODUCT_FORM){

            if (resultCode == RESULT_OK) {

                Product p = data.getParcelableExtra("product");
                Log.i(TAG," Pid"+p.getObjectId());
                ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
                query.include("productPostedBy");
                query.include("productBoughtBy");
                query.include("address");
                query.getInBackground(p.getObjectId(), new GetCallback<Product>() {
                    public void done(Product product, ParseException e) {
                        if (e == null) {
                            Log.i(TAG, " Product found: " + product.getProductName());
                            mProductsFragment.addNewProductToList(product);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }

        if(requestCode == FROM_DRAWER_MESSAGES_TO_LOGIN) {
            if (ParseUser.getCurrentUser() != null) {

                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                intent.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());
                startActivity(intent);
            }
        }

        if(requestCode == FROM_DRAWER_PROFILE_TO_LOGIN){
            if (ParseUser.getCurrentUser() != null) {

                Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                intent.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());
                startActivity(intent);
            }
        }

    }

    public void getProductsWithSearchTerm(final String term){

        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.isIndeterminate();
        pd.setMessage("Fetching your styles for " + term);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        Product.fetchProductWithSearchTerm(term, new FindCallback<Product>() {

            @Override
            public void done(List<Product> products, ParseException e) {
                pd.dismiss();
                isAllProducts = false;
                //swipeContainer.setRefreshing(false);
                if (e == null && products.size() > 0) {
                    //searchView.setQueryHint(String.valueOf(products.size())+ " results found");
                    Log.d("DEBUG", "Retrieved searched products " + products.size() + " products");
                    //lastSeen = products.get(0).getCreatedAt();
                    //Log.i("Last seen date: ", lastSeen.toString());
                    for (Product p : products) {
                        Log.i(TAG, "Productname: " + p.getProductName());
                        Log.i(TAG, "username : " + String.valueOf(p.getProductPostedBy().getUsername()));
                        Log.i(TAG, "Latitude : " + p.getAddress().getLatitude());
                        Log.i(TAG, "Longitude : " + p.getAddress().getLongitude());
                    }

                    mProductsFragment.addNewProductsToList((ArrayList<Product>) products);
                    //mProductsAdapter.updateItems(operation, products);

                } else {
                    if (e == null) {
                        Log.i(TAG, "no results found");
                        //searchView.setQueryHint("No results");
                        //showNoResultsDialog(term);
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
