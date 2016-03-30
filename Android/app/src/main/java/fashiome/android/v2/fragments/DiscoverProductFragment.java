package fashiome.android.v2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.v2.fragments.ProductListFragment;

public class DiscoverProductFragment extends Fragment {

    private Fragment previouslyActiveFragment = null;
    private ProductMapFragment productMapFragment;
    private ProductListFragment productListFragment;

    @Bind(R.id.btnList)
    Button btnList;

    @Bind(R.id.btnMap)
    Button btnMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_discover_v2, container, false);

        ButterKnife.bind(this, view);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProductMapFragment();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProductListFragment();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        insertProductListFragment();
    }

    private void insertProductListFragment() {

        if (productListFragment == null) {
            productListFragment = new ProductListFragment();
            productListFragment.setProductParseQuery(getParseQueryForProductList());
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.product_discover_fragment, productListFragment).commit();
    }

    private void insertProductMapFragment() {

        if (productMapFragment == null) {productMapFragment = new ProductMapFragment();}

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.product_discover_fragment, productMapFragment).commit();
    }

    private ParseQuery<Product> getParseQueryForProductList() {

        ParseQuery<Product>  parseQueryForProductList = ParseQuery.getQuery(Product.class);
        parseQueryForProductList.setLimit(20);
        parseQueryForProductList.orderByDescending("createdAt");
        parseQueryForProductList.include("productPostedBy");
        parseQueryForProductList.include("address");

        return parseQueryForProductList;
    }
}
