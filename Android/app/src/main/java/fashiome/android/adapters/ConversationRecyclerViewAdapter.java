package fashiome.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Conversation;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

/**
 * Created by dsaha on 3/22/16.
 */
public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<ConversationRecyclerViewAdapter.ConversationViewHolder> {

    private List<Conversation> mConversations = new ArrayList<Conversation>();

    public void setConversations (List<Conversation> messages) {
        this.mConversations = messages;
        notifyDataSetChanged();
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(view, view.getContext());;
        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(final ConversationViewHolder conversationViewHolder, int position) {

        if (mConversations == null) {
            return;
        }

        Conversation conversation =  mConversations.get(position);
        conversationViewHolder.configureViewWithConversation(conversation);
    }

    @Override
    public int getItemCount() {

        if (mConversations == null) {
            return 0;
        }

        return mConversations.size();
    }

    public Conversation getConversationAtIndex(int position) {
        if (getItemCount() < position) {
            return null;
        }

        return mConversations.get(position);
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rivImg)
        RoundedImageView rivUserProfileImage;

        @Bind(R.id.tvUserFullname)
        TextView tvUserFullname;

        @Bind(R.id.tvMoreText)
        TextView tvMoreText;

        Context mContext = null;

        public void configureViewWithConversation(Conversation conversation) {
            if (conversation == null) {
                return;
            }

            String URLString = null;

            if (conversation.getOtherUser() != null) {
                URLString = ImageURLGenerator.getInstance(mContext).URLForFBProfilePicture(conversation.getOtherUser().getFacebookId(), Utils.getScreenWidthInDp(mContext));
            }

            if (URLString != null || URLString.length() > 0) {
                Glide.with(mContext).load(URLString).into(rivUserProfileImage);
            }

            tvMoreText.setText(conversation.getLastMessage());
            tvUserFullname.setText(conversation.getOtherUser().getUsername());
        }

        public ConversationViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);

            mContext = context;
        }
    }
}
