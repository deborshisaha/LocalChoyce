package fashiome.android.v2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fashiome.android.R;
import fashiome.android.v2.fragments.MoreFragment;

/**
 * Created by dsaha on 4/3/16.
 */
public class BasicRecyclerViewAdapter extends RecyclerView.Adapter<BasicRecyclerViewAdapter.BasicRecyclerViewHolder> {

    private final Context mContext;
    private List<String> mData;
    private int[] mIcons;

    public void add(String s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class BasicRecyclerViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView ivMoreItemIcon;

        public BasicRecyclerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvMoreItemTitle);
            ivMoreItemIcon = (ImageView) view.findViewById(R.id.ivMoreItemIcon);
        }
    }

    public BasicRecyclerViewAdapter(Context context, String[] data, int[] icons) {
        mContext = context;
        if (data != null)
            mData = new ArrayList<String>(Arrays.asList(data));
        else mData = new ArrayList<String>();

        mIcons = icons;
    }

    public BasicRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.more_items_v2 , parent, false);
        return new BasicRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BasicRecyclerViewHolder holder, final int position) {
        holder.title.setText(mData.get(position));

        if (mIcons != null && mIcons.length > position && mIcons[position] != 0) {
            holder.ivMoreItemIcon.setVisibility(View.VISIBLE);
            holder.ivMoreItemIcon.setImageResource(mIcons[position]);
        } else {
            holder.ivMoreItemIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
