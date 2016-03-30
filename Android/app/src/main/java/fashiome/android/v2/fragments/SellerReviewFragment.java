package fashiome.android.v2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import fashiome.android.R;
import fashiome.android.adapters.SellerReviewRecyclerViewAdapter;
import fashiome.android.models.SellerReview;
import fashiome.android.models.User;

public class SellerReviewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SellerReviewRecyclerViewAdapter mSellerReviewRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean mSellerReviewLoading = false;

    private User mSeller = null;

    public static SellerReviewFragment newInstance(User seller) {
        SellerReviewFragment fragment = new SellerReviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(User.USER_KEY, seller);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSeller = (User) getArguments().getParcelable(User.USER_KEY);
        //getActivity().getIntent().getExtras().getParcelable(User.USER_KEY);

        if (mSeller != null) {
            loadSellerReviews(mSeller);
        }
    }

    private void loadSellerReviews (final User user) {

        if (mSellerReviewLoading == true) {
            swipeRefreshLayout.setEnabled(false);
            return;
        }

        mSellerReviewLoading = true;

        SellerReview.fetchSellerReview(user, new FindCallback<SellerReview>() {
            @Override
            public void done(List<SellerReview> sellerReviews, ParseException e) {
                swipeRefreshLayout.setRefreshing(false);
                mSellerReviewLoading = false;
                if (e == null) {
                    mSellerReviewRecyclerViewAdapter.setSellerReviews(sellerReviews);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recyclerview_conversations, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvConversations);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mSellerReviewRecyclerViewAdapter = new SellerReviewRecyclerViewAdapter();
        mRecyclerView.setAdapter(mSellerReviewRecyclerViewAdapter);

        swipeRefreshLayout.setRefreshing(true);
        loadSellerReviews(mSeller);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadSellerReviews(mSeller);
            }
        });

        return view;
    }

    public void setUser(User user) {
        this.mSeller = user;
    }
}
