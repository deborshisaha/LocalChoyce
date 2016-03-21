package fashiome.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.UserDetailsActivity;
import fashiome.android.models.User;


public class UserFollowerListAdapter extends RecyclerView.Adapter<UserFollowerListAdapter.ViewHolder> {

    Context context;

    public ArrayList<User> users = new ArrayList<>();

    public UserFollowerListAdapter(Context context){
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        @Bind(R.id.tvScreenName) TextView mScreenName;
        @Bind(R.id.tvName) TextView mName;
        @Bind(R.id.ivProfileImage) ImageView mImage;
        @Bind(R.id.bAddUser) Button mAddUser;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImage.setOnClickListener(this);
            mAddUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition(); // gets item position


            switch (v.getId()) {

                case R.id.ivProfileImage:

                    Log.i("info", "profile clicked ");

                    Intent i = new Intent(context, UserDetailsActivity.class);
                    i.putExtra("objectId", users.get(position).getObjectId());
                    context.startActivity(i);
                    break;

                case R.id.bAddUser:


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
        View mediaView = inflater.inflate(R.layout.list_user_followers, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(mediaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        User user = users.get(position);

        //holder.mAddUser.setVisibility(View.INVISIBLE);

        //holder.mDescription.setText(user.getDescription());
        holder.mName.setText(user.getUsername());
        //holder.mScreenName.setText("@" + user.getScreenName());


        final ImageView ivTemp = holder.mImage;
        ivTemp.setImageResource(0);

        Glide.with(context)
                .load(user.getProfilePictureURL())
                .asBitmap()
                //.placeholder(R.drawable.ic_placeholder)
                //.error(R.drawable.ic_error)
                .into(new BitmapImageViewTarget(ivTemp) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        //circularBitmapDrawable.setCircular(true);
                        circularBitmapDrawable.setCornerRadius(10);
                        //circularBitmapDrawable.setCornerRadius(Math.max(resource.getWidth(), resource.getHeight()) / 2.0f);
                        ivTemp.setImageDrawable(circularBitmapDrawable);
                    }
                });


    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public void appendList (ArrayList<User> u) {
        users.addAll(u);
        Log.i("info", "Number of users appended " + users.size());
    }

    public void addAtStartList (ArrayList<User> u) {

        users.addAll(0, u);
        Log.i("info", "Number of users prepended " + users.size());
    }

}