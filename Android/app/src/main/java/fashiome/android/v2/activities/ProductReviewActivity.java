package fashiome.android.v2.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import fashiome.android.R;
import fashiome.android.fragments.ConversationFragment;
import fashiome.android.fragments.ProductReviewFragment;
import fashiome.android.models.Product;

/**
 * Created by dsaha on 3/22/16.
 */
public class ProductReviewActivity extends AppCompatActivity {

    private ProductReviewFragment mProductReviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setTitle("Product reviews");

        Product product = (Product) getIntent().getExtras().getParcelable(Product.PRODUCT_KEY);

        if (product == null) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mProductReviewFragment =  ProductReviewFragment.newInstance(product);
        ft.replace(R.id.fragment_placeholder, mProductReviewFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modal_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.close) {
            finish();
        }

        return true;
    }
}
