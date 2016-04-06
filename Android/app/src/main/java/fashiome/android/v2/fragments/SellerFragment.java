package fashiome.android.v2.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.v2.activities.IntroAndLoginActivity;
import fashiome.android.activities.MainActivity;
import fashiome.android.v2.activities.ProductFormActivity;
import fashiome.android.v2.adapters.SellerInventoryTabsAdapter;

/**
 * Created by dsaha on 3/29/16.
 */
public class SellerFragment extends Fragment {

    @Bind(R.id.pstsSellerInventoryTab)
    PagerSlidingTabStrip pstsSellerInventoryTab;

    @Bind(R.id.vpSellerInventory)
    ViewPager vpSellerInventory;

    @Bind(R.id.fabAddProduct)
    FloatingActionButton fabAddProduct;

    @Bind(R.id.tvItemsRentedAmount)
    TextView tvItemsRentedAmount;

    @Bind(R.id.tvCurrency)
    TextView tvCurrency;

    @Bind(R.id.tvYearlyEarningsAmount)
    TextView tvYearlyEarningsAmount;


    final int FROM_FAB_TO_LOGIN = 300;

    private boolean fabInExplodedState = false;
    private SellerInventoryTabsAdapter mSellerInventoryTabsAdapter = null;

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

        if (mSellerInventoryTabsAdapter == null) {
            mSellerInventoryTabsAdapter = new SellerInventoryTabsAdapter(getActivity().getSupportFragmentManager());
        }

        if (vpSellerInventory.getAdapter() == null) {
            vpSellerInventory.setAdapter(mSellerInventoryTabsAdapter);
        }

        pstsSellerInventoryTab.setViewPager(vpSellerInventory);

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
        tvCurrency.setText((String)hm.get("currency"));
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
}