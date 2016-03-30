package fashiome.android.fragments;

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
import fashiome.android.v2.adapters.ProductReviewRecyclerViewAdapter;
import fashiome.android.models.Product;
import fashiome.android.models.ProductReview;

public class ProductReviewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProductReviewRecyclerViewAdapter mProductReviewRecyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean mReviewLoading = false;

    private Product mProduct = null;

    public static ProductReviewFragment newInstance(Product product) {
        ProductReviewFragment fragment = new ProductReviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(Product.PRODUCT_KEY, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProduct = (Product) getActivity().getIntent().getExtras().getParcelable(Product.PRODUCT_KEY);

        if (mProduct != null) {
            loadProductReviews(mProduct);
        }
    }

    private void loadProductReviews (final Product product) {

        if (mReviewLoading == true) {
            swipeRefreshLayout.setEnabled(false);
            return;
        }

        mReviewLoading = true;

        ProductReview.fetchProductReview(product, new FindCallback<ProductReview>() {
            @Override
            public void done(List<ProductReview> productReviews, ParseException e) {
                swipeRefreshLayout.setRefreshing(false);
                mReviewLoading = false;
                if (e == null) {
                    mProductReviewRecyclerViewAdapter.setProductReviews(productReviews);
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
        mProductReviewRecyclerViewAdapter = new ProductReviewRecyclerViewAdapter();
        mRecyclerView.setAdapter(mProductReviewRecyclerViewAdapter);

        swipeRefreshLayout.setRefreshing(true);
        loadProductReviews(mProduct);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadProductReviews(mProduct);
            }
        });

        return view;
    }
}
