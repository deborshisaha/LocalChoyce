package fashiome.android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;

import fashiome.android.R;
import fashiome.android.fragments.ProductsRecyclerViewFragment;
import fashiome.android.models.Product;

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

                Intent intent = new Intent(HomeActivity.this, ProductFormActivity.class);
                startActivityForResult(intent,100);

            }
        });


        //find embedded products recycler fragment
        mProductsFragment = (ProductsRecyclerViewFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentProducts);
    }


    public void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        View logo = getLayoutInflater().inflate(R.layout.user_profile_logo, null);
        toolbar.addView(logo);
        setSupportActionBar(toolbar);

        mProfileLogo = (ImageView) findViewById(R.id.ivProfileLogo);

        if(ParseUser.getCurrentUser() != null) {

            String profileUrl = ParseUser.getCurrentUser().get("profilePictureUrl").toString();
            Glide.with(HomeActivity.this).load(profileUrl).into(mProfileLogo);
        }


    }
    public void getUserProfileLogo(View view) {
        Log.i("info","profile clicked");
        if(ParseUser.getCurrentUser() != null){

            // Intent to UserProfileDetailPage

        } else {
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivityForResult(i, 200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 100:
                mProductsFragment.addNewProductToList((Product) data.getParcelableExtra("product"));
                break;

            case 200:
                //get the user's profile logo
                String profileUrl = ParseUser.getCurrentUser().get("profilePictureUrl").toString();
                Glide.with(HomeActivity.this).load(profileUrl).into(mProfileLogo);
                break;

            default:
                return;

        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       //getMenuInflater().inflate(R.menu.menu_home_activity, menu);
       return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       //noinspection SimplifiableIfStatement
        if (id == R.id.map_launcher) {
           return true;
       }

       return super.onOptionsItemSelected(item);
    }

    public void launchMap(View view) {
        Intent i = new Intent(HomeActivity.this,MapFullScreenActivity.class);
        //get list of products from ProductsFragment
        i.putParcelableArrayListExtra("products", mProductsFragment.getProductsAdapter().getAll());
        startActivity(i);
    }

}
