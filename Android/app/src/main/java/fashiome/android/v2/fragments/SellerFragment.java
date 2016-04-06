package fashiome.android.v2.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.UserBoughtItemsAdapter;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.v2.activities.IntroAndLoginActivity;
import fashiome.android.activities.MainActivity;
import fashiome.android.v2.activities.ProductFormActivity;
import fashiome.android.v2.adapters.SellerInventoryTabsAdapter;
import fashiome.android.v2.adapters.UserInventoryAdapter;

/**
 * Created by dsaha on 3/29/16.
 */
public class SellerFragment extends Fragment {

    @Bind(R.id.fabAddProduct)
    FloatingActionButton fabAddProduct;

    @Bind(R.id.tvItemsRentedAmount)
    TextView tvItemsRentedAmount;

    @Bind(R.id.tvCurrency)
    TextView tvCurrency;

    @Bind(R.id.tvYearlyEarningsAmount)
    TextView tvYearlyEarningsAmount;

    @Bind(R.id.rvInventory)
    RecyclerView rvIntentory;

    final int FROM_FAB_TO_LOGIN = 300;

    LinearLayoutManager linearLayoutManager;

    ArrayList<Product> fetchedProducts;


    private boolean fabInExplodedState = false;
    //private SellerInventoryTabsAdapter mSellerInventoryTabsAdapter = null;
    private UserInventoryAdapter mSellerInventoryTabsAdapter = null;

    public SellerFragment() {}

    public static SellerFragment newInstance() {
        SellerFragment fragment = new SellerFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_seller_v2, container, false);

        ButterKnife.bind(this, view);

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAddProduct.animate().scaleX(100).scaleY(100).setInterpolator(new AccelerateInterpolator()).setDuration(200).setStartDelay(300).setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        fabAddProduct.setImageDrawable(null);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent;
                        if (ParseUser.getCurrentUser() == null) {
                            intent = new Intent(getActivity(), IntroAndLoginActivity.class);
                            intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                            startActivityForResult(intent, FROM_FAB_TO_LOGIN);
                        } else {
                            startNewProductActivity();
                        }
                    }

                }).start();
            }
        });


        // Set layout manager to position the items
        linearLayoutManager =
                new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvIntentory.setLayoutManager(linearLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        // Create adapter passing in the sample user data
        if (mSellerInventoryTabsAdapter == null) {

            mSellerInventoryTabsAdapter = new UserInventoryAdapter(getContext());
        }
        rvIntentory.setAdapter(mSellerInventoryTabsAdapter);

        getAllProductInventoryFromParse();

        loadSellerStats();

        return view;
    }

    private void loadSellerStats() {

        HashMap<String, Object> params = new HashMap<String,Object>();

        if (ParseUser.getCurrentUser() != null) {
            params.put("user", ParseUser.getCurrentUser().getObjectId());
        }

        ParseCloud.callFunctionInBackground("seller_stats", params, new FunctionCallback<HashMap>() {

            @Override
            public void done(HashMap hm, ParseException e) {
                if (e == null) {
                    processSellerStats(hm);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processSellerStats(HashMap hm) {
        tvYearlyEarningsAmount.setText(hm.get("earnings_this_year")+"");
        tvItemsRentedAmount.setText(hm.get("items_rented")+"");
        tvCurrency.setText("$");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FROM_FAB_TO_LOGIN){
            if (ParseUser.getCurrentUser() != null) {
                startNewProductActivity();
            }
        }

    }

    public void startNewProductActivity() {
        Intent intent = new Intent(getActivity(), fashiome.android.v2.activities.ProductFormActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
        fabInExplodedState = true;
    }

    public void onResume() {
        super.onResume();

        if (fabInExplodedState) {

            fabAddProduct.animate().scaleX(1).scaleY(1).setInterpolator(new DecelerateInterpolator()).setDuration(400).setStartDelay(300).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    fabInExplodedState = false;
                    fabAddProduct.setImageResource(R.drawable.ic_plus);
                }

            }).start();
        }
    }

    public void getAllProductInventoryFromParse() {

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.setLimit(20);
        query.orderByDescending("createdAt");
        query.whereEqualTo("productPostedBy", (User)ParseUser.getCurrentUser());
        query.include("productPostedBy");
        query.include("productBoughtBy");
        query.include("address");
        query.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                Log.i("info", "found inventory products");
                if (e == null && objects.size() > 0) {
                    Log.i("info", "inventory products " + objects.size());
                    fetchedProducts = (ArrayList<Product>) objects;
                    // Create adapter passing in the sample user data

                    mSellerInventoryTabsAdapter.addAtStartList(fetchedProducts);
                    Log.i("info", "inventory items " + mSellerInventoryTabsAdapter.mProducts.size());
                    Log.i("info", " inventory username" + String.valueOf(fetchedProducts.get(0).getProductBoughtBy().getUsername()));
                    //adapter.notifyItemRangeInserted(0, twts.size()-1);
                    mSellerInventoryTabsAdapter.notifyDataSetChanged();

                }
            }
        });

    }
}