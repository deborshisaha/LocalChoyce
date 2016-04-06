package fashiome.android.v2.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fashiome.android.R;
import fashiome.android.v2.activities.ProductDetailsActivity;
import fashiome.android.models.Product;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;


public class BannerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private static final String TAG = "BannerAdapter";

    public ArrayList<Product> mProducts;

    public BannerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProducts = new ArrayList<Product>();
    }

    @Override
    public int getCount() {
        if (mProducts != null)
            return mProducts.size();
        return 0;
    }

    public void addAll(List<Product> products){
        if(products != null){
            mProducts.addAll(products);
        }
    }

    public void removeAll(){
        mProducts.clear();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final Product p = mProducts.get(position);
        Log.i(TAG, "productname: " + p.getProductName());
        p.getPhotos().size();

        View itemView = mLayoutInflater.inflate(R.layout.mapview_product, container, false);

        TextView title = (TextView) itemView.findViewById(R.id.tvItemName);
        TextView desc = (TextView) itemView.findViewById(R.id.tvDesc);
        TextView price = (TextView) itemView.findViewById(R.id.tvPrice);
        TextView rentals = (TextView) itemView.findViewById(R.id.tvNumberOfTimesRented);
        ImageView gender = (ImageView) itemView.findViewById(R.id.ivGender);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivItemPhoto);

        title.setText(p.getProductName());
        desc.setText(p.getProductDescription());
        price.setText("$ "+String.valueOf((int)p.getPrice())+"/day");
        rentals.setText(String.valueOf(p.getNumberOfRentals()));
        if(p.getGender().equalsIgnoreCase("M")){
            imageView.setImageResource(R.drawable.ic_male);
        }

        String URLString = ImageURLGenerator.getInstance(mContext)
                .URLForImageWithCloudinaryPublicId(p.getImageCloudinaryPublicId(0), Utils.getScreenWidthInDp(mContext));

        Log.i(TAG, URLString);

        if (URLString != null || URLString.length() > 0) {
            Log.i("info", "Loading image from glide " + URLString);

            Glide.with(this.mContext)
                    .load(URLString)
                    .into(imageView);

            container.addView(itemView);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("product", p);
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}