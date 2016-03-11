package fashiome.android.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import fashiome.android.R;
import fashiome.android.Utils;
import fashiome.android.models.Product;
import fashiome.android.utils.ImageURLGenerator;

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

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        String URLString = ImageURLGenerator.getInstance(this.mContext).URLForImageWithCloudinaryPublicId(mProduct.getImageCloudinaryPublicId(position), Utils.getScreenWidth(this.mContext));

        Log.d("DEBUG", URLString);

        if (URLString != null || URLString.length() > 0) {
            Glide.with(this.mContext).load(URLString).crossFade().into(imageView);
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}