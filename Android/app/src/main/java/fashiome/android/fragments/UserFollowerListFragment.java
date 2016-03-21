package fashiome.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import fashiome.android.R;
import fashiome.android.adapters.UserFollowerListAdapter;
import fashiome.android.models.Product;
import fashiome.android.models.User;


public class UserFollowerListFragment extends Fragment {


    RecyclerView rvUsers;

    UserFollowerListAdapter adapter;

    User user;

    ArrayList<User> fetchedUsers;

    LinearLayoutManager linearLayoutManager;

    public static UserFollowerListFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        UserFollowerListFragment userFollowerListFragment = new UserFollowerListFragment();
        userFollowerListFragment.setArguments(args);
        return userFollowerListFragment;
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_follower_list, container, false);
        //ButterKnife.bind(view);

        rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);

        Log.i("info", "oncreateview");

        // Set layout manager to position the items
        linearLayoutManager =
                new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvUsers.setLayoutManager(linearLayoutManager);

        // Create adapter passing in the sample user data
        adapter = new UserFollowerListAdapter(getContext());

        rvUsers.setAdapter(adapter);

        getFollowersListFromParse();

        //setupScrollListener();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("info", "oncreate");

        user = getArguments().getParcelable("user");

        fetchedUsers = new ArrayList<>();

    }

    public void getFollowersListFromParse(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFollower");
        query.whereEqualTo("followedUser", user);
        query.include("followedUser");
        query.include("followingUser");
        Log.i("info", "followed user id " + user.getObjectId());

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> followersList, ParseException e) {
                if (e == null) {
                    Log.i("Found followers ", String.valueOf(followersList.size()));
                    if (followersList.size() > 0) {
                        for(int i = 0; i < followersList.size(); i++) {
                            User u = (User) followersList.get(i).get("followingUser");
                            Log.i("info"," follower: "+u.getUsername());
                            fetchedUsers.add(u);
                        }
                    }
                    adapter.addAtStartList(fetchedUsers);
                    adapter.notifyDataSetChanged();

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

/*
    public void setupScrollListener()
    {
        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("info", "scroll - new followers needed " + newCursor);
                getFollowersList(userID, newCursor);
            }
        });
    }
*/

}