package fashiome.android.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cloudinary.utils.StringUtils;

import fashiome.android.R;
import fashiome.android.fragments.MessagesFragment;
import fashiome.android.fragments.ProductsRecyclerViewFragment;
import fashiome.android.models.Conversation;
import fashiome.android.models.Message;
import fashiome.android.models.User;

/**
 * Created by dsaha on 3/21/16.
 */
public class ChatRoomActivity extends AppCompatActivity {

    private MessagesFragment messagesFragment;
    private String mConversationIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        mConversationIdentifier = getIntent().getExtras().getString(Conversation.CONVERSATION_IDENTIFIER);

        getSupportActionBar().setTitle("Chat");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (!StringUtils.isEmpty(mConversationIdentifier)) {
            messagesFragment =  MessagesFragment.newInstance(mConversationIdentifier);
        }
        ft.replace(R.id.fragment_placeholder, messagesFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modal_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.close) {
            finish();
        }

        return true;
    }
}
