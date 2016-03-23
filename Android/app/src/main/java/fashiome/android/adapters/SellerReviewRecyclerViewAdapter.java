package fashiome.android.adapters;

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
import fashiome.android.models.Message;
import fashiome.android.models.SellerReview;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;


public class SellerReviewRecyclerViewAdapter extends RecyclerView.Adapter<SellerReviewRecyclerViewAdapter.SellerReviewViewHolder> {

    private List<SellerReview> mSellerReviews;

    @Override
    public SellerReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        SellerReviewViewHolder sellerReviewViewHolder = new SellerReviewViewHolder(view, view.getContext());;
        return sellerReviewViewHolder;
    }

    @Override
    public void onBindViewHolder(SellerReviewViewHolder sellerReviewViewHolder, int position) {
        if (mSellerReviews == null) {
            return;
        }

        SellerReview sellerReview =  mSellerReviews.get(position);
        sellerReviewViewHolder.configureViewWithSellerReview(sellerReview);
    }

    @Override
    public int getItemCount() {

        if (mSellerReviews == null) {
            return 0;
        }

        return mSellerReviews.size();
    }

    public SellerReview getSellerReviewAtIndex(int position) {
        if (getItemCount() < position) {
            return null;
        }

        return mSellerReviews.get(position);
    }

    public void setSellerReviews(List<SellerReview> sellerReviews) {
        this.mSellerReviews = sellerReviews;
        notifyDataSetChanged();
    }

    public class SellerReviewViewHolder extends RecyclerView.ViewHolder {

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

        public void configureViewWithSellerReview(SellerReview sellerReview) {

            if (sellerReview == null) {
                return;
            }

            String URLString = null;

            if (sellerReview.getUser() != null) {
                URLString = ImageURLGenerator.getInstance(mContext).URLForFBProfilePicture(sellerReview.getUser().getFacebookId(), Utils.getScreenWidthInDp(mContext));
            }

            if (URLString != null || URLString.length() > 0) {
                Glide.with(mContext).load(URLString).into(rivUserProfileImage);
            }

            Utils.setRating(starLinearLayout, (int) sellerReview.getRating(), mContext);
            tvMoreText.setText(sellerReview.getBody());
            tvTitle.setText(sellerReview.getHeader());
            tvTimeAgo.setText("7h");
        }

        public SellerReviewViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);

            mContext = context;
        }
    }
}