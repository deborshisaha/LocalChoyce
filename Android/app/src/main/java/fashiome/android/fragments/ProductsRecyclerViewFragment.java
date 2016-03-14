package fashiome.android.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.ProductDetailsActivity;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.animators.ProductResultsAnimator;
import fashiome.android.helpers.ItemClickSupport;
import fashiome.android.models.Product;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductsRecyclerViewFragment extends Fragment {

    /* View bindings */
    @Bind(R.id.rvProduct)
    RecyclerView mProductRecyclerView;

    public ProductAdapter getProductsAdapter() {
        return mProductsAdapter;
    }

    /* Private member variables */
    private ProductAdapter mProductsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ProductsRecyclerViewFragment() {
    }

    public static ProductsRecyclerViewFragment newInstance() {
        ProductsRecyclerViewFragment productsRecyclerViewFragment = new ProductsRecyclerViewFragment();
        return productsRecyclerViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recyclerview_product, container, false);

        ButterKnife.bind(this, view);
        mProductRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mProductRecyclerView.setLayoutManager(mLayoutManager);

        mProductsAdapter = new ProductAdapter(getContext());
        mProductRecyclerView.setAdapter(mProductsAdapter);


        ItemClickSupport.addTo(mProductRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Product product = mProductsAdapter.getProductAtIndex(position);

                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                        intent.putExtra(Product.PRODUCT_KEY, product);

                        startActivity(intent);
                    }
                }
        );

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.setLimit(10);
        query.include("user");

        query.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> products, ParseException e) {
                if (e == null) {
                    Log.d("DEBUG", "Retrieved " + products.size() + " products");
                    mProductsAdapter.updateItems(true, products);
                } else {
                    Log.d("DEBUG", "Error: " + e.getMessage());
                }
            }
        });

        return view;
    }

    public void addNewProductToList(Product product){
        mProductsAdapter.add(0, product);
        mProductsAdapter.notifyDataSetChanged();
        mProductRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductRecyclerView.scrollToPosition(0);
            }
        }, 1000);
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        startContentAnimation();
//    }
//
//    private void startContentAnimation() {
//        mProductsAdapter.updateItems(true);
//    }

}
