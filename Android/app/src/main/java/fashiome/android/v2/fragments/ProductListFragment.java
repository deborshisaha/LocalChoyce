package fashiome.android.v2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.models.Product;
import fashiome.android.utils.Constants;
import fashiome.android.v2.adapters.ProductReviewRecyclerViewAdapter;

public class ProductListFragment extends Fragment {

    @Bind(R.id.rcvProductRecyclerView)
    RecyclerView productRecyclerView;

    public ProductListFragment() {}

    public void setProductParseQuery(ParseQuery<Product> productParseQuery) {
        this.productParseQuery = productParseQuery;
    }

    public void setProductAdapter(ProductAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }

    private ParseQuery<Product> productParseQuery;
    private ProductAdapter productAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_product_list_v2, container, false);

        ButterKnife.bind(this, view);

        if (productAdapter == null) {
            productAdapter = new ProductAdapter(getActivity());
        }

        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getActivity());
        }

        productRecyclerView.setLayoutManager(linearLayoutManager);
        productRecyclerView.setAdapter(productAdapter);

        if (productParseQuery != null) {
            productParseQuery.findInBackground(new FindCallback<Product>() {
                @Override
                public void done(List<Product> products, ParseException e) {
                    productAdapter.updateItems(Constants.REFRESH_OPERATION, products);
                }
            });
        }
        return view;
    }
}
