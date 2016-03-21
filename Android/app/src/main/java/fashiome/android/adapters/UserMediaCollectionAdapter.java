package fashiome.android.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.ProductDetailsActivity;
import fashiome.android.activities.UserDetailsActivity;
import fashiome.android.models.Product;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

public class UserMediaCollectionAdapter extends RecyclerView.Adapter<UserMediaCollectionAdapter.ViewHolder> {

    Context context;

    public ArrayList<Product> products = new ArrayList<>();

    public UserMediaCollectionAdapter(Context context){
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        //@Bind(R.id.llMediaContainer) LinearLayout mMediaContainer;
        @Bind(R.id.ivUserImage) ImageView mImage;
        @Bind(R.id.tvProductName) TextView mProductName;
        @Bind(R.id.tvPrice) TextView mPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position

            switch (v.getId()) {

                case R.id.ivUserImage:

                    Log.i("info", "product image clicked ");
                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    i.putExtra("product", products.get(position));
                    context.startActivity(i);
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
        //View mediaView = inflater.inflate(R.layout.list_user_collection, parent, false);
        View mediaView = inflater.inflate(R.layout.collection_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(mediaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        Product product = products.get(position);

        //holder.mMediaContainer.removeAllViews();

        //View v = LayoutInflater.from(context).inflate(R.layout.collection_item, null);

/*
        Glide.with(context)
                .load(tweet.getPictureUrl())
                .crossFade()
                //.placeholder(R.drawable.ic_placeholder)
                //.error(R.drawable.ic_error)
                .into(ivMediaImage);
*/
        //holder.mMediaContainer.addView(v);
        Log.i("Size of photo array ",String.valueOf(product.getPhotos().size()));
        if(product.getPhotos().size()>0) {

            String URLString = ImageURLGenerator.getInstance(context).URLForImageWithCloudinaryPublicId(product.getImageCloudinaryPublicId(0),
                    Utils.getScreenWidthInDp(context));

            Log.i("info","photo url: "+URLString);

            Glide.with(context).load(URLString).into(holder.mImage);
            holder.mProductName.setText(product.getProductName());
            holder.mPrice.setText("$"+String.valueOf(product.getPrice()));
        }
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public void appendList (ArrayList<Product> t) {
        products.addAll(t);
        Log.i("info", "Number of tweets appended " + products.size());
    }

    public void addAtStartList (ArrayList<Product> t) {

        products.addAll(0, t);
        Log.i("info", "Number of tweets prepended " + products.size());
    }

}