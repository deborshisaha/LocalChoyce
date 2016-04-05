package fashiome.android.v2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

/**
 * Created by dsaha on 4/4/16.
 */
public class SuggestedItemAdapter  extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<Product> mArrayOfProducts = new ArrayList<Product>();

    @Override
    public int getCount() {
        return mArrayOfProducts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return true;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.item_product_recyclerview, container, false);

        if (mArrayOfProducts.size() < position) {return null;}

        Product product = mArrayOfProducts.get(position);

        configure(itemView, product);

        container.addView(itemView);

        return itemView;
    }

    public SuggestedItemAdapter(Context context, ArrayList<Product> products) {
        super();
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mArrayOfProducts != null) {
            mArrayOfProducts.addAll(products);
        } else {
            mArrayOfProducts = new ArrayList<Product>();
            mArrayOfProducts.addAll(products);
        }
    }

    public SuggestedItemAdapter(Context context) {
        super();
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setProducts (ArrayList<Product> products) {

        if (mArrayOfProducts != null) {
            mArrayOfProducts.addAll(products);
        } else {
            mArrayOfProducts = new ArrayList<Product>();
            mArrayOfProducts.addAll(products);
        }
    }

    private void configure (View view, Product product){

        User user = product.getProductPostedBy();

        String productImageURLString = ImageURLGenerator.getInstance(this.mContext).URLForImageWithCloudinaryPublicId(product.getProductPrimaryImageCloudinaryPublicId(), Utils.getScreenWidthInDp(this.mContext));
        String profileImageURLString = null;

        final RoundedImageView rivProductPrimaryImage = (RoundedImageView)view.findViewById(R.id.rivProductPrimaryImage);
        final RoundedImageView rivProfilePicture = (RoundedImageView)view.findViewById(R.id.rivProfilePicture);
        final TextView tvProductTitle= (TextView)view.findViewById(R.id.tvProductTitle);
        final TextView tvProductDescription= (TextView)view.findViewById(R.id.tvProductDescription);
        final TextView tvPrice= (TextView)view.findViewById(R.id.tvPrice);
        final TextView tvProductByUserName= (TextView)view.findViewById(R.id.tvProductByUserName);

        rivProductPrimaryImage.setImageResource(0);
        if (productImageURLString != null || productImageURLString.length() > 0) {

            Glide.with(this.mContext)
                    .load(productImageURLString)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(rivProductPrimaryImage);
        }

        tvProductTitle.setText(product.getProductName());
        tvProductDescription.setText(product.getProductDescription());
        tvPrice.setText("$" + String.valueOf((int) product.getPrice()) + "/day");

        if (user != null) {
            tvProductByUserName.setText(user.getUsername());
            profileImageURLString = ImageURLGenerator.getInstance(this.mContext).URLForFBProfilePicture(user.getFacebookId(), Utils.getScreenWidthInDp(this.mContext));

            if (profileImageURLString != null || profileImageURLString.length() > 0) {
                Glide.with(this.mContext).load(profileImageURLString).into(rivProfilePicture);
            }

        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
