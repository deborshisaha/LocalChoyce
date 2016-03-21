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

import java.util.ArrayList;

import fashiome.android.R;
import fashiome.android.adapters.UserMessagesAdapter;
import fashiome.android.models.User;

public class UserMessagesFragment extends Fragment {

    final static int REFRESH_OPERATION = 1;
    final static int SCROLL_OPERATION = 0;

    RecyclerView rvMessage;
    SwipeRefreshLayout swipeContainer;

    User user;

    UserMessagesAdapter adapter;

    ArrayList<String> fetchedMessages;

    LinearLayoutManager linearLayoutManager;

    public static UserMessagesFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        UserMessagesFragment userMessagesFragment = new UserMessagesFragment();
        userMessagesFragment.setArguments(args);
        return userMessagesFragment;
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_collection_list, container, false);
        //ButterKnife.bind(view);

        rvMessage = (RecyclerView) view.findViewById(R.id.rvTweets);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        Log.i("info", "oncreateview");

        // Set layout manager to position the items
        linearLayoutManager =
                new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvMessage.setLayoutManager(linearLayoutManager);

        // Attach the adapter to the recyclerview to populate items

        rvMessage.setAdapter(adapter);

        fetchedMessages = getAllMessagesFromParse();
        adapter.addAtStartList(fetchedMessages);
        Log.i("info", "adapter size " + adapter.messages.size());
        //adapter.notifyItemRangeInserted(0, twts.size()-1);
        adapter.notifyDataSetChanged();

        //setupScrollListener();

        setupSwipeRefreshListener();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("info", "oncreate");

        user = getArguments().getParcelable("user");

        fetchedMessages = new ArrayList<>();

        // Create adapter passing in the sample user data
        adapter = new UserMessagesAdapter(getContext());

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

    public ArrayList<String> getAllMessagesFromParse(){

        ArrayList<String> messages = new ArrayList<>();

        for(int i =0 ; i<10; i++) {
            messages.add("Hello "+String.valueOf(i));
        }
        return messages;

/*
        Product.fetchProducts(date, new FindCallback<Product>() {
            @Override
            public void done(List<Product> products, ParseException e) {
                swipeContainer.setRefreshing(false);
                if (e == null && products.size() > 0) {
                    Log.d("DEBUG", "Retrieved " + products.size() + " products");
                    lastSeen = products.get(0).getCreatedAt();
                    Log.i("Last seen date: ", lastSeen.toString());
                    for (Product p : products) {
                        Log.i("info", "CreatedAt: " + p.getCreatedAt().toString());
                        Log.i("info", "username : " + String.valueOf(p.getProductPostedBy().getUsername()));
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
                    if (e == null) {
                        Log.i("info", "no results found");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        });
*/

    }

/*
    public void setupScrollListener()
    {
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("info", "scroll - new tweets needed " + since_id);
                getTimeline(0, max_id, null, SCROLL_OPERATION);
            }
        });
    }
*/

}