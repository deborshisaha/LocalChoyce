package fashiome.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
import fashiome.android.utils.Constants;
import fashiome.android.v2.activities.IntroAndLoginActivity;

public class HomeActivity extends AppCompatActivity {

    ImageView mProfileLogo;
    ProductsRecyclerViewFragment mProductsFragment;
    boolean isAllProducts = true;
    MenuItem searchItem;
    SearchView searchView;
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"main  fab clicked");

                Intent intent = null;

                if (ParseUser.getCurrentUser() == null) {
                    intent = new Intent(HomeActivity.this, IntroAndLoginActivity.class);
                    intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                    startActivityForResult(intent, 300);
                } else {
                    intent = new Intent(HomeActivity.this, ProductFormActivity.class);
                    startActivityForResult(intent, 100);
                }
            }
        });


        mProductsFragment = (ProductsRecyclerViewFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentProducts);
    }


    public void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //View logo = getLayoutInflater().inflate(R.layout.home_activity_toolbar, null);
        //toolbar.addView(logo);
        setSupportActionBar(toolbar);


        //getSupportActionBar().setIcon(R.drawable.ic_app_logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
/*
        mProfileLogo = (RoundedImageView) findViewById(R.id.ivProfileLogo);
        mProfileLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser() != null) {
                    //Intent i = new Intent(HomeActivity.this, UserProfileActivity.class);
                    Intent i = new Intent(HomeActivity.this, UserDetailsActivity.class);
                    i.putExtra("objectId", ParseUser.getCurrentUser().getObjectId());
                    startActivity(i);
                } else {
                    Intent i = new Intent(HomeActivity.this, IntroAndLoginActivity.class);
                    i.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                    startActivityForResult(i, 200);

                }
            }
        });

        if(ParseUser.getCurrentUser() != null) {
            String profileUrl = ParseUser.getCurrentUser().get("profilePictureUrl").toString();
            Glide.with(HomeActivity.this).load(profileUrl).into(mProfileLogo);
        }
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 100:

                // --- Receiving ---
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
                                Log.i(TAG," Product found: "+product.getProductName());
                                mProductsFragment.addNewProductToList(product);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;

            case 200:
                //get the user's profile logo
                String profileUrl = ParseUser.getCurrentUser().get("profilePictureUrl").toString();
                Glide.with(HomeActivity.this).load(profileUrl).into(mProfileLogo);
                break;

            case 300:

                if (ParseUser.getCurrentUser() != null) {
                    Intent intent = new Intent(HomeActivity.this, ProductFormActivity.class);
                    startActivityForResult(intent, 100);
                }

                break;

            default:
                return;

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

                    if((newText.length() == 0) && (!isAllProducts)) {
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

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.map_launcher) {
            //launchMap();
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

/*
    public void launchMap() {
        Intent i = new Intent(HomeActivity.this,MapFullScreenActivity.class);
        i.putParcelableArrayListExtra("products", mProductsFragment.getProductsAdapter().getAll());
        startActivity(i);
    }
*/

    public void getProductsWithSearchTerm(final String term){

        final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
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

    public void showNoResultsDialog(String term) {

        String message = "No results for "+term;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        alertDialogBuilder
                .setTitle(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_info)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /* this method is overridden to prevent the UP/BACK button_hollow from creating a new activity
instead of showing the old activity */
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }


}
