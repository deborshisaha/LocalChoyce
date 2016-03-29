package fashiome.android.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.MainActivity;
import fashiome.android.activities.ProductDetailsActivity;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.animators.ProductResultsAnimator;
import fashiome.android.helpers.ItemClickSupport;
import fashiome.android.models.Product;
import fashiome.android.utils.Constants;
import fashiome.android.utils.Utils;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;
import jp.wasabeef.recyclerview.animators.FlipInRightYAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductsRecyclerViewFragment extends Fragment {

    private static final String TAG = ProductsRecyclerViewFragment.class.getSimpleName();

    /* View bindings */
    @Bind(R.id.rvProduct)
    RecyclerView mProductRecyclerView;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;


    public ProductAdapter getProductsAdapter() {
        return mProductsAdapter;
    }

    /* Private member variables */
    private ProductAdapter mProductsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Date lastSeen;

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
        //mProductRecyclerView.setAdapter(mProductsAdapter);

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mProductsAdapter);
        mProductRecyclerView.setAdapter(alphaAdapter);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        mProductRecyclerView.setItemAnimator(animator);
        mProductRecyclerView.getItemAnimator().setAddDuration(200);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i(TAG, "Refresh to get new items for date" + new Date().toString());
                getAllProductsFromParse(Constants.REFRESH_OPERATION, lastSeen);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);


        ItemClickSupport.addTo(mProductRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Product product = mProductsAdapter.getProductAtIndex(position);

                        Log.i(TAG, "url before: " + String.valueOf(product.getProductPostedBy().getProfilePictureURL()));
                        Log.i(TAG, "Product Rating : " + product.getProductRating());
                        Log.i(TAG, "Price : " + product.getPrice());
                        Log.i(TAG, "User Rating : " + product.getProductPostedBy().getRating());

                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                        intent.putExtra("product", product);
                        startActivityForResult(intent, 500);
                        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        //getActivity().overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);



                    }
                });

        getAllProductsFromParse(Constants.NEW_SEARCH_OPERATION, null);

        return view;
    }

    public void getAllProductsFromParse(final int operation, Date date){

        final ProgressDialog pd = new ProgressDialog(getContext());
                pd.isIndeterminate();
                pd.setMessage("Fetching your styles...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

        Product.fetchProducts(date, new FindCallback<Product>() {
            @Override
            public void done(List<Product> products, ParseException e) {
                pd.dismiss();
                swipeContainer.setRefreshing(false);
                if (e == null && products.size() > 0) {
                    Log.d("DEBUG", "Retrieved " + products.size() + " products");
                    lastSeen = products.get(0).getCreatedAt();
                    Log.i(TAG,"Last seen date: "+lastSeen.toString());
                    for(Product p: products) {
                        Log.i(TAG,"CreatedAt: "+p.getCreatedAt().toString());
                        Log.i(TAG,"CreatedAtmillis: "+p.getCreatedAt().getTime());
                        Log.i(TAG,"username : "+String.valueOf(p.getProductPostedBy().getUsername()));
                        Log.i(TAG, "Latitude : " + p.getAddress().getLatitude());
                        Log.i(TAG, "Longitude : " + p.getAddress().getLongitude());
                        Log.i(TAG, "Product Rating : " + p.getProductRating());
                        Log.i(TAG, "Product Rentals : " + p.getNumberOfRentals());
                        Log.i(TAG, "Price : " + p.getPrice());
                        Log.i(TAG, "User Rating : " + p.getProductPostedBy().getRating());

                        String relativeTimeAgo = DateUtils.getRelativeTimeSpanString(p.getCreatedAt().getTime(),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

                        Log.i(TAG,"Relative time "+ relativeTimeAgo);

                    }
                    mProductsAdapter.updateItems(operation, products);
                    mProductRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProductRecyclerView.scrollToPosition(0);
                        }
                    }, 1000);

                } else {
                    if(e == null){
                        Log.i(TAG,"no results found");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    public void addNewProductsToList(ArrayList<Product> products){
        mProductsAdapter.removeAll();
        mProductsAdapter.notifyDataSetChanged();
        mProductsAdapter.addAll(0, products);
        mProductsAdapter.notifyItemRangeInserted(0, products.size());
        mProductRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductRecyclerView.scrollToPosition(0);
            }
        }, 1000);

    }

    public void addNewProductToList(Product product){

        mProductsAdapter.add(0, product);
        mProductsAdapter.notifyItemInserted(0);
        mProductRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductRecyclerView.scrollToPosition(0);
            }
        }, 1000);
    }

}
