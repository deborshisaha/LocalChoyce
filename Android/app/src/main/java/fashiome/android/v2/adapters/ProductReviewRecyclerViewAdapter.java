package fashiome.android.v2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.models.ProductReview;
import fashiome.android.models.SellerReview;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

public class ProductReviewRecyclerViewAdapter extends RecyclerView.Adapter<ProductReviewRecyclerViewAdapter.ProductReviewViewHolder>  {

    private List<ProductReview> mProductReviews;

    @Override
    public ProductReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        ProductReviewViewHolder productReviewViewHolder = new ProductReviewViewHolder(view, view.getContext());
        return productReviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductReviewViewHolder productReviewViewHolder, int position) {
        if (mProductReviews == null) {
            return;
        }

        ProductReview productReview =  mProductReviews.get(position);
        productReviewViewHolder.configureViewWithProductReview(productReview);
    }

    @Override
    public int getItemCount() {

        if (mProductReviews == null) {
            return 0;
        }

        return mProductReviews.size();
    }

    public ProductReview getProductReviewAtIndex(int position) {
        if (getItemCount() < position) {
            return null;
        }

        return mProductReviews.get(position);
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.mProductReviews = productReviews;
        notifyDataSetChanged();
    }

    public class ProductReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rivProfilePicture)
        RoundedImageView rivUserProfileImage;

        @Bind(R.id.tvTitle)
        TextView tvTitle;

        @Bind(R.id.tvMoreText)
        TextView tvMoreText;

        @Bind(R.id.tvTimeAgo)
        TextView tvTimeAgo;

        @Bind(R.id.ratingUser)
        LinearLayout starLinearLayout;

        Context mContext = null;

        public void configureViewWithProductReview(ProductReview productReview) {

            if (productReview == null) {
                return;
            }

            String URLString = null;

            if (productReview.getUser() != null) {
                URLString = ImageURLGenerator.getInstance(mContext).URLForFBProfilePicture(productReview.getUser().getFacebookId(), Utils.getScreenWidthInDp(mContext));
            }

            if (URLString != null || URLString.length() > 0) {
                Glide.with(mContext).load(URLString).into(rivUserProfileImage);
            }

            Utils.setRating(starLinearLayout, (int) productReview.getRating(), mContext);
            tvMoreText.setText(productReview.getBody());
            tvTitle.setText(productReview.getHeader());
            tvTimeAgo.setText("9h");
        }

        public ProductReviewViewHolder(View view, Context context) {

            super(view);

            ButterKnife.bind(this, view);

            mContext = context;
        }
    }
}
