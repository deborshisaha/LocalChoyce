package fashiome.android.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cloudinary.utils.StringUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.List;

import fashiome.android.R;
import fashiome.android.adapters.MessagesRecyclerViewAdapter;
import fashiome.android.listeners.OnListFragmentInteractionListener;
import fashiome.android.managers.Push;
import fashiome.android.models.Conversation;
import fashiome.android.models.Message;
import fashiome.android.models.User;

public class MessagesFragment extends Fragment {

    private static final String ARG_CHAT_TO_USER = "fashiome.android.productowner";

    private OnListFragmentInteractionListener mListener;
    private MessagesRecyclerViewAdapter mMessagesRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private EditText mChatEditText;
    private Button mSendButton;
    private String mConversationIdentifierString;
    private static final int POLL_INTERVAL = 1000;
    private Handler mHandler = new Handler();
    private int mPreviousMessageCount = 0;

    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {

            if (!StringUtils.isEmpty(mConversationIdentifierString)) {
                loadMessages(mConversationIdentifierString);
            }

            mHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    public MessagesFragment() {}

    public static MessagesFragment newInstance(User chatToUser) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CHAT_TO_USER, chatToUser);
        fragment.setArguments(args);
        return fragment;
    }

    public static MessagesFragment newInstance(String conversationIdentifier) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(Conversation.CONVERSATION_IDENTIFIER, conversationIdentifier);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConversationIdentifierString = getActivity().getIntent().getExtras().getString(Conversation.CONVERSATION_IDENTIFIER);

        if (!StringUtils.isEmpty(mConversationIdentifierString)) {
            loadMessages(mConversationIdentifierString);
        }

        mHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

    }

    private  void  loadMessages (String identifier) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.isIndeterminate();
        pd.setMessage("Loading messages");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (mMessagesRecyclerViewAdapter !=null && mMessagesRecyclerViewAdapter.getItemCount() == 0) {
            pd.show();
        }

        Message.fetchMessagesOfConversationIdentifier(identifier, new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {

                pd.dismiss();

                if (e == null) {

                    if (messages.size() > mPreviousMessageCount) {
                        mMessagesRecyclerViewAdapter.setMessages(messages);
                        mRecyclerView.scrollToPosition(messages.size() - 1);
                        mPreviousMessageCount = messages.size();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recyclerview_messages, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvMessages);
        mChatEditText = (EditText)  view.findViewById(R.id.etMessage);
        mSendButton = (Button)  view.findViewById(R.id.btSend);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mChatEditText.getText().toString();

                if (!StringUtils.isEmpty(text)) {
                    send(text, new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                mChatEditText.setText("");

                                if (!StringUtils.isEmpty(mConversationIdentifierString)) {
                                    loadMessages(mConversationIdentifierString);

                                    String otherId = idOfOtherUser(mConversationIdentifierString);
                                    if (!StringUtils.isEmpty(otherId)) {
                                        try {
                                            Push.userSentMessage(otherId, mConversationIdentifierString);
                                        } catch (JSONException jsonexception) {
                                            jsonexception.printStackTrace();
                                        }

                                    }

                                }
                            }
                        }

                    });
                }
            }
        });


        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mMessagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter();
        mRecyclerView.setAdapter(mMessagesRecyclerViewAdapter);

        return view;
    }

    private void send(String text, SaveCallback saveCallback) {
        Message message = new Message();
        message.setFromUser((User) ParseUser.getCurrentUser());
        message.setText(text);

        if (!StringUtils.isEmpty(mConversationIdentifierString)) {
            message.saveInBackground(mConversationIdentifierString, saveCallback);
        }
    }

    private String idOfOtherUser (String conversationIdentifierString) {

        String[] parts = conversationIdentifierString.split(Conversation.SPLITER);

        if (!StringUtils.isEmpty(parts[0]) && parts[0].equals(ParseUser.getCurrentUser().getObjectId())) {
            return parts[1];
        } else if (!StringUtils.isEmpty(parts[1]) && parts[1].equals(ParseUser.getCurrentUser().getObjectId())){
            return parts[0];
        }

        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mRefreshMessagesRunnable);
        mRefreshMessagesRunnable = null;
    }
}
