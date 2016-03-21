package fashiome.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;

public class UserMessagesAdapter extends RecyclerView.Adapter<UserMessagesAdapter.ViewHolder> {

    Context context;

    public ArrayList<String> messages = new ArrayList<>();

    public UserMessagesAdapter(Context context){
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        @Bind(R.id.tvScreenName) TextView mScreenName;
        @Bind(R.id.tvName) TextView mName;
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

            Intent i;

            switch (v.getId()) {

                case R.id.ivProfileImage:


                    Log.i("info", "profile image clicked ");

/*
                    Log.i("info", "profile image clicked :" + users.get(position).getScreenName());
                    Log.i("info", "profile banner url :" + users.get(position).getBanner_image_url());

                    //listener.onProfileImageClicked(tweets.get(position).getUser().getUserId(), position);

                    i = new Intent(context, UserProfileDetailsActivity.class);
                    i.putExtra("user", users.get(position));
                    context.startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
*/
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
        View mediaView = inflater.inflate(R.layout.list_user_messages, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(mediaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        String msg = messages.get(position);

        //holder.mAddUser.setVisibility(View.INVISIBLE);

        //holder.mDescription.setText(user.getDescription());
        holder.mName.setText(msg);
        //holder.mScreenName.setText("@" + user.getScreenName());

/*
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
*/

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void appendList (ArrayList<String> u) {
        messages.addAll(u);
        Log.i("info", "Number of users appended " + messages.size());
    }

    public void addAtStartList (ArrayList<String> u) {

        messages.addAll(0, u);
        Log.i("info", "Number of users prepended " + messages.size());
    }

}