package fashiome.android.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.animators.ProductResultsAnimator;
import fashiome.android.models.Product;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductsRecyclerViewFragment extends Fragment {

    /* View bindings */
    @Bind(R.id.rvProduct)
    RecyclerView mProductRecyclerView;

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
        mProductRecyclerView.setItemAnimator(new ProductResultsAnimator());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startContentAnimation();
    }

    private void startContentAnimation() {
        mProductsAdapter.updateItems(true);
    }

}
