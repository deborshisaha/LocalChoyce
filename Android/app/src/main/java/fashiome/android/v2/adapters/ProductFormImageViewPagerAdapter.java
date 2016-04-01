package fashiome.android.v2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fashiome.android.R;

public class ProductFormImageViewPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<Bitmap> arrayOfBitmaps = new ArrayList<Bitmap>();

    @Override
    public int getCount() {

        if (arrayOfBitmaps != null) {
            return arrayOfBitmaps.size();
        }

        return 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Bitmap bmp = null;

        if (arrayOfBitmaps.size() > position) {
            bmp = arrayOfBitmaps.get(position);
        } else {
            return null;
        }

        View itemView = mLayoutInflater.inflate(R.layout.product_image_viewpager_item_v2, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(bmp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(itemView);

        return itemView;
    }

    public ProductFormImageViewPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Bitmap takenImage) {
        if (arrayOfBitmaps == null) {
            arrayOfBitmaps = new ArrayList<Bitmap>();
        }

        if (arrayOfBitmaps.size() < 5) {
            arrayOfBitmaps.add(takenImage);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
