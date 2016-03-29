package fashiome.android.v2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import fashiome.android.R;
import fashiome.android.activities.ChatRoomActivity;
import fashiome.android.adapters.ConversationRecyclerViewAdapter;
import fashiome.android.helpers.ItemClickSupport;
import fashiome.android.models.Conversation;

/**
 * Created by dsaha on 3/29/16.
 */
public class ConversationsFragment  extends Fragment {

    private RecyclerView mRecyclerView;
    private ConversationRecyclerViewAdapter mConversationRecyclerViewAdapter;
    private int mPreviousConversationCount;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean mConversationLoading = false;

    public static ConversationsFragment newInstance() {
        ConversationsFragment fragment = new ConversationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadConversations () {

        if (mConversationLoading == true) {
            swipeRefreshLayout.setEnabled(false);
            return;
        }

        mConversationLoading = true;

        Conversation.fetchConversations(new FindCallback<Conversation>() {

            @Override
            public void done(List<Conversation> conversations, ParseException e) {

                swipeRefreshLayout.setRefreshing(false);
                mConversationLoading = false;

                if (e == null) {

                    if (conversations.size() > mPreviousConversationCount) {
                        mConversationRecyclerViewAdapter.setConversations(Conversation.filterMyConversations(conversations));
                        mRecyclerView.scrollToPosition(conversations.size() - 1);
                        mPreviousConversationCount = conversations.size();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversations_v2, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvConversations);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mConversationRecyclerViewAdapter = new ConversationRecyclerViewAdapter();
        mRecyclerView.setAdapter(mConversationRecyclerViewAdapter);

        swipeRefreshLayout.setRefreshing(true);
        loadConversations();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadConversations();
            }
        });

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Conversation conversation = mConversationRecyclerViewAdapter.getConversationAtIndex(position);

                        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
                        intent.putExtra(Conversation.CONVERSATION_IDENTIFIER, conversation.getConverstaionIdentifier());
                        startActivityForResult(intent, 500);
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.stay);
                    }
                });

        return view;
    }
}
