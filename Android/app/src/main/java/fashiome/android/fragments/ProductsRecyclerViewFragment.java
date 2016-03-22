package fashiome.android.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.ProductDetailsActivity;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.animators.ProductResultsAnimator;
import fashiome.android.helpers.ItemClickSupport;
import fashiome.android.models.Product;
import fashiome.android.utils.Constants;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;
import jp.wasabeef.recyclerview.animators.FlipInRightYAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductsRecyclerViewFragment extends Fragment {

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
                Log.i("info", "Refresh to get new items for date" + new Date().toString());
                getAllProductsFromParse(Constants.REFRESH_OPERATION, lastSeen);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        ItemClickSupport.addTo(mProductRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Product product = mProductsAdapter.getProductAtIndex(position);

                        Log.i("info", "url before: " + String.valueOf(product.getProductPostedBy().getProfilePictureURL()));

                        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                        intent.putExtra("product", product);

                        startActivityForResult(intent, 500);

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
                    Log.i("Last seen date: ",lastSeen.toString());
                    for(Product p: products) {
                        Log.i("info","CreatedAt: "+p.getCreatedAt().toString());
                        Log.i("info","username : "+String.valueOf(p.getProductPostedBy().getUsername()));
                        Log.i("info", "Latitude : " + p.getAddress().getLatitude());
                        Log.i("info", "Longitude : " + p.getAddress().getLongitude());
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
                        Log.i("info","no results found");
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
