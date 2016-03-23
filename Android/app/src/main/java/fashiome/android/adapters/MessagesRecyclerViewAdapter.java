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
import fashiome.android.models.Message;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;


public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessageViewHolder> {

    private List<Message> mMessages = new ArrayList<Message>();

    public void setMessages (List<Message> messages) {
        this.mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        MessageViewHolder messageViewHolder = null;

        if (viewType == Message.MESSAGE_TYPE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            messageViewHolder = new MessagesReceivedViewHolder(view, view.getContext());
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            messageViewHolder  = new MessagesSentViewHolder(view, view.getContext());
        }

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder messageViewHolder, int position) {

        if (mMessages == null) {
            return;
        }

        Message message =  mMessages.get(position);
        messageViewHolder.configureViewWithMessage(message);

    }

    @Override
    public int getItemViewType(int position) {

        Message message = null;

        if (mMessages == null) {
            return -1;
        }

        if (position < mMessages.size()) {
            message =  (Message) mMessages.get(position);
        } else {
            return -1;
        }

        return message.getType();
    }


    @Override
    public int getItemCount() {

        if (mMessages == null) {
            return 0;
        }

        return mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rivImg)
        RoundedImageView rivUserProfileImage;

        @Bind(R.id.tvText)
        TextView tvMessage;

        Context mContext = null;

        public void configureViewWithMessage(Message message) {
            if (message == null) {
                return;
            }

            String URLString = null;

            if (message.getFromUser() != null) {
                URLString = ImageURLGenerator.getInstance(mContext).URLForFBProfilePicture(message.getFromUser().getFacebookId(), Utils.getScreenWidthInDp(mContext));
            }

            if (URLString != null || URLString.length() > 0) {
                Glide.with(mContext).load(URLString).into(rivUserProfileImage);
            }

            tvMessage.setText(message.getText());
        }

        public MessageViewHolder(View view, Context context) {
            super(view);
            ButterKnife.bind(this, view);

            mContext = context;
        }
    }

    public class MessagesSentViewHolder extends MessageViewHolder {

        public MessagesSentViewHolder(View view, Context context) {
            super(view, context);
        }

        public void configureViewWithMessage(Message message, Context context) {
            super.configureViewWithMessage(message);
        }
    }

    public class MessagesReceivedViewHolder extends MessageViewHolder {

        public MessagesReceivedViewHolder(View view, Context context) {
            super(view, context);
        }

        public void configureViewWithMessage(Message message,Context context ) {
            super.configureViewWithMessage(message );
        }
    }
}
