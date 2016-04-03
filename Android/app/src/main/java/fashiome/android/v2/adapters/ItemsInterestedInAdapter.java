package fashiome.android.v2.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.utils.Utils;
import fashiome.android.v2.classes.SearchCriteria;

/**
 * Created by dsaha on 4/2/16.
 */
public class ItemsInterestedInAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PHOTO_ANIMATION_DELAY = 300;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private SearchCriteria.Gender gender;

    private final Context context;
    private final int cellSize;

    private static final int[] him_blur_photos = {R.drawable.blur_personalization_bag, R.drawable.blur_personalization_shoes, R.drawable.blur_personalization_coat, R.drawable.blur_personalization_wrist_watch};
    private static final int[] him_sharp_photos = {R.drawable.personalization_shoes, R.drawable.personalization_shoes, R.drawable.personalization_coat, R.drawable.personalization_wrist_watch};

    private static final int[] her_blur_photos = {R.drawable.blur_personalization_bag, R.drawable.blur_personalization_shoes, R.drawable.blur_personalization_coat, R.drawable.blur_personalization_wrist_watch};
    private static final int[] her_sharp_photos = {R.drawable.personalization_shoes, R.drawable.personalization_shoes, R.drawable.personalization_coat, R.drawable.personalization_wrist_watch};

    private static String[] herCategories = {"Bag","Dress","Shoe","Watch"};
    private static String[] hisCategories = {"Bag","Shoe","Coat","Watch"};

    private boolean lockedAnimations = false;
    private int lastAnimatedItem = -1;

    public ItemsInterestedInAdapter(Context context, SearchCriteria.Gender gender) {
        this.context = context;
        this.cellSize = Utils.getScreenWidth(context) / 2;
        this.gender = gender;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.items_interested_in_view_holder_v2, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        return new PhotoViewHolder(view, this.gender);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindPhoto((PhotoViewHolder) holder, position);
    }

    private void bindPhoto(final PhotoViewHolder holder, int position) {

        if (holder.selected) {
            holder.ivItemsInterestedInImage.setImageResource(sharpPhotos(this.gender)[position]);
        } else {
            holder.ivItemsInterestedInImage.setImageResource(blurPhotos(this.gender)[position]);
        }

        holder.tvItemsInterestedInImage.setText(getCategory(this.gender)[position]);

        animatePhoto(holder);
        if (lastAnimatedItem < position) lastAnimatedItem = position;
    }

    private void animatePhoto(PhotoViewHolder viewHolder) {
        if (!lockedAnimations) {
            if (lastAnimatedItem == viewHolder.getPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = PHOTO_ANIMATION_DELAY + viewHolder.getPosition() * 100;

            viewHolder.cvItemsInterestedInHolder.setAlpha(0);

            viewHolder.cvItemsInterestedInHolder.animate()
                    .alpha(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return blurPhotos(this.gender).length;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivItemsInterestedInImage)
        ImageView ivItemsInterestedInImage;
        @Bind(R.id.tvItemsInterestedIn)
        TextView tvItemsInterestedInImage;
        @Bind(R.id.cvItemsInterestedIn)
        CardView cvItemsInterestedInHolder;
        @Bind(R.id.ivSelectedMarker)
        ImageView ivSelectedMarker;

        private SearchCriteria.Gender gender;
        public boolean selected;

        public PhotoViewHolder(View view, SearchCriteria.Gender gender) {
            super(view);
            ButterKnife.bind(this, view);
            selected = false;
            this.gender = gender;
        }

        public void tapped(int position) {

            selected = !selected;

            if (selected) {
                ivSelectedMarker.setVisibility(View.VISIBLE);
                ivItemsInterestedInImage.setImageResource(sharpPhotos(this.gender)[position]);
            } else {
                ivSelectedMarker.setVisibility(View.GONE);
                ivItemsInterestedInImage.setImageResource(blurPhotos(this.gender)[position]);
            }
        }

        public void dismiss(AnimatorListenerAdapter animatorListenerAdapter) {

            if (!selected) {
                this.cvItemsInterestedInHolder.animate().alpha(0).setDuration(500).setStartDelay(200).setListener(animatorListenerAdapter);
            }
        }

        public boolean isSelected() {
            return selected;
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }

    private static int[] blurPhotos(SearchCriteria.Gender gender) {

        if (gender == SearchCriteria.Gender.GENDER_F) {
            return her_blur_photos;
        } else if (gender == SearchCriteria.Gender.GENDER_M) {
            return him_blur_photos;
        }

        return null;
    }

    private static int[] sharpPhotos(SearchCriteria.Gender gender) {
        if (gender == SearchCriteria.Gender.GENDER_F) {
            return her_sharp_photos;
        } else if (gender == SearchCriteria.Gender.GENDER_M) {
            return him_sharp_photos;
        }

        return null;
    }

    public static String[] getCategory(SearchCriteria.Gender gender) {
        if (gender == SearchCriteria.Gender.GENDER_F) {
            return herCategories;
        } else if (gender == SearchCriteria.Gender.GENDER_M) {
            return hisCategories;
        }

        return null;
    }

}
