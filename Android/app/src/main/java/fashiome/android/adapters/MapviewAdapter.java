package fashiome.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.MapFullScreenActivity;
import fashiome.android.v2.activities.ProductDetailsActivity;
import fashiome.android.models.Product;
import fashiome.android.utils.Constants;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;


public class MapviewAdapter extends RecyclerView.Adapter<MapviewAdapter.ViewHolder> {

    Context context;

    private static final String TAG = MapviewAdapter.class.getSimpleName();

    public ArrayList<Product> mProducts = new ArrayList<>();

    public MapviewAdapter(Context context){
        this.context = context;
    }

    public void updateItems(int operation, List<Product> products) {

        if (mProducts == null) {
            mProducts = new ArrayList<Product>();
        }

        if (mProducts.size() > 0 && operation == Constants.NEW_SEARCH_OPERATION) {
            Log.i("info","Clearing items for new search results");
            mProducts.clear();
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        @Bind(R.id.tvItemName) TextView title;
        @Bind(R.id.tvDesc) TextView desc;
        @Bind(R.id.ivItemPhoto) ImageView pic;
        @Bind(R.id.tvPrice) TextView price;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            pic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position

            switch (v.getId()) {

                case R.id.ivItemPhoto:


                    //Log.i("info", "product image clicked ");

                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    i.putExtra("product", mProducts.get(position));
                    context.startActivity(i);
                    //((Activity)context).overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);

                    break;


                default:
                    break;

            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View mediaView = inflater.inflate(R.layout.mapview_product, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(mediaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        Product product = mProducts.get(position);

        //holder.mAddUser.setVisibility(View.INVISIBLE);

        //holder.desc.setText(product.getProductDescription());
        holder.title.setText(product.getProductName());
        //holder.mScreenName.setText("@" + user.getScreenName());

        holder.pic.setImageResource(0);


        if(product.getPhotos().size()>0) {

            String URLString = ImageURLGenerator.getInstance(context).URLForImageWithCloudinaryPublicId(product.getImageCloudinaryPublicId(0),
                    Utils.getScreenWidthInDp(context));

            Log.i(TAG,"photo url: "+URLString);

            Glide.with(context).load(URLString).into(holder.pic);
        }
    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void appendList (ArrayList<Product> u) {
        mProducts.addAll(u);
        Log.i(TAG, "Number of products appended " + mProducts.size());
    }

    public void addAtStartList (ArrayList<Product> u) {

        mProducts.addAll(0, u);
        Log.i(TAG, "Number of products prepended " + mProducts.size());
    }

}