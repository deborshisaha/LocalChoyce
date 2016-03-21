package fashiome.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import fashiome.android.R;
import fashiome.android.fragments.ProductsRecyclerViewFragment;
import fashiome.android.models.Product;
import fashiome.android.models.User;

public class HomeActivity extends AppCompatActivity {

    ImageView mProfileLogo;
    ProductsRecyclerViewFragment mProductsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        View logo = getLayoutInflater().inflate(R.layout.home_activity_toolbar, null);
        toolbar.addView(logo);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.drawable.ic_app_logo);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 100:

                // --- Receiving ---
                if (resultCode == RESULT_OK) {

                    Product p = data.getParcelableExtra("product");
                    Log.i("info"," Pid"+p.getObjectId());
                    ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
                    query.include("productPostedBy");
                    query.include("productBoughtBy");
                    query.include("address");
                    query.getInBackground(p.getObjectId(), new GetCallback<Product>() {
                        public void done(Product product, ParseException e) {
                            if (e == null) {
                                Log.i("info"," Product found: "+product.getProductName());
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
       return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.map_launcher) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchMap(View view) {
        Intent i = new Intent(HomeActivity.this,MapFullScreenActivity.class);
        i.putParcelableArrayListExtra("products", mProductsFragment.getProductsAdapter().getAll());
        startActivity(i);
    }

}
