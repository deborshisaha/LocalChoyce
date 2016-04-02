package fashiome.android.v2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.MapviewAdapter;
import fashiome.android.adapters.ProductAdapter;
import fashiome.android.fragments.ProductsRecyclerViewFragment;
import fashiome.android.models.Product;
import fashiome.android.utils.Constants;
import fashiome.android.v2.fragments.ProductListFragment;

public class DiscoverProductFragment extends Fragment {

    private Fragment previouslyActiveFragment = null;
    private ProductMapFragment productMapFragment;
    private ProductListFragment productListFragment;
    private ProductAdapter productAdapter;
    //private MapviewAdapter mapviewAdapter;
    private BannerAdapter bannerAdapter;
    private static final String TAG = "DiscoverProductFragment";

    @Bind(R.id.btnList)
    Button btnList;

    @Bind(R.id.btnMap)
    Button btnMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_discover_v2, container, false);

        ButterKnife.bind(this, view);

        /* Initialization */
        if(productAdapter == null) {
            productAdapter = new ProductAdapter(getActivity());
        }

        if(bannerAdapter == null){
            bannerAdapter = new BannerAdapter(getActivity());
        }
        //if(mapviewAdapter == null){
        //    mapviewAdapter = new MapviewAdapter(getActivity());
       // }

        ParseQuery<Product> query = getParseQueryForProductList();
        query.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> products, ParseException e) {
                Log.i(TAG, "Parse query got products "+products.size());
                productAdapter.updateItems(Constants.NEW_SEARCH_OPERATION, products);
                productAdapter.notifyDataSetChanged();
                //mapviewAdapter.updateItems(Constants.NEW_SEARCH_OPERATION, products);
                //mapviewAdapter.notifyDataSetChanged();
                //getChildFragmentManager().findFragmentById()
                bannerAdapter.addAll(products);
                bannerAdapter.notifyDataSetChanged();
            }
        });

        btnMap.setOnClickListener(onClickListenerForMapButton());
        btnList.setOnClickListener(onClickListenerForListButton());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        insertProductListFragment();
    }

    private View.OnClickListener onClickListenerForListButton (){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //productListFragment.getView().setVisibility(View.VISIBLE);
                insertProductListFragment();

            }
        };
    }

    private View.OnClickListener onClickListenerForMapButton (){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //productListFragment.getView().setVisibility(View.GONE);
                insertProductMapFragment();

            }
        };
    }

    private void insertProductListFragment() {

        if (productListFragment == null) {
            productListFragment = new ProductListFragment();
            productListFragment.setProductAdapter(productAdapter);
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.product_discover_fragment, productListFragment).commit();


/*
        if (getChildFragmentManager().findFragmentById(R.id.product_discover_fragment) == null) {
            transaction.add(R.id.product_discover_fragment, productListFragment).commit();
        }
*/
    }

    private void insertProductMapFragment() {

        if (productMapFragment == null) {
            productMapFragment = new ProductMapFragment();
            //productMapFragment.setProductAdapter(productAdapter);
            productMapFragment.setProductAdapter(bannerAdapter);
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.product_discover_fragment, productMapFragment).commit();

/*
        if (getChildFragmentManager().findFragmentById(R.id.product_discover_fragment) == null) {
            transaction.add(R.id.product_discover_fragment, productMapFragment).commit();
        }
*/
    }

    private ParseQuery<Product> getParseQueryForProductList() {

        ParseQuery<Product>  parseQueryForProductList = ParseQuery.getQuery(Product.class);
        parseQueryForProductList.setLimit(20);
        parseQueryForProductList.orderByDescending("createdAt");
        parseQueryForProductList.include("productPostedBy");
        parseQueryForProductList.include("productBoughtBy");
        parseQueryForProductList.include("address");

        return parseQueryForProductList;
    }
}
