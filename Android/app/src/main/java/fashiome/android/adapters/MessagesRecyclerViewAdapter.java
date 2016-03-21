package fashiome.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Message;


public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessageViewHolder> {

    private List<Message> mMessages;

    public void setMessages (List<Message> messages) {
        this.mMessages = messages;
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        MessageViewHolder messageViewHolder = null;

        if (viewType == Message.MESSAGE_TYPE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_received, parent, false);
            messageViewHolder = new MessagesReceivedViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_sent, parent, false);
            messageViewHolder  = new MessagesSentViewHolder(view);
        }

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder messageViewHolder, int position) {

        Message message =  mMessages.get(position);
        messageViewHolder.configureViewWithMessage(message);

    }

    @Override
    public int getItemViewType(int position) {
        Message message =  mMessages.get(position);

        if (message == null) {
            return -1;
        }

        return message.getType();
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rivProfilePicture)
        RoundedImageView rivUserProfileImage;

        @Bind(R.id.tvText)
        TextView tvMessage;

        public void configureViewWithMessage(Message message) {
            if (message == null) {
                return;
            }

            tvMessage.setText(message.getText());
        }

        public MessageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class MessagesSentViewHolder extends MessageViewHolder {

        public MessagesSentViewHolder(View view) {
            super(view);
        }

        public void configureViewWithMessage(Message message) {
            super.configureViewWithMessage(message);
        }
    }

    public class MessagesReceivedViewHolder extends MessageViewHolder {

        public MessagesReceivedViewHolder(View view) {
            super(view);
        }

        public void configureViewWithMessage(Message message) {
            super.configureViewWithMessage(message);
        }
    }
}
