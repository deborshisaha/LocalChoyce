package fashiome.android.v2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.v2.classes.SearchCriteria;

public class DiscoverProductFragment extends Fragment {

    private SearchCriteria sc;
    private ProductMapFragment productMapFragment;
    private ProductListFragment productListFragment;
    private static final String TAG = "DiscoverProductFragment";
    private List<Product> currentProducts = new ArrayList<>();

    @Bind(R.id.btnList)
    Button btnList;

    @Bind(R.id.btnMap)
    Button btnMap;

    public DiscoverProductFragment(SearchCriteria sc) {
        super();
        this.sc = sc;
    }

    public DiscoverProductFragment(){}

    KProgressHUD hud = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_discover_v2, container, false);

        ButterKnife.bind(this, view);

        /* Initialization */

        insertProductMapFragment();
        insertProductListFragment();

        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setMaxProgress(101);
        hud.setLabel("Fetching your styles");
        hud.show();

        ParseQuery<Product> query = getParseQueryForProductList();
        query.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> products, ParseException e) {

                hud.dismiss();
                Log.i(TAG, "Parse query got products " + products.size());
                currentProducts.addAll(products);
                if(productListFragment != null && currentProducts.size() > 0){
                    productListFragment.newData(currentProducts);
                }

                if(productMapFragment != null && currentProducts.size() > 0){
                    productMapFragment.newData(currentProducts);
                }
            }
        });

        btnMap.setOnClickListener(onClickListenerForMapButton());
        btnList.setOnClickListener(onClickListenerForListButton());

        return view;
    }

    private View.OnClickListener onClickListenerForListButton (){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProductListFragment();
            }
        };
    }

    private View.OnClickListener onClickListenerForMapButton (){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProductMapFragment();
            }
        };
    }

    private void insertProductListFragment() {

        Fragment f = getChildFragmentManager().findFragmentById(R.id.product_discover_fragment);

        if(f != null && f instanceof ProductListFragment) {
            return;
        } else {
            if (productListFragment == null) {
                productListFragment = new ProductListFragment();
                if (currentProducts.size() > 0) {
                    productListFragment.newData(currentProducts);
                }
            }

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.product_discover_fragment, productListFragment).commit();
        }
    }

    private void insertProductMapFragment() {

        Fragment f = getChildFragmentManager().findFragmentById(R.id.product_discover_fragment);

        if(f != null && f instanceof ProductMapFragment) {
            return;
        } else {

            if (productMapFragment == null) {
                productMapFragment = new ProductMapFragment();
                if (currentProducts.size() > 0) {
                    productMapFragment.newData(currentProducts);
                }
            }

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.product_discover_fragment, productMapFragment).commit();
        }
    }

    private ParseQuery<Product> getParseQueryForProductList() {

        ParseQuery<Product>  mainQuery = null;

        ParseQuery<Product>  genderQuery = ParseQuery.getQuery(Product.class);

        if (this.sc != null) {

            ArrayList<String> valuesList = new ArrayList<String>(this.sc.getSearchCriteriaItems().values());

            List<ParseQuery<Product>> queries = new ArrayList<ParseQuery<Product>>();

            for (String term:valuesList) {
                queries.add(getParseQueryForTermInName(term, this.sc.getGenderString()));
            }

            mainQuery = ParseQuery.or(queries);

        } else {
            mainQuery = genderQuery;
        }

        mainQuery.orderByDescending("createdAt");
        mainQuery.include("productPostedBy");
        mainQuery.include("productBoughtBy");
        mainQuery.include("address");

        return mainQuery;
    }

    public ParseQuery<Product> getParseQueryForTermInName (String term, String gender) {
        ParseQuery<Product>  q = ParseQuery.getQuery(Product.class);
        q.whereContains("productName", term);
        q.whereContains("gender", gender);
        return q;
    }
}
