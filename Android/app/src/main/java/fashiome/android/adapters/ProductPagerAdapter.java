package fashiome.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;


import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

public class ProductPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    private Product mProduct;

    public ProductPagerAdapter(Context context, Product product) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProduct = product;
    }


    @Override
    public int getCount() {
        return mProduct.getPhotos().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.product_pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        String URLString = ImageURLGenerator.getInstance(this.mContext).URLForImageWithCloudinaryPublicId(mProduct.getImageCloudinaryPublicId(position), Utils.getScreenWidth(this.mContext));

        Log.d("DEBUG", URLString);

        if (URLString != null || URLString.length() > 0) {
            Log.i("info", "Loading image from glide");
            Glide.with(this.mContext).load(URLString).into(imageView);

/*
            Glide.with(mContext).load(URLString).asBitmap().into(new BitmapImageViewTarget(imageView) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    //imageView.setImageDrawable(placeholder);
                }

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    //mBitmapMap.put(position, resource);
                    //progressBar.setVisibility(View.INVISIBLE);
                    imageView.setImageBitmap(resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    Log.i("info","image load failed");
                    //progressBar.setVisibility(View.INVISIBLE);
                }
            });
*/
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}