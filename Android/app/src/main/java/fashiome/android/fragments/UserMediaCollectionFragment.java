package fashiome.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fashiome.android.R;
import fashiome.android.adapters.UserMediaCollectionAdapter;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class UserMediaCollectionFragment extends Fragment {

    final static int REFRESH_OPERATION = 1;
    final static int SCROLL_OPERATION = 0;

    RecyclerView rvMedia;
    SwipeRefreshLayout swipeContainer;

    User user;

    UserMediaCollectionAdapter adapter;

    ArrayList<Product> fetchedProducts;

    StaggeredGridLayoutManager staggeredGridLayoutManager;

    public static UserMediaCollectionFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        UserMediaCollectionFragment userMediaCollectionFragment = new UserMediaCollectionFragment();
        userMediaCollectionFragment.setArguments(args);
        return userMediaCollectionFragment;
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_collection_list, container, false);
        //ButterKnife.bind(view);

        rvMedia = (RecyclerView) view.findViewById(R.id.rvTweets);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        Log.i("info", "oncreateview");

        // Set layout manager to position the items
        /*linearLayoutManager =
                new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvTweets.setLayoutManager(linearLayoutManager);*/

        // Attach the adapter to the recyclerview to populate items

        //rvTweets.setAdapter(adapter);

        // Set layout manager to position the items
        staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvMedia.setLayoutManager(staggeredGridLayoutManager);

        adapter = new UserMediaCollectionAdapter(getActivity());

        getAllProductsFromParseForMedia();

        //setupScrollListener();

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
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void setAnimations(){

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        rvMedia.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));

        //ScaleInLeftAnimator animator = new ScaleInLeftAnimator();
        //FlipInLeftYAnimator animator = new FlipInLeftYAnimator();
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        //animator.setInterpolator(new OvershootInterpolator());
        rvMedia.setItemAnimator(animator);
        rvMedia.getItemAnimator().setAddDuration(200);
        rvMedia.getItemAnimator().setRemoveDuration(400);

    }

    public void getAllProductsFromParseForMedia() {

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.setLimit(20);
        query.orderByDescending("createdAt");
        query.whereEqualTo("productPostedBy", user);
        query.include("productPostedBy");
        query.include("productBoughtBy");
        query.include("address");
        query.findInBackground(new FindCallback<Product>() {
            @Override
            public void done(List<Product> objects, ParseException e) {
                Log.i("info","found media");
                if (e == null && objects.size() > 0) {
                    Log.i("info","found media "+objects.size());
                    fetchedProducts = (ArrayList<Product>)objects;
                    // Create adapter passing in the sample user data
                    setAnimations();
                    adapter.addAtStartList(fetchedProducts);
                    Log.i("info", "media adapter size " + adapter.products.size());
                    Log.i("info"," photos size" + String.valueOf(fetchedProducts.get(0).getPhotos().size()));
                    //adapter.notifyItemRangeInserted(0, twts.size()-1);
                    adapter.notifyDataSetChanged();

                }
            }
        });

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