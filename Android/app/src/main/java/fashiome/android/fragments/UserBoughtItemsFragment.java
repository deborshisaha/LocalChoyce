package fashiome.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import fashiome.android.R;
import fashiome.android.adapters.UserBoughtItemsAdapter;
import fashiome.android.adapters.UserMessagesAdapter;
import fashiome.android.models.Product;
import fashiome.android.models.User;

public class UserBoughtItemsFragment extends Fragment {

    final static int REFRESH_OPERATION = 1;
    final static int SCROLL_OPERATION = 0;

    RecyclerView rvBought;
    SwipeRefreshLayout swipeContainer;

    UserBoughtItemsAdapter adapter;

    ArrayList<Product> fetchedProducts;

    LinearLayoutManager linearLayoutManager;

    User user;

    public static UserBoughtItemsFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        UserBoughtItemsFragment userBoughtItemsFragment = new UserBoughtItemsFragment();
        userBoughtItemsFragment.setArguments(args);
        return userBoughtItemsFragment;
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_collection_list, container, false);
        //ButterKnife.bind(view);

        rvBought = (RecyclerView) view.findViewById(R.id.rvItems);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        Log.i("info", "oncreateview");

        // Set layout manager to position the items
        linearLayoutManager =
                new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvBought.setLayoutManager(linearLayoutManager);

        // Attach the adapter to the recyclerview to populate items
        // Create adapter passing in the sample user data
        adapter = new UserBoughtItemsAdapter(getContext());

        rvBought.setAdapter(adapter);

        getAllBoughtProductsFromParse();

        setupSwipeRefreshListener();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("info", "oncreate");

        user = getArguments().getParcelable("user");

        fetchedProducts = new ArrayList<>();

    }

    public void setupSwipeRefreshListener(){
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i("info", "refresh - new items needed ");
                //getTimeline(since_id, 0, null, REFRESH_OPERATION);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void getAllBoughtProductsFromParse() {

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.setLimit(20);
        query.orderByDescending("createdAt");
        query.whereEqualTo("productBoughtBy", user);
        query.include("productPostedBy");
        query.include("productBoughtBy");
        query.include("address");
        query.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                Log.i("info","found bought product");
                if (e == null && objects.size() > 0) {
                    Log.i("info","bought products "+objects.size());
                    fetchedProducts = (ArrayList<Product>)objects;
                    // Create adapter passing in the sample user data

                    adapter.addAtStartList(fetchedProducts);
                    Log.i("info", "bought items " + adapter.products.size());
                    Log.i("info"," bought username" + String.valueOf(fetchedProducts.get(0).getProductBoughtBy().getUsername()));
                    //adapter.notifyItemRangeInserted(0, twts.size()-1);
                    adapter.notifyDataSetChanged();

                }
            }
        });

    }
/*
    public void setupScrollListener()
    {
        rvBought.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("info", "scroll - new tweets needed " + since_id);
                getTimeline(0, max_id, null, SCROLL_OPERATION);
            }
        });
    }
*/

}