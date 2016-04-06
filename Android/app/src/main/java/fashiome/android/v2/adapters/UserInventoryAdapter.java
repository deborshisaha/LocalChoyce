package fashiome.android.v2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.adapters.UserBoughtItemsAdapter;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.utils.Constants;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;
import fashiome.android.v2.activities.ProductDetailsActivity;
import fashiome.android.v2.activities.UserProfileActivity;

public class UserInventoryAdapter extends RecyclerView.Adapter<UserInventoryAdapter.InventoryViewHolder> {

    private boolean animationsLocked = false;
    private int lastAnimatedPosition = -1;
    private boolean delayEnterAnimation = true;

    /**
     * Private member variables
     */
    public ArrayList<Product> mProducts = new ArrayList<>();
    private Context mContext;

    /**
     * Public APIs
     */
    public UserInventoryAdapter() {super();}

    public UserInventoryAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public UserInventoryAdapter(ArrayList<Product> products, Context context) {
        super();
        this.mProducts = products;
        this.mContext = context;
    }
    private static final String TAG = "ProductAdapter";

    @Override
    public void onBindViewHolder(InventoryViewHolder productViewHolder, int position) {
        Product product = mProducts.get(position);
        productViewHolder.configure(product);
        runEnterAnimation(productViewHolder.itemView, position);
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        InventoryViewHolder productViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        productViewHolder = new InventoryViewHolder(inflater.inflate(R.layout.item_product_recyclerview, viewGroup, false), this.mContext);

        return productViewHolder;
    }

    @Override
    public void onViewAttachedToWindow(InventoryViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void addAtStartList (ArrayList<Product> u) {

        mProducts.addAll(0, u);
        Log.i("info", "Number of products prepended " + mProducts.size());
    }

    @Override
    public int getItemCount() {

        if (mProducts != null) {
            return this.mProducts.size();
        }

        return 0;
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(600);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(500)
                    .start();
        }
    }

    public Product getProductAtIndex(int index) {

        if (getItemCount() < index) {
            return null;
        }

        return mProducts.get(index);
    }

    public void add(int index, Product product){
        mProducts.add(index, product);
    }

    public void addAll(int index, ArrayList<Product> products){
        mProducts.addAll(index, products);
    }

    public void removeAll(){
        mProducts.clear();
    }


    public void updateItems(int operation, List<Product> products) {

        if (mProducts == null) {
            mProducts = new ArrayList<Product>();
        }

        if (mProducts.size() > 0 && operation == Constants.NEW_SEARCH_OPERATION) {
            mProducts.clear();
            Log.i("info", "Clearing items for new search results "+mProducts.size());
            notifyDataSetChanged();
        }

        if (products != null && products.size() > 0) {
            if(operation == Constants.NEW_SEARCH_OPERATION) {
                int size = mProducts.size();
                Log.i("info", "appending " + products.size() + " items to earlier " + mProducts.size());
                mProducts.addAll(products);
                Log.i("info", "new range 0 to " + mProducts.size());
                notifyItemRangeInserted(size, products.size());
            }
            if(operation == Constants.REFRESH_OPERATION) {
                Log.i("info", "prepending " + products.size() + " items to earlier " + mProducts.size());
                mProducts.addAll(0, products);
                Log.i("info", "new range 0 to " + mProducts.size());
                notifyItemRangeInserted(0, products.size());

            }
        }
    }

    public ArrayList<Product> getAll(){
        return mProducts;
    }

    /**
     * Inner class
     */
    public class InventoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* View bindings */
        @Bind(R.id.rivProductPrimaryImage)
        RoundedImageView rivProductPrimaryImage;

        @Bind(R.id.tvProductDescription)
        TextView tvProductDescription;

        @Bind(R.id.tvProductTitle)
        TextView tvProductTitle;

        @Bind(R.id.rivProfilePicture)
        RoundedImageView rivProfilePicture;

        @Bind(R.id.tvProductByUserName)
        TextView tvProductByUserName;

        @Bind(R.id.tvPrice)
        TextView tvPrice;

        /* Private variables */
        private Context mContext;

        public InventoryViewHolder(View itemView, Context context) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            this.mContext = context;
            itemView.setOnClickListener(this);
            rivProfilePicture.setOnClickListener(this);
            rivProductPrimaryImage.setOnClickListener(this);
            tvProductByUserName.setOnClickListener(this);
            tvProductTitle.setOnClickListener(this);
            tvProductDescription.setOnClickListener(this);
        }

        public void configure(Product product) {

            User user = product.getProductPostedBy();

            String productImageURLString = ImageURLGenerator.getInstance(this.mContext).URLForImageWithCloudinaryPublicId(product.getProductPrimaryImageCloudinaryPublicId(), Utils.getScreenWidthInDp(this.mContext));

            Log.i(TAG, productImageURLString);
            String profileImageURLString = null;
            rivProductPrimaryImage.setImageResource(0);
            if (productImageURLString != null || productImageURLString.length() > 0) {

                Glide.with(this.mContext)
                        .load(productImageURLString)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                //pd.dismiss();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                //pd.dismiss();
                                return false;
                            }
                        })
                        .into(rivProductPrimaryImage);
            }

            tvProductTitle.setText(product.getProductName());
            tvProductDescription.setText(product.getProductDescription());
            tvPrice.setText("$" + String.valueOf((int) product.getPrice()) + "/day");

            rivProfilePicture.setImageResource(0);
            if (user != null) {
                tvProductByUserName.setText(user.getUsername());
                profileImageURLString = ImageURLGenerator.getInstance(this.mContext).URLForFBProfilePicture(user.getFacebookId(), Utils.getScreenWidthInDp(this.mContext));

                if (profileImageURLString != null || profileImageURLString.length() > 0) {
                    Glide.with(this.mContext).load(profileImageURLString).into(rivProfilePicture);
                }

            }
        }

        @Override
        public void onClick(View v) {

            Log.i(TAG, "click detected");
            Intent intent = null;

            switch (v.getId()) {

                case R.id.rivProfilePicture:

/*
                    if (ParseUser.getCurrentUser() == null) {
                        intent = new Intent(mContext, IntroAndLoginActivity.class);
                        intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                        mContext.startActivity(intent);
                    } else {
*/
                    Log.i("info", "user profile image clicked");

                    intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra(User.USER_KEY, mProducts.get(getLayoutPosition()).getProductPostedBy());
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)mContext, (View)rivProfilePicture, "profile");
                    mContext.startActivity(intent, options.toBundle());

                    //}
                    break;

                case R.id.rivProductPrimaryImage:
                    intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("product", mProducts.get(getLayoutPosition()));

                    ActivityOptionsCompat options1 = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, (View)rivProductPrimaryImage, "rivProductPrimaryImage");

                    mContext.startActivity(intent);
                    //mContext.startActivity(intent, options1.toBundle());
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    public void onViewDetachedFromWindow(InventoryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(InventoryViewHolder holder) {
        super.onViewRecycled(holder);
    }
}