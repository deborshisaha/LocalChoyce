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

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.ProductDetailsActivity;
import fashiome.android.models.Product;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

public class UserBoughtItemsAdapter extends RecyclerView.Adapter<UserBoughtItemsAdapter.ViewHolder> {

    Context context;

    public ArrayList<Product> products = new ArrayList<>();

    public UserBoughtItemsAdapter(Context context){
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        @Bind(R.id.tvName) TextView mName;
        @Bind(R.id.tvDescription) TextView mDescription;
        @Bind(R.id.tvMessageTimestamp) TextView mTimestamp;
        @Bind(R.id.ivProfileImage) ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position

            switch (v.getId()) {

                case R.id.ivProfileImage:


                    Log.i("info", "product image clicked ");

                    Intent i = new Intent(context, ProductDetailsActivity.class);
                    i.putExtra("product", products.get(position));
                    context.startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.card_flip_left_in, R.anim.card_flip_left_out);

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
        View mediaView = inflater.inflate(R.layout.list_user_bought_items, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(mediaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        Product product = products.get(position);

        //holder.mAddUser.setVisibility(View.INVISIBLE);

        holder.mDescription.setText(product.getProductDescription());
        holder.mName.setText(product.getProductName());
        //holder.mScreenName.setText("@" + user.getScreenName());

        holder.mImage.setImageResource(0);


        if(product.getPhotos().size()>0) {

            String URLString = ImageURLGenerator.getInstance(context).URLForImageWithCloudinaryPublicId(product.getImageCloudinaryPublicId(0),
                    Utils.getScreenWidthInDp(context));

            Log.i("info","photo url: "+URLString);

            Glide.with(context).load(URLString).into(holder.mImage);
        }
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public void appendList (ArrayList<Product> u) {
        products.addAll(u);
        Log.i("info", "Number of products appended " + products.size());
    }

    public void addAtStartList (ArrayList<Product> u) {

        products.addAll(0, u);
        Log.i("info", "Number of products prepended " + products.size());
    }

}