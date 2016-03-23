package fashiome.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
}
