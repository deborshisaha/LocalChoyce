package fashiome.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudinary.utils.StringUtils;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import fashiome.android.R;
import fashiome.android.adapters.MessagesRecyclerViewAdapter;
import fashiome.android.listeners.OnListFragmentInteractionListener;
import fashiome.android.models.Conversation;
import fashiome.android.models.Message;

public class MessagesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MessagesRecyclerViewAdapter mMessagesRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    public MessagesFragment() {
    }

    public static MessagesFragment newInstance(int columnCount) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        String conversationIdString = getActivity().getIntent().getExtras().getString(Conversation.CONVERSATION_ID);

        if (!StringUtils.isEmpty(conversationIdString)) {
            Message.fetchMessagesOfConversation(conversationIdString, new FindCallback<Message>() {
                @Override
                public void done(List<Message> messages, ParseException e) {
                    if (e == null) {
                        mMessagesRecyclerViewAdapter.setMessages(messages);
                    }
                }
            });
        }

        //Conversation.findConversationWithParticipant()

        //String conversationIdString = getActivity().getIntent().getExtras().getString(Conversation.CONVERSATION_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview_messages, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvMessages);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mMessagesRecyclerViewAdapter = new MessagesRecyclerViewAdapter();
        mRecyclerView.setAdapter(mMessagesRecyclerViewAdapter);

        return view;
    }


}
