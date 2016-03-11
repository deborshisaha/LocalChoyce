package fashiome.android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.Utils;
import fashiome.android.models.Address;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.utils.ImageURLGenerator;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    /**
     * Private member variables
     */
    private List<Product> mProducts;
    private Context mContext;

    /**
     * Public APIs
     */
    public ProductAdapter() {super();}

    public ProductAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public ProductAdapter(List<Product> products, Context context) {
        super();
        this.mProducts = products;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int position) {
        Product product = mProducts.get(position);
        productViewHolder.configure(product);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        ProductViewHolder productViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        productViewHolder = new ProductViewHolder(inflater.inflate(R.layout.item_product_recyclerview, viewGroup, false), this.mContext);

        return productViewHolder;
    }

    @Override
    public void onViewAttachedToWindow(ProductViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {

        if (mProducts != null) {
            return this.mProducts.size();
        }

        return 0;
    }

    public Product getProductAtIndex(int index) {

        if (getItemCount() < index) {
            return null;
        }

        return mProducts.get(index);
    }

    public void updateItems(boolean animated, List<Product> products) {

        if (mProducts == null) {
            mProducts = new ArrayList<Product>();
        }

        if (mProducts.size() > 0) {
            mProducts.clear();
        }

        if (products !=null) {
            mProducts.addAll(products);
        }

        if (animated) {
            notifyItemRangeInserted(0, mProducts.size());
        } else {
            notifyDataSetChanged();
        }
    }

    /*
    private List<Product> getDummyProducts () {

        final List<Product> mProducts = new ArrayList<Product>();
        final User user = new User();
        user.setPassword("test!@#");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("DEBUG", "Saved User");
                    final Product p2 = new Product();
                    p2.setProductPostedBy(user);
                    p2.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                mProducts.add(p2);
                                Log.d("DEBUG", "Associated");
                            } else {
                                Log.d("DEBUG", e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("DEBUG", "Error >>" + e.getMessage());
                }
            }
        });

        return mProducts;
    }
    */

    /**
     * Inner class
     */
    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        /* View bindings */
        @Bind(R.id.rivProductPrimaryImage)
        RoundedImageView rivProductPrimaryImage;

        @Bind(R.id.tvProductDescription)
        TextView tvProductDescription;

        @Bind(R.id.tvProductTitle)
        TextView tvProductTitle;

        @Bind(R.id.rivProfilePicture)
        RoundedImageView rivProfilePicture;

        @Bind(R.id.tvProductByUserName )
        TextView tvProductByUserName;

        /* Private variables */
        private Context mContext;

        public ProductViewHolder(View itemView, Context context) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            this.mContext = context;
        }

        public void configure(Product product) {

            String URLString = ImageURLGenerator.getInstance(this.mContext).URLForImageWithCloudinaryPublicId(product.getProductPrimaryImageCloudinaryPublicId(), Utils.getScreenWidth(this.mContext));

            Log.d("DEBUG", URLString);

            if (URLString != null || URLString.length() > 0) {
                Glide.with(this.mContext).load(URLString).into(rivProductPrimaryImage);
            }

            tvProductTitle.setText(product.getProductName());
            tvProductDescription.setText(product.getProductDescription());

//            if ( product.getProductPostedBy().getFullName() != null) {
//                tvProductByUserName.setText( product.getProductPostedBy().getFullName());
//            }

        }
    }
}
