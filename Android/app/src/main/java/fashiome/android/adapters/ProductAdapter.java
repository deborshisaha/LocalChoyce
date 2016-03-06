package fashiome.android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;

/**
 * Created by dsaha on 3/5/16.
 */
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
        return this.mProducts.size();
    }

    public void updateItems(boolean animated) {

        if (mProducts == null) {
            mProducts = new ArrayList<Product>();
        }

        if (mProducts.size() > 0) {
            mProducts.clear();
        }

        mProducts.addAll(getDummyProducts());
        if (animated) {
            notifyItemRangeInserted(0, mProducts.size());
        } else {
            notifyDataSetChanged();
        }
    }

    private List<Product> getDummyProducts () {

        List<Product> mProducts = new ArrayList<Product>();

        Product p1 = new Product("Beach flipflops", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");
        Product p2 = new Product("Coat", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");
        Product p3 = new Product("Coat", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");
        Product p4 = new Product("Coat", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");

        Product p5 = new Product("Beach flipflops", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");
        Product p6 = new Product("Coat", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");
        Product p7 = new Product("Coat", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");
        Product p8 = new Product("Coat", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the ...");

        mProducts.add(p1);
        mProducts.add(p2);
        mProducts.add(p3);
        mProducts.add(p4);
        mProducts.add(p5);
        mProducts.add(p6);
        mProducts.add(p7);
        mProducts.add(p8);

        return mProducts;
    }

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

        @Bind(R.id.cardViewProduct)
        CardView cardViewProduct;

        /* Private variables */
        private Context mContext;

        public ProductViewHolder(View itemView, Context context) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            this.mContext = context;
        }

        public void configure(Product product) {

            tvProductTitle.setText(product.getProductTitle());
            tvProductDescription.setText(product.getProductDescription());
        }
    }
}
